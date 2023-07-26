package io.liftgate.localize

import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
internal object LocalizerInternals
{
    fun generateSnakeCaseID(method: Method) = method.name
        .split("(?=[A-Z])".toRegex())
        .joinToString("_") { it.lowercase() }

    fun generateFileName(kClass: KClass<*>) = kClass.java.simpleName
        .removeSuffix("Lang")
        .lowercase()
}
