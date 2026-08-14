package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 85 representation of one bounded educational session.
 *
 * The record preserves only explicitly supplied education-domain metadata:
 *
 * - one education-session identity;
 * - one explicitly supplied subject identity;
 * - and one bounded educational objective.
 *
 * The subject IdentityId is descriptive input to the education domain only.
 * Its presence does not authenticate the subject or establish trust,
 * authorization, session validity, owner status, child status, or guardian
 * authority.
 *
 * This record deliberately contains no:
 *
 * - Brain;
 * - Constitution;
 * - Executive;
 * - Planner;
 * - education-specific runtime;
 * - education-specific Memory Authority;
 * - education-specific Security Authority;
 * - child classification;
 * - guardian approval;
 * - constitutional Decision;
 * - Task or Plan;
 * - capability binding;
 * - execution request;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - constitutional Learning result;
 * - Memory commitment;
 * - or persistence authority.
 *
 * EDUCATION_DOMAIN != ANOTHER_INTELLIGENCE.
 * EDUCATION_SESSION != SECURITY_SESSION.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 */
@ConsistentCopyVisibility
data class EducationSessionRecord private constructor(
    val sessionId: EducationSessionId,
    val subjectIdentityId: IdentityId,
    val objective: EducationObjective,
) {
    companion object {

        fun create(
            sessionId: EducationSessionId,
            subjectIdentityId: IdentityId,
            objective: EducationObjective,
        ): EducationSessionRecord {
            return EducationSessionRecord(
                sessionId = sessionId,
                subjectIdentityId = subjectIdentityId,
                objective = objective,
            )
        }
    }
}
