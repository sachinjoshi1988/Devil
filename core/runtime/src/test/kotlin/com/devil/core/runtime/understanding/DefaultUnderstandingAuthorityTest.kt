package com.devil.core.runtime.understanding

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnderstandingAuthorityTest {

    @Test
    fun `understand defers without inventing meaning`() {
        val context = createContext(
            "trace-understanding-default-001",
        )
        val authority: UnderstandingAuthority =
            DefaultUnderstandingAuthority()

        val result = authority.understand(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization = createAuthorization(context.traceId),
            conversationIntake =
                createConversationIntake(context.traceId),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            UnderstandingAuthorityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.understanding)
        assertNull(result.error)
    }

    @Test
    fun `understand rejects identity result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-002",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-understanding-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                conversationIntake =
                    createConversationIntake(context.traceId),
            )
        }
    }

    @Test
    fun `understand rejects trust result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-003",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from(
                        "trace-understanding-trust-other",
                    ),
                ),
                authorization = createAuthorization(context.traceId),
                conversationIntake =
                    createConversationIntake(context.traceId),
            )
        }
    }

    @Test
    fun `understand rejects authorization result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-004",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from(
                        "trace-understanding-authorization-other",
                    ),
                ),
                conversationIntake =
                    createConversationIntake(context.traceId),
            )
        }
    }

    @Test
    fun `understand rejects conversation intake from a different trace`() {
        val context = createContext(
            "trace-understanding-default-005",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity = createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(context.traceId),
                conversationIntake = createConversationIntake(
                    TraceId.from(
                        "trace-understanding-intake-other",
                    ),
                ),
            )
        }
    }

    private fun createIdentity(
        traceId: TraceId,
    ): IdentityResult {
        return IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.UNRESOLVED,
        )
    }

    private fun createTrust(
        traceId: TraceId,
    ): TrustResult {
        return TrustResult.create(
            traceId = traceId,
            status = TrustStatus.DEFERRED,
        )
    }

    private fun createAuthorization(
        traceId: TraceId,
    ): AuthorizationResult {
        return AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.DEFERRED,
        )
    }

    private fun createConversationIntake(
        traceId: TraceId,
    ): ConversationIntakeAuthorityResult {
        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status = ConversationIntakeAuthorityStatus.DEFERRED,
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_063_000L,
            ),
        )
    }
}
