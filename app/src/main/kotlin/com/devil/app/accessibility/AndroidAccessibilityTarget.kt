package com.devil.app.accessibility

import java.util.Locale

/**
 * Preserves one explicit Stage 38 accessibility target.
 *
 * The target contains normalized text only.
 *
 * It does not represent interpreted user intent, a selected capability,
 * authorization, execution approval, observation, verification, or outcome.
 */
@ConsistentCopyVisibility
data class AndroidAccessibilityTarget private constructor(
    val text: String,
    val normalizedText: String,
) {
    companion object {

        fun fromText(
            text: String,
        ): AndroidAccessibilityTarget {
            val preservedText =
                text.trim()

            require(preservedText.isNotEmpty()) {
                "Android accessibility target text must not be blank."
            }

            return AndroidAccessibilityTarget(
                text = preservedText,
                normalizedText =
                    normalize(
                        preservedText,
                    ),
            )
        }

        internal fun normalize(
            text: String,
        ): String {
            return text
                .trim()
                .lowercase(Locale.ROOT)
                .replace(
                    Regex("\\s+"),
                    " ",
                )
        }
    }
}
