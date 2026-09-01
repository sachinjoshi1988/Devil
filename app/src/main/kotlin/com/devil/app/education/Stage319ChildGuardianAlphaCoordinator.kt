package com.devil.app.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.privacy.PrivacyDisclosureDecision
import com.devil.core.model.privacy.PrivacyExposureAssessment
import com.devil.core.runtime.education.AgeAppropriateTeachingCoordinator
import com.devil.core.runtime.education.AgeAppropriateTeachingPreparationStatus
import com.devil.core.runtime.education.ChildEducationCoordinator
import com.devil.core.runtime.education.ChildEducationPreparationStatus
import com.devil.core.runtime.education.ChildPrivacyBoundaryCoordinator
import com.devil.core.runtime.education.ChildPrivacyBoundaryPreparationStatus
import com.devil.core.runtime.education.GuardianEducationPolicyCoordinator
import com.devil.core.runtime.education.GuardianEducationPolicyPreparationStatus

/**
 * Stage 319 bounded Child/Guardian Alpha coordinator.
 *
 * This coordinator composes already-existing governed contracts only:
 *
 * explicit Education session + explicit Stage 44 ChildGuardianContext
 * -> Stage 143 Child Education
 * -> explicit Stage 44 policy decision and satisfaction evidence
 * -> Stage 144 Guardian Policy Foundation
 * -> Stage 145 Age-Appropriate Teaching
 * -> explicit Stage 46 privacy exposure and disclosure evidence
 * -> Stage 146 Child Privacy Boundary
 * -> bounded Stage 319 Alpha result.
 *
 * It deliberately does not create or infer any upstream authority/evidence.
 *
 * CHILD_GUARDIAN_ALPHA != CHILD_CLASSIFICATION.
 * CHILD_CLASSIFICATION != AUTHENTICATION.
 * CHILD_GUARDIAN_ALPHA != GUARDIAN_AUTHORITY.
 * GUARDIAN_AUTHORITY != GUARDIAN_APPROVAL.
 * GUARDIAN_APPROVAL != DEVIL_AUTHORIZATION.
 * CHILD_POLICY_SATISFIED != DEVIL_AUTHORIZATION.
 * AGE_APPROPRIATE_CONTEXT != AGE_INFERENCE.
 * PRIVACY_ALLOWED != DEVIL_AUTHORIZATION.
 * PRIVACY_BOUNDARY != DISCLOSURE_OCCURRED.
 * PREPARED != EDUCATION_DELIVERED.
 * PREPARED != LEARNING_VERIFIED.
 */
class Stage319ChildGuardianAlphaCoordinator(
    private val childEducationCoordinator:
        ChildEducationCoordinator =
        ChildEducationCoordinator(),
    private val guardianEducationPolicyCoordinator:
        GuardianEducationPolicyCoordinator =
        GuardianEducationPolicyCoordinator(),
    private val ageAppropriateTeachingCoordinator:
        AgeAppropriateTeachingCoordinator =
        AgeAppropriateTeachingCoordinator(),
    private val childPrivacyBoundaryCoordinator:
        ChildPrivacyBoundaryCoordinator =
        ChildPrivacyBoundaryCoordinator(),
) {
    fun prepare(
        traceId: TraceId,
        educationSession: EducationSessionRecord,
        childGuardianContext: ChildGuardianContext,
        policyDecision: ChildPolicyDecision,
        policySatisfaction: ChildPolicySatisfactionResult,
        exposureAssessment: PrivacyExposureAssessment,
        disclosureDecision: PrivacyDisclosureDecision,
        childEducationFocus: String,
        childEducationObjective: String,
        guardianPolicyFocus: String,
        teachingLevel: String,
        teachingApproach: String,
        teachingObjective: String,
        privacyBoundaryFocus: String,
    ): Stage319ChildGuardianAlphaResult {
        val childEducationPreparation =
            childEducationCoordinator.prepare(
                traceId = traceId,
                educationSession = educationSession,
                childGuardianContext = childGuardianContext,
                childEducationFocus = childEducationFocus,
                childEducationObjective = childEducationObjective,
            )

        if (
            childEducationPreparation.status !=
            ChildEducationPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val childEducation =
            requireNotNull(childEducationPreparation.childEducation)

        val guardianPolicyPreparation =
            guardianEducationPolicyCoordinator.prepare(
                traceId = traceId,
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = policySatisfaction,
                guardianPolicyFocus = guardianPolicyFocus,
            )

        if (
            guardianPolicyPreparation.status !=
            GuardianEducationPolicyPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val guardianEducationPolicy =
            requireNotNull(guardianPolicyPreparation.guardianPolicy)

        val ageAppropriateTeachingPreparation =
            ageAppropriateTeachingCoordinator.prepare(
                traceId = traceId,
                guardianEducationPolicy = guardianEducationPolicy,
                teachingLevel = teachingLevel,
                teachingApproach = teachingApproach,
                teachingObjective = teachingObjective,
            )

        if (
            ageAppropriateTeachingPreparation.status !=
            AgeAppropriateTeachingPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val ageAppropriateTeaching =
            requireNotNull(
                ageAppropriateTeachingPreparation.teaching,
            )

        val childPrivacyBoundaryPreparation =
            childPrivacyBoundaryCoordinator.prepare(
                traceId = traceId,
                ageAppropriateTeaching = ageAppropriateTeaching,
                exposureAssessment = exposureAssessment,
                disclosureDecision = disclosureDecision,
                privacyBoundaryFocus = privacyBoundaryFocus,
            )

        if (
            childPrivacyBoundaryPreparation.status !=
            ChildPrivacyBoundaryPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val childPrivacyBoundary =
            requireNotNull(
                childPrivacyBoundaryPreparation.childPrivacyBoundary,
            )

        return Stage319ChildGuardianAlphaResult.create(
            status = Stage319ChildGuardianAlphaStatus.AVAILABLE,
            childEducation = childEducation,
            guardianEducationPolicy = guardianEducationPolicy,
            ageAppropriateTeaching = ageAppropriateTeaching,
            childPrivacyBoundary = childPrivacyBoundary,
        )
    }

    private fun deferred(): Stage319ChildGuardianAlphaResult =
        Stage319ChildGuardianAlphaResult.create(
            status = Stage319ChildGuardianAlphaStatus.DEFERRED,
        )
}
