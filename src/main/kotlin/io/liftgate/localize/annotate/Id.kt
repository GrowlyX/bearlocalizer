package io.liftgate.localize.annotate

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Id(
    val value: String
)
