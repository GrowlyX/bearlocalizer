package io.liftgate.localize

import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
data class ComponentMapping<T>(
    val id: String,
    val owner: KClass<*>,
    val mapToValue: (T) -> String
)
