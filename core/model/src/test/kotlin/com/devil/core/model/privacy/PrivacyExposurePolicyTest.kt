package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals

class PrivacyExposurePolicyTest {

    private val policy =
        PrivacyExposurePolicy()

    @Test
    fun `public information is allowed`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PUBLIC,
                    target =
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.ALLOWED,
            result.status,
        )
    }

    @Test
    fun `private information is allowed for bounded internal processing`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PRIVATE,
                    target =
                        PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.ALLOWED,
            result.status,
        )
    }

    @Test
    fun `private owner presentation without protected context is restricted`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PRIVATE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = false,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.RESTRICTED,
            result.status,
        )
    }

    @Test
    fun `private owner presentation with protected context is allowed`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PRIVATE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.ALLOWED,
            result.status,
        )
    }

    @Test
    fun `private information is blocked from external exposure`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PRIVATE,
                    target =
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.BLOCKED,
            result.status,
        )
    }

    @Test
    fun `sensitive information without protected context is restricted`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.SENSITIVE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = false,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.RESTRICTED,
            result.status,
        )
    }

    @Test
    fun `protected sensitive owner presentation may pass privacy gate`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.SENSITIVE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.ALLOWED,
            result.status,
        )
    }

    @Test
    fun `sensitive external exposure stays blocked even with protected context`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.SENSITIVE,
                    target =
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.BLOCKED,
            result.status,
        )
    }

    @Test
    fun `highly sensitive external exposure fails closed`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.HIGHLY_SENSITIVE,
                    target =
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.BLOCKED,
            result.status,
        )
    }

    @Test
    fun `highly sensitive owner presentation fails closed`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.HIGHLY_SENSITIVE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.BLOCKED,
            result.status,
        )
    }

    @Test
    fun `highly sensitive data requires protected internal processing`() {
        val result =
            policy.assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.HIGHLY_SENSITIVE,
                    target =
                        PrivacyExposureTarget.INTERNAL_PROCESSING,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyExposureStatus.ALLOWED,
            result.status,
        )
    }
}
