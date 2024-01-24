package dev.inmo.kroles.repos.kv.protected_roles

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.repos.ProtectedRolesRepo
import dev.inmo.kroles.repos.ReadProtectedRolesRepo
import dev.inmo.kroles.repos.WriteProtectedRolesRepo
import dev.inmo.micro_utils.repos.KeyValueRepo
import dev.inmo.micro_utils.repos.ReadKeyValueRepo

class KeyValueProtectedRolesRepo(
    private val protectedKeyValueRepo: KeyValueRepo<BaseRoleSubject, Boolean>
) : ReadProtectedRolesRepo by ReadKeyValueProtectedRolesRepo(protectedKeyValueRepo),
    WriteProtectedRolesRepo by WriteKeyValueProtectedRolesRepo(protectedKeyValueRepo),
    ProtectedRolesRepo
