package dev.inmo.kroles.roles

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class BaseRole(
    val plain: String
) {
    companion object {
        val EMPTY = BaseRole("")
    }
}
