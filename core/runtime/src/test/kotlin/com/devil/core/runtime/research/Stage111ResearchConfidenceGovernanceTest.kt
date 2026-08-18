package com.devil.core.runtime.research

import com.devil.core.model.common.TraceId
import com.devil.core.model.research.ResearchConfidence
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Stage111ResearchConfidenceGovernanceTest {

    private val coordinator =
        ResearchConfidenceCoordinator()

    @Test
    fun `coordinator preserves exact Stage 110 assessment and full evidence provenance`() {
        val first =
            evidence(
                trace =
                    "trace-stage-111-first",
                source =
                    "source-stage-111-first",
                description =
                    "First bounded Stage 111 research source.",
            )

        val second =
            evidence(
                trace =
                    "trace-stage-111-second",
                source =
                    "source-stage-111-second",
                description =
                    "Second bounded Stage 111 research source.",
            )

        val corroborationAssessment =
            corroborationAssessment(
                evidence =
                    listOf(
                        first,
                        second,
                    ),
                corroboration =
                    ResearchCorroborationStatus.CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONSISTENT,
            )

        val result =
            coordinator.establish(
                corroborationAssessment =
                    corroborationAssessment,
                confidence =
                    ResearchConfidence.from(
                        rawValue = 82,
                    ),
            )

        assertSame(
            corroborationAssessment,
            result.corroborationAssessment,
        )

        assertSame(
            first,
            result.corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[0],
        )

        assertSame(
            second,
            result.corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[1],
        )

        assertEquals(
            82,
            result.confidence.value,
        )
    }

    @Test
    fun `high research confidence does not alter represented conflict`() {
        val supporting =
            evidence(
                trace =
                    "trace-stage-111-support",
                source =
                    "source-stage-111-support",
                description =
                    "The supplied source supports proposition S.",
            )

        val disputing =
            evidence(
                trace =
                    "trace-stage-111-dispute",
                source =
                    "source-stage-111-dispute",
                description =
                    "The supplied source disputes proposition S.",
            )

        val corroborationAssessment =
            corroborationAssessment(
                evidence =
                    listOf(
                        supporting,
                        disputing,
                    ),
                corroboration =
                    ResearchCorroborationStatus.NOT_CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONFLICTING,
            )

        val result =
            coordinator.establish(
                corroborationAssessment =
                    corroborationAssessment,
                confidence =
                    ResearchConfidence.from(
                        rawValue = 100,
                    ),
            )

        assertEquals(
            100,
            result.confidence.value,
        )

        assertEquals(
            ResearchConflictStatus.CONFLICTING,
            result.corroborationAssessment.conflict,
        )

        assertEquals(
            ResearchCorroborationStatus.NOT_CORROBORATED,
            result.corroborationAssessment.corroboration,
        )

        assertSame(
            supporting,
            result.corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[0],
        )

        assertSame(
            disputing,
            result.corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence[1],
        )
    }

    @Test
    fun `research confidence does not manufacture consensus or synthesis`() {
        val source =
            evidence(
                trace =
                    "trace-stage-111-indeterminate",
                source =
                    "source-stage-111-indeterminate",
                description =
                    "One bounded source describes proposition T.",
            )

        val corroborationAssessment =
            corroborationAssessment(
                evidence =
                    listOf(
                        source,
                    ),
                corroboration =
                    ResearchCorroborationStatus.INDETERMINATE,
                conflict =
                    ResearchConflictStatus.INDETERMINATE,
            )

        val result =
            coordinator.establish(
                corroborationAssessment =
                    corroborationAssessment,
                confidence =
                    ResearchConfidence.from(
                        rawValue = 91,
                    ),
            )

        assertEquals(
            ResearchCorroborationStatus.INDETERMINATE,
            result.corroborationAssessment.corroboration,
        )

        assertEquals(
            ResearchConflictStatus.INDETERMINATE,
            result.corroborationAssessment.conflict,
        )

        assertSame(
            source,
            result.corroborationAssessment
                .sourceAssessmentSet
                .evidenceSet
                .evidence.single(),
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
            sourceReference = source,
            sourceKind =
                "stage-111-test",
            description = description,
        )
    }

    private fun corroborationAssessment(
        evidence: List<ResearchEvidence>,
        corroboration: ResearchCorroborationStatus,
        conflict: ResearchConflictStatus,
    ): ResearchCorroborationAssessment {
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
                                ResearchSourceAuthenticity.UNKNOWN,
                            trust =
                                ResearchSourceTrust.UNESTABLISHED,
                            freshness =
                                ResearchSourceFreshness.UNKNOWN,
                        )
                    },
            )

        return ResearchCorroborationAssessment.create(
            sourceAssessmentSet =
                sourceAssessmentSet,
            corroboration =
                corroboration,
            conflict =
                conflict,
        )
    }
}
