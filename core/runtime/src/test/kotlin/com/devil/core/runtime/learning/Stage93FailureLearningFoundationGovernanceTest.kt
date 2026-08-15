package com.devil.core.runtime.learning

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.EvidenceBasedLearningRecord
import com.devil.core.model.learning.FailureLearningRecord
import com.devil.core.model.outcome.OutcomeState
import com.devil.core.model.worldmodel.WorldModelRepresentation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage93FailureLearningFoundationGovernanceTest {

    @Test
    fun `verified failure may prepare bounded failure lesson`() {
        val traceId =
            TraceId.from(
                "trace-stage93-failure-learning-001",
            )

        val learning =
            evidenceBasedLearning(
                traceId = traceId,
            )

        val result =
            FailureLearningCoordinator().prepare(
                traceId = traceId,
                evidenceBasedLearning = learning,
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "The verified attempted path did not achieve its intended outcome.",
            )

        assertEquals(
            FailureLearningPreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            learning,
            record.evidenceBasedLearning,
        )

        assertEquals(
            OutcomeState.VERIFIED_FAILURE,
            record.outcomeState,
        )

        assertEquals(
            "The verified attempted path did not achieve its intended outcome.",
            record.lesson,
        )
    }

    @Test
    fun `failure learning preserves exact stage92 evidence provenance`() {
        val learning =
            evidenceBasedLearning(
                TraceId.from(
                    "trace-stage93-failure-learning-002",
                ),
            )

        val record =
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    learning,
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "Preserve the exact evidence-backed learning provenance.",
            )

        assertSame(
            learning,
            record.evidenceBasedLearning,
        )

        assertSame(
            learning.worldModelRepresentation,
            record.evidenceBasedLearning
                .worldModelRepresentation,
        )
    }

    @Test
    fun `failure lesson is normalized`() {
        val record =
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-failure-learning-003",
                        ),
                    ),
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "  Verified attempted path requires reconsideration.  ",
            )

        assertEquals(
            "Verified attempted path requires reconsideration.",
            record.lesson,
        )
    }

    @Test
    fun `model rejects verified success as failure learning`() {
        assertFailsWith<IllegalArgumentException> {
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-failure-learning-004",
                        ),
                    ),
                outcomeState =
                    OutcomeState.VERIFIED_SUCCESS,
                lesson =
                    "This must not become a failure lesson.",
            )
        }
    }

    @Test
    fun `model rejects partial success as failure learning`() {
        assertFailsWith<IllegalArgumentException> {
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-failure-learning-005",
                        ),
                    ),
                outcomeState =
                    OutcomeState.PARTIAL_SUCCESS,
                lesson =
                    "Partial completion must not be rewritten as total failure.",
            )
        }
    }

    @Test
    fun `model rejects inconclusive result as failure learning`() {
        assertFailsWith<IllegalArgumentException> {
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-failure-learning-006",
                        ),
                    ),
                outcomeState =
                    OutcomeState.INCONCLUSIVE,
                lesson =
                    "Uncertainty must not become a failure lesson.",
            )
        }
    }

    @Test
    fun `model rejects blank failure lesson`() {
        assertFailsWith<IllegalArgumentException> {
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-failure-learning-007",
                        ),
                    ),
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson = "   ",
            )
        }
    }

    @Test
    fun `verified success remains deferred at coordinator boundary`() {
        val traceId =
            TraceId.from(
                "trace-stage93-failure-learning-008",
            )

        val result =
            FailureLearningCoordinator().prepare(
                traceId = traceId,
                evidenceBasedLearning =
                    evidenceBasedLearning(traceId),
                outcomeState =
                    OutcomeState.VERIFIED_SUCCESS,
                lesson =
                    "This supplied lesson must not be accepted.",
            )

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `partial success remains deferred rather than becoming total failure`() {
        val traceId =
            TraceId.from(
                "trace-stage93-failure-learning-009",
            )

        val result =
            FailureLearningCoordinator().prepare(
                traceId = traceId,
                evidenceBasedLearning =
                    evidenceBasedLearning(traceId),
                outcomeState =
                    OutcomeState.PARTIAL_SUCCESS,
                lesson =
                    "Partial success must retain its distinct semantics.",
            )

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `inconclusive result remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage93-failure-learning-010",
            )

        val result =
            FailureLearningCoordinator().prepare(
                traceId = traceId,
                evidenceBasedLearning =
                    evidenceBasedLearning(traceId),
                outcomeState =
                    OutcomeState.INCONCLUSIVE,
                lesson =
                    "Insufficient evidence must not become failure learning.",
            )

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank lesson remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage93-failure-learning-011",
            )

        val result =
            FailureLearningCoordinator().prepare(
                traceId = traceId,
                evidenceBasedLearning =
                    evidenceBasedLearning(traceId),
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson = "   ",
            )

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `stage92 provenance from another trace fails closed`() {
        val result =
            FailureLearningCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage93-failure-learning-012",
                    ),
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-failure-learning-other",
                        ),
                    ),
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "Cross-trace evidence must never be accepted.",
            )

        assertEquals(
            FailureLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one failure learning record`() {
        assertFailsWith<IllegalArgumentException> {
            FailureLearningPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage93-result-001",
                    ),
                status =
                    FailureLearningPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `prepared result rejects record from another trace`() {
        val record =
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(
                        TraceId.from(
                            "trace-stage93-result-other",
                        ),
                    ),
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "Bounded failure lesson.",
            )

        assertFailsWith<IllegalArgumentException> {
            FailureLearningPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage93-result-002",
                    ),
                status =
                    FailureLearningPreparationStatus.PREPARED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle failure learning record`() {
        val traceId =
            TraceId.from(
                "trace-stage93-result-003",
            )

        val record =
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning(traceId),
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "Bounded failure lesson.",
            )

        assertFailsWith<IllegalArgumentException> {
            FailureLearningPreparationResult.create(
                traceId = traceId,
                status =
                    FailureLearningPreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no fabricated failure learning record`() {
        val result =
            FailureLearningPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage93-result-004",
                    ),
                status =
                    FailureLearningPreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun evidenceBasedLearning(
        traceId: TraceId,
    ): EvidenceBasedLearningRecord {
        val representation =
            WorldModelRepresentation.create(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "capability-stage93-failure-learning-source",
                    ),
                description =
                    "Evidence-backed World Model representation preserved for Stage 93 Failure Learning.",
            )

        return EvidenceBasedLearningRecord.create(
            worldModelRepresentation =
                representation,
            proposition =
                "Evidence-backed information may be considered by constitutional Learning.",
        )
    }
}
