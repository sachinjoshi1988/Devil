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
import com.devil.core.model.understanding.UnderstandingLanguageEvidenceStatus
import com.devil.core.model.understanding.UnderstandingScript
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Stage 337E executable proof for the provider-neutral Multilingual Language
 * Foundation.
 *
 * SCRIPT_RECOGNIZED != LANGUAGE_IDENTIFIED.
 * LANGUAGE_DETECTED != LANGUAGE_VERIFIED.
 * LANGUAGE_DECLARED != LANGUAGE_VERIFIED.
 * DEVANAGARI != HINDI.
 * DEVANAGARI != MARATHI.
 * LANGUAGE_IDENTIFIED != UNDERSTANDING_COMPLETE.
 * TRANSLITERATED != TRANSLATED.
 * INPUT_SOURCE != UNDERSTANDING_AUTHORITY.
 */
class Stage337EMultilingualLanguageFoundationTest {

    private val resolver =
        DefaultUnderstandingEvaluationResolver()

    @Test
    fun `bounded English greeting carries detected English evidence`() {
        val understanding =
            evaluate(
                source = ContextSource.TEXT,
                content = "Hello Devil",
                trace = "trace-stage337e-english-greeting",
            )

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            UnderstandingIntent.GREETING,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingScript.LATIN,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.DETECTED,
            understanding.languageEvidence.status,
        )
        assertEquals(
            "en",
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `bounded English action request carries detected English evidence`() {
        val understanding =
            evaluate(
                source = ContextSource.TEXT,
                content = "Lower the volume",
                trace = "trace-stage337e-english-action",
            )

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            UnderstandingScript.LATIN,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.DETECTED,
            understanding.languageEvidence.status,
        )
        assertEquals(
            "en",
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `unsupported Latin content preserves script without inventing language`() {
        val understanding =
            evaluate(
                source = ContextSource.TEXT,
                content = "Can you help me with this?",
                trace = "trace-stage337e-unsupported-latin",
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertEquals(
            UnderstandingScript.LATIN,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.UNKNOWN,
            understanding.languageEvidence.status,
        )
        assertNull(
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `Devanagari remains language unknown rather than guessed Hindi or Marathi`() {
        val understanding =
            evaluate(
                source = ContextSource.TEXT,
                content = "कखग घचज",
                trace = "trace-stage337e-devanagari",
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertEquals(
            UnderstandingScript.DEVANAGARI,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.UNKNOWN,
            understanding.languageEvidence.status,
        )
        assertNull(
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `Latin and Devanagari content is mixed and language unknown`() {
        val understanding =
            evaluate(
                source = ContextSource.TEXT,
                content = "Devil सेटिंग खोलो",
                trace = "trace-stage337e-mixed",
            )

        assertEquals(
            UnderstandingScript.MIXED,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.UNKNOWN,
            understanding.languageEvidence.status,
        )
        assertNull(
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `digits punctuation and whitespace do not invent script or language`() {
        val understanding =
            evaluate(
                source = ContextSource.TEXT,
                content = "123 !!!",
                trace = "trace-stage337e-non-language",
            )

        assertEquals(
            UnderstandingScript.UNKNOWN,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.UNKNOWN,
            understanding.languageEvidence.status,
        )
        assertNull(
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `equivalent text and voice English preserve same language and semantic policy`() {
        val text =
            evaluate(
                source = ContextSource.TEXT,
                content = "Lower the volume",
                trace = "trace-stage337e-text",
            )

        val voice =
            evaluate(
                source = ContextSource.VOICE,
                content = "Lower the volume",
                trace = "trace-stage337e-voice",
            )

        assertEquals(
            text.semantics,
            voice.semantics,
        )
        assertEquals(
            text.languageEvidence,
            voice.languageEvidence,
        )
        assertEquals(
            ContextSource.TEXT,
            text.context.source,
        )
        assertEquals(
            ContextSource.VOICE,
            voice.context.source,
        )
    }

    private fun evaluate(
        source: ContextSource,
        content: String,
        trace: String,
    ) =
        resolver.evaluate(
            UnderstandingEvaluationRequest.create(
                conversationIntake =
                    ConversationIntakeResult.create(
                        record =
                            ConversationIntakeRecord.create(
                                input =
                                    ConversationInput.create(
                                        context =
                                            ContextEnvelope.create(
                                                traceId =
                                                    TraceId.from(trace),
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
                                                            1_754_000_337_005L,
                                                        ),
                                            ),
                                        content = content,
                                    ),
                                state =
                                    ConversationIntakeState.ACCEPTED,
                                rationale =
                                    "Stage 337E accepted conversation intake.",
                            ),
                    ),
            ),
        )
}
