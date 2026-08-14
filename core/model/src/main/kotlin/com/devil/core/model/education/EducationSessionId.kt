package com.devil.core.model.education

/**
 * Identifies one bounded education-domain session.
 *
 * Education session identity exists only to preserve the identity of one
 * explicitly supplied educational activity.
 *
 * It does not establish:
 *
 * - Devil identity;
 * - subject identity;
 * - owner identity;
 * - child classification;
 * - guardian authority;
 * - authentication;
 * - trust;
 * - authorization;
 * - security-session validity;
 * - a constitutional Decision;
 * - a Task or Plan;
 * - capability readiness;
 * - execution approval;
 * - Learning;
 * - Memory eligibility;
 * - persistence approval;
 * - or verified Outcome.
 *
 * EDUCATION_SESSION_ID != SECURITY_SESSION_ID.
 * EDUCATION_SESSION_ID != CONVERSATION_ID.
 * EDUCATION_SESSION_ID != AUTHORITY.
 */
@ConsistentCopyVisibility
data class EducationSessionId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): EducationSessionId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Education session identity must not be blank."
            }

            return EducationSessionId(
                value = normalizedValue,
            )
        }
    }
}
