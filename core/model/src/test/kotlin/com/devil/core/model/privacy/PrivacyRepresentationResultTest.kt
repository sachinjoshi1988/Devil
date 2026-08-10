package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PrivacyRepresentationResultTest {

    @Test
    fun `metadata only must not retain protected representation`() {
        val result =
            PrivacyRepresentationResult.create(
                status =
                    PrivacyRepresentationStatus.METADATA_ONLY,
                classification =
                    PrivacyDataClassification.SENSITIVE,
                representation = null,
            )

        assertNull(result.representation)
    }

    @Test
    fun `suppressed must reject retained representation`() {
        assertFailsWith<IllegalArgumentException> {
            PrivacyRepresentationResult.create(
                status =
                    PrivacyRepresentationStatus.SUPPRESSED,
                classification =
                    PrivacyDataClassification.HIGHLY_SENSITIVE,
                representation =
                    "must-not-survive",
            )
        }
    }

    @Test
    fun `full requires representation`() {
        assertFailsWith<IllegalArgumentException> {
            PrivacyRepresentationResult.create(
                status =
                    PrivacyRepresentationStatus.FULL,
                classification =
                    PrivacyDataClassification.PUBLIC,
                representation = null,
            )
        }
    }
}
