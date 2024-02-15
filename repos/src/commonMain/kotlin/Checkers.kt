package dev.inmo.kroles.repos

import dev.inmo.kroles.roles.BaseRole
import dev.inmo.kroles.roles.rwm.*


suspend fun ReadRolesRepo.isIdentifierAllowed(
    subject: BaseRoleSubject,
    prefix: String,
    identifier: RWMRole.Identifier,
    accessCheck: RightsChecker
): Boolean = getAllRoles(subject).isIdentifierAllowed(
    prefix = prefix,
    identifier = identifier,
    accessCheck = accessCheck,
)

suspend fun ReadRolesRepo.isIdentifierAllowed(
    subject: BaseRoleSubject,
    prefix: String,
    identifier: RWMRole.Identifier,
    requiredAccess: String
): Boolean = getAllRoles(subject).isIdentifierAllowed(
    prefix = prefix,
    identifier = identifier,
    requiredAccess = requiredAccess,
)

suspend fun ReadRolesRepo.isIdentifierAllowed(
    subject: BaseRoleSubject,
    prefix: String,
    identifier: RWMRole.Identifier,
    read: Boolean = false,
    write: Boolean = false,
    manage: Boolean = false
): Boolean = getAllRoles(subject).isIdentifierAllowed(
    prefix = prefix,
    identifier = identifier,
    read = read,
    write = write,
    manage = manage
)

suspend fun ReadRolesRepo.isAccessAllowed(
    subject: BaseRoleSubject,
    prefix: String,
    accessCheck: RightsChecker
): Boolean = getAllRoles(subject).isAccessAllowed(prefix = prefix, accessCheck = accessCheck)

suspend fun ReadRolesRepo.isAccessAllowed(
    subject: BaseRoleSubject,
    prefix: String,
    requiredAccess: String
): Boolean = getAllRoles(subject).isAccessAllowed(prefix = prefix, requiredAccess = requiredAccess)

suspend fun ReadRolesRepo.isAccessAllowed(
    subject: BaseRoleSubject,
    prefix: String,
    read: Boolean = false,
    write: Boolean = false,
    manage: Boolean = false
): Boolean = getAllRoles(subject).isAccessAllowed(prefix = prefix, read = read, write = write, manage = manage)

suspend fun ReadRolesRepo.getAllowedIdentifiers(
    subject: BaseRoleSubject,
    prefix: String,
    accessCheck: RightsChecker
): List<RWMRole.Identifier>? = getAllRoles(subject).getAllowedIdentifiers(prefix = prefix, accessCheck = accessCheck)

suspend fun ReadRolesRepo.getAllowedIdentifiers(
    subject: BaseRoleSubject,
    prefix: String,
    requiredAccess: String
): List<RWMRole.Identifier>? = getAllRoles(subject).getAllowedIdentifiers(
    prefix = prefix,
    requiredAccess = requiredAccess
)

suspend fun ReadRolesRepo.getAllowedIdentifiers(
    subject: BaseRoleSubject,
    prefix: String,
    read: Boolean = false,
    write: Boolean = false,
    manage: Boolean = false,
): List<RWMRole.Identifier>? = getAllRoles(subject).getAllowedIdentifiers(
    prefix = prefix,
    read = read,
    write = write,
    manage = manage
)


suspend fun ReadRolesRepo.includesBaseRole(subject: BaseRoleSubject, role: BaseRole): Boolean = getAllRoles(subject).includesBaseRole(
    role = role
)