package dev.inmo.kroles.repos

interface ReadProtectedRolesRepo {
    suspend fun allowInclude(subject: BaseRoleSubject): Boolean
    suspend fun allowExclude(subject: BaseRoleSubject): Boolean

    object AlwaysUnprotected : ReadProtectedRolesRepo {
        override suspend fun allowInclude(subject: BaseRoleSubject): Boolean {
            return true
        }
        override suspend fun allowExclude(subject: BaseRoleSubject): Boolean {
            return true
        }
    }
}

interface WriteProtectedRolesRepo {
    suspend fun protect(subject: BaseRoleSubject, allowInclude: Boolean = true)
    suspend fun unprotect(subject: BaseRoleSubject)
}

interface ProtectedRolesRepo : ReadProtectedRolesRepo, WriteProtectedRolesRepo
