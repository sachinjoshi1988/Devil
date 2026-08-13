package com.devil.core.runtime
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import com.devil.core.runtime.execution.ExecutionStatus
import com.devil.core.runtime.learning.LearningEvidenceStatus
import com.devil.core.runtime.learning.LearningStatus
import com.devil.core.runtime.memory.MemoryAuthorityStatus
import com.devil.core.runtime.memory.MemoryCommitmentStatus
import com.devil.core.runtime.memory.MemoryPersistenceStatus
import com.devil.core.runtime.memory.MemoryProposalStatus
import com.devil.core.runtime.observation.ObservationEvidenceStatus
import com.devil.core.runtime.observation.ObservationStatus
import com.devil.core.runtime.outcome.OutcomeEvidenceStatus
import com.devil.core.runtime.outcome.OutcomeStatus
import com.devil.core.runtime.verification.VerificationEvidenceStatus
import com.devil.core.runtime.verification.VerificationStatus
import com.devil.core.runtime.worldmodel.WorldModelUpdateEvidenceStatus
import com.devil.core.runtime.worldmodel.WorldModelUpdateStatus
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
/**
 * Stage 49 constitutional end-to-end runtime matrix.
 *
 * This test does not create another runtime, Brain, Executive, planner,
 * authority, memory domain, execution path, or production policy.
 *
 * Existing behavioral tests verify the individual authorities and runtime
 * result mappings. This matrix protects the complete production orchestration
 * shape and the critical constitutional non-equivalence boundaries in one
 * explicit Stage 49 gate.
 *
 * Conversation intake, conversation-record formation, and conversation
 * persistence are bounded Conversation Domain responsibilities. Their
 * placement does not replace or bypass the constitutional authorities.
 */
class Stage49ConstitutionalRuntimeMatrixTest {
    @Test
    fun `unified runtime preserves one ordered constitutional path`() {
        val source =
            unifiedRuntimeSource()
        val orderedMarkers =
            listOf(
                "constitutionValidationAuthority.validate(",
                "identityAuthority.resolve(",
                "trustAuthority.evaluate(",
                "authorizationAuthority.authorize(",
                "conversationIntakeAuthority.intake(",
                "conversationRecordAuthority.record(",
                "conversationPersistenceAuthority.evaluatePersistence(",
                "understandingAuthority.understand(",
                "decisionAuthority.decide(",
                "taskAuthority.createTask(",
                "planAuthority.createPlan(",
                "capabilitySelectionAuthority.select(",
                "executiveReadinessAuthority.evaluate(",
                "executionAuthority.evaluate(",
                "executionAttemptPort.attempt(",
                "observationEvidencePort.observe(",
                "observationAuthority.observe(",
                "verificationEvidencePort.verify(",
                "verificationAuthority.verify(",
                "outcomeEvidencePort.establish(",
                "outcomeAuthority.establish(",
                "worldModelUpdateEvidencePort.establish(",
                "worldModelUpdateAuthority.evaluateUpdate(",
                "learningEvidencePort.establish(",
                "learningAuthority.evaluateLearning(",
                "memoryProposalAuthority.evaluateProposal(",
                "memoryAuthority.evaluateMemory(",
                "memoryCommitmentAuthority.evaluateCommitment(",
                "memoryPersistenceAuthority.evaluatePersistence(",
            )
        var previousIndex = -1
        orderedMarkers.forEach { marker ->
            val currentIndex =
                source.indexOf(
                    marker,
                    startIndex = previousIndex + 1,
                )
            assertTrue(
                currentIndex >= 0,
                "Missing constitutional runtime marker: $marker",
            )
            assertTrue(
                currentIndex > previousIndex,
                "Constitutional runtime marker is out of order: $marker",
            )
            previousIndex = currentIndex
        }
    }
    @Test
    fun `runtime preserves trace identity across downstream constitutional evidence`() {
        val source =
            unifiedRuntimeSource()
        val traceGuards =
            listOf(
                "validation.traceId == context.traceId",
                "conversationIntake.traceId ==",
                "conversationRecord.traceId == context.traceId",
                "conversationPersistence.traceId == context.traceId",
                "readiness.traceId == context.traceId",
                "execution.traceId == context.traceId",
                "executionAttempt.traceId == context.traceId",
                "observationEvidence.traceId == context.traceId",
                "observation.traceId == context.traceId",
                "verificationEvidence.traceId == context.traceId",
                "verification.traceId == context.traceId",
                "outcomeEvidence.traceId == context.traceId",
                "outcome.traceId == context.traceId",
                "worldModelUpdateEvidence.traceId == context.traceId",
                "worldModelUpdate.traceId == context.traceId",
                "learningEvidence.traceId == context.traceId",
                "learning.traceId == context.traceId",
                "memoryProposal.traceId == context.traceId",
                "memory.traceId == context.traceId",
                "memoryCommitment.traceId == context.traceId",
                "memoryPersistence.traceId == context.traceId",
            )
        traceGuards.forEach { guard ->
            assertTrue(
                source.contains(guard),
                "Missing constitutional trace guard: $guard",
            )
        }
    }
    @Test
    fun `execution approval attempt observation verification and outcome remain distinct`() {
        assertEquals(
            ExecutionStatus.APPROVED,
            ExecutionStatus.valueOf("APPROVED"),
        )
        assertEquals(
            ExecutionAttemptStatus.ATTEMPTED,
            ExecutionAttemptStatus.valueOf("ATTEMPTED"),
        )
        assertEquals(
            ObservationEvidenceStatus.OBSERVED,
            ObservationEvidenceStatus.valueOf("OBSERVED"),
        )
        assertEquals(
            ObservationStatus.OBSERVED,
            ObservationStatus.valueOf("OBSERVED"),
        )
        assertEquals(
            VerificationEvidenceStatus.VERIFIED,
            VerificationEvidenceStatus.valueOf("VERIFIED"),
        )
        assertEquals(
            VerificationStatus.VERIFIED,
            VerificationStatus.valueOf("VERIFIED"),
        )
        assertEquals(
            OutcomeEvidenceStatus.ESTABLISHED,
            OutcomeEvidenceStatus.valueOf("ESTABLISHED"),
        )
        assertEquals(
            OutcomeStatus.ESTABLISHED,
            OutcomeStatus.valueOf("ESTABLISHED"),
        )
    }
    @Test
    fun `world model learning and memory stages remain constitutionally distinct`() {
        assertEquals(
            WorldModelUpdateEvidenceStatus.ESTABLISHED,
            WorldModelUpdateEvidenceStatus.valueOf("ESTABLISHED"),
        )

        assertEquals(
            WorldModelUpdateStatus.APPLICABLE,
            WorldModelUpdateStatus.valueOf("APPLICABLE"),
        )
        assertEquals(
            LearningEvidenceStatus.ESTABLISHED,
            LearningEvidenceStatus.valueOf("ESTABLISHED"),
        )

        assertEquals(
            LearningStatus.LEARNABLE,
            LearningStatus.valueOf("LEARNABLE"),
        )
        assertEquals(
            MemoryProposalStatus.PROPOSABLE,
            MemoryProposalStatus.valueOf("PROPOSABLE"),
        )
        assertEquals(
            MemoryAuthorityStatus.COMMITTABLE,
            MemoryAuthorityStatus.valueOf("COMMITTABLE"),
        )
        assertEquals(
            MemoryCommitmentStatus.COMMITTABLE,
            MemoryCommitmentStatus.valueOf("COMMITTABLE"),
        )
        assertEquals(
            MemoryPersistenceStatus.PERSISTABLE,
            MemoryPersistenceStatus.valueOf("PERSISTABLE"),
        )
    }
    @Test
    fun `memory persistence remains evaluation rather than fabricated persistence`() {
        val source =
            unifiedRuntimeSource()
        assertTrue(
            source.contains(
                "MemoryPersistenceStatus.PERSISTABLE",
            ),
        )
        assertTrue(
            source.contains(
                "status = RuntimeStatus.ACCEPTED",
            ),
        )
        assertTrue(
            source.contains(
                "It activates no capability",
            ),
        )
        assertTrue(
            source.contains(
                "persists, stores, exposes, recalls, deletes, or commits no logical memory",
            ),
        )
    }
    private fun unifiedRuntimeSource(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/core/runtime/DefaultUnifiedDevilRuntime.kt",
                ),
                File(
                    "core/runtime/src/main/kotlin/com/devil/core/runtime/DefaultUnifiedDevilRuntime.kt",
                ),
            )
        val sourceFile =
            candidates.firstOrNull {
                it.isFile
            }
                ?: error(
                    "Unable to locate production DefaultUnifiedDevilRuntime source.",
                )
        return sourceFile.readText()
    }
}
