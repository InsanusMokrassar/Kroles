package dev.inmo.kroles.roles.rwm

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

fun interface RightsChecker {
    suspend operator fun invoke(rights: RWMRole.AccessRights): Boolean
    suspend operator fun invoke(rights: String): Boolean = invoke(RWMRole.AccessRights(rights))

    operator fun plus(other: RightsChecker): RightsChecker {
        val first = this
        return RightsChecker { first(it) || other(it) }
    }

    operator fun times(other: RightsChecker): RightsChecker {
        val first = this
        return RightsChecker { first(it) && other(it) }
    }

    operator fun not(): RightsChecker {
        val first = this
        return RightsChecker { !first(it) }
    }

    @Serializable
    @JvmInline
    value class Default(private val rights: RWMRole.AccessRights) : RightsChecker {
        override suspend fun invoke(rights: RWMRole.AccessRights): Boolean = rights in this.rights
        override suspend fun invoke(rights: String): Boolean = invoke(RWMRole.AccessRights(rights))
    }

    companion object {
        operator fun invoke(rights: RWMRole.AccessRights) = Default(rights)
        operator fun invoke(rights: String) = invoke(RWMRole.AccessRights(rights))
        operator fun invoke(read: Boolean, write: Boolean, manage: Boolean) = Default(
            RWMRole.AccessRights(
                read,
                write,
                manage
            )
        )
    }
}
