package com.devil.core.model.research

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ResearchConfidenceStage111Test {

    @Test
    fun `research confidence accepts inclusive bounded range`() {
        assertEquals(
            0,
            ResearchConfidence.from(
                rawValue = 0,
            ).value,
        )

        assertEquals(
            100,
            ResearchConfidence.from(
                rawValue = 100,
            ).value,
        )
    }

    @Test
    fun `research confidence rejects values outside bounded range`() {
        assertFailsWith<IllegalArgumentException> {
            ResearchConfidence.from(
                rawValue = -1,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            ResearchConfidence.from(
                rawValue = 101,
            )
        }
    }

    @Test
    fun `confidence assessment preserves exact Stage 110 assessment`() {
        val corroborationAssessment =
            corroborationAssessment()

        val confidence =
            ResearchConfidence.from(
                rawValue = 73,
            )

        val result =
            ResearchConfidenceAssessment.create(
                corroborationAssessment =
                    corroborationAssessment,
                confidence = confidence,
            )

        assertSame(
            corroborationAssessment,
            result.corroborationAssessment,
        )

        assertSame(
            confidence,
            result.confidence,
        )

        assertEquals(
            73,
            result.confidence.value,
        )
    }

    private fun corroborationAssessment():
        ResearchCorroborationAssessment {
        val evidence =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-111-model",
                    ),
                sourceReference =
                    "source-stage-111-model",
                sourceKind =
                    "stage-111-test",
                description =
                    "Bounded Stage 111 model research evidence.",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(
                        evidence,
                    ),
            )

        val sourceAssessment =
            ResearchSourceAssessment.create(
                evidence = evidence,
                authenticity =
                    ResearchSourceAuthenticity.UNKNOWN,
                trust =
                    ResearchSourceTrust.UNESTABLISHED,
                freshness =
                    ResearchSourceFreshness.UNKNOWN,
            )

        val sourceAssessmentSet =
            ResearchSourceAssessmentSet.create(
                evidenceSet = evidenceSet,
                assessments =
                    listOf(
                        sourceAssessment,
                    ),
            )

        return ResearchCorroborationAssessment.create(
            sourceAssessmentSet =
                sourceAssessmentSet,
            corroboration =
                ResearchCorroborationStatus.INDETERMINATE,
            conflict =
                ResearchConflictStatus.INDETERMINATE,
        )
    }
}
