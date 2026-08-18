package com.devil.core.runtime.research

import com.devil.core.model.common.TraceId
import com.devil.core.model.research.ResearchEvidence
import com.devil.core.model.research.ResearchEvidenceSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage108ResearchEvidenceEvaluationGovernanceTest {

    private val evaluator =
        DefaultResearchEvidenceEvaluator()

    @Test
    fun `default evaluator fails closed without approved Research trust policy`() {
        val evidenceSet =
            evidenceSet(
                supportingDescription =
                    "Source A supports claim X.",
                disputingDescription =
                    "Source B disputes claim X.",
            )

        val result =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
            )

        assertEquals(
            ResearchEvidenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )

        assertSame(
            evidenceSet,
            result.evidenceSet,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `evaluation preserves exact conflicting evidence objects`() {
        val supporting =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-108-support",
                    ),
                sourceReference =
                    "supporting-source-stage-108",
                sourceKind =
                    "external-analysis",
                description =
                    "The supplied source supports claim X.",
            )

        val disputing =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-108-dispute",
                    ),
                sourceReference =
                    "disputing-source-stage-108",
                sourceKind =
                    "external-analysis",
                description =
                    "The supplied source disputes claim X.",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(
                        supporting,
                        disputing,
                    ),
            )

        val result =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
            )

        assertEquals(
            ResearchEvidenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )

        assertSame(
            supporting,
            result.evidenceSet.evidence[0],
        )

        assertSame(
            disputing,
            result.evidenceSet.evidence[1],
        )
    }

    @Test
    fun `independent evidence trace identities remain independent`() {
        val evidenceSet =
            evidenceSet(
                supportingDescription =
                    "Independent source A description.",
                disputingDescription =
                    "Independent source B description.",
            )

        val result =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
            )

        assertEquals(
            2,
            result.evidenceSet
                .evidence
                .map { evidence ->
                    evidence.traceId
                }
                .distinct()
                .size,
        )
    }

    @Test
    fun `unavailable evaluation does not become consensus or synthesis`() {
        val evidenceSet =
            evidenceSet(
                supportingDescription =
                    "One source reports proposition Y.",
                disputingDescription =
                    "Another source disputes proposition Y.",
            )

        val result =
            evaluator.evaluate(
                evidenceSet = evidenceSet,
            )

        assertEquals(
            ResearchEvidenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )

        assertEquals(
            2,
            result.evidenceSet.evidence.size,
        )

        assertEquals(
            "One source reports proposition Y.",
            result.evidenceSet.evidence[0].description,
        )

        assertEquals(
            "Another source disputes proposition Y.",
            result.evidenceSet.evidence[1].description,
        )
    }

    private fun evidenceSet(
        supportingDescription: String,
        disputingDescription: String,
    ): ResearchEvidenceSet {
        return ResearchEvidenceSet.create(
            evidence =
                listOf(
                    ResearchEvidence.create(
                        traceId =
                            TraceId.from(
                                "trace-stage-108-a",
                            ),
                        sourceReference =
                            "source-stage-108-a",
                        sourceKind =
                            "external-analysis",
                        description =
                            supportingDescription,
                    ),
                    ResearchEvidence.create(
                        traceId =
                            TraceId.from(
                                "trace-stage-108-b",
                            ),
                        sourceReference =
                            "source-stage-108-b",
                        sourceKind =
                            "external-analysis",
                        description =
                            disputingDescription,
                    ),
                ),
        )
    }
}
