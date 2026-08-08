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

class DefaultSecurityTransitionAuthorityTest {

    @Test
    fun `default authority safely defers without security transition policy`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-security-transition-authority-001",
            ),
            currentStage = SecurityStage.LOCKED,
            requestedStage = SecurityStage.WAKE,
        )

        val authority: SecurityTransitionAuthority =
            DefaultSecurityTransitionAuthority()

        val result = authority.evaluateTransition(request)

        assertEquals(
            request.context.traceId,
            result.traceId,
        )
        assertEquals(
            SecurityTransitionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `authority does not advance security state when evaluation is unavailable`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-security-transition-authority-002",
            ),
            currentStage = SecurityStage.WAKE,
            requestedStage = SecurityStage.AUTHENTICATION,
        )

        val result =
            DefaultSecurityTransitionAuthority()
                .evaluateTransition(request)

        assertEquals(
            SecurityStage.WAKE,
            request.currentState.stage,
        )
        assertEquals(
            SecurityStage.AUTHENTICATION,
            request.requestedStage,
        )
        assertEquals(
            SecurityTransitionStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `authority preserves approved bounded transition request`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-security-transition-authority-003",
            ),
            currentStage = SecurityStage.LOCKED,
            requestedStage = SecurityStage.WAKE,
        )

        val evaluator =
            object : SecurityTransitionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: SecurityTransitionRequest,
                ): SecurityTransitionEvaluationResult {
                    return SecurityTransitionEvaluationResult.create(
                        traceId = traceId,
                        status =
                            SecurityTransitionEvaluationStatus.APPROVED,
                        request = request,
                    )
                }
            }

        val authority =
            DefaultSecurityTransitionAuthority(
                evaluator = evaluator,
            )

        val result = authority.evaluateTransition(request)

        assertEquals(
            SecurityTransitionStatus.APPROVED,
            result.status,
        )
        assertEquals(
            request,
            result.request,
        )

        assertEquals(
            SecurityStage.LOCKED,
            request.currentState.stage,
        )
    }

    @Test
    fun `authority rejects evaluator result from another trace`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-security-transition-authority-004",
            ),
            currentStage = SecurityStage.LOCKED,
            requestedStage = SecurityStage.WAKE,
        )

        val evaluator =
            object : SecurityTransitionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: SecurityTransitionRequest,
                ): SecurityTransitionEvaluationResult {
                    return SecurityTransitionEvaluationResult.create(
                        traceId =
                            TraceId.from(
                                "trace-security-transition-authority-evaluation-other",
                            ),
                        status =
                            SecurityTransitionEvaluationStatus.UNAVAILABLE,
                    )
                }
            }

        val authority =
            DefaultSecurityTransitionAuthority(
                evaluator = evaluator,
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluateTransition(request)
        }
    }

    @Test
    fun `authority rejects mapped result from another trace`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-security-transition-authority-005",
            ),
            currentStage = SecurityStage.LOCKED,
            requestedStage = SecurityStage.WAKE,
        )

        val mapper =
            object : SecurityTransitionResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: SecurityTransitionEvaluationResult,
                ): SecurityTransitionResult {
                    return SecurityTransitionResult.create(
                        traceId =
                            TraceId.from(
                                "trace-security-transition-authority-result-other",
                            ),
                        status = SecurityTransitionStatus.DEFERRED,
                    )
                }
            }

        val authority =
            DefaultSecurityTransitionAuthority(
                resultMapper = mapper,
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluateTransition(request)
        }
    }

    @Test
    fun `session request remains only a request when default authority defers`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-security-transition-authority-006",
            ),
            currentStage = SecurityStage.AUTHENTICATION,
            requestedStage = SecurityStage.SESSION,
        )

        val result =
            DefaultSecurityTransitionAuthority()
                .evaluateTransition(request)

        assertEquals(
            SecurityStage.AUTHENTICATION,
            request.currentState.stage,
        )
        assertEquals(
            SecurityStage.SESSION,
            request.requestedStage,
        )
        assertEquals(
            SecurityTransitionStatus.DEFERRED,
            result.status,
        )
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
                    trustLevel =
                        ContextTrustLevel.UNVERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_195_000L,
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
