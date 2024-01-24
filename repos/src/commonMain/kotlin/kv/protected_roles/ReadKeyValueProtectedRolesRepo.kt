package dev.inmo.kroles.repos.kv.protected_roles

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.repos.ReadProtectedRolesRepo
import dev.inmo.micro_utils.repos.ReadKeyValueRepo

class ReadKeyValueProtectedRolesRepo(
    private val protectedKeyValueRepo: ReadKeyValueRepo<BaseRoleSubject, Boolean>
) : ReadProtectedRolesRepo {
    override suspend fun allowInclude(subject: BaseRoleSubject): Boolean {
        return protectedKeyValueRepo.get(subject) == true
    }
    override suspend fun allowExclude(subject: BaseRoleSubject): Boolean {
        return protectedKeyValueRepo.contains(subject)
    }
}
