package io.liftgate.localize.test

import io.liftgate.localize.Localizer
import io.liftgate.localize.identity.Identity
import io.liftgate.localize.identity.IdentityImpl
import io.liftgate.localize.placeholder.PlaceholderProcessor
import io.liftgate.localize.properties.PropertiesResourceBucket
import org.junit.jupiter.api.Test
import java.util.*

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
class LocalizeTest
{
    @Test
    fun localizer()
    {
        val noOp = object : Identity
        {
            override fun identifier() = UUID.randomUUID()
            override fun username() = "Console"

            override fun sendMessage(message: String)
            {
                println(message)
            }

            override fun player() = Unit
        }

        Localizer
            .configure(
                object : IdentityImpl
                {
                    override fun identity(uniqueId: UUID) = noOp
                    override fun identity(username: String) = noOp
                },
                object : PlaceholderProcessor
                {
                    override fun transform(identity: Identity?, message: String) = message
                }
            ) {
                PropertiesResourceBucket(it)
            }

        val testLang = Localizer.build<TestLang>()
        println(testLang.playerJoins(noOp))
        println(testLang.playerLeaves(noOp, "destroyed"))
    }
}
