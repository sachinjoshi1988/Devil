package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PrivacyExposureRequestTest {

    @Test
    fun `request preserves explicit privacy context`() {
        val request =
            PrivacyExposureRequest.create(
                classification =
                    PrivacyDataClassification.SENSITIVE,
                target =
                    PrivacyExposureTarget.OWNER_PRESENTATION,
            )

        assertEquals(
            PrivacyDataClassification.SENSITIVE,
            request.classification,
        )
        assertEquals(
            PrivacyExposureTarget.OWNER_PRESENTATION,
            request.target,
        )
        assertFalse(
            request.protectedContextEstablished,
        )
    }
}
