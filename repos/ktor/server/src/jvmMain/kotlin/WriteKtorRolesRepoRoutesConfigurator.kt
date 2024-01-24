package dev.inmo.kroles.repos.ktor.repos.ktor.server

import dev.inmo.kroles.repos.ReadRolesRepo
import dev.inmo.kroles.repos.WriteRolesRepo
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

fun Route.configureWriteRolesRepoRoutes(
    repo: WriteRolesRepo
) {

}
