package io.liftgate.localize.identity

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
object IdentityService
{
    private var identity: IdentityImpl? = null

    fun identities() = identity
        ?: throw IllegalStateException(
            "Identity services have not yet been registered for this platform"
        )

    internal fun register(identity: IdentityImpl)
    {
        this.identity = identity
    }
}
