package io.liftgate.localize.test

import io.liftgate.localize.annotate.*
import io.liftgate.localize.identity.Identity

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
interface TestLang
{
    @Colored
    @Id("player_login")
    @Describe("Broadcasts this message to the server when a player logs in!")
    @DefaultsTo("%player% joined the game!")
    fun playerJoins(
        @Self
        @Component("username")
        player: Identity
    ): List<String>

    @Colored
    @Describe("Broadcasts this message to the server when a player logs out!")
    @DefaultsTo("%player% left the game for %reason%!")
    fun playerLeaves(
        @Self
        @Component("username")
        player: Identity,
        reason: String
    ): List<String>
}
