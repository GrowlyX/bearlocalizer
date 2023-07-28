package io.liftgate.localize

import io.liftgate.localize.identity.IdentityImpl
import io.liftgate.localize.placeholder.PlaceholderProcessor
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
object Localizer
{
    val registry = mutableMapOf<KClass<*>, Any>()
    lateinit var bucketBuilder: (KClass<*>) -> ResourceBucket

    @JvmOverloads
    inline fun <reified T : Any> of(
        noinline builder: (KClass<*>) -> ResourceBucket = bucketBuilder
    ) = of(T::class, builder)

    @JvmOverloads
    fun <T : Any> of(
        kClass: KClass<T>,
        builder: (KClass<*>) -> ResourceBucket = bucketBuilder
    ): T
    {
        if (registry[kClass] != null)
        {
            return registry[kClass] as T
        }

        val generator = LocalizationGenerator(kClass)

        val resourceBucket = builder(kClass)
        resourceBucket.load()

        if (resourceBucket.isEmpty())
        {
            resourceBucket.storeGenerations(
                generator.descriptors.values.toList()
            )
        }

        return buildProxyInstance(
            kClass, generator
        ).apply {
            generator.resourceBucket = resourceBucket
            registry[kClass] = this
        } as T
    }

    private fun buildProxyInstance(clazz: KClass<*>, invocationHandler: InvocationHandler) = Proxy
        .newProxyInstance(
            clazz.java.classLoader,
            arrayOf(clazz.java),
            invocationHandler
        )

    fun configure(
        identityImpl: IdentityImpl,
        placeholderProcessor: PlaceholderProcessor,
        bucketBuilder: (KClass<*>) -> ResourceBucket
    )
    {
        MappingRegistry.registerDefaultMappings()
        identityImpl.register()
        placeholderProcessor.register()

        this.bucketBuilder = bucketBuilder
    }
}
