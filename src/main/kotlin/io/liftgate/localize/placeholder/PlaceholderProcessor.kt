package io.liftgate.localize.placeholder

/**
 * @author GrowlyX
 * @since 7/26/2023
 */
interface PlaceholderProcessor
{
    companion object
    {
        @JvmStatic
        fun <T> build(
            type: Class<T>,
            transformFunc: (T, String) -> String
        ): PlaceholderProcessor
        {
            return object : PlaceholderProcessor
            {
                override fun transform(identity: Any?, message: String): String
                {
                    if (identity == null)
                    {
                        return message
                    }

                    if (identity.javaClass != type)
                    {
                        return message
                    }

                    return transformFunc(identity as T, message)
                }
            }
        }
    }

    fun transform(identity: Any?, message: String): String
}
