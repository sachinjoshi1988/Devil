package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PrivacyDisclosurePolicyTest {

    private val exposurePolicy =
        PrivacyExposurePolicy()

    private val disclosurePolicy =
        PrivacyDisclosurePolicy()

    @Test
    fun `public external exposure permits full treatment`() {
        val decision =
            disclose(
                classification =
                    PrivacyDataClassification.PUBLIC,
                target =
                    PrivacyExposureTarget.EXTERNAL_SYSTEM,
            )

        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            decision.status,
        )
        assertEquals(
            PrivacyDisclosureTreatment.FULL,
            decision.treatment,
        )
    }

    @Test
    fun `restricted exposure becomes metadata only`() {
        val decision =
            disclose(
                classification =
                    PrivacyDataClassification.PRIVATE,
                target =
                    PrivacyExposureTarget.OWNER_PRESENTATION,
                protectedContextEstablished = false,
            )

        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            decision.status,
        )
        assertEquals(
            PrivacyDisclosureTreatment.METADATA_ONLY,
            decision.treatment,
        )
    }

    @Test
    fun `sensitive protected owner presentation becomes redacted`() {
        val decision =
            disclose(
                classification =
                    PrivacyDataClassification.SENSITIVE,
                target =
                    PrivacyExposureTarget.OWNER_PRESENTATION,
                protectedContextEstablished = true,
            )

        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            decision.status,
        )
        assertEquals(
            PrivacyDisclosureTreatment.REDACTED,
            decision.treatment,
        )
    }

    @Test
    fun `blocked privacy exposure produces blocked disclosure`() {
        val decision =
            disclose(
                classification =
                    PrivacyDataClassification.HIGHLY_SENSITIVE,
                target =
                    PrivacyExposureTarget.EXTERNAL_SYSTEM,
                protectedContextEstablished = true,
            )

        assertEquals(
            PrivacyDisclosureStatus.BLOCKED,
            decision.status,
        )
        assertNull(decision.treatment)
    }

    @Test
    fun `unavailable privacy exposure remains unavailable without treatment`() {
        val exposureRequest =
            PrivacyExposureRequest.create(
                classification =
                    PrivacyDataClassification.HIGHLY_SENSITIVE,
                target =
                    PrivacyExposureTarget.OWNER_PRESENTATION,
                protectedContextEstablished = false,
            )

        val unavailableExposure =
            PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.UNAVAILABLE,
                request = exposureRequest,
                rationale =
                    "Privacy exposure could not be safely established.",
            )

        val decision =
            disclosurePolicy.evaluate(
                PrivacyDisclosureRequest.create(
                    exposureAssessment = unavailableExposure,
                ),
            )

        assertEquals(
            PrivacyDisclosureStatus.UNAVAILABLE,
            decision.status,
        )
        assertNull(decision.treatment)
    }

    private fun disclose(
        classification: PrivacyDataClassification,
        target: PrivacyExposureTarget,
        protectedContextEstablished: Boolean = false,
    ): PrivacyDisclosureDecision {
        val exposure =
            exposurePolicy.assess(
                PrivacyExposureRequest.create(
                    classification = classification,
                    target = target,
                    protectedContextEstablished =
                        protectedContextEstablished,
                ),
            )

        return disclosurePolicy.evaluate(
            PrivacyDisclosureRequest.create(
                exposureAssessment = exposure,
            ),
        )
    }
}
