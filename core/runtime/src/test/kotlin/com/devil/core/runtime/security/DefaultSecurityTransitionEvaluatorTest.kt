package com.devil.core.runtime.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.model.security.SecurityTransitionRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultSecurityTransitionEvaluatorTest {

    @Test
    fun `evaluate returns unavailable without inventing security transition policy`() {
        val traceId =
            TraceId.from(
                "trace-default-security-evaluator-001",
            )
        val evaluator: SecurityTransitionEvaluator =
            DefaultSecurityTransitionEvaluator()

        val result = evaluator.evaluate(
            traceId = traceId,
            request =
                createRequest(
                    traceId = traceId,
                    currentStage = SecurityStage.LOCKED,
                    requestedStage = SecurityStage.WAKE,
                ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            SecurityTransitionEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate does not treat authentication request as authentication proof`() {
        val traceId =
            TraceId.from(
                "trace-default-security-evaluator-002",
            )
        val request =
            createRequest(
                traceId = traceId,
                currentStage = SecurityStage.WAKE,
                requestedStage = SecurityStage.AUTHENTICATION,
            )

        val result =
            DefaultSecurityTransitionEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            SecurityStage.WAKE,
            request.currentState.stage,
        )
        assertEquals(
            SecurityStage.AUTHENTICATION,
            request.requestedStage,
        )
        assertEquals(
            SecurityTransitionEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate does not treat session request as session creation`() {
        val traceId =
            TraceId.from(
                "trace-default-security-evaluator-003",
            )
        val request =
            createRequest(
                traceId = traceId,
                currentStage = SecurityStage.AUTHENTICATION,
                requestedStage = SecurityStage.SESSION,
            )

        val result =
            DefaultSecurityTransitionEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            SecurityStage.AUTHENTICATION,
            request.currentState.stage,
        )
        assertEquals(
            SecurityStage.SESSION,
            request.requestedStage,
        )
        assertEquals(
            SecurityTransitionEvaluationStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `evaluate does not treat owner mode request as owner authority`() {
        val traceId =
            TraceId.from(
                "trace-default-security-evaluator-004",
            )
        val request =
            createRequest(
                traceId = traceId,
                currentStage = SecurityStage.SESSION,
                requestedStage = SecurityStage.OWNER_MODE,
            )

        val result =
            DefaultSecurityTransitionEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            SecurityStage.SESSION,
            request.currentState.stage,
        )
        assertEquals(
            SecurityStage.OWNER_MODE,
            request.requestedStage,
        )
        assertEquals(
            SecurityTransitionEvaluationStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `evaluate does not approve high security confirmation without policy`() {
        val traceId =
            TraceId.from(
                "trace-default-security-evaluator-005",
            )
        val request =
            createRequest(
                traceId = traceId,
                currentStage = SecurityStage.OWNER_MODE,
                requestedStage =
                    SecurityStage.HIGH_SECURITY_CONFIRMATION,
            )

        val result =
            DefaultSecurityTransitionEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            SecurityTransitionEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultSecurityTransitionEvaluator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-default-security-evaluator-006",
                    ),
                request =
                    createRequest(
                        traceId =
                            TraceId.from(
                                "trace-default-security-evaluator-other",
                            ),
                        currentStage = SecurityStage.LOCKED,
                        requestedStage = SecurityStage.WAKE,
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
        currentStage: SecurityStage,
        requestedStage: SecurityStage,
    ): SecurityTransitionRequest {
        return SecurityTransitionRequest.create(
            context =
                ContextEnvelope.create(
                    traceId = traceId,
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.SYSTEM,
                    trustLevel = ContextTrustLevel.UNVERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_193_000L,
                        ),
                ),
            currentState =
                SecurityStateRecord.create(
                    stage = currentStage,
                    rationale =
                        "Current constitutional security stage is established.",
                ),
            requestedStage = requestedStage,
            rationale =
                "Security transition evaluation was requested.",
        )
    }
}
