package io.liftgate.localize

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
data class MethodDescriptor(
    val id: String,
    val description: List<String>,
    val defaultValue: List<String>,
    val replacements: List<ParameterDescriptor>
)

data class ParameterDescriptor(
    val component: String? = null
)
