package dev.inmo.kroles.repos.kv.roles

import dev.inmo.kroles.repos.BaseRoleSubject
import dev.inmo.kroles.repos.ReadRolesRepo
import dev.inmo.kroles.roles.BaseRole
import dev.inmo.micro_utils.pagination.Pagination
import dev.inmo.micro_utils.pagination.PaginationResult
import dev.inmo.micro_utils.pagination.changeResultsUnchecked
import dev.inmo.micro_utils.pagination.utils.doForAllWithNextPaging
import dev.inmo.micro_utils.pagination.utils.getAllByWithNextPaging
import dev.inmo.micro_utils.pagination.utils.optionallyReverse
import dev.inmo.micro_utils.pagination.utils.paginate
import dev.inmo.micro_utils.repos.ReadKeyValuesRepo
import dev.inmo.micro_utils.repos.pagination.maxPagePagination

class ReadKeyValueRolesRepo(
    private val keyValuesRepo: ReadKeyValuesRepo<BaseRoleSubject, BaseRole>,
) : ReadRolesRepo {
    override suspend fun getDirectSubjects(role: BaseRole): List<BaseRoleSubject> {
        return keyValuesRepo.getAllByWithNextPaging(keyValuesRepo.maxPagePagination()) {
            keys(role, it)
        }
    }

    override suspend fun getDirectRoles(subject: BaseRoleSubject): List<BaseRole> {
        return keyValuesRepo.getAll(subject)
    }

    override suspend fun getAll(): Map<BaseRoleSubject, List<BaseRole>> = keyValuesRepo.getAll()

    override suspend fun getAllRolesByPagination(
        pagination: Pagination,
        reversed: Boolean
    ): PaginationResult<BaseRole> {
        val customRoles = mutableSetOf<BaseRole>()
        doForAllWithNextPaging {
            keyValuesRepo.keys(it).also { paginationResult ->
                paginationResult.results.forEach { subject ->
                    if (subject is BaseRoleSubject.OtherRole) {
                        customRoles.add(subject.role as? BaseRole ?: return@forEach)
                    }
                }
            }
        }

        val result = customRoles.paginate(pagination.optionallyReverse(customRoles.size, reversed))
        return if (reversed) {
            result.changeResultsUnchecked(
                result.results.reversed()
            )
        } else {
            result
        }
    }

    override suspend fun getAllSubjectsByPagination(pagination: Pagination, reversed: Boolean): PaginationResult<BaseRoleSubject> {
        return keyValuesRepo.keys(pagination, reversed)
    }

    override suspend fun contains(subject: BaseRoleSubject, role: BaseRole): Boolean {
        return keyValuesRepo.contains(subject, role)
    }

    override suspend fun containsAny(subject: BaseRoleSubject, roles: List<BaseRole>): Boolean {
        return roles.any {
            keyValuesRepo.contains(subject, it)
        }
    }
}