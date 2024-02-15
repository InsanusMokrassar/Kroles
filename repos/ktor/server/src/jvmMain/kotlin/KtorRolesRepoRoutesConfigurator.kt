package dev.inmo.kroles.repos.ktor.repos.ktor.server

import dev.inmo.kroles.repos.RolesRepo
import dev.inmo.kroles.repos.repos.ktor.RolesKtorConstants
import io.ktor.server.routing.*

fun Route.configureRolesRepoRoutes(
    repo: RolesRepo,
    rootPath: String? = RolesKtorConstants.DefaultRolesRootPathPart
) {
    rootPath ?.let {
        route(rootPath) {
            configureReadRolesRepoRoutes(repo)
            configureWriteRolesRepoRoutes(repo)
        }
    } ?: let {
        configureReadRolesRepoRoutes(repo)
        configureWriteRolesRepoRoutes(repo)
    }
}
