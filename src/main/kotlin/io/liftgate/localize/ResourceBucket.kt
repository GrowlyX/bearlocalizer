package io.liftgate.localize

/**
 * @author GrowlyX
 * @since 7/25/2023
 */
interface ResourceBucket
{
    fun storeGenerations(descriptors: List<MethodDescriptor>)
    fun template(id: String): List<String>
}
