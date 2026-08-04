package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultAuthorizationEvaluationResultMapperTest {

    @Test
    fun `map preserves authorized continuation state`() {
        val traceId = TraceId.from(
            "trace-authorization-result-mapper-001",
        )
        val mapper: AuthorizationEvaluationResultMapper =
            DefaultAuthorizationEvaluationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            assessment = createAssessment(
                AuthorizationEvaluationState.AUTHORIZED,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            AuthorizationStatus.AUTHORIZED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves denied continuation state`() {
        val result = DefaultAuthorizationEvaluationResultMapper().map(
            traceId = TraceId.from(
                "trace-authorization-result-mapper-002",
            ),
            assessment = createAssessment(
                AuthorizationEvaluationState.DENIED,
            ),
        )

        assertEquals(
            AuthorizationStatus.DENIED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves deferred continuation state`() {
        val result = DefaultAuthorizationEvaluationResultMapper().map(
            traceId = TraceId.from(
                "trace-authorization-result-mapper-003",
            ),
            assessment = createAssessment(
                AuthorizationEvaluationState.DEFERRED,
            ),
        )

        assertEquals(
            AuthorizationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    private fun createAssessment(
        state: AuthorizationEvaluationState,
    ): AuthorizationAssessment {
        return AuthorizationAssessment.create(
            subjectIdentityId = IdentityId.from(
                "subject-authorization-result-mapper",
            ),
            state = state,
            rationale = "Bounded constitutional authorization assessment.",
        )
    }
}
