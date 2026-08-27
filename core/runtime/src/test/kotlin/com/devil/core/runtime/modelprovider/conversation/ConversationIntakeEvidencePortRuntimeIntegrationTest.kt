package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationRecordAuthority
import com.devil.core.runtime.conversation.ConversationRecordResult
import com.devil.core.runtime.conversation.ConversationRecordStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame

/**
 * Stage 313 integration coverage for the passive constitutional
 * conversation-intake evidence boundary.
 *
 * The test verifies that:
 *
 * - the single Unified Devil Runtime remains the producer of constitutional
 *   conversation-intake evidence;
 * - the Stage 313 evidence port observes the exact authority result instance;
 * - that same exact result continues downstream to ConversationRecordAuthority;
 * - trace identity remains unchanged;
 * - and the evidence port creates no replacement authority or intake result.
 *
 * OBSERVATION OF INTAKE != CONVERSATION INTAKE AUTHORITY.
 * INTAKE ACCEPTED != MODEL AUTHORIZATION.
 * INTAKE ACCEPTED != MODEL OUTPUT VERIFIED.
 * GENERATED != VERIFIED.
 */
class ConversationIntakeEvidencePortRuntimeIntegrationTest {

    @Test
    fun `runtime exposes exact constitutional conversation intake result to passive evidence port`() {
        val input =
            ConversationInput.create(
                context =
                    ContextEnvelope.create(
                        traceId =
                            TraceId.from(
                                "trace-stage-313-intake-evidence-001",
                            ),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEST,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_313_000L,
                            ),
                    ),
                content =
                    "Stage 313 bounded conversation.",
            )

        var observed:
            ConversationIntakeAuthorityResult? = null

        var downstream:
            ConversationIntakeAuthorityResult? = null

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationIntakeEvidencePort =
                    ConversationIntakeEvidencePort {
                        conversationIntake ->

                        observed = conversationIntake
                    },
                conversationRecordAuthority =
                    object : ConversationRecordAuthority {
                        override fun record(
                            conversationIntake:
                                ConversationIntakeAuthorityResult,
                        ): ConversationRecordResult {
                            downstream = conversationIntake

                            return ConversationRecordResult.create(
                                traceId =
                                    conversationIntake.traceId,
                                status =
                                    ConversationRecordStatus.DEFERRED,
                            )
                        }
                    },
            )

        runtime.accept(input)

        val observedResult =
            assertNotNull(observed)

        val downstreamResult =
            assertNotNull(downstream)

        assertEquals(
            input.context.traceId,
            observedResult.traceId,
        )

        assertSame(
            observedResult,
            downstreamResult,
            "Stage 313 evidence port must observe the exact intake instance preserved downstream.",
        )
    }
}
