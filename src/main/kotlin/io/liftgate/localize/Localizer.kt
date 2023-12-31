package io.liftgate.localize

import io.liftgate.localize.placeholder.NoOpPlaceholderProcessor
import io.liftgate.localize.placeholder.PlaceholderProcessor
import io.liftgate.localize.placeholder.PlaceholderService
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
    ) = of(T::class.java, builder)

    @JvmOverloads
    fun <T : Any> of(
        _class: Class<T>,
        builder: (KClass<*>) -> ResourceBucket = bucketBuilder
    ): T
    {
        val kClass = _class.kotlin
        if (registry[kClass] != null)
        {
            return registry[kClass] as T
        }

        val generator = LocalizationGenerator(kClass)

        val resourceBucket = builder(kClass)
        resourceBucket.load()

        val descriptors = generator.descriptors.values.toList()

        if (
            resourceBucket.isEmpty() ||
            resourceBucket.requiresConfigValidations(descriptors)
        )
        {
            resourceBucket.storeGenerations(descriptors)
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
            invocationHandler)

    @JvmOverloads
    fun configure(
        placeholderProcessor: PlaceholderProcessor = NoOpPlaceholderProcessor,
        bucketBuilder: (KClass<*>) -> ResourceBucket
    )
    {
        PlaceholderService.register(placeholderProcessor)

        this.bucketBuilder = bucketBuilder
    }
}
