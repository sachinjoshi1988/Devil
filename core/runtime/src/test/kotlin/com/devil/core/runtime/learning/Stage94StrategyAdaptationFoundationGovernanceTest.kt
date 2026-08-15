package com.devil.core.runtime.learning

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.EvidenceBasedLearningRecord
import com.devil.core.model.learning.FailureLearningRecord
import com.devil.core.model.learning.StrategyAdaptationRecord
import com.devil.core.model.outcome.OutcomeState
import com.devil.core.model.worldmodel.WorldModelRepresentation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage94StrategyAdaptationFoundationGovernanceTest {

    @Test
    fun `verified failure learning may prepare bounded strategy adaptation`() {
        val traceId =
            TraceId.from(
                "trace-stage94-strategy-adaptation-001",
            )

        val failureLearning =
            failureLearning(
                traceId = traceId,
            )

        val result =
            StrategyAdaptationCoordinator().prepare(
                traceId = traceId,
                failureLearning = failureLearning,
                adaptedStrategy =
                    "Consider a constitutionally governed alternative capability path.",
            )

        assertEquals(
            StrategyAdaptationPreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            failureLearning,
            record.failureLearning,
        )

        assertEquals(
            "Consider a constitutionally governed alternative capability path.",
            record.adaptedStrategy,
        )
    }

    @Test
    fun `strategy adaptation preserves exact stage93 failure provenance`() {
        val failureLearning =
            failureLearning(
                TraceId.from(
                    "trace-stage94-strategy-adaptation-002",
                ),
            )

        val record =
            StrategyAdaptationRecord.create(
                failureLearning = failureLearning,
                adaptedStrategy =
                    "Consider another bounded planning approach.",
            )

        assertSame(
            failureLearning,
            record.failureLearning,
        )

        assertSame(
            failureLearning.evidenceBasedLearning,
            record.failureLearning.evidenceBasedLearning,
        )

        assertSame(
            failureLearning.evidenceBasedLearning
                .worldModelRepresentation,
            record.failureLearning
                .evidenceBasedLearning
                .worldModelRepresentation,
        )
    }

    @Test
    fun `adapted strategy proposition is normalized`() {
        val record =
            StrategyAdaptationRecord.create(
                failureLearning =
                    failureLearning(
                        TraceId.from(
                            "trace-stage94-strategy-adaptation-003",
                        ),
                    ),
                adaptedStrategy =
                    "  Consider a bounded alternative planning approach.  ",
            )

        assertEquals(
            "Consider a bounded alternative planning approach.",
            record.adaptedStrategy,
        )
    }

    @Test
    fun `model rejects blank adapted strategy`() {
        assertFailsWith<IllegalArgumentException> {
            StrategyAdaptationRecord.create(
                failureLearning =
                    failureLearning(
                        TraceId.from(
                            "trace-stage94-strategy-adaptation-004",
                        ),
                    ),
                adaptedStrategy = "   ",
            )
        }
    }

    @Test
    fun `blank adapted strategy remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage94-strategy-adaptation-005",
            )

        val result =
            StrategyAdaptationCoordinator().prepare(
                traceId = traceId,
                failureLearning =
                    failureLearning(traceId),
                adaptedStrategy = "   ",
            )

        assertEquals(
            StrategyAdaptationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `stage93 provenance from another trace fails closed`() {
        val result =
            StrategyAdaptationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage94-strategy-adaptation-006",
                    ),
                failureLearning =
                    failureLearning(
                        TraceId.from(
                            "trace-stage94-strategy-adaptation-other",
                        ),
                    ),
                adaptedStrategy =
                    "Cross-trace learning must not influence adaptation.",
            )

        assertEquals(
            StrategyAdaptationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one strategy adaptation record`() {
        assertFailsWith<IllegalArgumentException> {
            StrategyAdaptationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage94-result-001",
                    ),
                status =
                    StrategyAdaptationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `prepared result rejects record from another trace`() {
        val record =
            StrategyAdaptationRecord.create(
                failureLearning =
                    failureLearning(
                        TraceId.from(
                            "trace-stage94-result-other",
                        ),
                    ),
                adaptedStrategy =
                    "Consider another bounded approach.",
            )

        assertFailsWith<IllegalArgumentException> {
            StrategyAdaptationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage94-result-002",
                    ),
                status =
                    StrategyAdaptationPreparationStatus.PREPARED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle strategy adaptation record`() {
        val traceId =
            TraceId.from(
                "trace-stage94-result-003",
            )

        val record =
            StrategyAdaptationRecord.create(
                failureLearning =
                    failureLearning(traceId),
                adaptedStrategy =
                    "Consider another bounded approach.",
            )

        assertFailsWith<IllegalArgumentException> {
            StrategyAdaptationPreparationResult.create(
                traceId = traceId,
                status =
                    StrategyAdaptationPreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no fabricated strategy adaptation`() {
        val result =
            StrategyAdaptationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage94-result-004",
                    ),
                status =
                    StrategyAdaptationPreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun failureLearning(
        traceId: TraceId,
    ): FailureLearningRecord {
        val representation =
            WorldModelRepresentation.create(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "capability-stage94-strategy-source",
                    ),
                description =
                    "Evidence-backed World Model representation preserved for Stage 94 Strategy Adaptation.",
            )

        val evidenceBasedLearning =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    representation,
                proposition =
                    "Evidence-backed information may be considered by constitutional Learning.",
            )

        return FailureLearningRecord.create(
            evidenceBasedLearning =
                evidenceBasedLearning,
            outcomeState =
                OutcomeState.VERIFIED_FAILURE,
            lesson =
                "The verified attempted path did not achieve its intended outcome.",
        )
    }
}
