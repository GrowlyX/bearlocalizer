package io.liftgate.localize.model.impl.testing

import io.liftgate.localize.annotate.*
import io.liftgate.localize.identity.Identity

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
interface CoreLang
{
    @Id("events.player-login")
    @Describe("Broadcasts this message to the server when a player logs in!")
    @DefaultsTo("%player% joined the game!")
    fun playerJoins(
        @Self
        @Component("username")
        player: Identity
    ): String
}
