package io.liftgate.localize.placeholder

/**
 * @author GrowlyX
 * @since 8/15/2023
 */
object NoOpPlaceholderProcessor : PlaceholderProcessor by (
    PlaceholderProcessor.build(Any::class.java) { _, message -> message }
)
