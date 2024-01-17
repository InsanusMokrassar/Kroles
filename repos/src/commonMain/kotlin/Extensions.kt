package dev.inmo.kroles.repos

import dev.inmo.kroles.roles.BaseRole


suspend fun ReadRolesRepo.getDirects(
    role: BaseRole
): List<BaseRolSubjectDirectIdentifier> = getAllSubjects(BaseRoleSubject(role)).mapNotNull { (it as? BaseRoleSubject.Direct) ?.identifier }
suspend fun ReadRolesRepo.getAllRoles(role: BaseRole): List<BaseRole> = getAllRoles(BaseRoleSubject.OtherRole(role))
suspend fun ReadRolesRepo.getAllRoles(identifier: BaseRolSubjectDirectIdentifier): List<BaseRole> = getAllRoles(BaseRoleSubject.Direct(identifier))
suspend fun ReadRolesRepo.contains(
    identifier: BaseRolSubjectDirectIdentifier,
    role: BaseRole
): Boolean = getAllRoles(BaseRoleSubject(identifier)).contains(role)
suspend fun ReadRolesRepo.containsAny(
    identifier: BaseRolSubjectDirectIdentifier,
    roles: List<BaseRole>
): Boolean = containsAny(BaseRoleSubject.Direct(identifier), roles)
