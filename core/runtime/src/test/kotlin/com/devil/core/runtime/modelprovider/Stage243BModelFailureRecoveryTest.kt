package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.reliability.RecoveryAttemptBudget
import com.devil.core.model.reliability.RecoveryStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage243BModelFailureRecoveryTest {

    @Test
    fun `assessed Stage 243A context prepares bounded AI failure recovery with exact provenance`() {
        val hallucinationResistance = assessedHallucinationResistance()
        val budget =
            RecoveryAttemptBudget.create(
                maximumAttempts = 3,
                attemptsAlreadyUsed = 1,
            )

        val result =
            ModelFailureRecoveryCoordinator()
                .prepare(
                    hallucinationResistance = hallucinationResistance,
                    recoveryStrategy = RecoveryStrategy.RETRY_SAME_OPERATION,
                    attemptBudget = budget,
                    recoveryRationale =
                        "  Bounded model-domain recovery preparation without execution.  ",
                )

        assertEquals(
            ModelFailureRecoveryStatus.PREPARED,
            result.status,
        )

        assertSame(
            hallucinationResistance,
            result.hallucinationResistance,
        )

        assertSame(
            hallucinationResistance.modelOutputVerification,
            result.hallucinationResistance.modelOutputVerification,
        )

        assertSame(
            hallucinationResistance.modelOutputVerification.interpretation,
            result.hallucinationResistance.modelOutputVerification.interpretation,
        )

        assertSame(
            budget,
            result.attemptBudget,
        )

        assertEquals(
            RecoveryStrategy.RETRY_SAME_OPERATION,
            result.recoveryStrategy,
        )

        assertEquals(
            "Bounded model-domain recovery preparation without execution.",
            result.recoveryRationale,
        )
    }

    @Test
    fun `non assessed Stage 243A context keeps Stage 243B deferred`() {
        val hallucinationResistance =
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.DEFERRED,
                modelOutputVerification = verifiedOutput(),
            )

        val result =
            ModelFailureRecoveryCoordinator()
                .prepare(
                    hallucinationResistance = hallucinationResistance,
                    recoveryStrategy = RecoveryStrategy.RETRY_SAME_OPERATION,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 2,
                        ),
                    recoveryRationale =
                        "Bounded recovery preparation.",
                )

        assertEquals(
            ModelFailureRecoveryStatus.DEFERRED,
            result.status,
        )

        assertSame(
            hallucinationResistance,
            result.hallucinationResistance,
        )

        assertNull(result.recoveryStrategy)
        assertNull(result.attemptBudget)
        assertNull(result.recoveryRationale)
    }

    @Test
    fun `missing recovery strategy keeps Stage 243B deferred`() {
        val result =
            ModelFailureRecoveryCoordinator()
                .prepare(
                    hallucinationResistance = assessedHallucinationResistance(),
                    recoveryStrategy = null,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 2,
                        ),
                    recoveryRationale =
                        "Bounded recovery preparation.",
                )

        assertEquals(
            ModelFailureRecoveryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.recoveryStrategy)
        assertNull(result.attemptBudget)
        assertNull(result.recoveryRationale)
    }

    @Test
    fun `missing recovery budget keeps Stage 243B deferred`() {
        val result =
            ModelFailureRecoveryCoordinator()
                .prepare(
                    hallucinationResistance = assessedHallucinationResistance(),
                    recoveryStrategy = RecoveryStrategy.REINITIALIZE_COMPONENT,
                    attemptBudget = null,
                    recoveryRationale =
                        "Bounded recovery preparation.",
                )

        assertEquals(
            ModelFailureRecoveryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.recoveryStrategy)
        assertNull(result.attemptBudget)
        assertNull(result.recoveryRationale)
    }

    @Test
    fun `exhausted recovery budget keeps Stage 243B deferred`() {
        val result =
            ModelFailureRecoveryCoordinator()
                .prepare(
                    hallucinationResistance = assessedHallucinationResistance(),
                    recoveryStrategy = RecoveryStrategy.RECONNECT_SOURCE,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 2,
                            attemptsAlreadyUsed = 2,
                        ),
                    recoveryRationale =
                        "Bounded recovery preparation.",
                )

        assertEquals(
            ModelFailureRecoveryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.recoveryStrategy)
        assertNull(result.attemptBudget)
        assertNull(result.recoveryRationale)
    }

    @Test
    fun `blank recovery rationale keeps Stage 243B deferred`() {
        val result =
            ModelFailureRecoveryCoordinator()
                .prepare(
                    hallucinationResistance = assessedHallucinationResistance(),
                    recoveryStrategy = RecoveryStrategy.RETRY_SAME_OPERATION,
                    attemptBudget =
                        RecoveryAttemptBudget.create(
                            maximumAttempts = 2,
                        ),
                    recoveryRationale = "   ",
                )

        assertEquals(
            ModelFailureRecoveryStatus.DEFERRED,
            result.status,
        )

        assertNull(result.recoveryStrategy)
        assertNull(result.attemptBudget)
        assertNull(result.recoveryRationale)
    }

    @Test
    fun `prepared result requires assessed Stage 243A provenance`() {
        val deferredHallucinationResistance =
            ModelHallucinationResistanceResult.create(
                status = ModelHallucinationResistanceStatus.DEFERRED,
                modelOutputVerification = verifiedOutput(),
            )

        assertFailsWith<IllegalArgumentException> {
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.PREPARED,
                hallucinationResistance =
                    deferredHallucinationResistance,
                recoveryStrategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
                recoveryRationale =
                    "Bounded recovery preparation.",
            )
        }
    }

    @Test
    fun `prepared result requires non exhausted recovery budget`() {
        assertFailsWith<IllegalArgumentException> {
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.PREPARED,
                hallucinationResistance =
                    assessedHallucinationResistance(),
                recoveryStrategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 1,
                        attemptsAlreadyUsed = 1,
                    ),
                recoveryRationale =
                    "Bounded recovery preparation.",
            )
        }
    }

    @Test
    fun `prepared result normalizes recovery rationale`() {
        val result =
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.PREPARED,
                hallucinationResistance =
                    assessedHallucinationResistance(),
                recoveryStrategy =
                    RecoveryStrategy.REINITIALIZE_COMPONENT,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 3,
                    ),
                recoveryRationale =
                    "  Normalized Stage 243B recovery rationale.  ",
            )

        assertEquals(
            "Normalized Stage 243B recovery rationale.",
            result.recoveryRationale,
        )
    }

    @Test
    fun `prepared result rejects blank recovery rationale`() {
        assertFailsWith<IllegalArgumentException> {
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.PREPARED,
                hallucinationResistance =
                    assessedHallucinationResistance(),
                recoveryStrategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
                recoveryRationale = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle recovery strategy`() {
        assertFailsWith<IllegalArgumentException> {
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.DEFERRED,
                hallucinationResistance =
                    assessedHallucinationResistance(),
                recoveryStrategy =
                    RecoveryStrategy.RETRY_SAME_OPERATION,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle recovery budget`() {
        assertFailsWith<IllegalArgumentException> {
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.DEFERRED,
                hallucinationResistance =
                    assessedHallucinationResistance(),
                attemptBudget =
                    RecoveryAttemptBudget.create(
                        maximumAttempts = 2,
                    ),
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle recovery rationale`() {
        assertFailsWith<IllegalArgumentException> {
            ModelFailureRecoveryResult.create(
                status = ModelFailureRecoveryStatus.DEFERRED,
                hallucinationResistance =
                    assessedHallucinationResistance(),
                recoveryRationale =
                    "Must not be present.",
            )
        }
    }

    private fun assessedHallucinationResistance():
        ModelHallucinationResistanceResult {
        return ModelHallucinationResistanceCoordinator()
            .assess(
                modelOutputVerification = verifiedOutput(),
                resistanceBasisDescription =
                    "Explicit bounded Stage 243B hallucination-resistance basis.",
                resistanceAssessmentDescription =
                    "Model-domain Stage 243B resistance assessment only.",
            )
    }

    private fun verifiedOutput():
        ModelOutputVerificationResult {
        return ModelOutputVerificationCoordinator()
            .verify(
                interpretation = interpretedOutput(),
                verificationBasisDescription =
                    "Explicit bounded Stage 243B model-output verification basis.",
                verificationAssessmentDescription =
                    "Model-domain Stage 243B verification preparation only.",
            )
    }

    private fun interpretedOutput():
        ModelOutputInterpretationResult {
        return ModelOutputInterpretationCoordinator()
            .interpret(
                modelContext = assembledContext(),
                rawModelOutput =
                    "Explicitly supplied untrusted Stage 243B model output.",
                interpretationDescription =
                    "Bounded interpretation without truth promotion.",
            )
    }

    private fun assembledContext(): ModelContextAssemblyResult {
        return ModelContextAssemblyCoordinator()
            .assemble(
                structuredReasoning = integratedReasoning(),
                modelContextObjective =
                    "Bounded Stage 243B model-context objective.",
                assembledContextDescription =
                    "Bounded Stage 243B assembled model context.",
            )
    }

    private fun integratedReasoning():
        StructuredReasoningIntegrationResult {
        return StructuredReasoningIntegrationCoordinator()
            .integrate(
                toolUsingIntelligence = preparedToolUse(),
                reasoningObjective =
                    "Bounded Stage 243B reasoning objective.",
                structuredReasoningDescription =
                    "Bounded Stage 243B structured-reasoning context.",
            )
    }

    private fun preparedToolUse(): ToolUsingIntelligenceResult {
        return ToolUsingIntelligenceCoordinator()
            .prepare(
                routing = routedModel(),
                capability = capability(),
                toolUseIntentDescription =
                    "Bounded Stage 243B upstream tool-use context.",
            )
    }

    private fun routedModel(): ModelRoutingResult {
        return ModelRoutingCoordinator()
            .route(
                providerArchitecture =
                    availableProviderArchitecture(),
                routingRationale =
                    "Explicit bounded Stage 243B model routing destination.",
            )
    }

    private fun availableProviderArchitecture():
        ModelProviderArchitectureResult {
        return ModelProviderArchitectureCoordinator()
            .prepare(
                providerId =
                    "provider:stage243b:test",
                providerName =
                    "Stage 243B Test Provider",
                providerDescription =
                    "Provider-neutral Stage 243B AI-failure recovery foundation.",
            )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage243b-test",
                ),
            category =
                enumValues<CapabilityCategory>().first(),
            name =
                "Stage 243B Test Capability",
            description =
                "Existing Devil capability preserved through bounded Stage 243B AI-failure recovery preparation.",
        )
    }
}
