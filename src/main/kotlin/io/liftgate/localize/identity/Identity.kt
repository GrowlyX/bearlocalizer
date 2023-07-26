package io.liftgate.localize.identity

import java.util.UUID

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
interface Identity
{
    fun identifier(): UUID
    fun username(): String

    fun sendMessage(message: String)
    fun player(): Any
}
