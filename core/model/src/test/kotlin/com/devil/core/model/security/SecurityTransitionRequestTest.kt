package com.devil.core.model.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SecurityTransitionRequestTest {

    @Test
    fun `create preserves context current state requested stage and normalized rationale`() {
        val context = createContext(
            "trace-security-transition-001",
        )
        val currentState = SecurityStateRecord.create(
            stage = SecurityStage.LOCKED,
            rationale = "Application is locked.",
        )

        val request = SecurityTransitionRequest.create(
            context = context,
            currentState = currentState,
            requestedStage = SecurityStage.WAKE,
            rationale = "  Wake attention was requested.  ",
        )

        assertEquals(context, request.context)
        assertEquals(currentState, request.currentState)
        assertEquals(
            SecurityStage.WAKE,
            request.requestedStage,
        )
        assertEquals(
            "Wake attention was requested.",
            request.rationale,
        )
    }

    @Test
    fun `requesting authentication does not alter the current wake state`() {
        val currentState = SecurityStateRecord.create(
            stage = SecurityStage.WAKE,
            rationale = "Wake attention is established.",
        )

        val request = SecurityTransitionRequest.create(
            context = createContext(
                "trace-security-transition-002",
            ),
            currentState = currentState,
            requestedStage = SecurityStage.AUTHENTICATION,
            rationale = "Authentication evaluation was requested.",
        )

        assertEquals(
            SecurityStage.WAKE,
            request.currentState.stage,
        )
        assertEquals(
            SecurityStage.AUTHENTICATION,
            request.requestedStage,
        )
    }

    @Test
    fun `request model does not enforce constitutional transition adjacency`() {
        val currentState = SecurityStateRecord.create(
            stage = SecurityStage.LOCKED,
            rationale = "Application is locked.",
        )

        val request = SecurityTransitionRequest.create(
            context = createContext(
                "trace-security-transition-003",
            ),
            currentState = currentState,
            requestedStage = SecurityStage.OWNER_MODE,
            rationale = "Owner Mode transition evaluation was requested.",
        )

        assertEquals(
            SecurityStage.OWNER_MODE,
            request.requestedStage,
        )
    }

    @Test
    fun `create rejects request targeting current security stage`() {
        val currentState = SecurityStateRecord.create(
            stage = SecurityStage.SESSION,
            rationale = "Session stage is established.",
        )

        assertFailsWith<IllegalArgumentException> {
            SecurityTransitionRequest.create(
                context = createContext(
                    "trace-security-transition-004",
                ),
                currentState = currentState,
                requestedStage = SecurityStage.SESSION,
                rationale = "Session was requested.",
            )
        }
    }

    @Test
    fun `create rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityTransitionRequest.create(
                context = createContext(
                    "trace-security-transition-005",
                ),
                currentState = SecurityStateRecord.create(
                    stage = SecurityStage.LOCKED,
                    rationale = "Application is locked.",
                ),
                requestedStage = SecurityStage.WAKE,
                rationale = "   ",
            )
        }
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_191_000L,
                ),
        )
    }
}
