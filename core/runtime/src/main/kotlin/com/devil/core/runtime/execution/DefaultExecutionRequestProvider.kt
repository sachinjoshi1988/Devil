package com.devil.core.runtime.execution

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.executive.ExecutiveReadinessStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus

/**
 * Default Stage 12 constitutional execution-request provider.
 *
 * A request is available only when the Plan Authority produced one PlanRecord,
 * Capability Selection selected one registered non-KNOWLEDGE capability, and
 * Executive Readiness produced affirmative readiness.
 *
 * Stage 337M preserves read-only KNOWLEDGE capability selection without turning
 * knowledge retrieval into execution.
 *
 * A selected KNOWLEDGE capability therefore produces no ExecutionRequest even
 * when the plan exists and Executive Readiness is READY.
 *
 * Deferred dependencies remain unavailable. Dependency failures preserve their
 * matching errors.
 *
 * This implementation does not establish capability health, check operating-
 * system permission, activate capabilities, execute actions, observe execution,
 * verify outcomes, or report final success.
 *
 * KNOWLEDGE_CAPABILITY_SELECTED != EXECUTION_REQUEST.
 * KNOWLEDGE_QUERY != EXECUTION.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 */
class DefaultExecutionRequestProvider : ExecutionRequestProvider {

    override fun provide(
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
        readiness: ExecutiveReadinessResult,
    ): ExecutionRequestResult {
        require(capability.traceId == plan.traceId) {
            "Plan and capability selection results must use the same trace identity."
        }

        require(readiness.traceId == plan.traceId) {
            "Plan and Executive readiness results must use the same trace identity."
        }

        return when (plan.status) {
            PlanAuthorityStatus.CREATED -> {
                when (capability.status) {
                    CapabilitySelectionStatus.SELECTED -> {
                        when (readiness.status) {
                            ExecutiveReadinessStatus.READY -> {
                                val selectedCapability =
                                    requireNotNull(
                                        capability.capability,
                                    )

                                if (
                                    selectedCapability.category ==
                                    CapabilityCategory.KNOWLEDGE
                                ) {
                                    ExecutionRequestResult.create(
                                        traceId = plan.traceId,
                                        status =
                                            ExecutionRequestStatus.UNAVAILABLE,
                                    )
                                } else {
                                    ExecutionRequestResult.create(
                                        traceId = plan.traceId,
                                        status =
                                            ExecutionRequestStatus.AVAILABLE,
                                        request =
                                            ExecutionRequest.create(
                                                plan =
                                                    requireNotNull(
                                                        plan.plan,
                                                    ),
                                                capability =
                                                    selectedCapability,
                                            ),
                                    )
                                }
                            }

                            ExecutiveReadinessStatus.DEFERRED ->
                                ExecutionRequestResult.create(
                                    traceId = plan.traceId,
                                    status =
                                        ExecutionRequestStatus.UNAVAILABLE,
                                )

                            ExecutiveReadinessStatus.FAILED ->
                                ExecutionRequestResult.create(
                                    traceId = plan.traceId,
                                    status =
                                        ExecutionRequestStatus.FAILED,
                                    error =
                                        requireNotNull(
                                            readiness.error,
                                        ),
                                )
                        }
                    }

                    CapabilitySelectionStatus.DEFERRED ->
                        ExecutionRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                ExecutionRequestStatus.UNAVAILABLE,
                        )

                    CapabilitySelectionStatus.FAILED ->
                        ExecutionRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                ExecutionRequestStatus.FAILED,
                            error =
                                requireNotNull(
                                    capability.error,
                                ),
                        )
                }
            }

            PlanAuthorityStatus.DEFERRED ->
                ExecutionRequestResult.create(
                    traceId = plan.traceId,
                    status =
                        ExecutionRequestStatus.UNAVAILABLE,
                )

            PlanAuthorityStatus.FAILED ->
                ExecutionRequestResult.create(
                    traceId = plan.traceId,
                    status =
                        ExecutionRequestStatus.FAILED,
                    error =
                        requireNotNull(
                            plan.error,
                        ),
                )
        }
    }
}
