package io.liftgate.localize

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
interface ResourceBucket
{
    companion object
    {
        @JvmStatic
        val EXPLICIT_NULL = emptyList<String>()
    }

    fun load()
    fun isEmpty(): Boolean

    fun storeGenerations(descriptors: List<MethodDescriptor>)
    fun template(id: String): List<String>?

    fun requiresConfigValidations(descriptors: List<MethodDescriptor>) = descriptors
        .any { template(it.id) === EXPLICIT_NULL }
}
