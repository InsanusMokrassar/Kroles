package dev.inmo.kroles.repos.kv.protected_roles

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.repos.WriteProtectedRolesRepo
import dev.inmo.micro_utils.repos.WriteKeyValueRepo
import dev.inmo.micro_utils.repos.set
import dev.inmo.micro_utils.repos.unset

class WriteKeyValueProtectedRolesRepo(
    private val protectedKeyValueRepo: WriteKeyValueRepo<BaseRoleSubject, Boolean>
) : WriteProtectedRolesRepo {
    override suspend fun protect(subject: BaseRoleSubject, allowInclude: Boolean) {
        protectedKeyValueRepo.set(subject, allowInclude)
    }

    override suspend fun unprotect(subject: BaseRoleSubject) {
        protectedKeyValueRepo.unset(subject)
    }
}
