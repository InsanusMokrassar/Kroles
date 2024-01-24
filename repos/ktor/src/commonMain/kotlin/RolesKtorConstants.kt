package dev.inmo.kroles.repos.repos.ktor

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.roles.BaseRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

object RolesKtorConstants {
    val RoleSubjectsSerializer = ListSerializer(BaseRoleSubject.serializer())

    @Serializable
    data class IncludeExcludeWrapper(
        val subject: BaseRoleSubject,
        val role: BaseRole
    )

    @Serializable
    data class IncludesExcludesWrapper(
        val subject: BaseRoleSubject,
        val roles: List<BaseRole>
    )

    @Serializable
    data class ModifyWrapper(
        val subject: BaseRoleSubject,
        val toExclude: List<BaseRole>,
        val toInclude: List<BaseRole>
    )

    const val DefaultRolesRootPathPart = "roles"

    const val RoleQueryParameterName = "role"
    const val SubjectIdentifierQueryParameterName = "subject_identifier"
    const val SubjectRoleQueryParameterName = "subject_role"
    const val RoleSubjectQueryParameterName = "subject"

    const val GetDirectSubjectsPathPart = "getSubjectsByRole"
    const val GetDirectRolesPathPart = "getDirectSubjectRoles"
    const val GetAllPathPart = "getAll"
    const val GetAllRolesPathPart = "getAllRoles"
    const val GetAllRolesByPaginationPathPart = "getRolesPage"
    const val GetAllSubjectsRolesByPaginationPathPart = "getSubjects"
    const val ContainsPathPart = "contains"
    const val ContainsAnyPathPart = "containsAny"

    const val IncludeDirectPathPart = "includeDirect"
    const val IncludeDirectsPathPart = "includeDirects"
    const val ExcludeDirectPathPart = "excludeDirect"
    const val ExcludeDirectsPathPart = "excludeDirects"
    const val ModifyDirectPathPart = "modifyDirect"
    const val CreateRolePathPart = "createRole"
    const val RemoveRolePathPart = "removeRole"
    const val IncludedDirectsFlowPathPart = "includedDirectsFlow"
    const val ExcludedDirectsFlowPathPart = "excludedDirectsFlow"
    const val CreatedRoleFlowPathPart = "createdRoleFlow"
    const val RemovedRoleFlowPathPart = "removedRoleFlow"

}