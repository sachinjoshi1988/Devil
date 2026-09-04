package com.devil.core.model.understanding

/**
 * Preserves structured semantic meaning established by bounded understanding.
 *
 * This record contains meaning only.
 *
 * It does not select a constitutional decision, authorize work, select a
 * capability, execute an action, create an observation, verify an effect,
 * or establish an Outcome.
 *
 * LANGUAGE_INTERPRETED != UNDERSTANDING_AUTHORITY.
 * INTENT_RECOGNIZED != DECISION_SELECTED.
 * ACTIONABLE != AUTHORIZED.
 * ACTIONABLE != EXECUTABLE.
 * UNRESOLVED_REFERENCE != GUESSED_REFERENCE.
 * SEMANTIC_CANDIDATE != VERIFIED_USER_INTENT.
 */
@ConsistentCopyVisibility
data class UnderstandingSemantics private constructor(
    val intent: UnderstandingIntent,
    val actionability: UnderstandingActionability,
    val meaning: String,
    val target: String?,
    val predicate: String?,
    val arguments: List<UnderstandingSemanticArgument>,
) {

    companion object {

        fun create(
            intent: UnderstandingIntent,
            actionability: UnderstandingActionability,
            meaning: String,
            target: String? = null,
            predicate: String? = null,
            arguments: List<UnderstandingSemanticArgument> = emptyList(),
        ): UnderstandingSemantics {
            val normalizedMeaning = meaning.trim()

            require(normalizedMeaning.isNotEmpty()) {
                "Understanding semantic meaning must not be blank."
            }

            val normalizedTarget =
                target
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            val normalizedPredicate =
                predicate
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            val normalizedArguments =
                arguments.toList()

            require(
                normalizedArguments
                    .map { argument -> argument.name }
                    .distinct()
                    .size == normalizedArguments.size,
            ) {
                "Understanding semantic argument names must be unique."
            }

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

                    require(normalizedPredicate == null) {
                        "Open-target understanding must not contain a predicate."
                    }

                    require(normalizedArguments.isEmpty()) {
                        "Open-target understanding must not contain semantic arguments."
                    }
                }

                UnderstandingIntent.ACTION_REQUEST,
                UnderstandingIntent.INFORMATION_QUERY,
                -> {
                    require(
                        actionability ==
                            UnderstandingActionability.ACTIONABLE,
                    ) {
                        "Action-request and information-query understanding must be actionable."
                    }

                    require(normalizedTarget != null) {
                        "Action-request and information-query understanding require a target."
                    }

                    require(normalizedPredicate != null) {
                        "Action-request and information-query understanding require a predicate."
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

                    require(normalizedPredicate == null) {
                        "Greeting and informational understanding must not contain a predicate."
                    }

                    require(normalizedArguments.isEmpty()) {
                        "Greeting and informational understanding must not contain semantic arguments."
                    }
                }
            }

            return UnderstandingSemantics(
                intent = intent,
                actionability = actionability,
                meaning = normalizedMeaning,
                target = normalizedTarget,
                predicate = normalizedPredicate,
                arguments = normalizedArguments,
            )
        }
    }
}
