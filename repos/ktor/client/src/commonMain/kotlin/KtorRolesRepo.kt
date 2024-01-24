package dev.inmo.kroles.repos.repos.ktor.client

import dev.inmo.kroles.repos.ReadRolesRepo
import dev.inmo.kroles.repos.RolesRepo
import dev.inmo.kroles.repos.WriteRolesRepo
import dev.inmo.kroles.repos.repos.ktor.RolesKtorConstants
import io.ktor.client.HttpClient

class KtorRolesRepo(
    private val client: HttpClient,
    private val rootPath: String = RolesKtorConstants.DefaultRolesRootPathPart
) : RolesRepo,
    ReadRolesRepo by ReadKtorRolesRepo(
        client,
        rootPath
    ),
    WriteRolesRepo by WriteKtorRolesRepo(
        client,
        rootPath
    )
