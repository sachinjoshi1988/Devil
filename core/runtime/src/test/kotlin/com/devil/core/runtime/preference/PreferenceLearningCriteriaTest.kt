package com.devil.core.runtime.preference

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PreferenceLearningCriteriaTest {

    @Test
    fun `criteria require repeated evidence`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceLearningCriteria.create(
                minimumIndependentEvidence = 1,
                minimumConfidence = 0.75,
            )
        }
    }

    @Test
    fun `criteria preserve explicit caller policy`() {
        val criteria =
            PreferenceLearningCriteria.create(
                minimumIndependentEvidence = 3,
                minimumConfidence = 0.8,
            )

        assertEquals(
            3,
            criteria.minimumIndependentEvidence,
        )
        assertEquals(
            0.8,
            criteria.minimumConfidence,
        )
    }
}
