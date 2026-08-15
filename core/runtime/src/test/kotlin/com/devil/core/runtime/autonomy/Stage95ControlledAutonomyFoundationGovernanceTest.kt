package com.devil.core.runtime.autonomy

import com.devil.core.model.autonomy.ControlledAutonomyRecord
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

class Stage95ControlledAutonomyFoundationGovernanceTest {

    @Test
    fun `strategy adaptation may prepare bounded controlled autonomy record`() {
        val traceId =
            TraceId.from(
                "trace-stage95-controlled-autonomy-001",
            )

        val adaptation =
            strategyAdaptation(
                traceId = traceId,
            )

        val result =
            ControlledAutonomyCoordinator().prepare(
                traceId = traceId,
                strategyAdaptation = adaptation,
                scope =
                    "Permit later constitutional reconsideration of the bounded failed approach.",
            )

        assertEquals(
            ControlledAutonomyPreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            adaptation,
            record.strategyAdaptation,
        )

        assertEquals(
            "Permit later constitutional reconsideration of the bounded failed approach.",
            record.scope,
        )
    }

    @Test
    fun `controlled autonomy preserves complete stage94 provenance`() {
        val adaptation =
            strategyAdaptation(
                TraceId.from(
                    "trace-stage95-controlled-autonomy-002",
                ),
            )

        val record =
            ControlledAutonomyRecord.create(
                strategyAdaptation = adaptation,
                scope =
                    "Bounded reconsideration only.",
            )

        assertSame(
            adaptation,
            record.strategyAdaptation,
        )

        assertSame(
            adaptation.failureLearning,
            record.strategyAdaptation.failureLearning,
        )

        assertSame(
            adaptation.failureLearning.evidenceBasedLearning,
            record.strategyAdaptation
                .failureLearning
                .evidenceBasedLearning,
        )

        assertSame(
            adaptation.failureLearning
                .evidenceBasedLearning
                .worldModelRepresentation,
            record.strategyAdaptation
                .failureLearning
                .evidenceBasedLearning
                .worldModelRepresentation,
        )
    }

    @Test
    fun `controlled autonomy scope is normalized`() {
        val record =
            ControlledAutonomyRecord.create(
                strategyAdaptation =
                    strategyAdaptation(
                        TraceId.from(
                            "trace-stage95-controlled-autonomy-003",
                        ),
                    ),
                scope =
                    "  Bounded constitutional reconsideration only.  ",
            )

        assertEquals(
            "Bounded constitutional reconsideration only.",
            record.scope,
        )
    }

    @Test
    fun `model rejects blank controlled autonomy scope`() {
        assertFailsWith<IllegalArgumentException> {
            ControlledAutonomyRecord.create(
                strategyAdaptation =
                    strategyAdaptation(
                        TraceId.from(
                            "trace-stage95-controlled-autonomy-004",
                        ),
                    ),
                scope = "   ",
            )
        }
    }

    @Test
    fun `blank controlled autonomy scope remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage95-controlled-autonomy-005",
            )

        val result =
            ControlledAutonomyCoordinator().prepare(
                traceId = traceId,
                strategyAdaptation =
                    strategyAdaptation(traceId),
                scope = "   ",
            )

        assertEquals(
            ControlledAutonomyPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `stage94 provenance from another trace fails closed`() {
        val result =
            ControlledAutonomyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage95-controlled-autonomy-006",
                    ),
                strategyAdaptation =
                    strategyAdaptation(
                        TraceId.from(
                            "trace-stage95-controlled-autonomy-other",
                        ),
                    ),
                scope =
                    "Cross-trace adaptation must not enter Controlled Autonomy.",
            )

        assertEquals(
            ControlledAutonomyPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one controlled autonomy record`() {
        assertFailsWith<IllegalArgumentException> {
            ControlledAutonomyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage95-result-001",
                    ),
                status =
                    ControlledAutonomyPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `prepared result rejects controlled autonomy record from another trace`() {
        val record =
            ControlledAutonomyRecord.create(
                strategyAdaptation =
                    strategyAdaptation(
                        TraceId.from(
                            "trace-stage95-result-other",
                        ),
                    ),
                scope =
                    "Bounded reconsideration only.",
            )

        assertFailsWith<IllegalArgumentException> {
            ControlledAutonomyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage95-result-002",
                    ),
                status =
                    ControlledAutonomyPreparationStatus.PREPARED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle controlled autonomy record`() {
        val traceId =
            TraceId.from(
                "trace-stage95-result-003",
            )

        val record =
            ControlledAutonomyRecord.create(
                strategyAdaptation =
                    strategyAdaptation(traceId),
                scope =
                    "Bounded reconsideration only.",
            )

        assertFailsWith<IllegalArgumentException> {
            ControlledAutonomyPreparationResult.create(
                traceId = traceId,
                status =
                    ControlledAutonomyPreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no fabricated autonomy record`() {
        val result =
            ControlledAutonomyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage95-result-004",
                    ),
                status =
                    ControlledAutonomyPreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun strategyAdaptation(
        traceId: TraceId,
    ): StrategyAdaptationRecord {
        val representation =
            WorldModelRepresentation.create(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "capability-stage95-autonomy-source",
                    ),
                description =
                    "Evidence-backed World Model representation preserved for Stage 95 Controlled Autonomy.",
            )

        val evidenceBasedLearning =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    representation,
                proposition =
                    "Evidence-backed information may be considered by constitutional Learning.",
            )

        val failureLearning =
            FailureLearningRecord.create(
                evidenceBasedLearning =
                    evidenceBasedLearning,
                outcomeState =
                    OutcomeState.VERIFIED_FAILURE,
                lesson =
                    "The verified attempted path did not achieve its intended outcome.",
            )

        return StrategyAdaptationRecord.create(
            failureLearning =
                failureLearning,
            adaptedStrategy =
                "Consider a constitutionally governed alternative approach.",
        )
    }
}
