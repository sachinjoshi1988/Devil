package com.devil.app.accessibility

/**
 * Stage 179 bounded representation of one accessibility-derived screen element.
 *
 * This record preserves supplied Android accessibility metadata only.
 *
 * It does not:
 *
 * - identify user intent;
 * - select an execution target;
 * - verify semantic meaning;
 * - perform an accessibility action;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 180 Reliable Target Resolution.
 */
@ConsistentCopyVisibility
data class AndroidScreenElementRecord private constructor(
    val position: Int,
    val text: String?,
    val contentDescription: String?,
) {
    companion object {

        fun create(
            position: Int,
            text: String?,
            contentDescription: String?,
        ): AndroidScreenElementRecord {
            require(position >= 0) {
                "Android screen element position must not be negative."
            }

            val normalizedText =
                text
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            val normalizedContentDescription =
                contentDescription
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            require(
                normalizedText != null ||
                    normalizedContentDescription != null,
            ) {
                "Android screen elements require text or content description."
            }

            return AndroidScreenElementRecord(
                position = position,
                text = normalizedText,
                contentDescription = normalizedContentDescription,
            )
        }
    }
}
