package io.liftgate.localize

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
data class ComponentMapping(
    val id: String,
    val mapToValue: (Any) -> String
)
