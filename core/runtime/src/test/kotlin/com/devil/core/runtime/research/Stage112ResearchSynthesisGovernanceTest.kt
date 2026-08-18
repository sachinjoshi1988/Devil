package com.devil.core.runtime.research

import com.devil.core.model.common.TraceId
import com.devil.core.model.research.ResearchConfidence
import com.devil.core.model.research.ResearchConfidenceAssessment
import com.devil.core.model.research.ResearchConflictStatus
import com.devil.core.model.research.ResearchCorroborationAssessment
import com.devil.core.model.research.ResearchCorroborationStatus
import com.devil.core.model.research.ResearchEvidence
import com.devil.core.model.research.ResearchEvidenceSet
import com.devil.core.model.research.ResearchSourceAssessment
import com.devil.core.model.research.ResearchSourceAssessmentSet
import com.devil.core.model.research.ResearchSourceAuthenticity
import com.devil.core.model.research.ResearchSourceFreshness
import com.devil.core.model.research.ResearchSourceTrust
import com.devil.core.model.research.ResearchSynthesisStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage112ResearchSynthesisGovernanceTest {

    private val coordinator =
        ResearchSynthesisCoordinator()

    @Test
    fun `coordinator preserves complete Stage 107 through Stage 111 provenance`() {
        val first =
            evidence(
                trace = "trace-stage-112-first",
                source = "source-first",
                description =
                    "The first bounded source supports proposition S.",
            )

        val second =
            evidence(
                trace = "trace-stage-112-second",
                source = "source-second",
                description =
                    "The second bounded source supports proposition S.",
            )

        val confidenceAssessment =
            confidenceAssessment(
                evidence =
                    listOf(
                        first,
                        second,
                    ),
                corroboration =
                    ResearchCorroborationStatus.CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONSISTENT,
                confidence = 80,
            )

        val result =
            coordinator.establish(
                confidenceAssessment = confidenceAssessment,
                status =
                    ResearchSynthesisStatus.SYNTHESIZED,
                description =
                    "Bounded synthesis of the supplied consistent research material.",
            )

        assertSame(
            confidenceAssessment,
            result.confidenceAssessment,
        )

        assertSame(
            first,
            result.confidenceAssessment
                .corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[0],
        )

        assertSame(
            second,
            result.confidenceAssessment
                .corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[1],
        )

        assertEquals(
            ResearchSynthesisStatus.SYNTHESIZED,
            result.status,
        )
    }

    @Test
    fun `maximum confidence cannot convert represented conflict into synthesis`() {
        val supporting =
            evidence(
                trace = "trace-stage-112-support",
                source = "supporting-source",
                description =
                    "The supplied source supports claim T.",
            )

        val disputing =
            evidence(
                trace = "trace-stage-112-dispute",
                source = "disputing-source",
                description =
                    "The supplied source disputes claim T.",
            )

        val confidenceAssessment =
            confidenceAssessment(
                evidence =
                    listOf(
                        supporting,
                        disputing,
                    ),
                corroboration =
                    ResearchCorroborationStatus.CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONFLICTING,
                confidence = 100,
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.establish(
                confidenceAssessment = confidenceAssessment,
                status =
                    ResearchSynthesisStatus.SYNTHESIZED,
                description =
                    "A conflicting research state must not be silently resolved.",
            )
        }

        assertEquals(
            ResearchConflictStatus.CONFLICTING,
            confidenceAssessment
                .corroborationAssessment
                .conflict,
        )

        assertSame(
            supporting,
            confidenceAssessment
                .corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[0],
        )

        assertSame(
            disputing,
            confidenceAssessment
                .corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[1],
        )
    }

    @Test
    fun `indeterminate research remains deferred instead of becoming consensus or truth`() {
        val source =
            evidence(
                trace = "trace-stage-112-indeterminate",
                source = "indeterminate-source",
                description =
                    "One bounded source describes proposition U.",
            )

        val confidenceAssessment =
            confidenceAssessment(
                evidence =
                    listOf(
                        source,
                    ),
                corroboration =
                    ResearchCorroborationStatus.INDETERMINATE,
                conflict =
                    ResearchConflictStatus.INDETERMINATE,
                confidence = 99,
            )

        val result =
            coordinator.establish(
                confidenceAssessment = confidenceAssessment,
                status =
                    ResearchSynthesisStatus.DEFERRED,
            )

        assertSame(
            confidenceAssessment,
            result.confidenceAssessment,
        )

        assertEquals(
            ResearchSynthesisStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.description,
        )

        assertEquals(
            ResearchCorroborationStatus.INDETERMINATE,
            result.confidenceAssessment
                .corroborationAssessment
                .corroboration,
        )

        assertEquals(
            ResearchConflictStatus.INDETERMINATE,
            result.confidenceAssessment
                .corroborationAssessment
                .conflict,
        )
    }

    private fun evidence(
        trace: String,
        source: String,
        description: String,
    ): ResearchEvidence {
        return ResearchEvidence.create(
            traceId =
                TraceId.from(
                    trace,
                ),
            sourceReference =
                source,
            sourceKind =
                "stage-112-governance-test",
            description =
                description,
        )
    }

    private fun confidenceAssessment(
        evidence: List<ResearchEvidence>,
        corroboration: ResearchCorroborationStatus,
        conflict: ResearchConflictStatus,
        confidence: Int,
    ): ResearchConfidenceAssessment {
        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence = evidence,
            )

        val sourceAssessmentSet =
            ResearchSourceAssessmentSet.create(
                evidenceSet = evidenceSet,
                assessments =
                    evidence.map { item ->
                        ResearchSourceAssessment.create(
                            evidence = item,
                            authenticity =
                                ResearchSourceAuthenticity.ESTABLISHED,
                            trust =
                                ResearchSourceTrust.TRUSTED,
                            freshness =
                                ResearchSourceFreshness.CURRENT,
                        )
                    },
            )

        val corroborationAssessment =
            ResearchCorroborationAssessment.create(
                sourceAssessmentSet = sourceAssessmentSet,
                corroboration = corroboration,
                conflict = conflict,
            )

        return ResearchConfidenceAssessment.create(
            corroborationAssessment =
                corroborationAssessment,
            confidence =
                ResearchConfidence.from(
                    confidence,
                ),
        )
    }
}
