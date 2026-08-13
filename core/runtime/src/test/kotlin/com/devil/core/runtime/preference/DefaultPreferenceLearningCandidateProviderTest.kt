package com.devil.core.runtime.preference

import com.devil.core.model.common.TraceId
import com.devil.core.model.preference.PreferenceEvidence
import com.devil.core.model.preference.PreferenceEvidenceSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultPreferenceLearningCandidateProviderTest {

    private val evaluator =
        DefaultPreferenceLearningEvaluator()

    private val provider =
        DefaultPreferenceLearningCandidateProvider()

    private val criteria =
        PreferenceLearningCriteria.create(
            minimumIndependentEvidence = 2,
            minimumConfidence = 0.75,
        )

    @Test
    fun `qualified repeated preference becomes available candidate`() {
        val evidenceSet =
            evidenceSet(
                "Google Maps",
                "Google Maps",
                "Google Maps",
            )

        val learningResult =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
                criteria = criteria,
            )

        val result =
            provider.provide(
                evidenceSet = evidenceSet,
                learningResult = learningResult,
            )

        assertEquals(
            PreferenceLearningCandidateStatus.AVAILABLE,
            result.status,
        )

        val candidate =
            requireNotNull(result.candidate)

        assertEquals(
            "usual-map-app",
            candidate.key,
        )
        assertEquals(
            "Google Maps",
            candidate.value,
        )
        assertEquals(
            3,
            candidate.supportingEvidenceCount,
        )
        assertEquals(
            3,
            candidate.totalEvidenceCount,
        )
    }

    @Test
    fun `single occurrence remains unavailable`() {
        val evidenceSet =
            evidenceSet(
                "Google Maps",
            )

        val learningResult =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
                criteria = criteria,
            )

        val result =
            provider.provide(
                evidenceSet = evidenceSet,
                learningResult = learningResult,
            )

        assertEquals(
            PreferenceLearningCandidateStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.candidate)
    }

    @Test
    fun `conflicting insufficient preference remains unavailable`() {
        val evidenceSet =
            evidenceSet(
                "Google Maps",
                "Google Maps",
                "Waze",
            )

        val learningResult =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
                criteria = criteria,
            )

        val result =
            provider.provide(
                evidenceSet = evidenceSet,
                learningResult = learningResult,
            )

        assertEquals(
            PreferenceLearningCandidateStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.candidate)
    }

    @Test
    fun `tied preference evidence remains unavailable`() {
        val evidenceSet =
            evidenceSet(
                "Google Maps",
                "Waze",
            )

        val learningResult =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
                criteria = criteria,
            )

        val result =
            provider.provide(
                evidenceSet = evidenceSet,
                learningResult = learningResult,
            )

        assertEquals(
            PreferenceLearningCandidateStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.candidate)
    }

    @Test
    fun `provider rejects result for another preference key`() {
        val evidenceSet =
            evidenceSet(
                "Google Maps",
                "Google Maps",
            )

        val foreign =
            PreferenceLearningResult.create(
                key = "usual-music-app",
                status =
                    PreferenceLearningStatus.QUALIFIED,
                candidateValue = "Google Maps",
                confidence = 1.0,
                supportingEvidenceCount = 2,
                totalEvidenceCount = 2,
            )

        assertFailsWith<IllegalArgumentException> {
            provider.provide(
                evidenceSet = evidenceSet,
                learningResult = foreign,
            )
        }
    }

    @Test
    fun `provider rejects fabricated evidence count`() {
        val evidenceSet =
            evidenceSet(
                "Google Maps",
                "Google Maps",
            )

        val fabricated =
            PreferenceLearningResult.create(
                key = "usual-map-app",
                status =
                    PreferenceLearningStatus.QUALIFIED,
                candidateValue = "Google Maps",
                confidence = 1.0,
                supportingEvidenceCount = 2,
                totalEvidenceCount = 3,
            )

        assertFailsWith<IllegalArgumentException> {
            provider.provide(
                evidenceSet = evidenceSet,
                learningResult = fabricated,
            )
        }
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
                            "trace-preference-candidate-provider-${index + 1}",
                        ),
                    key = "usual-map-app",
                    value = value,
                )
            },
        )
    }
}
