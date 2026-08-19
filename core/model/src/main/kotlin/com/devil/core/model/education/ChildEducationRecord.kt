package com.devil.core.model.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildSubjectClassification

/**
 * Immutable Stage 143 representation of one bounded Child Education
 * integration context.
 *
 * This record preserves:
 *
 * - one existing EducationSessionRecord;
 * - one explicitly supplied existing Stage 44 ChildGuardianContext;
 * - exact subject-identity continuity between those contexts;
 * - explicit CHILD classification supplied by the existing Stage 44 context;
 * - one explicitly supplied nonblank child-education focus;
 * - one explicitly supplied nonblank child-education objective.
 *
 * This record does not:
 *
 * - infer child status from age, appearance, voice, behaviour, device,
 *   profile, relationship, or name;
 * - authenticate a child or guardian;
 * - establish guardian authority;
 * - obtain guardian approval;
 * - replace Stage 44 ChildGuardianPolicy;
 * - implement Stage 144 Guardian Policy Foundation;
 * - generate or execute lessons, homework, curriculum, or teaching;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - or communicate with Android or platform APIs.
 *
 * CHILD_EDUCATION_CONTEXT != CHILD_CLASSIFICATION.
 * CHILD_CLASSIFICATION != AUTHENTICATION.
 * CHILD_EDUCATION_INTEGRATION != GUARDIAN_AUTHORITY.
 * CHILD_EDUCATION_INTEGRATION != GUARDIAN_APPROVAL.
 * CHILD_EDUCATION_CONTEXT != EDUCATION_DELIVERED.
 * EDUCATION_DOMAIN != ANOTHER_INTELLIGENCE.
 */
@ConsistentCopyVisibility
data class ChildEducationRecord private constructor(
    val educationSession: EducationSessionRecord,
    val childGuardianContext: ChildGuardianContext,
    val childEducationFocus: String,
    val childEducationObjective: String,
) {
    companion object {

        fun create(
            educationSession: EducationSessionRecord,
            childGuardianContext: ChildGuardianContext,
            childEducationFocus: String,
            childEducationObjective: String,
        ): ChildEducationRecord {
            require(
                educationSession.subjectIdentityId ==
                    childGuardianContext.subjectIdentityId,
            ) {
                "Child Education requires matching education and child-policy subject identities."
            }

            require(
                childGuardianContext.classification ==
                    ChildSubjectClassification.CHILD,
            ) {
                "Child Education requires an explicitly supplied CHILD classification."
            }

            val normalizedFocus =
                childEducationFocus.trim()

            val normalizedObjective =
                childEducationObjective.trim()

            require(normalizedFocus.isNotEmpty()) {
                "Child Education focus must not be blank."
            }

            require(normalizedObjective.isNotEmpty()) {
                "Child Education objective must not be blank."
            }

            return ChildEducationRecord(
                educationSession = educationSession,
                childGuardianContext = childGuardianContext,
                childEducationFocus = normalizedFocus,
                childEducationObjective = normalizedObjective,
            )
        }
    }
}
