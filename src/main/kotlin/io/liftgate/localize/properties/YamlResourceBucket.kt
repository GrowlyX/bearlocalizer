package io.liftgate.localize.properties

import com.amihaiemil.eoyaml.Yaml
import com.amihaiemil.eoyaml.YamlMapping
import com.amihaiemil.eoyaml.YamlNode
import io.liftgate.localize.MethodDescriptor
import io.liftgate.localize.ResourceBucket
import java.io.File
import java.util.Date
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
class YamlResourceBucket(
    private val type: KClass<*>,
    private val file: File = File("${type.java.name}.yaml")
) : ResourceBucket
{
    private lateinit var mapping: YamlMapping

    override fun load()
    {
        if (!file.exists())
        {
            file.createNewFile()
        }

        mapping = Yaml
            .createYamlInput(file)
            .readYamlMapping()
    }

    override fun isEmpty() = mapping.isEmpty

    override fun storeGenerations(
        descriptors: List<MethodDescriptor>
    )
    {
        val builder = Yaml.createMutableYamlMappingBuilder()

        descriptors
            .forEach {
                val sequence = Yaml
                    .createMutableYamlSequenceBuilder()

                it.defaultValue.forEach { value ->
                    sequence.add(value)
                }

                builder.add(
                    it.id,
                    sequence.build(
                        it.description
                    )
                )
            }

        mapping = builder
            .build(
                listOf(
                    "Generated on ${Date()} by BearLocalizer",
                    "Maps to class ${type.java.name}"
                )
            )

        Yaml
            .createYamlPrinter(file.printWriter())
            .print(
                this.mapping
            )

        load()
    }

    override fun template(id: String): List<String>
    {
        val sequence = mapping
            .yamlSequence(id)

        return sequence
            .mapIndexed { index, _ ->
                sequence.string(index)
            }
    }
}
