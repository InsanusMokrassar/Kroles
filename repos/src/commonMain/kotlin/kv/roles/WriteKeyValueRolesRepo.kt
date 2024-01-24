package dev.inmo.kroles.repos.kv.roles

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.repos.ReadProtectedRolesRepo
import dev.inmo.kroles.repos.WriteRolesRepo
import dev.inmo.kroles.roles.BaseRole
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.TagLogger
import dev.inmo.kslog.common.w
import dev.inmo.micro_utils.pagination.utils.doForAllWithNextPaging
import dev.inmo.micro_utils.repos.KeyValuesRepo
import dev.inmo.micro_utils.repos.MapsReposDefaultMutableSharedFlow
import dev.inmo.micro_utils.repos.add
import dev.inmo.micro_utils.repos.pagination.maxPagePagination
import dev.inmo.micro_utils.repos.remove
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asSharedFlow

class WriteKeyValueRolesRepo(
    private val keyValuesRepo: KeyValuesRepo<BaseRoleSubject, BaseRole>,
    private val protectedRolesRepo: ReadProtectedRolesRepo = ReadProtectedRolesRepo.AlwaysUnprotected,
    private val logger: KSLog = TagLogger("WriteKeyValueRolesRepo")
) : WriteRolesRepo {
    private val _roleIncluded = MapsReposDefaultMutableSharedFlow<Pair<BaseRoleSubject, BaseRole>>()
    override val roleIncluded: Flow<Pair<BaseRoleSubject, BaseRole>> = _roleIncluded.asSharedFlow()
    private val _roleExcluded = MapsReposDefaultMutableSharedFlow<Pair<BaseRoleSubject, BaseRole>>()
    override val roleExcluded: Flow<Pair<BaseRoleSubject, BaseRole>> = _roleExcluded.asSharedFlow()
    private val _roleCreated = MapsReposDefaultMutableSharedFlow<BaseRole>()
    override val roleCreated: Flow<BaseRole> = _roleCreated.asSharedFlow()
    private val _roleRemoved = MapsReposDefaultMutableSharedFlow<BaseRole>()
    override val roleRemoved: Flow<BaseRole> = _roleRemoved.asSharedFlow()

    override suspend fun includeDirect(subject: BaseRoleSubject, role: BaseRole): Boolean {
        if (!protectedRolesRepo.allowInclude(subject)) {
            logger.w { "Unable to include role \"$role\" to the subject \"$subject\" (protected)" }
            return false
        }

        return runCatching {
            if (keyValuesRepo.contains(subject, role)) {
                false
            } else {
                keyValuesRepo.add(
                    subject,
                    role
                )
                true
            }
        }.getOrElse {
            logger.w(it) { "Unable to include role \"$role\" to the subject \"$subject\"" }
            false
        }.also {
            if (it) {
                _roleIncluded.emit(subject to role)
            }
        }
    }

    override suspend fun excludeDirect(subject: BaseRoleSubject, role: BaseRole): Boolean {
        if (!protectedRolesRepo.allowExclude(subject)) {
            logger.w { "Unable to exclude role \"$role\" to the subject \"$subject\" (protected)" }
            return false
        }

        return runCatching {
            keyValuesRepo.remove(
                subject,
                role
            )
            true
        }.getOrElse {
            logger.w(it) { "Unable to exclude role \"$role\" to the subject \"$subject\"" }
            false
        }.also {
            if (it) {
                _roleExcluded.emit(subject to role)
            }
        }
    }

    override suspend fun createRole(newRole: BaseRole): Boolean {
        return runCatching {
            val registered = keyValuesRepo.contains(
                BaseRoleSubject(newRole),
                BaseRole.EMPTY
            )
            if (registered) {
                false
            } else {
                includeDirect(BaseRoleSubject.OtherRole(newRole), BaseRole.EMPTY)
            }
        }.getOrElse {
            logger.w(it) { "Unable to create role \"$newRole\"" }
            false
        }.also {
            if (it) {
                _roleCreated.emit(newRole)
            }
        }
    }

    override suspend fun removeRole(role: BaseRole): Boolean {
        return runCatching {
            val subject = BaseRoleSubject.OtherRole(role)

            val subjectsWithRole = mutableListOf<BaseRoleSubject>()

            val pagination = keyValuesRepo.maxPagePagination()

            doForAllWithNextPaging(pagination) {
                keyValuesRepo.keys(role, pagination).also { keysPagination ->
                    subjectsWithRole.addAll(keysPagination.results)
                }
            }

            val roleList = listOf(role)

            keyValuesRepo.clear(subject)
            keyValuesRepo.remove(subjectsWithRole.associateWith { roleList })
            true
        }.getOrElse {
            logger.w(it) { "Unable to remove role \"$role\"" }
            false
        }.also {
            if (it) {
                _roleRemoved.emit(role)
            }
        }
    }
}