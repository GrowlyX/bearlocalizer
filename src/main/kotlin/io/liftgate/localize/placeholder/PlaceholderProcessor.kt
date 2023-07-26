package io.liftgate.localize.placeholder

import io.liftgate.localize.identity.Identity

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
interface PlaceholderProcessor
{
    fun transform(identity: Identity?, message: String): String

    fun register()
    {
        PlaceholderService.register(this)
    }
}
