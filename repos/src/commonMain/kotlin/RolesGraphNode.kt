package dev.inmo.kroles.repos

import dev.inmo.kroles.roles.BaseRole

sealed interface RoleSubjectGraphNode {
    val subject: BaseRoleSubject
    val childNodes: Set<RoleSubjectGraphNode>

    sealed interface Mutable : RoleSubjectGraphNode {
        val parentNodes: MutableSet<Mutable>
        override val childNodes: MutableSet<Mutable>

        fun immutable(immutableMap: MutableMap<Mutable, Immutable> = mutableMapOf()): Immutable

        companion object {
            operator fun invoke(roleSubject: BaseRoleSubject) = when (roleSubject) {
                is BaseRoleSubject.OtherRole -> RoleNode.MutableRoleNode(roleSubject.role)
                is BaseRoleSubject.Direct -> DirectNode.MutableDirectNode(roleSubject.identifier)
            }
        }
    }
    sealed interface Immutable : RoleSubjectGraphNode {
        override val childNodes: Set<Immutable>

        fun allChildren(): Set<RoleSubjectGraphNode> = allChildren(emptySet())
    }
    sealed interface DirectNode : RoleSubjectGraphNode {
        val identifier: BaseRolSubjectDirectIdentifier
        override val subject: BaseRoleSubject.Direct
        data class ImmutableDirectNode(override val identifier: BaseRolSubjectDirectIdentifier, override val childNodes: Set<Immutable>) : DirectNode, Immutable {
            override val subject: BaseRoleSubject.Direct = BaseRoleSubject.Direct(identifier)

            override fun hashCode(): Int {
                return identifier.hashCode()
            }
        }
        data class MutableDirectNode(
            override val identifier: BaseRolSubjectDirectIdentifier,
            override val childNodes: MutableSet<Mutable> = mutableSetOf()
        ) : DirectNode, Mutable {
            override val subject: BaseRoleSubject.Direct = BaseRoleSubject.Direct(identifier)
            override val parentNodes: MutableSet<Mutable>
                get() = mutableSetOf()

            override fun immutable(
                immutableMap: MutableMap<Mutable, Immutable>
            ): Immutable {
                immutableMap[this] ?.let { return it }

                val fakeImmutableSet = mutableSetOf<Immutable>()
                val immutable = ImmutableDirectNode(
                    identifier,
                    fakeImmutableSet
                )
                immutableMap[this] = immutable
                childNodes.forEach {
                    fakeImmutableSet.add(it.immutable(immutableMap))
                }
                return immutable
            }

            override fun hashCode(): Int {
                return identifier.hashCode()
            }
        }
    }
    sealed interface RoleNode : RoleSubjectGraphNode {
        val role: BaseRole
        override val subject: BaseRoleSubject.OtherRole
        data class ImmutableRoleNode(
            override val role: BaseRole,
            override val childNodes: Set<Immutable>
        ) : RoleNode, Immutable {
            override val subject: BaseRoleSubject.OtherRole = BaseRoleSubject.OtherRole(role)

            override fun hashCode(): Int {
                return role.hashCode()
            }
        }
        data class MutableRoleNode(
            override val role: BaseRole,
            override val parentNodes: MutableSet<Mutable> = mutableSetOf(),
            override val childNodes: MutableSet<Mutable> = mutableSetOf()
        ) : RoleNode, Mutable {
            override val subject: BaseRoleSubject.OtherRole = BaseRoleSubject.OtherRole(role)

            override fun immutable(
                immutableMap: MutableMap<Mutable, Immutable>
            ): Immutable {
                immutableMap[this] ?.let { return it }

                val fakeParentNodesImmutableSet = mutableSetOf<Immutable>()
                val fakeChildrenNodesImmutableSet = mutableSetOf<Immutable>()
                val immutable = ImmutableRoleNode(
                    role,
                    fakeChildrenNodesImmutableSet,
                )
                immutableMap[this] = immutable
                parentNodes.forEach {
                    fakeParentNodesImmutableSet.add(it.immutable(immutableMap))
                }
                childNodes.forEach {
                    fakeChildrenNodesImmutableSet.add(it.immutable(immutableMap))
                }
                return immutable
            }

            override fun hashCode(): Int {
                return role.hashCode()
            }
        }
    }
}
fun RoleSubjectGraphNode.Immutable.allChildren(exclude: Set<RoleSubjectGraphNode.Immutable>): Set<RoleSubjectGraphNode.Immutable> {
    return childNodes.fold(childNodes) { acc, roleSubjectGraphNode ->
        if (roleSubjectGraphNode !in exclude) {
            (acc + roleSubjectGraphNode.allChildren(exclude + acc))
        } else {
            acc
        }
    }
}

private fun createTempNode(
    subject: BaseRoleSubject,
    parent: RoleSubjectGraphNode.Mutable?,
    directSubNodes: Map<BaseRoleSubject, Set<BaseRole>>,
    rolesNodesMap: MutableMap<BaseRoleSubject, RoleSubjectGraphNode.Mutable>
): RoleSubjectGraphNode.Mutable {
    val node = RoleSubjectGraphNode.Mutable(
        subject,
    )
    rolesNodesMap[subject] = node
    parent ?.let { node.parentNodes.add(it) }
    node.childNodes.addAll(
        getTempNodes(node, directSubNodes, rolesNodesMap)
    )
    return node
}
private fun RoleSubjectGraphNode.Mutable.collectMutableTempNodes(
    target: MutableSet<RoleSubjectGraphNode.Mutable> = mutableSetOf()
): MutableSet<RoleSubjectGraphNode.Mutable> {
    if (target.add(this)) {
        childNodes.filter {
            target.add(it)
        }.forEach {
            it.collectMutableTempNodes(target)
        }
    }
    return target
}
private fun getTempNodes(
    parent: RoleSubjectGraphNode.Mutable,
    directSubNodes: Map<BaseRoleSubject, Set<BaseRole>>,
    rolesNodesMap: MutableMap<BaseRoleSubject, RoleSubjectGraphNode.Mutable>
): Set<RoleSubjectGraphNode.Mutable> {
    return directSubNodes[parent.subject] ?.map {
        val subject = BaseRoleSubject.OtherRole(it)
        rolesNodesMap[subject] ?.also {
            it.parentNodes.add(parent)
        } ?: createTempNode(subject, parent, directSubNodes, rolesNodesMap)
    } ?.toSet() ?.fold(mutableSetOf()) { acc, mutableTempNode ->
        mutableTempNode.collectMutableTempNodes(acc)
    } ?: emptySet()
}

sealed interface GraphNode<T> {
    val value: T
    val parents: Set<GraphNode<T>>
    val children: Set<GraphNode<T>>

    val allChildren: Set<GraphNode<T>>
        get() = children + children.flatMap { it.allChildren }.toSet()
    val allParents: Set<GraphNode<T>>
        get() = parents + parents.flatMap { it.allParents }.toSet()

    private class ImmutableGraphNode<T>(
        override val value: T,
        override val parents: MutableSet<ImmutableGraphNode<T>>,
        override val children: MutableSet<ImmutableGraphNode<T>>
    ) : GraphNode<T>

    private class MutableGraphNode<T>(
        override val value: T,
        override val parents: MutableSet<MutableGraphNode<T>>,
        override val children: MutableSet<MutableGraphNode<T>>
    ) : GraphNode<T>

    companion object {
        fun <T> buildGraph(dataMap: Map<T, Iterable<T>>): Map<T, GraphNode<T>> {
            val nodesMap = mutableMapOf<T, MutableGraphNode<T>>()

            dataMap.forEach { (k, vs) ->
                val kNode = nodesMap.getOrPut(k) { MutableGraphNode(k, mutableSetOf(), mutableSetOf()) }
                vs.forEach { v ->
                    val vNode = nodesMap.getOrPut(v) { MutableGraphNode(v, mutableSetOf(), mutableSetOf()) }
                    vNode.parents.add(kNode)
                    kNode.children.add(vNode)
                }
            }

            return nodesMap.toMap()
        }
    }
}


fun buildRolesNodesGraph(directSubNodes: Map<BaseRoleSubject, Set<BaseRole>>): Map<BaseRoleSubject, GraphNode<BaseRoleSubject>> {
    return GraphNode.buildGraph(directSubNodes.mapValues { it.value.map { BaseRoleSubject(it) } })
}