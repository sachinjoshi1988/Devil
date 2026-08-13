package com.devil.core.runtime

import com.devil.core.model.conversation.ConversationInput
import com.devil.core.runtime.authorization.AuthorizationAuthority
import com.devil.core.runtime.authorization.DefaultAuthorizationAuthority
import com.devil.core.runtime.capability.CapabilitySelectionAuthority
import com.devil.core.runtime.capability.DefaultCapabilitySelectionAuthority
import com.devil.core.runtime.constitution.ConstitutionValidationAuthority
import com.devil.core.runtime.constitution.ConstitutionValidationStatus
import com.devil.core.runtime.constitution.DefaultConstitutionValidationAuthority
import com.devil.core.runtime.conversation.ConversationIntakeAuthority
import com.devil.core.runtime.conversation.ConversationPersistenceAuthority
import com.devil.core.runtime.conversation.ConversationRecordAuthority
import com.devil.core.runtime.conversation.DefaultConversationPersistenceAuthority
import com.devil.core.runtime.conversation.DefaultConversationRecordAuthority
import com.devil.core.runtime.conversation.DefaultConversationIntakeAuthority
import com.devil.core.runtime.decision.DecisionAuthority
import com.devil.core.runtime.decision.DefaultDecisionAuthority
import com.devil.core.runtime.executive.DefaultExecutiveReadinessAuthority
import com.devil.core.runtime.executive.ExecutiveReadinessAuthority
import com.devil.core.runtime.execution.DefaultExecutionAttemptPort
import com.devil.core.runtime.execution.DefaultExecutionAuthority
import com.devil.core.runtime.execution.ExecutionAttemptPort
import com.devil.core.runtime.execution.ExecutionAuthority
import com.devil.core.runtime.identity.DefaultIdentityAuthority
import com.devil.core.runtime.identity.IdentityAuthority
import com.devil.core.runtime.learning.DefaultLearningAuthority
import com.devil.core.runtime.learning.DefaultLearningEvidencePort
import com.devil.core.runtime.learning.LearningAuthority
import com.devil.core.runtime.learning.LearningEvidencePort
import com.devil.core.runtime.memory.DefaultMemoryAuthority
import com.devil.core.runtime.memory.DefaultMemoryCommitmentAuthority
import com.devil.core.runtime.memory.DefaultMemoryPersistenceAuthority
import com.devil.core.runtime.memory.DefaultMemoryProposalAuthority
import com.devil.core.runtime.memory.MemoryAuthority
import com.devil.core.runtime.memory.MemoryCommitmentAuthority
import com.devil.core.runtime.memory.MemoryPersistenceAuthority
import com.devil.core.runtime.memory.MemoryPersistenceStatus
import com.devil.core.runtime.memory.MemoryProposalAuthority
import com.devil.core.runtime.observation.DefaultObservationEvidencePort
import com.devil.core.runtime.observation.ObservationEvidencePort
import com.devil.core.runtime.observation.DefaultObservationAuthority
import com.devil.core.runtime.observation.ObservationAuthority
import com.devil.core.runtime.outcome.DefaultOutcomeAuthority
import com.devil.core.runtime.outcome.DefaultOutcomeEvidencePort
import com.devil.core.runtime.outcome.OutcomeAuthority
import com.devil.core.runtime.outcome.OutcomeEvidencePort
import com.devil.core.runtime.plan.DefaultPlanAuthority
import com.devil.core.runtime.plan.PlanAuthority
import com.devil.core.runtime.task.DefaultTaskAuthority
import com.devil.core.runtime.task.TaskAuthority
import com.devil.core.runtime.trust.DefaultTrustAuthority
import com.devil.core.runtime.trust.TrustAuthority
import com.devil.core.runtime.understanding.DefaultUnderstandingAuthority
import com.devil.core.runtime.understanding.UnderstandingAuthority
import com.devil.core.runtime.verification.DefaultVerificationAuthority
import com.devil.core.runtime.verification.DefaultVerificationEvidencePort
import com.devil.core.runtime.verification.VerificationAuthority
import com.devil.core.runtime.verification.VerificationEvidencePort
import com.devil.core.runtime.worldmodel.DefaultWorldModelUpdateEvidencePort
import com.devil.core.runtime.worldmodel.DefaultWorldModelUpdateAuthority
import com.devil.core.runtime.worldmodel.WorldModelUpdateEvidencePort
import com.devil.core.runtime.worldmodel.WorldModelUpdateAuthority

/**
 * Default constitutional runtime coordinator.
 *
 * This implementation preserves one ordered runtime path from constitutional
 * validation through bounded logical-memory persistence evaluation. Bounded conversation-record formation and conversation-persistence evaluation remain separate conversation-domain responsibilities. Conversation
 * intake is positioned after authorization and before understanding.
 *
 * The supplied ConversationInput owns the authoritative constitutional context.
 * This coordinator does not absorb the responsibilities of its bounded
 * authorities.
 *
 * It activates no capability, invokes no platform API, fabricates no execution
 * attempt, observation, verification, outcome, World Model update, learning,
 * memory-proposal, Memory Authority, memory-commitment, or memory-persistence
 * evidence, mutates no world state, changes no task or plan state, creates,
 * persists, stores, exposes, recalls, deletes, or commits no logical memory,
 * performs no external communication, and makes no unverified success claim.
 */
class DefaultUnifiedDevilRuntime(
    private val constitutionValidationAuthority:
        ConstitutionValidationAuthority =
        DefaultConstitutionValidationAuthority(),
    private val identityAuthority: IdentityAuthority =
        DefaultIdentityAuthority(),
    private val trustAuthority: TrustAuthority =
        DefaultTrustAuthority(),
    private val authorizationAuthority:
        AuthorizationAuthority =
        DefaultAuthorizationAuthority(),
    private val conversationIntakeAuthority:
        ConversationIntakeAuthority =
        DefaultConversationIntakeAuthority(),
    private val conversationRecordAuthority:
        ConversationRecordAuthority =
        DefaultConversationRecordAuthority(),
    private val conversationPersistenceAuthority:
        ConversationPersistenceAuthority =
        DefaultConversationPersistenceAuthority(),
    private val understandingAuthority:
        UnderstandingAuthority =
        DefaultUnderstandingAuthority(),
    private val decisionAuthority: DecisionAuthority =
        DefaultDecisionAuthority(),
    private val taskAuthority: TaskAuthority =
        DefaultTaskAuthority(),
    private val planAuthority: PlanAuthority =
        DefaultPlanAuthority(),
    private val capabilitySelectionAuthority:
        CapabilitySelectionAuthority =
        DefaultCapabilitySelectionAuthority(),
    private val executiveReadinessAuthority:
        ExecutiveReadinessAuthority =
        DefaultExecutiveReadinessAuthority(),
    private val executionAuthority:
        ExecutionAuthority =
        DefaultExecutionAuthority(),
    private val executionAttemptPort:
        ExecutionAttemptPort =
        DefaultExecutionAttemptPort(),
    private val observationEvidencePort:
        ObservationEvidencePort =
        DefaultObservationEvidencePort(),
    private val observationAuthority:
        ObservationAuthority =
        DefaultObservationAuthority(),
    private val verificationEvidencePort:
        VerificationEvidencePort =
        DefaultVerificationEvidencePort(),
    private val verificationAuthority:
        VerificationAuthority =
        DefaultVerificationAuthority(),
    private val outcomeEvidencePort:
        OutcomeEvidencePort =
        DefaultOutcomeEvidencePort(),
    private val outcomeAuthority:
        OutcomeAuthority =
        DefaultOutcomeAuthority(),
    private val worldModelUpdateEvidencePort:
        WorldModelUpdateEvidencePort =
        DefaultWorldModelUpdateEvidencePort(),
    private val worldModelUpdateAuthority:
        WorldModelUpdateAuthority =
        DefaultWorldModelUpdateAuthority(),
    private val learningEvidencePort:
        LearningEvidencePort =
        DefaultLearningEvidencePort(),

    private val learningAuthority:
        LearningAuthority =
        DefaultLearningAuthority(),
    private val memoryProposalAuthority:
        MemoryProposalAuthority =
        DefaultMemoryProposalAuthority(),
    private val memoryAuthority: MemoryAuthority =
        DefaultMemoryAuthority(),
    private val memoryCommitmentAuthority:
        MemoryCommitmentAuthority =
        DefaultMemoryCommitmentAuthority(),
    private val memoryPersistenceAuthority:
        MemoryPersistenceAuthority =
        DefaultMemoryPersistenceAuthority(),
) : UnifiedDevilRuntime {

    override fun accept(
        input: ConversationInput,
    ): RuntimeResult {
        val context = input.context

        val validation =
            constitutionValidationAuthority.validate(context)

        require(validation.traceId == context.traceId) {
            "Context and constitutional validation result must use the same trace identity."
        }

        if (
            validation.status ==
            ConstitutionValidationStatus.INVALID
        ) {
            return RuntimeResult.create(
                traceId = context.traceId,
                status = RuntimeStatus.REJECTED,
                error = requireNotNull(validation.error),
            )
        }

        val identity =
            identityAuthority.resolve(context)

        val trust = trustAuthority.evaluate(
            context = context,
            identity = identity,
        )

        val authorization =
            authorizationAuthority.authorize(
                context = context,
                identity = identity,
                trust = trust,
            )

        val conversationIntake =
            conversationIntakeAuthority.intake(
                input = input,
                identity = identity,
                trust = trust,
                authorization = authorization,
            )

        require(
            conversationIntake.traceId ==
                context.traceId,
        ) {
            "Context and conversation-intake result must use the same trace identity."
        }

        val conversationRecord =
            conversationRecordAuthority.record(
                conversationIntake = conversationIntake,
            )

        require(conversationRecord.traceId == context.traceId) {
            "Context and conversation-record result must use the same trace identity."
        }

        val conversationPersistence =
            conversationPersistenceAuthority.evaluatePersistence(
                conversationRecord = conversationRecord,
            )

        require(conversationPersistence.traceId == context.traceId) {
            "Context and conversation persistence result must use the same trace identity."
        }

        val understanding =
            understandingAuthority.understand(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                conversationIntake = conversationIntake,
            )

        val decision = decisionAuthority.decide(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
        )

        val task = taskAuthority.createTask(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
        )

        val plan = planAuthority.createPlan(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
        )

        val capability =
            capabilitySelectionAuthority.select(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
            )

        val readiness =
            executiveReadinessAuthority.evaluate(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
                capability = capability,
            )

        require(readiness.traceId == context.traceId) {
            "Context and Executive readiness result must use the same trace identity."
        }

        val execution = executionAuthority.evaluate(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
            plan = plan,
            capability = capability,
            readiness = readiness,
        )

        require(execution.traceId == context.traceId) {
            "Context and execution result must use the same trace identity."
        }

        val executionAttempt =
            executionAttemptPort.attempt(
                execution = execution,
            )

        require(executionAttempt.traceId == context.traceId) {
            "Context and execution-attempt result must use the same trace identity."
        }

        val observationEvidence =
            observationEvidencePort.observe(
                executionAttempt = executionAttempt,
            )

        require(observationEvidence.traceId == context.traceId) {
            "Context and observation-evidence result must use the same trace identity."
        }

        val observation = observationAuthority.observe(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
            plan = plan,
            capability = capability,
            readiness = readiness,
            execution = execution,
            executionAttempt = executionAttempt,
            observationEvidence = observationEvidence,
        )

        require(observation.traceId == context.traceId) {
            "Context and observation result must use the same trace identity."
        }

        val verificationEvidence =
            verificationEvidencePort.verify(
                observation = observation,
            )

        require(verificationEvidence.traceId == context.traceId) {
            "Context and verification-evidence result must use the same trace identity."
        }

        val verification = verificationAuthority.verify(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
            plan = plan,
            capability = capability,
            readiness = readiness,
            execution = execution,
            observation = observation,
            verificationEvidence = verificationEvidence,
        )

        require(verification.traceId == context.traceId) {
            "Context and verification result must use the same trace identity."
        }

        val outcomeEvidence =
            outcomeEvidencePort.establish(
                verification = verification,
                verificationEvidence = verificationEvidence,
            )

        require(outcomeEvidence.traceId == context.traceId) {
            "Context and outcome-evidence result must use the same trace identity."
        }

        val outcome = outcomeAuthority.establish(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
            plan = plan,
            capability = capability,
            readiness = readiness,
            execution = execution,
            observation = observation,
            verification = verification,
            outcomeEvidence = outcomeEvidence,
        )

        require(outcome.traceId == context.traceId) {
            "Context and outcome result must use the same trace identity."
        }

        val worldModelUpdateEvidence =
            worldModelUpdateEvidencePort.establish(
                outcome = outcome,
            )

        require(worldModelUpdateEvidence.traceId == context.traceId) {
            "Context and World Model update-evidence result must use the same trace identity."
        }

        val worldModelUpdate =
            worldModelUpdateAuthority.evaluateUpdate(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
                capability = capability,
                readiness = readiness,
                execution = execution,
                observation = observation,
                verification = verification,
                outcome = outcome,
                worldModelUpdateEvidence = worldModelUpdateEvidence,
            )

        require(worldModelUpdate.traceId == context.traceId) {
            "Context and World Model update result must use the same trace identity."
        }

        val learningEvidence =
            learningEvidencePort.establish(
                worldModelUpdate = worldModelUpdate,
            )

        require(learningEvidence.traceId == context.traceId) {
            "Context and Learning-evidence result must use the same trace identity."
        }

        val learning =
            learningAuthority.evaluateLearning(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
                capability = capability,
                readiness = readiness,
                execution = execution,
                observation = observation,
                verification = verification,
                outcome = outcome,
                worldModelUpdate = worldModelUpdate,
                learningEvidence = learningEvidence,
            )

        require(learning.traceId == context.traceId) {
            "Context and learning result must use the same trace identity."
        }

        val memoryProposal =
            memoryProposalAuthority.evaluateProposal(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
                capability = capability,
                readiness = readiness,
                execution = execution,
                observation = observation,
                verification = verification,
                outcome = outcome,
                worldModelUpdate = worldModelUpdate,
                learning = learning,
            )

        require(memoryProposal.traceId == context.traceId) {
            "Context and memory proposal result must use the same trace identity."
        }

        val memory = memoryAuthority.evaluateMemory(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
            plan = plan,
            capability = capability,
            readiness = readiness,
            execution = execution,
            observation = observation,
            verification = verification,
            outcome = outcome,
            worldModelUpdate = worldModelUpdate,
            learning = learning,
            memoryProposal = memoryProposal,
        )

        require(memory.traceId == context.traceId) {
            "Context and Memory Authority result must use the same trace identity."
        }

        val memoryCommitment =
            memoryCommitmentAuthority.evaluateCommitment(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
                capability = capability,
                readiness = readiness,
                execution = execution,
                observation = observation,
                verification = verification,
                outcome = outcome,
                worldModelUpdate = worldModelUpdate,
                learning = learning,
                memoryProposal = memoryProposal,
                memory = memory,
            )

        require(memoryCommitment.traceId == context.traceId) {
            "Context and memory commitment result must use the same trace identity."
        }

        val memoryPersistence =
            memoryPersistenceAuthority.evaluatePersistence(
                context = context,
                identity = identity,
                trust = trust,
                authorization = authorization,
                understanding = understanding,
                decision = decision,
                task = task,
                plan = plan,
                capability = capability,
                readiness = readiness,
                execution = execution,
                observation = observation,
                verification = verification,
                outcome = outcome,
                worldModelUpdate = worldModelUpdate,
                learning = learning,
                memoryProposal = memoryProposal,
                memory = memory,
                memoryCommitment = memoryCommitment,
            )

        require(memoryPersistence.traceId == context.traceId) {
            "Context and memory persistence result must use the same trace identity."
        }

        return when (memoryPersistence.status) {
            MemoryPersistenceStatus.PERSISTABLE ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.ACCEPTED,
                )

            MemoryPersistenceStatus.DEFERRED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.DEFERRED,
                )

            MemoryPersistenceStatus.FAILED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.REJECTED,
                    error = requireNotNull(
                        memoryPersistence.error,
                    ),
                )
        }
    }
}
