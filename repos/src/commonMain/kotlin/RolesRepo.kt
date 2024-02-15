package dev.inmo.kroles.repos

import dev.inmo.kroles.roles.BaseRole
import dev.inmo.micro_utils.pagination.Pagination
import dev.inmo.micro_utils.pagination.PaginationResult
import dev.inmo.micro_utils.pagination.utils.getAllByWithNextPaging
import kotlinx.coroutines.flow.Flow


interface ReadRolesRepo {
    suspend fun getDirectSubjects(role: BaseRole): List<BaseRoleSubject>
    suspend fun getDirectRoles(subject: BaseRoleSubject): List<BaseRole>
    suspend fun getAll(): Map<BaseRoleSubject, List<BaseRole>>
    suspend fun getAllRoles(subject: BaseRoleSubject): List<BaseRole> = when (subject) {
        is BaseRoleSubject.OtherRole -> {
            val toCheck = linkedSetOf<BaseRole>()
            val roles = mutableSetOf<BaseRole>()
            toCheck.addAll(getDirectRoles(subject))
            roles.addAll(toCheck)

            while (toCheck.isNotEmpty()) {
                val current = toCheck.first()
                roles.add(current)

                toCheck.addAll(
                    getDirectRoles(BaseRoleSubject.OtherRole(current)).filter {
                        it !in roles
                    }
                )

                toCheck.remove(current)
            }

            roles.toList()
        }
        is BaseRoleSubject.Direct -> {
            val roles = getDirectRoles(subject)
            roles.flatMap {
                getAllRoles(BaseRoleSubject.OtherRole(it)) + it
            }.distinct()
        }
    }
    suspend fun getAllSubjectsByPagination(subject: BaseRoleSubject): Set<BaseRoleSubject> {
        val visitedSubjects = mutableSetOf<BaseRoleSubject>()
        val subjectsToVisit = mutableListOf<BaseRoleSubject>(subject)
        while (subjectsToVisit.isNotEmpty()) {
            when (val current = subjectsToVisit.removeFirst()) {
                is BaseRoleSubject.OtherRole -> {
                    val directSubjects = getDirectSubjects(current.role)
                    directSubjects.forEach {
                        if (visitedSubjects.add(it)) {
                            subjectsToVisit.add(it)
                        }
                    }
                }
                is BaseRoleSubject.Direct -> visitedSubjects.add(current)
            }
        }
        return visitedSubjects.toSet()
    }
    suspend fun getAllRolesByPagination(pagination: Pagination, reversed: Boolean = false): PaginationResult<BaseRole>
    suspend fun getAllRoles() = getAllByWithNextPaging {
        getAllRolesByPagination(it)
    }
    suspend fun getAllSubjectsByPagination(pagination: Pagination, reversed: Boolean = false): PaginationResult<BaseRoleSubject>
    suspend fun contains(subject: BaseRoleSubject, role: BaseRole): Boolean
    suspend fun containsAny(subject: BaseRoleSubject, roles: List<BaseRole>): Boolean
}


interface WriteRolesRepo {
    val roleIncluded: Flow<Pair<BaseRoleSubject, BaseRole>>
    val roleExcluded: Flow<Pair<BaseRoleSubject, BaseRole>>
    val roleCreated: Flow<BaseRole>
    val roleRemoved: Flow<BaseRole>

    suspend fun includeDirect(subject: BaseRoleSubject, role: BaseRole): Boolean
    suspend fun excludeDirect(subject: BaseRoleSubject, role: BaseRole): Boolean
    suspend fun createRole(newRole: BaseRole): Boolean
    suspend fun removeRole(role: BaseRole): Boolean

    suspend fun includeDirect(subject: BaseRoleSubject, roles: List<BaseRole>): Boolean {
        return roles.map {
            includeDirect(subject, it)
        }.all { it }
    }
    suspend fun excludeDirect(subject: BaseRoleSubject, roles: List<BaseRole>): Boolean {
        return roles.map {
            excludeDirect(subject, it)
        }.all { it }
    }
    suspend fun modifyDirect(subject: BaseRoleSubject, toExclude: List<BaseRole>, toInclude: List<BaseRole>): Boolean {
        return excludeDirect(subject, toExclude) && includeDirect(subject, toInclude)
    }
}

interface RolesRepo : ReadRolesRepo, WriteRolesRepo
