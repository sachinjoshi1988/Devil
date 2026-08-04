package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.common.TraceId

/**
 * Default Stage 4 mapping from authorization assessments to AuthorizationResult.
 *
 * The mapper preserves the established constitutional continuation state. It
 * does not authorize an individual capability, grant operating-system
 * permission, enter Owner Mode, execute actions, observe results, or verify
 * outcomes.
 */
class DefaultAuthorizationEvaluationResultMapper :
    AuthorizationEvaluationResultMapper {

    override fun map(
        traceId: TraceId,
        assessment: AuthorizationAssessment,
    ): AuthorizationResult {
        val status = when (assessment.state) {
            AuthorizationEvaluationState.AUTHORIZED ->
                AuthorizationStatus.AUTHORIZED

            AuthorizationEvaluationState.DENIED ->
                AuthorizationStatus.DENIED

            AuthorizationEvaluationState.DEFERRED ->
                AuthorizationStatus.DEFERRED
        }

        return AuthorizationResult.create(
            traceId = traceId,
            status = status,
        )
    }
}
