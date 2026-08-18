package com.devil.core.model.research

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ResearchCorroborationAssessmentStage110Test {

    @Test
    fun `assessment preserves exact Stage 109 source assessment set`() {
        val evidence =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-110-model",
                    ),
                sourceReference =
                    "source-stage-110-model",
                sourceKind =
                    "stage-110-test",
                description =
                    "Bounded Stage 110 research evidence.",
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

        val result =
            ResearchCorroborationAssessment.create(
                sourceAssessmentSet = sourceAssessmentSet,
                corroboration =
                    ResearchCorroborationStatus.INDETERMINATE,
                conflict =
                    ResearchConflictStatus.INDETERMINATE,
            )

        assertSame(
            sourceAssessmentSet,
            result.sourceAssessmentSet,
        )

        assertSame(
            evidence,
            result.sourceAssessmentSet
                .evidenceSet
                .evidence
                .single(),
        )

        assertEquals(
            ResearchCorroborationStatus.INDETERMINATE,
            result.corroboration,
        )

        assertEquals(
            ResearchConflictStatus.INDETERMINATE,
            result.conflict,
        )
    }
}
