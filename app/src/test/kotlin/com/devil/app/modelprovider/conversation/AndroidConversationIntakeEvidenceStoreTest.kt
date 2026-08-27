package com.devil.app.modelprovider.conversation

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
import kotlin.test.assertNull
import kotlin.test.assertSame

class AndroidConversationIntakeEvidenceStoreTest {

    @Test
    fun `runtime observed intake is consumed by exact trace identity`() {
        val store =
            AndroidConversationIntakeEvidenceStore()

        var downstream:
            ConversationIntakeAuthorityResult? = null

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationIntakeEvidencePort =
                    store,
                conversationRecordAuthority =
                    object : ConversationRecordAuthority {

                        override fun record(
                            conversationIntake:
                                ConversationIntakeAuthorityResult,
                        ): ConversationRecordResult {
                            downstream =
                                conversationIntake

                            return ConversationRecordResult.create(
                                traceId =
                                    conversationIntake.traceId,
                                status =
                                    ConversationRecordStatus.DEFERRED,
                            )
                        }
                    },
            )

        val input =
            input(
                traceValue =
                    "trace-stage-313-android-intake-store-001",
            )

        runtime.accept(input)

        val consumed =
            store.consume(
                traceId =
                    input.context.traceId,
            )

        assertSame(
            downstream,
            consumed,
            "Stage 313 must preserve the exact constitutional intake object.",
        )

        assertEquals(
            0,
            store.size(),
        )
    }

    @Test
    fun `evidence is one shot and cannot be consumed twice`() {
        val store =
            AndroidConversationIntakeEvidenceStore()

        val input =
            input(
                traceValue =
                    "trace-stage-313-android-intake-store-002",
            )

        DefaultUnifiedDevilRuntime(
            conversationIntakeEvidencePort =
                store,
        ).accept(input)

        val first =
            store.consume(
                input.context.traceId,
            )

        val second =
            store.consume(
                input.context.traceId,
            )

        kotlin.test.assertNotNull(first)
        assertNull(second)
    }

    @Test
    fun `unknown trace fails closed without substitute evidence`() {
        val store =
            AndroidConversationIntakeEvidenceStore()

        assertNull(
            store.consume(
                TraceId.from(
                    "trace-stage-313-android-intake-store-unknown",
                ),
            ),
        )
    }

    @Test
    fun `bounded store drops oldest evidence instead of becoming history storage`() {
        val store =
            AndroidConversationIntakeEvidenceStore(
                maximumEntries = 2,
            )

        val first =
            input(
                "trace-stage-313-android-intake-store-bound-001",
            )

        val second =
            input(
                "trace-stage-313-android-intake-store-bound-002",
            )

        val third =
            input(
                "trace-stage-313-android-intake-store-bound-003",
            )

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationIntakeEvidencePort =
                    store,
            )

        runtime.accept(first)
        runtime.accept(second)
        runtime.accept(third)

        assertEquals(
            2,
            store.size(),
        )

        assertNull(
            store.consume(first.context.traceId),
        )

        kotlin.test.assertNotNull(
            store.consume(second.context.traceId),
        )

        kotlin.test.assertNotNull(
            store.consume(third.context.traceId),
        )
    }

    private fun input(
        traceValue: String,
    ): ConversationInput {
        return ConversationInput.create(
            context =
                ContextEnvelope.create(
                    traceId =
                        TraceId.from(traceValue),
                    schemaVersion =
                        SchemaVersion.from(1),
                    source =
                        ContextSource.TEST,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_313_000L,
                        ),
                ),
            content =
                "Stage 313 bounded conversational evidence.",
        )
    }
}
