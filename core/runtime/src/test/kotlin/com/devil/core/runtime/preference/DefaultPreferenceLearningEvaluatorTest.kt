package com.devil.core.runtime.preference

import com.devil.core.model.common.TraceId
import com.devil.core.model.preference.PreferenceEvidence
import com.devil.core.model.preference.PreferenceEvidenceSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultPreferenceLearningEvaluatorTest {

    private val evaluator =
        DefaultPreferenceLearningEvaluator()

    @Test
    fun `one occurrence never qualifies as learned preference`() {
        val result =
            evaluator.evaluate(
                evidenceSet =
                    evidenceSet(
                        "Google Maps",
                    ),
                criteria =
                    PreferenceLearningCriteria.create(
                        minimumIndependentEvidence = 2,
                        minimumConfidence = 0.75,
                    ),
            )

        assertEquals(
            PreferenceLearningStatus.INSUFFICIENT_EVIDENCE,
            result.status,
        )
        assertEquals(
            "Google Maps",
            result.candidateValue,
        )
        assertEquals(
            1,
            result.supportingEvidenceCount,
        )
    }

    @Test
    fun `repeated independent consistent evidence may qualify`() {
        val result =
            evaluator.evaluate(
                evidenceSet =
                    evidenceSet(
                        "Google Maps",
                        "Google Maps",
                        "Google Maps",
                    ),
                criteria =
                    PreferenceLearningCriteria.create(
                        minimumIndependentEvidence = 3,
                        minimumConfidence = 0.75,
                    ),
            )

        assertEquals(
            PreferenceLearningStatus.QUALIFIED,
            result.status,
        )
        assertEquals(
            "Google Maps",
            result.candidateValue,
        )
        assertEquals(
            1.0,
            result.confidence,
        )
        assertEquals(
            3,
            result.supportingEvidenceCount,
        )
        assertEquals(
            3,
            result.totalEvidenceCount,
        )
    }

    @Test
    fun `conflicting evidence reduces confidence and may defer`() {
        val result =
            evaluator.evaluate(
                evidenceSet =
                    evidenceSet(
                        "Google Maps",
                        "Google Maps",
                        "Waze",
                    ),
                criteria =
                    PreferenceLearningCriteria.create(
                        minimumIndependentEvidence = 2,
                        minimumConfidence = 0.75,
                    ),
            )

        assertEquals(
            PreferenceLearningStatus.INSUFFICIENT_EVIDENCE,
            result.status,
        )
        assertEquals(
            "Google Maps",
            result.candidateValue,
        )
        assertEquals(
            2.0 / 3.0,
            result.confidence,
        )
    }

    @Test
    fun `equal strongest conflicting evidence remains ambiguous`() {
        val result =
            evaluator.evaluate(
                evidenceSet =
                    evidenceSet(
                        "Google Maps",
                        "Waze",
                    ),
                criteria =
                    PreferenceLearningCriteria.create(
                        minimumIndependentEvidence = 2,
                        minimumConfidence = 0.75,
                    ),
            )

        assertEquals(
            PreferenceLearningStatus.AMBIGUOUS,
            result.status,
        )
        assertNull(result.candidateValue)
        assertEquals(
            0.5,
            result.confidence,
        )
    }

    private fun evidenceSet(
        vararg values: String,
    ): PreferenceEvidenceSet {
        return PreferenceEvidenceSet.create(
            values.mapIndexed {
                    index,
                    value,
                ->
                PreferenceEvidence.create(
                    traceId =
                        TraceId.from(
                            "trace-preference-learning-${index + 1}",
                        ),
                    key = "usual-map-app",
                    value = value,
                )
            },
        )
    }
}
