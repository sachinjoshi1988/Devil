package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class PrivacyRepresentationReducerTest {

    private val exposurePolicy =
        PrivacyExposurePolicy()

    private val disclosurePolicy =
        PrivacyDisclosurePolicy()

    private val reducer =
        PrivacyRepresentationReducer()

    @Test
    fun `redacted representation never preserves original value`() {
        val protectedValue =
            "owner-private-value-123"

        val result =
            reducer.reduce(
                decision =
                    decision(
                        classification =
                            PrivacyDataClassification.SENSITIVE,
                        target =
                            PrivacyExposureTarget.OWNER_PRESENTATION,
                        protectedContextEstablished = true,
                    ),
                representation = protectedValue,
            )

        assertEquals(
            PrivacyRepresentationStatus.REDACTED,
            result.status,
        )

        assertEquals(
            "[REDACTED]",
            result.representation,
        )

        assertFalse(
            requireNotNull(result.representation)
                .contains(protectedValue),
        )
    }

    @Test
    fun `metadata only removes representation entirely`() {
        val result =
            reducer.reduce(
                decision =
                    decision(
                        classification =
                            PrivacyDataClassification.PRIVATE,
                        target =
                            PrivacyExposureTarget.OWNER_PRESENTATION,
                        protectedContextEstablished = false,
                    ),
                representation =
                    "private-value",
            )

        assertEquals(
            PrivacyRepresentationStatus.METADATA_ONLY,
            result.status,
        )
        assertNull(result.representation)
    }

    @Test
    fun `full treatment preserves normalized representation`() {
        val result =
            reducer.reduce(
                decision =
                    decision(
                        classification =
                            PrivacyDataClassification.PUBLIC,
                        target =
                            PrivacyExposureTarget.EXTERNAL_SYSTEM,
                    ),
                representation =
                    "  public value  ",
            )

        assertEquals(
            PrivacyRepresentationStatus.FULL,
            result.status,
        )
        assertEquals(
            "public value",
            result.representation,
        )
    }

    private fun decision(
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
