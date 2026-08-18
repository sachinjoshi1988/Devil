package com.devil.core.runtime.research

import com.devil.core.model.common.TraceId
import com.devil.core.model.research.ResearchConflictStatus
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

class Stage110ResearchCorroborationGovernanceTest {

    private val coordinator =
        ResearchCorroborationCoordinator()

    @Test
    fun `coordinator preserves exact Stage 109 assessment set and evidence identities`() {
        val first =
            evidence(
                trace = "trace-stage-110-first",
                source = "source-first",
                description =
                    "Source one supports proposition Z.",
            )

        val second =
            evidence(
                trace = "trace-stage-110-second",
                source = "source-second",
                description =
                    "Source two also supports proposition Z.",
            )

        val sourceAssessmentSet =
            assessmentSet(
                evidence =
                    listOf(
                        first,
                        second,
                    ),
            )

        val result =
            coordinator.establish(
                sourceAssessmentSet = sourceAssessmentSet,
                corroboration =
                    ResearchCorroborationStatus.CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONSISTENT,
            )

        assertSame(
            sourceAssessmentSet,
            result.sourceAssessmentSet,
        )

        assertSame(
            first,
            result.sourceAssessmentSet
                .evidenceSet
                .evidence[0],
        )

        assertSame(
            second,
            result.sourceAssessmentSet
                .evidenceSet
                .evidence[1],
        )

        assertEquals(
            ResearchCorroborationStatus.CORROBORATED,
            result.corroboration,
        )

        assertEquals(
            ResearchConflictStatus.CONSISTENT,
            result.conflict,
        )
    }

    @Test
    fun `conflicting research remains represented as conflict rather than silently resolved`() {
        val support =
            evidence(
                trace = "trace-stage-110-support",
                source = "support-source",
                description =
                    "The supplied source supports claim Q.",
            )

        val dispute =
            evidence(
                trace = "trace-stage-110-dispute",
                source = "dispute-source",
                description =
                    "The supplied source disputes claim Q.",
            )

        val sourceAssessmentSet =
            assessmentSet(
                evidence =
                    listOf(
                        support,
                        dispute,
                    ),
            )

        val result =
            coordinator.establish(
                sourceAssessmentSet = sourceAssessmentSet,
                corroboration =
                    ResearchCorroborationStatus.NOT_CORROBORATED,
                conflict =
                    ResearchConflictStatus.CONFLICTING,
            )

        assertEquals(
            ResearchConflictStatus.CONFLICTING,
            result.conflict,
        )

        assertEquals(
            ResearchCorroborationStatus.NOT_CORROBORATED,
            result.corroboration,
        )

        assertSame(
            support,
            result.sourceAssessmentSet
                .evidenceSet
                .evidence[0],
        )

        assertSame(
            dispute,
            result.sourceAssessmentSet
                .evidenceSet
                .evidence[1],
        )
    }

    @Test
    fun `unknown research state remains indeterminate instead of becoming truth`() {
        val source =
            evidence(
                trace = "trace-stage-110-indeterminate",
                source = "source-indeterminate",
                description =
                    "One bounded source describes proposition R.",
            )

        val result =
            coordinator.establish(
                sourceAssessmentSet =
                    assessmentSet(
                        evidence =
                            listOf(
                                source,
                            ),
                    ),
                corroboration =
                    ResearchCorroborationStatus.INDETERMINATE,
                conflict =
                    ResearchConflictStatus.INDETERMINATE,
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
                "stage-110-test",
            description = description,
        )
    }

    private fun assessmentSet(
        evidence: List<ResearchEvidence>,
    ): ResearchSourceAssessmentSet {
        val evidenceSet =
            ResearchEvidenceSet.create(
                evidence = evidence,
            )

        return ResearchSourceAssessmentSet.create(
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
    }
}
