package com.devil.core.model.education

/**
 * Immutable Stage 85 representation of one explicitly supplied educational
 * objective.
 *
 * An EducationObjective preserves:
 *
 * - one nonblank subject or topic;
 * - and one nonblank educational objective.
 *
 * The objective is supplied to this boundary.
 *
 * This type does not parse raw conversation text, infer learner intent,
 * determine curriculum, generate lessons, assess mastery, classify age,
 * apply child policy, authenticate anyone, grant authorization, execute an
 * action, perform constitutional Learning, or create Memory.
 *
 * USER_LEARNING_OBJECTIVE != DEVIL_CONSTITUTIONAL_LEARNING.
 * EDUCATION_OBJECTIVE != DECISION.
 * EDUCATION_OBJECTIVE != TASK.
 */
@ConsistentCopyVisibility
data class EducationObjective private constructor(
    val subject: String,
    val objective: String,
) {
    companion object {

        fun create(
            subject: String,
            objective: String,
        ): EducationObjective {
            val normalizedSubject = subject.trim()
            val normalizedObjective = objective.trim()

            require(normalizedSubject.isNotEmpty()) {
                "Education subject must not be blank."
            }

            require(normalizedObjective.isNotEmpty()) {
                "Education objective must not be blank."
            }

            return EducationObjective(
                subject = normalizedSubject,
                objective = normalizedObjective,
            )
        }
    }
}
