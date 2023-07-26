package io.liftgate.localize

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
class LocalizationGenerator(
    private val clazz: KClass<*>
) : InvocationHandler
{
    private val mappings =
        mutableMapOf<Method, (Array<out Any>) -> Unit>()

    init
    {
        clazz.java.declaredMethods
            .forEach {
                val descriptor = MethodDescriptor(
                    id = it
                        .getDeclaredAnnotation(Id::class.java)
                        ?.value
                        ?: LocalizerInternals.generateSnakeCaseID(it),
                    description = it
                        .getDeclaredAnnotation(Describe::class.java)
                        ?.value?.toList()
                        ?: listOf(),
                    defaultValue = it
                        .getDeclaredAnnotation(DefaultsTo::class.java)
                        ?.value?.toList()
                        ?: listOf(),
                    replacements = it.parameters
                        .map { parameter ->
                            ParameterDescriptor(
                                component = parameter
                                    .getAnnotation(Component::class.java)
                                    ?.value
                            )
                        }
                )

                mappings[it] =
            }
    }

    override fun invoke(
        proxy: Any,
        method: Method,
        args: Array<out Any>
    ): Any
    {

    }
}
