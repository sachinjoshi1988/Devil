package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals

class PrivacyDisclosureCoordinatorTest {

    @Test
    fun `coordinator delegates bounded disclosure treatment`() {
        val exposure =
            PrivacyExposurePolicy().assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.SENSITIVE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = true,
                ),
            )

        val result =
            PrivacyDisclosureCoordinator().evaluate(
                exposureAssessment = exposure,
            )

        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            PrivacyDisclosureTreatment.REDACTED,
            result.treatment,
        )
    }
}
