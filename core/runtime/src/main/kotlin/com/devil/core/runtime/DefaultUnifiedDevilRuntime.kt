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
import com.devil.core.runtime.conversation.DefaultConversationIntakeAuthority
import com.devil.core.runtime.decision.DecisionAuthority
import com.devil.core.runtime.decision.DefaultDecisionAuthority
import com.devil.core.runtime.executive.DefaultExecutiveReadinessAuthority
import com.devil.core.runtime.executive.ExecutiveReadinessAuthority
import com.devil.core.runtime.execution.DefaultExecutionAuthority
import com.devil.core.runtime.execution.ExecutionAuthority
import com.devil.core.runtime.identity.DefaultIdentityAuthority
import com.devil.core.runtime.identity.IdentityAuthority
import com.devil.core.runtime.observation.DefaultObservationAuthority
import com.devil.core.runtime.observation.ObservationAuthority
import com.devil.core.runtime.plan.DefaultPlanAuthority
import com.devil.core.runtime.plan.PlanAuthority
import com.devil.core.runtime.task.DefaultTaskAuthority
import com.devil.core.runtime.task.TaskAuthority
import com.devil.core.runtime.trust.DefaultTrustAuthority
import com.devil.core.runtime.trust.TrustAuthority
import com.devil.core.runtime.understanding.DefaultUnderstandingAuthority
import com.devil.core.runtime.understanding.UnderstandingAuthority
import com.devil.core.runtime.verification.DefaultVerificationAuthority
import com.devil.core.runtime.verification.VerificationAuthority
import com.devil.core.runtime.verification.VerificationStatus

/**
 * Default constitutional runtime coordinator.
 *
 * This implementation preserves one ordered runtime path from constitutional
 * validation through bounded verification evaluation. Conversation intake is
 * positioned after authorization and before understanding.
 *
 * The supplied ConversationInput owns the authoritative constitutional context.
 * This coordinator does not absorb the responsibilities of its bounded
 * authorities.
 *
 * It activates no capability, invokes no platform API, fabricates no execution
 * attempt, observation, or verification evidence, updates no world state,
 * produces no final Outcome, and makes no unverified success claim.
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
    private val observationAuthority:
        ObservationAuthority =
        DefaultObservationAuthority(),
    private val verificationAuthority:
        VerificationAuthority =
        DefaultVerificationAuthority(),
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
        )

        require(observation.traceId == context.traceId) {
            "Context and observation result must use the same trace identity."
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
        )

        require(verification.traceId == context.traceId) {
            "Context and verification result must use the same trace identity."
        }

        return when (verification.status) {
            VerificationStatus.VERIFIED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.ACCEPTED,
                )

            VerificationStatus.DEFERRED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.DEFERRED,
                )

            VerificationStatus.FAILED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.REJECTED,
                    error = requireNotNull(verification.error),
                )
        }
    }
}
