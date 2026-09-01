package com.devil.app.education

import com.devil.core.model.education.AgeAppropriateTeachingRecord
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.ChildPrivacyBoundaryRecord
import com.devil.core.model.education.GuardianEducationPolicyRecord

/**
 * Stage 319 bounded Child/Guardian Alpha result.
 *
 * AVAILABLE preserves the complete existing Stage 143 -> 146 provenance.
 * DEFERRED exposes no partial Alpha composition.
 *
 * This result is evidence of bounded context preparation only.
 * It is not authentication, guardian approval, authorization, execution,
 * verified learning, disclosure, constitutional Learning, or Memory.
 */
@ConsistentCopyVisibility
data class Stage319ChildGuardianAlphaResult private constructor(
    val status: Stage319ChildGuardianAlphaStatus,
    val childEducation: ChildEducationRecord?,
    val guardianEducationPolicy: GuardianEducationPolicyRecord?,
    val ageAppropriateTeaching: AgeAppropriateTeachingRecord?,
    val childPrivacyBoundary: ChildPrivacyBoundaryRecord?,
) {
    companion object {
        fun create(
            status: Stage319ChildGuardianAlphaStatus,
            childEducation: ChildEducationRecord? = null,
            guardianEducationPolicy: GuardianEducationPolicyRecord? = null,
            ageAppropriateTeaching: AgeAppropriateTeachingRecord? = null,
            childPrivacyBoundary: ChildPrivacyBoundaryRecord? = null,
        ): Stage319ChildGuardianAlphaResult {
            when (status) {
                Stage319ChildGuardianAlphaStatus.AVAILABLE -> {
                    require(childEducation != null) {
                        "Available Stage 319 Child/Guardian Alpha requires Child Education context."
                    }
                    require(guardianEducationPolicy != null) {
                        "Available Stage 319 Child/Guardian Alpha requires Guardian Policy context."
                    }
                    require(ageAppropriateTeaching != null) {
                        "Available Stage 319 Child/Guardian Alpha requires Age-Appropriate Teaching context."
                    }
                    require(childPrivacyBoundary != null) {
                        "Available Stage 319 Child/Guardian Alpha requires Child Privacy Boundary context."
                    }
                }

                Stage319ChildGuardianAlphaStatus.DEFERRED -> {
                    require(childEducation == null) {
                        "Deferred Stage 319 result must not expose Child Education context."
                    }
                    require(guardianEducationPolicy == null) {
                        "Deferred Stage 319 result must not expose Guardian Policy context."
                    }
                    require(ageAppropriateTeaching == null) {
                        "Deferred Stage 319 result must not expose Age-Appropriate Teaching context."
                    }
                    require(childPrivacyBoundary == null) {
                        "Deferred Stage 319 result must not expose Child Privacy Boundary context."
                    }
                }
            }

            return Stage319ChildGuardianAlphaResult(
                status = status,
                childEducation = childEducation,
                guardianEducationPolicy = guardianEducationPolicy,
                ageAppropriateTeaching = ageAppropriateTeaching,
                childPrivacyBoundary = childPrivacyBoundary,
            )
        }
    }
}
