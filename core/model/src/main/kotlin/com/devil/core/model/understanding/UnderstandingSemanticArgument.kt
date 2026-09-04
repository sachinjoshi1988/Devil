package com.devil.core.model.understanding

/**
 * Preserves one bounded named semantic value established during understanding.
 *
 * Values may deliberately preserve unresolved references or expressions exactly
 * enough for later authorities to reason about them without guessing.
 *
 * ARGUMENT_PRESENT != REFERENCE_RESOLVED.
 * UNRESOLVED_REFERENCE != GUESSED_REFERENCE.
 * SEMANTIC_VALUE != VERIFIED_FACT.
 */
@ConsistentCopyVisibility
data class UnderstandingSemanticArgument private constructor(
    val name: String,
    val value: String,
) {

    companion object {

        fun create(
            name: String,
            value: String,
        ): UnderstandingSemanticArgument {
            val normalizedName = name.trim()
            val normalizedValue = value.trim()

            require(normalizedName.isNotEmpty()) {
                "Understanding semantic argument name must not be blank."
            }

            require(normalizedValue.isNotEmpty()) {
                "Understanding semantic argument value must not be blank."
            }

            return UnderstandingSemanticArgument(
                name = normalizedName,
                value = normalizedValue,
            )
        }
    }
}
