package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals

class PrivacyExposureCoordinatorTest {

    @Test
    fun `coordinator delegates bounded privacy policy`() {
        val coordinator =
            PrivacyExposureCoordinator()

        val result =
            coordinator.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.HIGHLY_SENSITIVE,
                    target =
                        PrivacyExposureTarget.SUBJECT_PRESENTATION,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.BLOCKED,
            result.status,
        )
    }
}
