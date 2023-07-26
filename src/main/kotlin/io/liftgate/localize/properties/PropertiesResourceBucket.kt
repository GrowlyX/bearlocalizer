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
    private val type: KClass<*>,
    private val file: File = File(type.java.name)
) : ResourceBucket
{
    private val properties = Properties()
    private val mappings = mutableMapOf<String, List<String>>()

    override fun load()
    {
        if (!file.exists())
            return

        properties.load(
            file.inputStream()
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
                        generated += "# $def"
                    }
            }

            generated += "${it.id}=${it.defaultValue.first()}"
        }

        if (!file.exists())
        {
            file.createNewFile()
        }

        file.writeText(
            generated.joinToString("\n")
        )

        load()
    }

    override fun template(id: String) = mappings[id]
}
