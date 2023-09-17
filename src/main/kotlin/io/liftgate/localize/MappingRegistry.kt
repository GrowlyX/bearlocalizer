package io.liftgate.localize

import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
object MappingRegistry
{
    const val DEFAULT_LABEL = "__default__"

    val components = mutableMapOf<KClass<*>, MutableList<ComponentMapping>>()
    val defaultMappings = mutableMapOf<KClass<*>, ComponentMapping>()

    fun findComponentMatching(kClass: KClass<*>, id: String) =
        components[kClass]?.firstOrNull { it.id == id }

    inline fun <reified T> registerDefaultComponent(
        noinline lambda: (T) -> String
    )
    {
        defaultMappings[T::class] = ComponentMapping(
            id = DEFAULT_LABEL,
            mapToValue = {
                lambda(it as T)
            }
        )
    }

    inline fun <reified T> registerComponent(
        name: String, noinline lambda: (T) -> String
    )
    {
        components.putIfAbsent(T::class, mutableListOf())
        components[T::class]!! += ComponentMapping(
            id = name,
            mapToValue = {
                lambda(it as T)
            }
        )
    }
}
