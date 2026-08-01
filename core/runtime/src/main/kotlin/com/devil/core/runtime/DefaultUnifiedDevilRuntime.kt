package com.devil.core.runtime

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationAuthority
import com.devil.core.runtime.authorization.DefaultAuthorizationAuthority
import com.devil.core.runtime.capability.CapabilitySelectionAuthority
import com.devil.core.runtime.capability.DefaultCapabilitySelectionAuthority
import com.devil.core.runtime.constitution.ConstitutionValidationAuthority
import com.devil.core.runtime.constitution.ConstitutionValidationStatus
import com.devil.core.runtime.constitution.DefaultConstitutionValidationAuthority
import com.devil.core.runtime.decision.DecisionAuthority
import com.devil.core.runtime.decision.DefaultDecisionAuthority
import com.devil.core.runtime.executive.DefaultExecutiveReadinessAuthority
import com.devil.core.runtime.executive.ExecutiveReadinessAuthority
import com.devil.core.runtime.executive.ExecutiveReadinessStatus
import com.devil.core.runtime.identity.DefaultIdentityAuthority
import com.devil.core.runtime.identity.IdentityAuthority
import com.devil.core.runtime.plan.DefaultPlanAuthority
import com.devil.core.runtime.plan.PlanAuthority
import com.devil.core.runtime.task.DefaultTaskAuthority
import com.devil.core.runtime.task.TaskAuthority
import com.devil.core.runtime.trust.DefaultTrustAuthority
import com.devil.core.runtime.trust.TrustAuthority
import com.devil.core.runtime.understanding.DefaultUnderstandingAuthority
import com.devil.core.runtime.understanding.UnderstandingAuthority

/**
 * Default constitutional runtime coordinator.
 *
 * This implementation preserves the ordered Stage 2 path from constitutional
 * validation through Executive readiness. It coordinates bounded authorities
 * without absorbing their responsibilities.
 *
 * It performs no platform execution, invents no observations, verifies no
 * outcomes, and makes no unverified success claim.
 */
class DefaultUnifiedDevilRuntime(
    private val constitutionValidationAuthority:
        ConstitutionValidationAuthority =
        DefaultConstitutionValidationAuthority(),
    private val identityAuthority: IdentityAuthority =
        DefaultIdentityAuthority(),
    private val trustAuthority: TrustAuthority =
        DefaultTrustAuthority(),
    private val authorizationAuthority: AuthorizationAuthority =
        DefaultAuthorizationAuthority(),
    private val understandingAuthority: UnderstandingAuthority =
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
) : UnifiedDevilRuntime {

    override fun accept(
        context: ContextEnvelope,
    ): RuntimeResult {
        val validation = constitutionValidationAuthority.validate(context)

        require(validation.traceId == context.traceId) {
            "Context and constitutional validation result must use the same trace identity."
        }

        if (validation.status == ConstitutionValidationStatus.INVALID) {
            return RuntimeResult.create(
                traceId = context.traceId,
                status = RuntimeStatus.REJECTED,
                error = requireNotNull(validation.error),
            )
        }

        val identity = identityAuthority.resolve(context)

        val trust = trustAuthority.evaluate(
            context = context,
            identity = identity,
        )

        val authorization = authorizationAuthority.authorize(
            context = context,
            identity = identity,
            trust = trust,
        )

        val understanding = understandingAuthority.understand(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
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

        val capability = capabilitySelectionAuthority.select(
            context = context,
            identity = identity,
            trust = trust,
            authorization = authorization,
            understanding = understanding,
            decision = decision,
            task = task,
            plan = plan,
        )

        val readiness = executiveReadinessAuthority.evaluate(
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

        return when (readiness.status) {
            ExecutiveReadinessStatus.READY ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.ACCEPTED,
                )

            ExecutiveReadinessStatus.DEFERRED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.DEFERRED,
                )

            ExecutiveReadinessStatus.FAILED ->
                RuntimeResult.create(
                    traceId = context.traceId,
                    status = RuntimeStatus.REJECTED,
                    error = requireNotNull(readiness.error),
                )
        }
    }
}
