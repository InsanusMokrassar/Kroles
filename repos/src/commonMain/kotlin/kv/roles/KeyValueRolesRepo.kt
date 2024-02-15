package dev.inmo.kroles.repos.kv.roles

import dev.inmo.kroles.repos.*
import dev.inmo.kroles.roles.BaseRole
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.TagLogger
import dev.inmo.micro_utils.repos.KeyValuesRepo
import dev.inmo.micro_utils.repos.MapKeyValuesRepo

class KeyValueRolesRepo(
    keyValuesRepo: KeyValuesRepo<BaseRoleSubject, BaseRole> = MapKeyValuesRepo(),
    protectedRolesRepo: ReadProtectedRolesRepo = ReadProtectedRolesRepo.AlwaysUnprotected,
    logger: KSLog = TagLogger("WriteKeyValueRolesRepo")
) : RolesRepo,
    ReadRolesRepo by ReadKeyValueRolesRepo(keyValuesRepo),
    WriteRolesRepo by WriteKeyValueRolesRepo(keyValuesRepo, protectedRolesRepo, logger)