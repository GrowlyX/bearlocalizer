package io.liftgate.localize.test

import io.liftgate.localize.annotate.*
import io.liftgate.localize.identity.Identity

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
interface TestLang
{
    @Id("events.player-login")
    @Describe("Broadcasts this message to the server when a player logs in!")
    @DefaultsTo("%player% joined the game!")
    fun playerJoins(
        @Self
        @Component("username")
        player: Identity
    ): List<String>

    // BearLocalizer will generate ids if none is given
    @Colored
    @Describe("Broadcasts this message to the server when a player logs out!")
    @DefaultsTo("&6%player%&e left the game for &a%reason%&e!")
    fun playerLeaves(
        @Self // BearLocalizer defaults to the username if no component is provided
        player: Identity,
        reason: String
    ): List<String>
}
