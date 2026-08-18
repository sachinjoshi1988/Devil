package com.devil.core.runtime.research

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.research.ResearchEvidence
import com.devil.core.model.research.ResearchEvidenceSet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class ResearchEvidenceEvaluationResultStage108Test {

    @Test
    fun `unavailable result preserves exact Stage 107 evidence set`() {
        val evidenceSet =
            createEvidenceSet()

        val result =
            ResearchEvidenceEvaluationResult.create(
                status =
                    ResearchEvidenceEvaluationStatus.UNAVAILABLE,
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
    fun `assessable result preserves exact Stage 107 evidence set`() {
        val evidenceSet =
            createEvidenceSet()

        val result =
            ResearchEvidenceEvaluationResult.create(
                status =
                    ResearchEvidenceEvaluationStatus.ASSESSABLE,
                evidenceSet = evidenceSet,
            )

        assertEquals(
            ResearchEvidenceEvaluationStatus.ASSESSABLE,
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
    fun `failed result requires an error from one preserved evidence trace`() {
        val evidenceSet =
            createEvidenceSet()

        val traceId =
            evidenceSet.evidence.first().traceId

        val error =
            UniversalErrorRecord.create(
                errorCode =
                    ErrorCode.from(
                        "STAGE_108_RESEARCH_EVALUATION_FAILURE",
                    ),
                traceId = traceId,
                occurredAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_776_660_801_000L,
                    ),
                summary =
                    "Stage 108 synthetic Research evaluation failure.",
            )

        val result =
            ResearchEvidenceEvaluationResult.create(
                status =
                    ResearchEvidenceEvaluationStatus.FAILED,
                evidenceSet = evidenceSet,
                error = error,
            )

        assertEquals(
            ResearchEvidenceEvaluationStatus.FAILED,
            result.status,
        )

        assertSame(
            evidenceSet,
            result.evidenceSet,
        )

        assertSame(
            error,
            result.error,
        )
    }

    @Test
    fun `failed result rejects unrelated error trace`() {
        val evidenceSet =
            createEvidenceSet()

        assertFailsWith<IllegalArgumentException> {
            ResearchEvidenceEvaluationResult.create(
                status =
                    ResearchEvidenceEvaluationStatus.FAILED,
                evidenceSet = evidenceSet,
                error =
                    UniversalErrorRecord.create(
                        errorCode =
                            ErrorCode.from(
                                "STAGE_108_UNRELATED_FAILURE",
                            ),
                        traceId =
                            TraceId.from(
                                "trace-stage-108-unrelated",
                            ),
                        occurredAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_776_660_802_000L,
                            ),
                        summary =
                            "Unrelated Stage 108 failure.",
                    ),
            )
        }
    }

    private fun createEvidenceSet(): ResearchEvidenceSet {
        val first =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-108-result-a",
                    ),
                sourceReference =
                    "source-stage-108-result-a",
                sourceKind =
                    "bounded-research-source",
                description =
                    "Source A supplies bounded Research evidence.",
            )

        val second =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-108-result-b",
                    ),
                sourceReference =
                    "source-stage-108-result-b",
                sourceKind =
                    "bounded-research-source",
                description =
                    "Source B supplies independent bounded Research evidence.",
            )

        return ResearchEvidenceSet.create(
            evidence =
                listOf(
                    first,
                    second,
                ),
        )
    }
}
