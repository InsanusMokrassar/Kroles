package dev.inmo.kroles.repos

import dev.inmo.kroles.roles.BaseRole
import dev.inmo.micro_utils.common.Either
import dev.inmo.micro_utils.common.mapOnFirst
import dev.inmo.micro_utils.common.mapOnSecond
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

typealias BaseRolSubjectDirectIdentifier = String

@Serializable
sealed interface BaseRoleSubject {
    val rawValue: String

    companion object {
        operator fun invoke(identifier: BaseRolSubjectDirectIdentifier) = Direct(identifier)
        operator fun invoke(role: BaseRole) = OtherRole(role)
        operator fun invoke(either: Either<BaseRolSubjectDirectIdentifier, BaseRole>) = either.mapOnFirst {
            invoke(it)
        } ?: either.mapOnSecond {
            invoke(it)
        } ?: error("Unable to detect what to use in role subject creation with $either")
    }

    @Serializable
    @SerialName("RoleSubject")
    @JvmInline
    value class OtherRole(val role: BaseRole) : BaseRoleSubject { override val rawValue: String get() = role.plain }

    @Serializable
    @SerialName("CommonSubject")
    @JvmInline
    value class Direct(val identifier: BaseRolSubjectDirectIdentifier) : BaseRoleSubject { override val rawValue: String get() = identifier }
}