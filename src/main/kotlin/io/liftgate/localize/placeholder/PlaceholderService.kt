package io.liftgate.localize.placeholder

import io.liftgate.localize.identity.IdentityImpl

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
object PlaceholderService
{
    private var processor: PlaceholderProcessor? = null

    fun processor() = processor

    internal fun register(processor: PlaceholderProcessor)
    {
        this.processor = processor
    }
}
