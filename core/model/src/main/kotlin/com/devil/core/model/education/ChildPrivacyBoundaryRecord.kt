package com.devil.core.model.education

import com.devil.core.model.privacy.PrivacyDisclosureDecision
import com.devil.core.model.privacy.PrivacyExposureAssessment

/**
 * Immutable Stage 146 representation of one bounded Child Privacy Boundary.
 *
 * This record preserves:
 *
 * - one existing Stage 145 Age-Appropriate Teaching context;
 * - one existing Stage 46 PrivacyExposureAssessment;
 * - one existing Stage 46 PrivacyDisclosureDecision derived from that exact
 *   exposure assessment;
 * - one explicitly supplied nonblank privacy-boundary focus.
 *
 * Stage 146 integrates existing Education and Privacy domain evidence only.
 *
 * It does not:
 *
 * - infer privacy classification;
 * - create protected-context evidence;
 * - authenticate a child or guardian;
 * - establish guardian authority or approval;
 * - replace Stage 46 privacy policy;
 * - alter privacy exposure or disclosure decisions;
 * - expose or transmit protected content;
 * - grant constitutional authorization;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - or implement Stage 147 Homework Assistance.
 *
 * CHILD_PRIVACY_BOUNDARY != PRIVACY_AUTHORIZATION.
 * PRIVACY_ALLOWED != DEVIL_AUTHORIZATION.
 * GUARDIAN_CONTEXT != PROTECTED_PRIVACY_CONTEXT.
 * PRIVACY_BOUNDARY != DISCLOSURE_OCCURRED.
 */
@ConsistentCopyVisibility
data class ChildPrivacyBoundaryRecord private constructor(
    val ageAppropriateTeaching: AgeAppropriateTeachingRecord,
    val exposureAssessment: PrivacyExposureAssessment,
    val disclosureDecision: PrivacyDisclosureDecision,
    val privacyBoundaryFocus: String,
) {
    companion object {

        fun create(
            ageAppropriateTeaching: AgeAppropriateTeachingRecord,
            exposureAssessment: PrivacyExposureAssessment,
            disclosureDecision: PrivacyDisclosureDecision,
            privacyBoundaryFocus: String,
        ): ChildPrivacyBoundaryRecord {
            require(
                disclosureDecision.request.exposureAssessment ===
                    exposureAssessment,
            ) {
                "Child Privacy Boundary disclosure decision must belong to the supplied privacy exposure assessment."
            }

            val normalizedPrivacyBoundaryFocus =
                privacyBoundaryFocus.trim()

            require(normalizedPrivacyBoundaryFocus.isNotEmpty()) {
                "Child Privacy Boundary focus must not be blank."
            }

            return ChildPrivacyBoundaryRecord(
                ageAppropriateTeaching = ageAppropriateTeaching,
                exposureAssessment = exposureAssessment,
                disclosureDecision = disclosureDecision,
                privacyBoundaryFocus = normalizedPrivacyBoundaryFocus,
            )
        }
    }
}
