package com.devil.core.runtime.task

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 8 constitutional Task Authority coordinator.
 *
 * This authority obtains a bounded task-creation request, obtains one genuine
 * task identity, delegates task creation to the resolver, and maps the
 * resulting TaskRecord into the stable operational result contract.
 *
 * It does not resolve identity, evaluate trust, grant authorization, produce
 * understanding, select decisions, create plans, authorize capabilities,
 * execute actions, observe results, or verify outcomes.
 */
class DefaultTaskAuthority(
    private val requestProvider:
        TaskCreationRequestProvider =
        DefaultTaskCreationRequestProvider(),
    private val taskIdentityProvider:
        TaskIdentityProvider =
        DefaultTaskIdentityProvider(),
    private val resolver: TaskCreationResolver =
        DefaultTaskCreationResolver(),
    private val resultMapper: TaskCreationResultMapper =
        DefaultTaskCreationResultMapper(),
) : TaskAuthority {

    override fun createTask(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
    ): TaskAuthorityResult {
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

        val requestResult =
            requestProvider.provide(decision)

        require(requestResult.traceId == context.traceId) {
            "Context and task-creation request result must use the same trace identity."
        }

        return when (requestResult.status) {
            TaskCreationRequestStatus.AVAILABLE -> {
                val request =
                    requireNotNull(requestResult.request)

                val identityResult =
                    taskIdentityProvider.provide(
                        traceId = context.traceId,
                        request = request,
                    )

                require(identityResult.traceId == context.traceId) {
                    "Context and task identity result must use the same trace identity."
                }

                when (identityResult.status) {
                    TaskIdentityProvisionStatus.AVAILABLE -> {
                        val task = resolver.create(
                            request = request,
                            taskId =
                                requireNotNull(identityResult.taskId),
                        )

                        val result = resultMapper.map(
                            traceId = context.traceId,
                            task = task,
                        )

                        require(result.traceId == context.traceId) {
                            "Context and mapped task result must use the same trace identity."
                        }

                        result
                    }

                    TaskIdentityProvisionStatus.UNAVAILABLE ->
                        TaskAuthorityResult.create(
                            traceId = context.traceId,
                            status = TaskAuthorityStatus.DEFERRED,
                        )

                    TaskIdentityProvisionStatus.FAILED ->
                        TaskAuthorityResult.create(
                            traceId = context.traceId,
                            status = TaskAuthorityStatus.FAILED,
                            error = requireNotNull(
                                identityResult.error,
                            ),
                        )
                }
            }

            TaskCreationRequestStatus.UNAVAILABLE ->
                TaskAuthorityResult.create(
                    traceId = context.traceId,
                    status = TaskAuthorityStatus.DEFERRED,
                )

            TaskCreationRequestStatus.FAILED ->
                TaskAuthorityResult.create(
                    traceId = context.traceId,
                    status = TaskAuthorityStatus.FAILED,
                    error = requireNotNull(
                        requestResult.error,
                    ),
                )
        }
    }
}
