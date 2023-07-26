package io.liftgate.localize.identity

import java.util.*

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
interface IdentityImpl
{
    fun identity(uniqueId: UUID): Identity
    fun identity(username: String): Identity

    fun register()
    {
        IdentityService.register(this)
    }
}
