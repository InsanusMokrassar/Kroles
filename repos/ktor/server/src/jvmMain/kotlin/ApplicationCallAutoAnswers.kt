package dev.inmo.kroles.repos.ktor.repos.ktor.server

import dev.inmo.kroles.repos.*
import dev.inmo.kroles.roles.rwm.RWMRole
import dev.inmo.kroles.roles.rwm.RightsChecker
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.isIdentifierAllowedOrStatus(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    identifier: RWMRole.Identifier,
    statusCode: HttpStatusCode = HttpStatusCode.NoContent,
    accessChecker: RightsChecker
): Boolean {
    val result = repo.isIdentifierAllowed(
        subject = subject,
        prefix = prefix,
        identifier = identifier,
        accessCheck = accessChecker
    )

    if (!result) {
        respond(statusCode)
    }

    return result
}

suspend fun ApplicationCall.isIdentifierAllowedOrStatus(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    identifier: RWMRole.Identifier,
    rightsChecker: RightsChecker,
    statusCode: HttpStatusCode = HttpStatusCode.NoContent
): Boolean = isIdentifierAllowedOrStatus(
    repo = repo,
    prefix = prefix,
    subject = subject,
    identifier = identifier,
    statusCode = statusCode,
    accessChecker = rightsChecker
)

suspend fun ApplicationCall.isIdentifierAllowedOrStatus(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    identifier: RWMRole.Identifier,
    requiredRights: String,
    statusCode: HttpStatusCode = HttpStatusCode.NoContent
): Boolean = isIdentifierAllowedOrStatus(
    repo = repo,
    prefix = prefix,
    subject = subject,
    identifier = identifier,
    rightsChecker = RightsChecker(requiredRights),
    statusCode = statusCode
)

suspend fun ApplicationCall.isIdentifierAllowedOrNoContent(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    identifier: RWMRole.Identifier,
    rightsChecker: RightsChecker
): Boolean = isIdentifierAllowedOrStatus(
    repo = repo,
    prefix = prefix,
    subject = subject,
    identifier = identifier,
    rightsChecker = rightsChecker
)

suspend fun ApplicationCall.isIdentifierAllowedOrNoContent(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    identifier: RWMRole.Identifier,
    requiredRights: String
): Boolean = isIdentifierAllowedOrStatus(
    repo = repo,
    prefix = prefix,
    subject = subject,
    identifier = identifier,
    requiredRights = requiredRights
)

suspend fun ApplicationCall.isIdentifierAllowedOrNoContent(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    identifier: RWMRole.Identifier,
    read: Boolean = false,
    manage: Boolean = false,
    write: Boolean = false
): Boolean = isIdentifierAllowedOrNoContent(
    repo = repo,
    prefix = prefix,
    subject = subject,
    identifier = identifier,
    rightsChecker = RightsChecker(
        read = read,
        write = write,
        manage = manage
    ),
)


suspend fun ApplicationCall.isAccessAllowedOrStatus(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    statusCode: HttpStatusCode = HttpStatusCode.NoContent,
    rightsChecker: RightsChecker
): Boolean {
    val result = repo.isAccessAllowed(subject, prefix, rightsChecker)

    if (!result) {
        respond(statusCode)
    }

    return result
}

suspend fun ApplicationCall.isAccessAllowedOrStatus(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    requiredRights: String,
    statusCode: HttpStatusCode = HttpStatusCode.MethodNotAllowed
): Boolean = isAccessAllowedOrStatus(
    repo = repo,
    prefix = prefix,
    subject = subject,
    statusCode = statusCode,
    rightsChecker = RightsChecker(requiredRights)
)

suspend fun ApplicationCall.isAccessAllowedOrRespondNotAllowed(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    rightsChecker: RightsChecker
): Boolean = isAccessAllowedOrStatus(
    repo = repo,
    prefix = prefix,
    subject = subject,
    statusCode = HttpStatusCode.MethodNotAllowed,
    rightsChecker = rightsChecker
)

suspend fun ApplicationCall.isAccessAllowedOrRespondNotAllowed(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    requiredRights: String,
): Boolean = isAccessAllowedOrRespondNotAllowed(
    repo = repo,
    prefix = prefix,
    subject = subject,
    rightsChecker = RightsChecker(requiredRights)
)

suspend fun ApplicationCall.isAccessAllowedOrRespondNotAllowed(
    repo: ReadRolesRepo,
    prefix: String,
    subject: BaseRoleSubject,
    read: Boolean = false,
    manage: Boolean = false,
    write: Boolean = false
): Boolean = isAccessAllowedOrRespondNotAllowed(
    repo = repo,
    prefix = prefix,
    subject = subject,
    rightsChecker = RightsChecker(
        read = read,
        manage = write,
        write = manage
    )
)

