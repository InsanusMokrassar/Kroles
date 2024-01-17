package dev.inmo.kroles.roles.rwm

import dev.inmo.kroles.roles.BaseRole


suspend fun List<BaseRole>.isIdentifierAllowed(
    prefix: String,
    identifier: RWMRole.Identifier,
    accessCheck: RightsChecker
): Boolean {
    return any {
        RWMRole.checkRights(it, prefix, identifier, accessCheck)
    }
}

suspend fun List<BaseRole>.isIdentifierAllowed(
    prefix: String,
    identifier: RWMRole.Identifier,
    requiredAccess: String
): Boolean {
    return isIdentifierAllowed(prefix, identifier, RightsChecker(requiredAccess))
}

suspend fun List<BaseRole>.isIdentifierAllowed(
    prefix: String,
    identifier: RWMRole.Identifier,
    read: Boolean = false,
    manage: Boolean = false,
    write: Boolean = false
): Boolean {
    return isIdentifierAllowed(prefix, identifier, RightsChecker(read, write, manage))
}

suspend fun List<BaseRole>.isAccessAllowed(
    prefix: String,
    accessCheck: RightsChecker
): Boolean {
    return any {
        RWMRole.checkRights(it, prefix, null, accessCheck)
    }
}

suspend fun List<BaseRole>.isAccessAllowed(
    prefix: String,
    requiredAccess: String
): Boolean {
    return isAccessAllowed(prefix, RightsChecker(requiredAccess))
}

suspend fun List<BaseRole>.isAccessAllowed(
    prefix: String,
    read: Boolean = false,
    manage: Boolean = false,
    write: Boolean = false
): Boolean {
    return isAccessAllowed(prefix, RightsChecker(read, write, manage))
}

suspend fun List<BaseRole>.getAllowedIdentifiers(
    prefix: String,
    accessCheck: RightsChecker
): List<RWMRole.Identifier>? {
    return mapNotNull {
        val rmw = it.rwmRoleOrNull()?.takeIf { it.prefix == prefix } ?: return@mapNotNull null
        rmw.takeIf { accessCheck(rmw.rightsString) } ?.let { it.identifier ?: return null }
    }
}

suspend fun List<BaseRole>.getAllowedIdentifiers(
    prefix: String,
    requiredAccess: String
): List<RWMRole.Identifier>? = getAllowedIdentifiers(prefix, RightsChecker(requiredAccess))

suspend fun List<BaseRole>.getAllowedIdentifiers(
    prefix: String,
    read: Boolean = false,
    manage: Boolean = false,
    write: Boolean = false,
): List<RWMRole.Identifier>? = getAllowedIdentifiers(prefix, RightsChecker(read, write, manage))


suspend fun List<BaseRole>.includesBaseRole(role: BaseRole): Boolean {
    return role.rwmRoleOrNull()?.let {
        val identifier = it.identifier
        if (identifier == null) {
            isAccessAllowed(
                prefix = it.prefix,
                read = it.readAccess,
                manage = it.manageAccess,
                write = it.writeAccess
            )
        } else {
            isIdentifierAllowed(
                prefix = it.prefix,
                identifier = identifier,
                read = it.readAccess,
                manage = it.manageAccess,
                write = it.writeAccess
            )
        }
    } ?: contains(role)
}
