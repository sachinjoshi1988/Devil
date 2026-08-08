package com.devil.core.runtime.security

import com.devil.core.model.security.SessionValidityRequest

/**
 * Default Stage 23 constitutional Session Validity Authority coordinator.
 *
 * This authority receives one already-bounded SessionValidityRequest,
 * delegates constitutional session-validity evaluation, and maps that
 * evaluation into the stable operational SessionValidityResult contract.
 *
 * It introduces no independent session policy and grants itself no authority.
 *
 * In particular, this coordinator does not:
 *
 * - mutate SessionRecord,
 * - extend or renew a session,
 * - create a session,
 * - authenticate a subject,
 * - prove owner identity,
 * - establish trust,
 * - grant authorization,
 * - advance SecurityStage,
 * - enter Owner Mode,
 * - approve high-security confirmation,
 * - grant Android permission,
 * - permit capability execution,
 * - invoke Android credentials,
 * - or communicate with platform security mechanisms.
 *
 * Session establishment, mutation, renewal, revocation, authentication, and
 * security-stage transition remain separate controlled responsibilities.
 */
class DefaultSessionValidityAuthority(
    private val evaluator: SessionValidityEvaluator =
        DefaultSessionValidityEvaluator(),
    private val resultMapper: SessionValidityResultMapper =
        DefaultSessionValidityResultMapper(),
) : SessionValidityAuthority {

    override fun evaluateValidity(
        request: SessionValidityRequest,
    ): SessionValidityResult {
        val traceId = request.context.traceId

        val evaluation = evaluator.evaluate(
            traceId = traceId,
            request = request,
        )

        require(evaluation.traceId == traceId) {
            "Session validity request and evaluation result must use the same trace identity."
        }

        val result = resultMapper.map(
            traceId = traceId,
            evaluation = evaluation,
        )

        require(result.traceId == traceId) {
            "Session validity request and mapped result must use the same trace identity."
        }

        return result
    }
}
