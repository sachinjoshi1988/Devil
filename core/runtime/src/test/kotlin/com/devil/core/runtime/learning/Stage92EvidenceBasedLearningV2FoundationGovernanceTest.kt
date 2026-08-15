package com.devil.core.runtime.learning

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.EvidenceBasedLearningRecord
import com.devil.core.model.worldmodel.WorldModelRepresentation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage92EvidenceBasedLearningV2FoundationGovernanceTest {

    @Test
    fun `evidence backed world model representation may prepare bounded learning record`() {
        val traceId =
            TraceId.from(
                "trace-stage92-evidence-learning-001",
            )

        val representation =
            worldModelRepresentation(
                traceId = traceId,
            )

        val result =
            EvidenceBasedLearningCoordinator().prepare(
                traceId = traceId,
                worldModelRepresentation = representation,
                proposition =
                    "The established world-state evidence may be considered by constitutional Learning.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            representation,
            record.worldModelRepresentation,
        )

        assertEquals(
            "The established world-state evidence may be considered by constitutional Learning.",
            record.proposition,
        )
    }

    @Test
    fun `learning record preserves exact world model provenance`() {
        val representation =
            worldModelRepresentation(
                traceId =
                    TraceId.from(
                        "trace-stage92-evidence-learning-002",
                    ),
            )

        val record =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    representation,
                proposition =
                    "Preserve the exact evidence-backed representation.",
            )

        assertSame(
            representation,
            record.worldModelRepresentation,
        )
    }

    @Test
    fun `learning proposition is normalized`() {
        val record =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    worldModelRepresentation(
                        TraceId.from(
                            "trace-stage92-evidence-learning-003",
                        ),
                    ),
                proposition =
                    "  Bounded evidence-backed proposition.  ",
            )

        assertEquals(
            "Bounded evidence-backed proposition.",
            record.proposition,
        )
    }

    @Test
    fun `blank learning proposition is rejected by model`() {
        assertFailsWith<IllegalArgumentException> {
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    worldModelRepresentation(
                        TraceId.from(
                            "trace-stage92-evidence-learning-004",
                        ),
                    ),
                proposition = "   ",
            )
        }
    }

    @Test
    fun `blank proposition remains deferred at coordinator boundary`() {
        val traceId =
            TraceId.from(
                "trace-stage92-evidence-learning-005",
            )

        val result =
            EvidenceBasedLearningCoordinator().prepare(
                traceId = traceId,
                worldModelRepresentation =
                    worldModelRepresentation(traceId),
                proposition = "   ",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `world model representation from different trace fails closed`() {
        val result =
            EvidenceBasedLearningCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage92-evidence-learning-006",
                    ),
                worldModelRepresentation =
                    worldModelRepresentation(
                        TraceId.from(
                            "trace-stage92-evidence-learning-other",
                        ),
                    ),
                proposition =
                    "This must not cross trace boundaries.",
            )

        assertEquals(
            EvidenceBasedLearningPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one learning record`() {
        assertFailsWith<IllegalArgumentException> {
            EvidenceBasedLearningPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage92-result-001",
                    ),
                status =
                    EvidenceBasedLearningPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `prepared result rejects record from another trace`() {
        val record =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    worldModelRepresentation(
                        TraceId.from(
                            "trace-stage92-result-record-other",
                        ),
                    ),
                proposition =
                    "Bounded proposition.",
            )

        assertFailsWith<IllegalArgumentException> {
            EvidenceBasedLearningPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage92-result-002",
                    ),
                status =
                    EvidenceBasedLearningPreparationStatus.PREPARED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle learning record`() {
        val traceId =
            TraceId.from(
                "trace-stage92-result-003",
            )

        val record =
            EvidenceBasedLearningRecord.create(
                worldModelRepresentation =
                    worldModelRepresentation(traceId),
                proposition =
                    "Bounded proposition.",
            )

        assertFailsWith<IllegalArgumentException> {
            EvidenceBasedLearningPreparationResult.create(
                traceId = traceId,
                status =
                    EvidenceBasedLearningPreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no fabricated learning record`() {
        val result =
            EvidenceBasedLearningPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage92-result-004",
                    ),
                status =
                    EvidenceBasedLearningPreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun worldModelRepresentation(
        traceId: TraceId,
    ): WorldModelRepresentation {
        return WorldModelRepresentation.create(
            traceId = traceId,
            capabilityId =
                CapabilityId.from(
                    "capability-stage92-evidence-source",
                ),
            description =
                "Evidence-backed World Model representation established through the constitutional evidence chain.",
        )
    }
}
