package io.liftgate.localize

import io.liftgate.localize.annotate.Self
import java.lang.reflect.Parameter

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
data class MethodDescriptor(
    val id: String,
    val colorize: Boolean,
    val description: List<String>,
    val defaultValue: List<String>,
    val replacements: List<ParameterDescriptor>,
    val identityIndex: Int = replacements
        .indexOfFirst {
            it.parameter.isAnnotationPresent(Self::class.java)
        }
)

data class ParameterDescriptor(
    val id: String,
    val parameter: Parameter,
    val component: String? = null,
)
