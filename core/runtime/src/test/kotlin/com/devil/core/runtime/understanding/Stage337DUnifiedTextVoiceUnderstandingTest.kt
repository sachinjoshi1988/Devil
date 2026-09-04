package com.devil.core.runtime.understanding

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Stage 337D proof that text and recognized voice use one Understanding policy.
 *
 * Input provenance remains distinct, but equivalent English content must not
 * acquire a separate voice-specific semantic interpretation.
 *
 * INPUT_SOURCE != UNDERSTANDING_AUTHORITY.
 * VOICE_SOURCE != SPEAKER_AUTHENTICATED.
 */
class Stage337DUnifiedTextVoiceUnderstandingTest {

    @Test
    fun `same English request has same semantics for text and voice provenance`() {
        val resolver =
            DefaultUnderstandingEvaluationResolver()

        val textUnderstanding =
            resolver.evaluate(
                request(
                    source = ContextSource.TEXT,
                    traceId =
                        TraceId.from(
                            "trace-stage337d-text-understanding",
                        ),
                    content = "Lower the volume",
                ),
            )

        val voiceUnderstanding =
            resolver.evaluate(
                request(
                    source = ContextSource.VOICE,
                    traceId =
                        TraceId.from(
                            "trace-stage337d-voice-understanding",
                        ),
                    content = "Lower the volume",
                ),
            )

        assertEquals(
            UnderstandingState.COMPLETE,
            textUnderstanding.state,
        )
        assertEquals(
            UnderstandingState.COMPLETE,
            voiceUnderstanding.state,
        )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            textUnderstanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            voiceUnderstanding.semantics?.intent,
        )

        assertEquals(
            textUnderstanding.semantics,
            voiceUnderstanding.semantics,
        )

        assertEquals(
            ContextSource.TEXT,
            textUnderstanding.context.source,
        )
        assertEquals(
            ContextSource.VOICE,
            voiceUnderstanding.context.source,
        )
    }

    private fun request(
        source: ContextSource,
        traceId: TraceId,
        content: String,
    ): UnderstandingEvaluationRequest {
        return UnderstandingEvaluationRequest.create(
            conversationIntake =
                ConversationIntakeResult.create(
                    record =
                        ConversationIntakeRecord.create(
                            input =
                                ConversationInput.create(
                                    context =
                                        ContextEnvelope.create(
                                            traceId = traceId,
                                            schemaVersion =
                                                SchemaVersion.from(1),
                                            source = source,
                                            trustLevel =
                                                ContextTrustLevel.UNVERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                            observedAt =
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_788_000_337_004L,
                                                    ),
                                        ),
                                    content = content,
                                ),
                            state =
                                ConversationIntakeState.ACCEPTED,
                            rationale =
                                "Stage337D accepted conversation input.",
                        ),
                ),
        )
    }
}
