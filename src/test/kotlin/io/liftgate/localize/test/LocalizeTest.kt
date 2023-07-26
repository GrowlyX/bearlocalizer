package io.liftgate.localize.test

import io.liftgate.localize.Localizer
import io.liftgate.localize.ResourceBucket
import io.liftgate.localize.identity.Identity
import io.liftgate.localize.identity.IdentityImpl
import io.liftgate.localize.placeholder.PlaceholderProcessor
import io.liftgate.localize.properties.PropertiesResourceBucket
import io.liftgate.localize.properties.YamlResourceBucket
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalizeTest
{
    private val noOp = object : Identity
    {
        override fun identifier() = UUID.randomUUID()
        override fun username() = "Console"

        override fun sendMessage(message: String)
        {
            println(message)
        }

        override fun player() = Unit
    }

    @BeforeAll
    fun configure()
    {
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
                TODO()
            }
    }

    @BeforeEach
    fun clearRegistry()
    {
        Localizer.registry.clear()
    }

    @Test
    fun localizeUsingYamlBucket() = buildTestUsingBucket { YamlResourceBucket(it) }

    @Test
    fun localizeUsingPropertiesBucket() = buildTestUsingBucket { PropertiesResourceBucket(it) }

    fun buildTestUsingBucket(bucketCreator: (KClass<*>) -> ResourceBucket)
    {
        Localizer.bucketBuilder = bucketCreator

        val testLang = Localizer.of<TestLang>()
        println(testLang.playerJoins(noOp))
        println(testLang.playerLeaves(noOp, "destroyed"))
    }
}
