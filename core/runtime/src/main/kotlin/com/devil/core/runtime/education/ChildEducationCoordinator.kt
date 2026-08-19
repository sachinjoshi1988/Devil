package com.devil.core.runtime.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.EducationSessionRecord

/**
 * Stage 143 bounded Child Education Integration coordinator.
 *
 * This coordinator bridges the existing Education Domain with explicitly
 * supplied Stage 44 child-policy context.
 *
 * It accepts only an explicitly supplied CHILD classification and requires
 * exact subject-identity continuity between the Education session and the
 * ChildGuardianContext.
 *
 * It does not:
 *
 * - infer CHILD status;
 * - authenticate any subject or guardian;
 * - create ChildGuardianContext;
 * - create GuardianAuthorityRecord;
 * - obtain GuardianApprovalDecision;
 * - evaluate or replace ChildGuardianPolicy;
 * - treat guardian authority as guardian approval;
 * - implement Stage 144 Guardian Policy Foundation;
 * - perform age-appropriate adaptation;
 * - solve homework;
 * - generate lessons or curriculum;
 * - execute teaching;
 * - create another Brain, Executive, Planner, Memory Authority, or
 *   Security Authority;
 * - create Tasks or Plans;
 * - invoke execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - or communicate with Android or platform APIs.
 *
 * CHILD_EDUCATION_INTEGRATION != CHILD_CLASSIFICATION.
 * CHILD_CLASSIFICATION != AUTHENTICATION.
 * CHILD_EDUCATION_INTEGRATION != GUARDIAN_AUTHORITY.
 * CHILD_EDUCATION_INTEGRATION != GUARDIAN_APPROVAL.
 * PREPARED != EDUCATION_DELIVERED.
 */
class ChildEducationCoordinator {

    fun prepare(
        traceId: TraceId,
        educationSession: EducationSessionRecord,
        childGuardianContext: ChildGuardianContext,
        childEducationFocus: String,
        childEducationObjective: String,
    ): ChildEducationPreparationResult {
        if (
            educationSession.subjectIdentityId !=
            childGuardianContext.subjectIdentityId ||
            childGuardianContext.classification !=
            ChildSubjectClassification.CHILD ||
            childEducationFocus.isBlank() ||
            childEducationObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val childEducation =
            ChildEducationRecord.create(
                educationSession = educationSession,
                childGuardianContext = childGuardianContext,
                childEducationFocus = childEducationFocus,
                childEducationObjective = childEducationObjective,
            )

        return ChildEducationPreparationResult.create(
            traceId = traceId,
            status = ChildEducationPreparationStatus.PREPARED,
            childEducation = childEducation,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ChildEducationPreparationResult {
        return ChildEducationPreparationResult.create(
            traceId = traceId,
            status = ChildEducationPreparationStatus.DEFERRED,
        )
    }
}
