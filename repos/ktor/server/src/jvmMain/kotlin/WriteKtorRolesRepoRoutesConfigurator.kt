package dev.inmo.kroles.repos.ktor.repos.ktor.server

import dev.inmo.kroles.repos.WriteRolesRepo
import dev.inmo.kroles.repos.repos.ktor.RolesKtorConstants
import dev.inmo.kroles.roles.BaseRole
import dev.inmo.micro_utils.ktor.server.includeWebsocketHandling
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureWriteRolesRepoRoutes(
    repo: WriteRolesRepo
) {
    includeWebsocketHandling(
        RolesKtorConstants.RoleCreatedFlowPathPart,
        repo.roleCreated
    )
    includeWebsocketHandling(
        RolesKtorConstants.RoleRemovedFlowPathPart,
        repo.roleRemoved
    )
    includeWebsocketHandling(
        RolesKtorConstants.RoleIncludedFlowPathPart,
        repo.roleIncluded
    )
    includeWebsocketHandling(
        RolesKtorConstants.RoleExcludedFlowPathPart,
        repo.roleExcluded
    )

    post(RolesKtorConstants.IncludeDirectPathPart) {
        val wrapper = call.receive<RolesKtorConstants.IncludeExcludeWrapper>()
        call.respond(
            repo.includeDirect(
                wrapper.subject,
                wrapper.role
            )
        )
    }
    post(RolesKtorConstants.IncludeDirectsPathPart) {
        val wrapper = call.receive<RolesKtorConstants.IncludesExcludesWrapper>()
        call.respond(
            repo.includeDirect(
                wrapper.subject,
                wrapper.roles
            )
        )
    }

    post(RolesKtorConstants.ExcludeDirectPathPart) {
        val wrapper = call.receive<RolesKtorConstants.IncludeExcludeWrapper>()
        call.respond(
            repo.excludeDirect(
                wrapper.subject,
                wrapper.role
            )
        )
    }
    post(RolesKtorConstants.ExcludeDirectsPathPart) {
        val wrapper = call.receive<RolesKtorConstants.IncludesExcludesWrapper>()
        call.respond(
            repo.excludeDirect(
                wrapper.subject,
                wrapper.roles
            )
        )
    }
    post(RolesKtorConstants.ModifyDirectPathPart) {
        val wrapper = call.receive<RolesKtorConstants.ModifyWrapper>()
        call.respond(
            repo.modifyDirect(
                subject = wrapper.subject,
                toExclude = wrapper.toExclude,
                toInclude = wrapper.toInclude
            )
        )
    }

    post(RolesKtorConstants.CreateRolePathPart) {
        val role = call.receive<BaseRole>()
        call.respond(
            repo.createRole(role)
        )
    }
    post(RolesKtorConstants.RemoveRolePathPart) {
        val roles = call.receive<BaseRole>()
        call.respond(
            repo.removeRole(roles)
        )
    }
}
