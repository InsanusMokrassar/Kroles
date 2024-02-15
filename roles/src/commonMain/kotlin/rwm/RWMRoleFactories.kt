package dev.inmo.kroles.roles.rwm

import dev.inmo.kroles.roles.BaseRole


fun BaseRole.rwmRoleOrNull(): RWMRole? = RWMRole(this).takeIf { it.rightsStringNullable != null }
fun BaseRole.rwmRoleOrThrow(): RWMRole = RWMRole(this).also {
    require(it.rightsStringNullable != null)
}

fun rwmRoleOrNull(role: String): RWMRole? = BaseRole(role).rwmRoleOrNull()
fun rwmRoleOrThrow(role: String): RWMRole = BaseRole(role).rwmRoleOrThrow()
