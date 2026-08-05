package com.devil.core.runtime.capability

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.task.TaskAuthorityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 10 constitutional Capability Selection Authority coordinator.
 *
 * This authority obtains one bounded capability-selection request, obtains the
 * existing capability registry, delegates bounded selection resolution, and maps
 * the resolution into the stable operational result contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create tasks or plans, register capabilities,
 * invent capability-selection policy, establish availability or health, check
 * operating-system permission, execute actions, observe results, verify outcomes,
 * or report final outcomes.
 */
class DefaultCapabilitySelectionAuthority(
    private val requestProvider:
        CapabilitySelectionRequestProvider =
        DefaultCapabilitySelectionRequestProvider(),
    private val registry:
        CapabilityRegistry =
        DefaultCapabilityRegistry(),
    private val resolver:
        CapabilitySelectionResolver =
        DefaultCapabilitySelectionResolver(),
    private val resultMapper:
        CapabilitySelectionResultMapper =
        DefaultCapabilitySelectionResultMapper(),
) : CapabilitySelectionAuthority {

    override fun select(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
        task: TaskAuthorityResult,
        plan: PlanAuthorityResult,
    ): CapabilitySelectionResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(understanding.traceId == context.traceId) {
            "Context and understanding result must use the same trace identity."
        }

        require(decision.traceId == context.traceId) {
            "Context and decision result must use the same trace identity."
        }

        require(task.traceId == context.traceId) {
            "Context and task result must use the same trace identity."
        }

        require(plan.traceId == context.traceId) {
            "Context and plan result must use the same trace identity."
        }

        val requestResult = requestProvider.provide(plan)

        require(requestResult.traceId == context.traceId) {
            "Context and capability-selection request result must use the same trace identity."
        }

        return when (requestResult.status) {
            CapabilitySelectionRequestStatus.AVAILABLE -> {
                val request = requireNotNull(requestResult.request)

                val registryResult = registry.obtain(
                    traceId = context.traceId,
                    request = request,
                )

                require(registryResult.traceId == context.traceId) {
                    "Context and capability registry result must use the same trace identity."
                }

                when (registryResult.status) {
                    CapabilityRegistryStatus.AVAILABLE,
                    CapabilityRegistryStatus.UNAVAILABLE,
                    -> {
                        val resolution = resolver.resolve(
                            traceId = context.traceId,
                            request = request,
                            registry = registryResult,
                        )

                        require(resolution.traceId == context.traceId) {
                            "Context and capability selection resolution result must use the same trace identity."
                        }

                        val result = resultMapper.map(
                            traceId = context.traceId,
                            resolution = resolution,
                        )

                        require(result.traceId == context.traceId) {
                            "Context and mapped capability selection result must use the same trace identity."
                        }

                        result
                    }

                    CapabilityRegistryStatus.FAILED ->
                        CapabilitySelectionResult.create(
                            traceId = context.traceId,
                            status = CapabilitySelectionStatus.FAILED,
                            error = requireNotNull(registryResult.error),
                        )
                }
            }

            CapabilitySelectionRequestStatus.UNAVAILABLE ->
                CapabilitySelectionResult.create(
                    traceId = context.traceId,
                    status = CapabilitySelectionStatus.DEFERRED,
                )

            CapabilitySelectionRequestStatus.FAILED ->
                CapabilitySelectionResult.create(
                    traceId = context.traceId,
                    status = CapabilitySelectionStatus.FAILED,
                    error = requireNotNull(requestResult.error),
                )
        }
    }
}
