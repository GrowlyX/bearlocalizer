package io.liftgate.localize

import io.liftgate.localize.annotate.Component
import io.liftgate.localize.annotate.DefaultsTo
import io.liftgate.localize.annotate.Describe
import io.liftgate.localize.annotate.Id
import io.liftgate.localize.identity.Identity
import io.liftgate.localize.placeholder.PlaceholderService
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
class LocalizationGenerator(
    clazz: KClass<*>
) : InvocationHandler
{
    lateinit var resourceBucket: ResourceBucket
    private val mappings =
        mutableMapOf<Method, (Array<out Any>) -> List<String>>()

    val descriptors = mutableMapOf<Method, MethodDescriptor>()

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
                                id = parameter.name,
                                parameter = parameter,
                                component = parameter
                                    .getAnnotation(Component::class.java)
                                    ?.value
                            )
                        },
                )

                descriptors[it] = descriptor

                mappings[it] = { args ->
                    var template = resourceBucket
                        .template(descriptor.id)
                        ?: descriptor.defaultValue

                    for ((index, replacement) in descriptor.replacements.withIndex())
                    {
                        val replacementObject = args
                            .getOrNull(index)
                            ?: continue

                        var replacementVal = replacementObject.toString()

                        if (replacement.component != null)
                        {
                            MappingRegistry
                                .findComponentMatching(replacement.parameter.type.kotlin, replacement.component)
                                ?.apply {
                                    replacementVal = mapToValue(replacementObject)
                                }
                        } else
                        {
                            MappingRegistry
                                .defaultMappings[replacement.parameter.type.kotlin]
                                ?.apply {
                                    replacementVal = mapToValue(replacementObject)
                                }
                        }

                        template = template
                            .map { msg ->
                                msg.replace(
                                    "%${replacement.id}%",
                                    replacementVal
                                )
                            }
                            .toMutableList()
                    }

                    val selfIdentity = if (descriptor.identityIndex != -1)
                        args[descriptor.identityIndex] as Identity else null

                    template.map { message ->
                        PlaceholderService
                            .processor()
                            ?.transform(
                                selfIdentity,
                                message
                            )
                            ?: message
                    }
                }
            }
    }

    override fun invoke(
        proxy: Any,
        method: Method,
        args: Array<out Any>
    ) = mappings[method]
        ?.invoke(
            args
        )
        ?: listOf()
}
