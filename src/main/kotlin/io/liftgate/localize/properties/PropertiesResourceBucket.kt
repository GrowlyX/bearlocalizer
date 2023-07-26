package io.liftgate.localize.properties

import io.liftgate.localize.MethodDescriptor
import io.liftgate.localize.ResourceBucket
import java.io.File
import java.util.Date
import java.util.Properties
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
class PropertiesResourceBucket(
    private val type: KClass<*>
) : ResourceBucket
{
    private val properties = Properties()
    private val mappings = mutableMapOf<String, List<String>>()

    override fun load()
    {
        properties.load(
            File(type.java.name).inputStream()
        )

        properties.stringPropertyNames()
            .forEach {
                // TODO: no support for proper string lists?
                mappings[it] = listOf(properties.getProperty(it))
            }
    }

    override fun isEmpty() = properties.isEmpty

    override fun storeGenerations(
        descriptors: List<MethodDescriptor>
    )
    {
        val generated = mutableListOf(
            "# Generated on ${Date()} by BearLocalizer",
            "# Maps to class ${type.java.name}",
            "# ----"
        )

        descriptors.forEach {
            it.description
                .forEach { description ->
                    generated += "# $description"
                }

            if (it.defaultValue.isNotEmpty())
            {
                generated += "# Default value:"

                it.defaultValue
                    .forEach { def ->
                        generated += def
                    }
            }

            generated += "${it.id}=${it.defaultValue.first()}"
        }

        File(type.java.name)
            .writeText(
                generated.joinToString("\n")
            )

        load()
    }

    override fun template(id: String): List<String>?
    {
        TODO("Not yet implemented")
    }
}