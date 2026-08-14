package com.devil.core.model.creative

/**
 * Identifies one bounded Creative Media project.
 *
 * Project identity preserves only the identity of one explicitly supplied
 * creative-media project.
 *
 * It does not establish:
 *
 * - Devil identity;
 * - subject identity;
 * - owner identity;
 * - authentication;
 * - trust;
 * - authorization;
 * - security-session validity;
 * - a constitutional Decision;
 * - a Task or Plan;
 * - capability registration, availability, health, or readiness;
 * - execution approval;
 * - media generation;
 * - generated-output existence;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - constitutional Learning;
 * - Memory eligibility;
 * - persistence approval;
 * - or Story-to-Animation state.
 *
 * CREATIVE_MEDIA_PROJECT_ID != AUTHORITY.
 * CREATIVE_MEDIA_PROJECT_ID != CAPABILITY.
 * CREATIVE_MEDIA_PROJECT_ID != EXECUTION.
 */
@ConsistentCopyVisibility
data class CreativeMediaProjectId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): CreativeMediaProjectId {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Creative Media project identity must not be blank."
            }

            return CreativeMediaProjectId(
                value = normalizedValue,
            )
        }
    }
}
