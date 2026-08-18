package com.devil.core.model.research

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ResearchSourceAssessmentStage109Test {

    @Test
    fun `source assessment preserves exact research evidence`() {
        val evidence =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-109-source",
                    ),
                sourceReference =
                    "https://example.test/stage-109",
                sourceKind =
                    "external-research",
                description =
                    "Bounded Stage 109 research material.",
            )

        val assessment =
            ResearchSourceAssessment.create(
                evidence = evidence,
                authenticity =
                    ResearchSourceAuthenticity.UNKNOWN,
                trust =
                    ResearchSourceTrust.UNESTABLISHED,
                freshness =
                    ResearchSourceFreshness.UNKNOWN,
            )

        assertSame(
            evidence,
            assessment.evidence,
        )

        assertEquals(
            ResearchSourceAuthenticity.UNKNOWN,
            assessment.authenticity,
        )

        assertEquals(
            ResearchSourceTrust.UNESTABLISHED,
            assessment.trust,
        )

        assertEquals(
            ResearchSourceFreshness.UNKNOWN,
            assessment.freshness,
        )
    }

    @Test
    fun `assessment set requires exactly one assessment for each exact evidence object`() {
        val evidenceA =
            evidence(
                trace = "trace-stage-109-a",
                source = "source-a",
            )

        val evidenceB =
            evidence(
                trace = "trace-stage-109-b",
                source = "source-b",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(
                        evidenceA,
                        evidenceB,
                    ),
            )

        val assessmentA =
            assessment(evidenceA)

        assertFailsWith<IllegalArgumentException> {
            ResearchSourceAssessmentSet.create(
                evidenceSet = evidenceSet,
                assessments =
                    listOf(
                        assessmentA,
                    ),
            )
        }
    }

    private fun evidence(
        trace: String,
        source: String,
    ): ResearchEvidence {
        return ResearchEvidence.create(
            traceId = TraceId.from(trace),
            sourceReference = source,
            sourceKind = "stage-109-test",
            description = "Bounded research evidence for $source.",
        )
    }

    private fun assessment(
        evidence: ResearchEvidence,
    ): ResearchSourceAssessment {
        return ResearchSourceAssessment.create(
            evidence = evidence,
            authenticity =
                ResearchSourceAuthenticity.UNKNOWN,
            trust =
                ResearchSourceTrust.UNESTABLISHED,
            freshness =
                ResearchSourceFreshness.UNKNOWN,
        )
    }
}
