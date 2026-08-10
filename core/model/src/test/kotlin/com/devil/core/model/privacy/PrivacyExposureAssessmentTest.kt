package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PrivacyExposureAssessmentTest {

    @Test
    fun `assessment trims rationale`() {
        val request =
            PrivacyExposureRequest.create(
                classification =
                    PrivacyDataClassification.PUBLIC,
                target =
                    PrivacyExposureTarget.INTERNAL_PROCESSING,
            )

        val assessment =
            PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.ALLOWED,
                request = request,
                rationale = "  bounded privacy decision  ",
            )

        assertEquals(
            "bounded privacy decision",
            assessment.rationale,
        )
    }

    @Test
    fun `blank rationale is rejected`() {
        val request =
            PrivacyExposureRequest.create(
                classification =
                    PrivacyDataClassification.PUBLIC,
                target =
                    PrivacyExposureTarget.INTERNAL_PROCESSING,
            )

        assertFailsWith<IllegalArgumentException> {
            PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.ALLOWED,
                request = request,
                rationale = "   ",
            )
        }
    }
}
