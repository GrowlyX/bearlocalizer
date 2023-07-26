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

    inline fun <reified T> build(): T
    {
        if (registry[T::class] != null)
        {
            return registry[T::class] as T
        }

        val generator = LocalizationGenerator(T::class)

        val resourceBucket = bucketBuilder(T::class)
        resourceBucket.load()

        if (resourceBucket.isEmpty())
        {
            resourceBucket.storeGenerations(
                generator.descriptors.values.toList()
            )
        }

        return buildProxyInstance(
            T::class, generator
        ).apply {
            generator.resourceBucket = resourceBucket
            registry[T::class] = this
        } as T
    }

    fun buildProxyInstance(clazz: KClass<*>, invocationHandler: InvocationHandler) = Proxy
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
