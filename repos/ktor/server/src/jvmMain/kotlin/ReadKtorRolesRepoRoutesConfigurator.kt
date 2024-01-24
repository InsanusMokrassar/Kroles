package dev.inmo.kroles.repos.ktor.repos.ktor.server

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.repos.ReadRolesRepo
import dev.inmo.kroles.repos.repos.ktor.RolesKtorConstants
import dev.inmo.kroles.roles.BaseRole
import dev.inmo.micro_utils.ktor.server.getQueryParameter
import dev.inmo.micro_utils.ktor.server.getQueryParameterOrSendError
import dev.inmo.micro_utils.ktor.server.getQueryParametersOrSendError
import dev.inmo.micro_utils.pagination.extractPagination
import dev.inmo.micro_utils.repos.ktor.common.reversedParameterName
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.configureReadRolesRepoRoutes(
    repo: ReadRolesRepo
) {
    get(RolesKtorConstants.GetDirectSubjectsPathPart) {
        val role = call.getQueryParameterOrSendError(RolesKtorConstants.RoleQueryParameterName) ?.let(::BaseRole) ?: return@get
        call.respond(
            repo.getDirectSubjects(role)
        )
    }

    get(RolesKtorConstants.GetDirectRolesPathPart) {
        val subject = subjectOrError() ?: return@get
        call.respond(
            repo.getDirectRoles(
                subject
            )
        )
    }

    get(RolesKtorConstants.GetAllPathPart) {
        call.respond(repo.getAll())
    }

    get(RolesKtorConstants.GetAllRolesPathPart) {
        val subject = subjectOrError() ?: return@get
        call.respond(repo.getAllRoles(subject))
    }

    get(RolesKtorConstants.GetAllRolesByPaginationPathPart) {
        call.respond(repo.getAllRolesByPagination(call.extractPagination, call.getQueryParameter(reversedParameterName) ?.toBooleanStrictOrNull() ?: false))
    }

    get(RolesKtorConstants.GetAllSubjectsRolesByPaginationPathPart) {
        call.respond(repo.getAllSubjectsByPagination(call.extractPagination, call.getQueryParameter(reversedParameterName) ?.toBooleanStrictOrNull() ?: false))
    }

    get(RolesKtorConstants.ContainsPathPart) {
        call.respond(
            repo.contains(
                subjectOrError() ?: return@get,
                call.getQueryParameterOrSendError(RolesKtorConstants.RoleQueryParameterName) ?.let(::BaseRole) ?: return@get
            )
        )
    }

    get(RolesKtorConstants.ContainsPathPart) {
        call.respond(
            repo.contains(
                subjectOrError() ?: return@get,
                call.getQueryParameterOrSendError(RolesKtorConstants.RoleQueryParameterName) ?.let(::BaseRole) ?: return@get
            )
        )
    }

    get(RolesKtorConstants.ContainsAnyPathPart) {
        call.respond(
            repo.containsAny(
                subjectOrError() ?: return@get,
                call.getQueryParametersOrSendError(RolesKtorConstants.RoleQueryParameterName) ?.map(::BaseRole) ?: return@get
            )
        )
    }
}
