package com.devil.core.model.privacy

import kotlin.test.Test
import kotlin.test.assertEquals

class PrivacyDataClassificationTest {

    @Test
    fun `privacy classifications remain stable`() {
        assertEquals(
            listOf(
                PrivacyDataClassification.PUBLIC,
                PrivacyDataClassification.PRIVATE,
                PrivacyDataClassification.SENSITIVE,
                PrivacyDataClassification.HIGHLY_SENSITIVE,
            ),
            PrivacyDataClassification.entries,
        )
    }
}
