package com.devil.core.runtime.research

import com.devil.core.model.common.TraceId
import com.devil.core.model.research.ResearchEvidence
import com.devil.core.model.research.ResearchEvidenceSet
import com.devil.core.model.research.ResearchSourceAssessment
import com.devil.core.model.research.ResearchSourceAuthenticity
import com.devil.core.model.research.ResearchSourceFreshness
import com.devil.core.model.research.ResearchSourceTrust
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage109ResearchSourceAssessmentGovernanceTest {

    private val coordinator =
        ResearchSourceAssessmentCoordinator()

    @Test
    fun `assessable Stage 108 result preserves exact independent evidence objects`() {
        val first =
            evidence(
                trace = "trace-stage-109-first",
                source = "source-first",
            )

        val second =
            evidence(
                trace = "trace-stage-109-second",
                source = "source-second",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(
                        first,
                        second,
                    ),
            )

        val evaluation =
            ResearchEvidenceEvaluationResult.create(
                status =
                    ResearchEvidenceEvaluationStatus.ASSESSABLE,
                evidenceSet = evidenceSet,
            )

        val firstAssessment =
            assessment(
                evidence = first,
                authenticity =
                    ResearchSourceAuthenticity.ESTABLISHED,
                trust =
                    ResearchSourceTrust.TRUSTED,
                freshness =
                    ResearchSourceFreshness.CURRENT,
            )

        val secondAssessment =
            assessment(
                evidence = second,
                authenticity =
                    ResearchSourceAuthenticity.UNKNOWN,
                trust =
                    ResearchSourceTrust.UNESTABLISHED,
                freshness =
                    ResearchSourceFreshness.UNKNOWN,
            )

        val result =
            coordinator.establish(
                evaluation = evaluation,
                assessments =
                    listOf(
                        firstAssessment,
                        secondAssessment,
                    ),
            )

        assertSame(
            evidenceSet,
            result.evidenceSet,
        )

        assertSame(
            first,
            result.assessments[0].evidence,
        )

        assertSame(
            second,
            result.assessments[1].evidence,
        )

        assertEquals(
            2,
            result
                .assessments
                .map { it.evidence.traceId }
                .distinct()
                .size,
        )
    }

    @Test
    fun `conflicting source assessments remain independent and are not synthesized`() {
        val first =
            evidence(
                trace = "trace-stage-109-trusted",
                source = "source-trusted",
            )

        val second =
            evidence(
                trace = "trace-stage-109-untrusted",
                source = "source-untrusted",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(
                        first,
                        second,
                    ),
            )

        val result =
            coordinator.establish(
                evaluation =
                    ResearchEvidenceEvaluationResult.create(
                        status =
                            ResearchEvidenceEvaluationStatus.ASSESSABLE,
                        evidenceSet = evidenceSet,
                    ),
                assessments =
                    listOf(
                        assessment(
                            evidence = first,
                            authenticity =
                                ResearchSourceAuthenticity.ESTABLISHED,
                            trust =
                                ResearchSourceTrust.TRUSTED,
                            freshness =
                                ResearchSourceFreshness.CURRENT,
                        ),
                        assessment(
                            evidence = second,
                            authenticity =
                                ResearchSourceAuthenticity.NOT_ESTABLISHED,
                            trust =
                                ResearchSourceTrust.UNTRUSTED,
                            freshness =
                                ResearchSourceFreshness.STALE,
                        ),
                    ),
            )

        assertEquals(
            ResearchSourceTrust.TRUSTED,
            result.assessments[0].trust,
        )

        assertEquals(
            ResearchSourceTrust.UNTRUSTED,
            result.assessments[1].trust,
        )

        assertSame(
            first,
            result.assessments[0].evidence,
        )

        assertSame(
            second,
            result.assessments[1].evidence,
        )
    }

    @Test
    fun `unavailable Stage 108 evaluation cannot establish source assessment set`() {
        val evidence =
            evidence(
                trace = "trace-stage-109-unavailable",
                source = "source-unavailable",
            )

        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence =
                    listOf(evidence),
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.establish(
                evaluation =
                    ResearchEvidenceEvaluationResult.create(
                        status =
                            ResearchEvidenceEvaluationStatus.UNAVAILABLE,
                        evidenceSet = evidenceSet,
                    ),
                assessments =
                    listOf(
                        assessment(
                            evidence = evidence,
                            authenticity =
                                ResearchSourceAuthenticity.UNKNOWN,
                            trust =
                                ResearchSourceTrust.UNESTABLISHED,
                            freshness =
                                ResearchSourceFreshness.UNKNOWN,
                        ),
                    ),
            )
        }
    }

    private fun evidence(
        trace: String,
        source: String,
    ): ResearchEvidence {
        return ResearchEvidence.create(
            traceId =
                TraceId.from(trace),
            sourceReference = source,
            sourceKind = "stage-109-test",
            description =
                "Stage 109 bounded evidence from $source.",
        )
    }

    private fun assessment(
        evidence: ResearchEvidence,
        authenticity: ResearchSourceAuthenticity,
        trust: ResearchSourceTrust,
        freshness: ResearchSourceFreshness,
    ): ResearchSourceAssessment {
        return ResearchSourceAssessment.create(
            evidence = evidence,
            authenticity = authenticity,
            trust = trust,
            freshness = freshness,
        )
    }
}
