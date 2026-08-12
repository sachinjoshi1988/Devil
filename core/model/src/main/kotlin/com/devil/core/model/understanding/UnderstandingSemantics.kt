package com.devil.core.model.understanding

/**
 * Preserves structured semantic meaning established by bounded understanding.
 *
 * This record contains meaning only.
 *
 * It does not select a constitutional decision, authorize work, select a
 * capability, execute an action, create an observation, verify an effect,
 * or establish an Outcome.
 */
@ConsistentCopyVisibility
data class UnderstandingSemantics private constructor(
    val intent: UnderstandingIntent,
    val actionability: UnderstandingActionability,
    val meaning: String,
    val target: String?,
) {

    companion object {

        fun create(
            intent: UnderstandingIntent,
            actionability: UnderstandingActionability,
            meaning: String,
            target: String? = null,
        ): UnderstandingSemantics {
            val normalizedMeaning = meaning.trim()

            require(normalizedMeaning.isNotEmpty()) {
                "Understanding semantic meaning must not be blank."
            }

            val normalizedTarget =
                target
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            when (intent) {
                UnderstandingIntent.OPEN_TARGET -> {
                    require(
                        actionability ==
                            UnderstandingActionability.ACTIONABLE,
                    ) {
                        "Open-target understanding must be actionable."
                    }

                    require(normalizedTarget != null) {
                        "Open-target understanding requires a target."
                    }
                }

                UnderstandingIntent.GREETING,
                UnderstandingIntent.INFORMATIONAL,
                -> {
                    require(
                        actionability ==
                            UnderstandingActionability.NON_ACTIONABLE,
                    ) {
                        "Greeting and informational understanding must be non-actionable."
                    }

                    require(normalizedTarget == null) {
                        "Greeting and informational understanding must not contain an action target."
                    }
                }
            }

            return UnderstandingSemantics(
                intent = intent,
                actionability = actionability,
                meaning = normalizedMeaning,
                target = normalizedTarget,
            )
        }
    }
}
