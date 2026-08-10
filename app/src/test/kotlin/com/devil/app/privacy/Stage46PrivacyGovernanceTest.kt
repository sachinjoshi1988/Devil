package com.devil.app.privacy

import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyDisclosureStatus
import com.devil.core.model.privacy.PrivacyDisclosureTreatment
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureStatus
import com.devil.core.model.privacy.PrivacyExposureTarget
import com.devil.core.model.privacy.PrivacyRepresentationReducer
import com.devil.core.model.privacy.PrivacyRepresentationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage46PrivacyGovernanceTest {

    private val exposureCoordinator =
        PrivacyExposureCoordinator()

    private val disclosureCoordinator =
        PrivacyDisclosureCoordinator()

    private val representationReducer =
        PrivacyRepresentationReducer()

    @Test
    fun `private owner presentation fails closed without protected context`() {
        val exposure =
            exposureCoordinator.assess(
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
            exposure.status,
        )

        val disclosure =
            disclosureCoordinator.evaluate(
                exposureAssessment = exposure,
            )

        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            disclosure.status,
        )
        assertEquals(
            PrivacyDisclosureTreatment.METADATA_ONLY,
            disclosure.treatment,
        )

        val representation =
            representationReducer.reduce(
                decision = disclosure,
                representation =
                    "private-owner-value",
            )

        assertEquals(
            PrivacyRepresentationStatus.METADATA_ONLY,
            representation.status,
        )
        assertNull(
            representation.representation,
        )
    }

    @Test
    fun `sensitive protected owner presentation never retains original value`() {
        val exposure =
            exposureCoordinator.assess(
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
            exposure.status,
        )

        val disclosure =
            disclosureCoordinator.evaluate(
                exposureAssessment = exposure,
            )

        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            disclosure.status,
        )
        assertEquals(
            PrivacyDisclosureTreatment.REDACTED,
            disclosure.treatment,
        )

        val representation =
            representationReducer.reduce(
                decision = disclosure,
                representation =
                    "sensitive-owner-value",
            )

        assertEquals(
            PrivacyRepresentationStatus.REDACTED,
            representation.status,
        )
        assertEquals(
            "[REDACTED]",
            representation.representation,
        )
    }

    @Test
    fun `highly sensitive external exposure remains blocked`() {
        val exposure =
            exposureCoordinator.assess(
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
            exposure.status,
        )

        val disclosure =
            disclosureCoordinator.evaluate(
                exposureAssessment = exposure,
            )

        assertEquals(
            PrivacyDisclosureStatus.BLOCKED,
            disclosure.status,
        )
        assertNull(
            disclosure.treatment,
        )
    }
}
