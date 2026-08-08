package com.devil.core.runtime.security

import com.devil.core.model.security.SecurityTransitionRequest

/**
 * Default Stage 23 constitutional Security Transition Authority coordinator.
 *
 * This authority receives one already-bounded SecurityTransitionRequest,
 * delegates constitutional transition evaluation, and maps that evaluation
 * into the stable operational SecurityTransitionResult contract.
 *
 * It introduces no independent security policy and grants itself no authority.
 *
 * In particular, this coordinator does not:
 *
 * - advance or mutate SecurityStateRecord,
 * - authenticate a subject,
 * - prove owner identity,
 * - establish trust,
 * - grant authorization,
 * - create or validate a session,
 * - enter Owner Mode,
 * - approve high-security confirmation,
 * - grant Android permission,
 * - permit capability execution,
 * - invoke Android credentials,
 * - or communicate with platform security mechanisms.
 *
 * Actual security-state transition remains a separate controlled
 * responsibility and requires genuine constitutional evidence and policy.
 */
class DefaultSecurityTransitionAuthority(
    private val evaluator: SecurityTransitionEvaluator =
        DefaultSecurityTransitionEvaluator(),
    private val resultMapper: SecurityTransitionResultMapper =
        DefaultSecurityTransitionResultMapper(),
) : SecurityTransitionAuthority {

    override fun evaluateTransition(
        request: SecurityTransitionRequest,
    ): SecurityTransitionResult {
        val traceId = request.context.traceId

        val evaluation = evaluator.evaluate(
            traceId = traceId,
            request = request,
        )

        require(evaluation.traceId == traceId) {
            "Security transition request and evaluation result must use the same trace identity."
        }

        val result = resultMapper.map(
            traceId = traceId,
            evaluation = evaluation,
        )

        require(result.traceId == traceId) {
            "Security transition request and mapped result must use the same trace identity."
        }

        return result
    }
}
