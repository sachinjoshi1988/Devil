package com.devil.core.model.research

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class ResearchSynthesisStage112Test {

    @Test
    fun `synthesis preserves exact Stage 111 assessment and bounded description`() {
        val confidenceAssessment =
            confidenceAssessment(
                corroboration =
                    ResearchCorroborationStatus.CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONSISTENT,
                confidence = 72,
            )

        val result =
            ResearchSynthesisRecord.create(
                confidenceAssessment = confidenceAssessment,
                status = ResearchSynthesisStatus.SYNTHESIZED,
                description =
                    "  The supplied bounded research material supports proposition A.  ",
            )

        assertSame(
            confidenceAssessment,
            result.confidenceAssessment,
        )

        assertEquals(
            ResearchSynthesisStatus.SYNTHESIZED,
            result.status,
        )

        assertEquals(
            "The supplied bounded research material supports proposition A.",
            result.description,
        )
    }

    @Test
    fun `conflicting research cannot become synthesized even at maximum confidence`() {
        val confidenceAssessment =
            confidenceAssessment(
                corroboration =
                    ResearchCorroborationStatus.CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONFLICTING,
                confidence = 100,
            )

        assertFailsWith<IllegalArgumentException> {
            ResearchSynthesisRecord.create(
                confidenceAssessment = confidenceAssessment,
                status = ResearchSynthesisStatus.SYNTHESIZED,
                description =
                    "This description must not erase represented conflict.",
            )
        }
    }

    @Test
    fun `deferred synthesis carries no invented conclusion`() {
        val confidenceAssessment =
            confidenceAssessment(
                corroboration =
                    ResearchCorroborationStatus.INDETERMINATE,
                conflict =
                    ResearchConflictStatus.INDETERMINATE,
                confidence = 95,
            )

        val result =
            ResearchSynthesisRecord.create(
                confidenceAssessment = confidenceAssessment,
                status = ResearchSynthesisStatus.DEFERRED,
            )

        assertSame(
            confidenceAssessment,
            result.confidenceAssessment,
        )

        assertEquals(
            ResearchSynthesisStatus.DEFERRED,
            result.status,
        )

        assertNull(result.description)
    }

    private fun confidenceAssessment(
        corroboration: ResearchCorroborationStatus,
        conflict: ResearchConflictStatus,
        confidence: Int,
    ): ResearchConfidenceAssessment {
        val evidence =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-112-model-$confidence",
                    ),
                sourceReference =
                    "source-stage-112-model-$confidence",
                sourceKind =
                    "stage-112-model-test",
                description =
                    "Bounded Stage 112 model research evidence.",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(
                        evidence,
                    ),
            )

        val sourceAssessmentSet =
            ResearchSourceAssessmentSet.create(
                evidenceSet = evidenceSet,
                assessments =
                    listOf(
                        ResearchSourceAssessment.create(
                            evidence = evidence,
                            authenticity =
                                ResearchSourceAuthenticity.ESTABLISHED,
                            trust =
                                ResearchSourceTrust.TRUSTED,
                            freshness =
                                ResearchSourceFreshness.CURRENT,
                        ),
                    ),
            )

        val corroborationAssessment =
            ResearchCorroborationAssessment.create(
                sourceAssessmentSet = sourceAssessmentSet,
                corroboration = corroboration,
                conflict = conflict,
            )

        return ResearchConfidenceAssessment.create(
            corroborationAssessment = corroborationAssessment,
            confidence =
                ResearchConfidence.from(
                    confidence,
                ),
        )
    }
}
