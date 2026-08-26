package com.devil.core.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Stage 298 End-to-End Constitutional Tests for the established
 * DefaultUnifiedDevilRuntime constitutional path.
 *
 * This test surface validates existing end-to-end runtime behavior only.
 *
 * It does not create another runtime, Brain, Planner, Executive,
 * constitutional authority, execution path, Memory domain, or platform adapter.
 *
 * Runtime acceptance does not itself prove execution, Observation,
 * Verification, Outcome, Learning, Memory persistence, storage, or recall.
 *
 * Stage 298 does not modify Stage 49 runtime ordering and does not
 * implement Stage 299 Android Integration Tests.
 */
class Stage298EndToEndConstitutionalRuntimeTest {

    @Test
    fun `one conversation input traverses the existing unified constitutional runtime without fabricated success`() {
        val input =
            input(
                traceValue = "trace-stage298-e2e-001",
            )

        val result =
            DefaultUnifiedDevilRuntime().accept(input)

        assertEquals(
            input.context.traceId,
            result.traceId,
        )
        assertEquals(
            RuntimeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `end to end runtime preserves the protected constitutional authority ordering`() {
        val source = unifiedRuntimeSource()

        val orderedMarkers =
            listOf(
                "constitutionValidationAuthority.validate(",
                "identityAuthority.resolve(",
                "trustAuthority.evaluate(",
                "authorizationAuthority.authorize(",
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
                "memoryProposalEvidencePort.establish(",
                "memoryProposalAuthority.evaluateProposal(",
                "memoryAuthorityEvidencePort.establish(",
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
                "Missing Stage 298 constitutional runtime marker: $marker",
            )
            assertTrue(
                currentIndex > previousIndex,
                "Stage 298 constitutional runtime marker is out of order: $marker",
            )

            previousIndex = currentIndex
        }
    }

    @Test
    fun `end to end validation stops before Stage 299 Android integration testing`() {
        val source =
            File(
                "src/test/kotlin/com/devil/core/runtime/Stage298EndToEndConstitutionalRuntimeTest.kt",
            ).readText()

        assertTrue(
            source.contains(
                "does not implement Stage 299 Android Integration Tests",
            ),
        )
        assertTrue(
            source.contains(
                "Runtime acceptance does not itself prove execution",
            ),
        )
    }

    private fun input(
        traceValue: String,
    ): ConversationInput {
        return ConversationInput.create(
            context =
                ContextEnvelope.create(
                    traceId = TraceId.from(traceValue),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEST,
                    trustLevel = ContextTrustLevel.VERIFIED,
                    securityLevel = ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_298_000L,
                        ),
                ),
            content =
                "Bounded Stage 298 end-to-end constitutional runtime input.",
        )
    }

    private fun unifiedRuntimeSource(): String {
        return File(
            "src/main/kotlin/com/devil/core/runtime/DefaultUnifiedDevilRuntime.kt",
        ).readText()
    }
}
