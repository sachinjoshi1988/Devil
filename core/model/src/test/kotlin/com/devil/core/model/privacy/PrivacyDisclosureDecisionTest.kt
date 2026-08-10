package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PrivacyDisclosureDecisionTest {

    @Test
    fun `available disclosure requires treatment`() {
        val request =
            request()

        assertFailsWith<IllegalArgumentException> {
            PrivacyDisclosureDecision.create(
                status =
                    PrivacyDisclosureStatus.AVAILABLE,
                treatment = null,
                request = request,
                rationale = "Available.",
            )
        }
    }

    @Test
    fun `blocked disclosure must not contain treatment`() {
        val request =
            request()

        assertFailsWith<IllegalArgumentException> {
            PrivacyDisclosureDecision.create(
                status =
                    PrivacyDisclosureStatus.BLOCKED,
                treatment =
                    PrivacyDisclosureTreatment.SUPPRESSED,
                request = request,
                rationale = "Blocked.",
            )
        }
    }

    @Test
    fun `decision preserves original exposure assessment`() {
        val request =
            request()

        val decision =
            PrivacyDisclosureDecision.create(
                status =
                    PrivacyDisclosureStatus.AVAILABLE,
                treatment =
                    PrivacyDisclosureTreatment.FULL,
                request = request,
                rationale =
                    "Bounded disclosure.",
            )

        assertEquals(
            request.exposureAssessment,
            decision.request.exposureAssessment,
        )
    }

    private fun request(): PrivacyDisclosureRequest {
        val exposure =
            PrivacyExposurePolicy().assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PUBLIC,
                    target =
                        PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )

        return PrivacyDisclosureRequest.create(
            exposureAssessment = exposure,
        )
    }
}
