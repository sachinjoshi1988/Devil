package com.devil.core.runtime.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultConversationIntakeAuthorityTest {

    @Test
    fun `intake accepts input when constitutional continuation is authorized`() {
        val input = createInput(
            "trace-conversation-intake-default-001",
        )
        val authority: ConversationIntakeAuthority =
            DefaultConversationIntakeAuthority()

        val result = authority.intake(
            input = input,
            identity = createIdentity(input.context.traceId),
            trust = createTrust(input.context.traceId),
            authorization = AuthorizationResult.create(
                traceId = input.context.traceId,
                status = AuthorizationStatus.AUTHORIZED,
            ),
        )

        assertEquals(
            ConversationIntakeAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.ACCEPTED,
            requireNotNull(result.intake).record.state,
        )
        assertEquals(input, result.intake.record.input)
        assertNull(result.error)
    }

    @Test
    fun `intake rejects input when constitutional continuation is denied`() {
        val input = createInput(
            "trace-conversation-intake-default-002",
        )

        val result = DefaultConversationIntakeAuthority().intake(
            input = input,
            identity = createIdentity(input.context.traceId),
            trust = createTrust(input.context.traceId),
            authorization = AuthorizationResult.create(
                traceId = input.context.traceId,
                status = AuthorizationStatus.DENIED,
            ),
        )

        assertEquals(
            ConversationIntakeAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.REJECTED,
            requireNotNull(result.intake).record.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `intake defers input when constitutional authorization is deferred`() {
        val input = createInput(
            "trace-conversation-intake-default-003",
        )

        val result = DefaultConversationIntakeAuthority().intake(
            input = input,
            identity = createIdentity(input.context.traceId),
            trust = createTrust(input.context.traceId),
            authorization = AuthorizationResult.create(
                traceId = input.context.traceId,
                status = AuthorizationStatus.DEFERRED,
            ),
        )

        assertEquals(
            ConversationIntakeAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            ConversationIntakeState.DEFERRED,
            requireNotNull(result.intake).record.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `intake preserves failed authorization error`() {
        val input = createInput(
            "trace-conversation-intake-default-004",
        )
        val error = createError(input.context.traceId)

        val result = DefaultConversationIntakeAuthority().intake(
            input = input,
            identity = createIdentity(input.context.traceId),
            trust = createTrust(input.context.traceId),
            authorization = AuthorizationResult.create(
                traceId = input.context.traceId,
                status = AuthorizationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(
            ConversationIntakeAuthorityStatus.FAILED,
            result.status,
        )
        assertNull(result.intake)
        assertEquals(error, result.error)
    }

    @Test
    fun `intake rejects identity result from a different trace`() {
        val input = createInput(
            "trace-conversation-intake-default-005",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultConversationIntakeAuthority().intake(
                input = input,
                identity = createIdentity(
                    TraceId.from(
                        "trace-conversation-intake-identity-other",
                    ),
                ),
                trust = createTrust(input.context.traceId),
                authorization = AuthorizationResult.create(
                    traceId = input.context.traceId,
                    status = AuthorizationStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `intake rejects trust result from a different trace`() {
        val input = createInput(
            "trace-conversation-intake-default-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultConversationIntakeAuthority().intake(
                input = input,
                identity = createIdentity(input.context.traceId),
                trust = createTrust(
                    TraceId.from(
                        "trace-conversation-intake-trust-other",
                    ),
                ),
                authorization = AuthorizationResult.create(
                    traceId = input.context.traceId,
                    status = AuthorizationStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `intake rejects authorization result from a different trace`() {
        val input = createInput(
            "trace-conversation-intake-default-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultConversationIntakeAuthority().intake(
                input = input,
                identity = createIdentity(input.context.traceId),
                trust = createTrust(input.context.traceId),
                authorization = AuthorizationResult.create(
                    traceId = TraceId.from(
                        "trace-conversation-intake-authorization-other",
                    ),
                    status = AuthorizationStatus.DEFERRED,
                ),
            )
        }
    }

    private fun createInput(
        traceValue: String,
    ): ConversationInput {
        return ConversationInput.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(traceValue),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_062_000L,
                ),
            ),
            content = "Please play my favourite music.",
        )
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

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "AUTHORIZATION_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_062_500L,
            ),
            summary = "Authorization evaluation failed.",
        )
    }
}
