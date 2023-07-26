package io.liftgate.localize

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Component(
    val value: String
)
