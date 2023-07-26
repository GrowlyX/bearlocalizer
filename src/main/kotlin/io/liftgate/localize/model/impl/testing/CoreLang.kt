package io.liftgate.localize.model.impl.testing

import io.liftgate.localize.annotate.Component
import io.liftgate.localize.annotate.DefaultsTo
import io.liftgate.localize.annotate.Describe
import io.liftgate.localize.annotate.Id
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
    fun playerJoins(@Component("username") player: Identity): String
}
