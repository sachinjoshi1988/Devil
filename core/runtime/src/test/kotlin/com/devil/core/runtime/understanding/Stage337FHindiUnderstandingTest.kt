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
 * Stage 337F bounded Hindi Understanding proof.
 *
 * DEVANAGARI != HINDI.
 * HINDI_POLICY_MATCH != LANGUAGE_VERIFIED.
 * HINDI_UNDERSTOOD != AUTHORIZED.
 * HINDI_UNDERSTOOD != CAPABILITY_AVAILABLE.
 * HINDI_UNDERSTOOD != EXECUTABLE.
 * UNRESOLVED_REFERENCE != GUESSED_REFERENCE.
 * TRANSLITERATED != TRANSLATED.
 * INPUT_SOURCE != UNDERSTANDING_AUTHORITY.
 */
class Stage337FHindiUnderstandingTest {

    private val resolver =
        DefaultUnderstandingEvaluationResolver()

    @Test
    fun `Hindi greeting establishes bounded detected Hindi evidence`() {
        val understanding =
            evaluate(
                content = "नमस्ते डेविल",
                trace = "trace-stage337f-greeting",
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
            UnderstandingScript.DEVANAGARI,
            understanding.languageEvidence.script,
        )
        assertEquals(
            UnderstandingLanguageEvidenceStatus.DETECTED,
            understanding.languageEvidence.status,
        )
        assertEquals(
            "hi",
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `Hindi open settings reuses existing open target semantics`() {
        val understanding =
            evaluate(
                content = "सेटिंग खोलो",
                trace = "trace-stage337f-open-settings",
            )

        assertEquals(
            UnderstandingIntent.OPEN_TARGET,
            understanding.semantics?.intent,
        )
        assertEquals(
            "settings",
            understanding.semantics?.target,
        )
        assertEquals(
            "hi",
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `Hindi volume decrease and increase reuse canonical action semantics`() {
        val decrease =
            evaluate(
                content = "आवाज़ कम करो",
                trace = "trace-stage337f-volume-down",
            )

        val increase =
            evaluate(
                content = "आवाज़ बढ़ाओ",
                trace = "trace-stage337f-volume-up",
            )

        assertEquals(
            "volume",
            decrease.semantics?.target,
        )
        assertEquals(
            "decrease",
            decrease.semantics?.predicate,
        )
        assertEquals(
            "volume",
            increase.semantics?.target,
        )
        assertEquals(
            "increase",
            increase.semantics?.predicate,
        )
    }

    @Test
    fun `Hindi set volume preserves structured numeric value`() {
        val understanding =
            evaluate(
                content = "आवाज़ 30 प्रतिशत करो",
                trace = "trace-stage337f-set-volume",
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            understanding.semantics?.intent,
        )
        assertEquals(
            "set",
            understanding.semantics?.predicate,
        )
        assertEquals(
            listOf("value", "unit"),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.name },
        )
        assertEquals(
            listOf("30", "percent"),
            understanding.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
    }

    @Test
    fun `Hindi battery and latest notification questions reuse information query semantics`() {
        val battery =
            evaluate(
                content = "मेरी बैटरी कितनी है",
                trace = "trace-stage337f-battery",
            )

        val notification =
            evaluate(
                content = "मेरा लेटेस्ट नोटिफिकेशन पढ़ो",
                trace = "trace-stage337f-notification",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            battery.semantics?.intent,
        )
        assertEquals(
            "battery level",
            battery.semantics?.target,
        )
        assertEquals(
            "query",
            battery.semantics?.predicate,
        )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            notification.semantics?.intent,
        )
        assertEquals(
            "latest notification",
            notification.semantics?.target,
        )
        assertEquals(
            "query",
            notification.semantics?.predicate,
        )
    }

    @Test
    fun `Hindi alarm preserves unresolved time expression`() {
        val understanding =
            evaluate(
                content = "कल सुबह सात बजे का अलार्म लगाओ",
                trace = "trace-stage337f-alarm",
            )

        assertEquals(
            "alarm",
            understanding.semantics?.target,
        )
        assertEquals(
            "set",
            understanding.semantics?.predicate,
        )
        assertEquals(
            "time_expression",
            understanding.semantics
                ?.arguments
                ?.single()
                ?.name,
        )
        assertEquals(
            "कल सुबह सात बजे",
            understanding.semantics
                ?.arguments
                ?.single()
                ?.value,
        )
    }

    @Test
    fun `Hindi messaging and call preserve unresolved references rather than guessing`() {
        val send =
            evaluate(
                content = "राहुल को मैसेज भेजो: वहीं रुको",
                trace = "trace-stage337f-send",
            )

        val reply =
            evaluate(
                content = "राहुल को जवाब दो: मैं आ रहा हूँ",
                trace = "trace-stage337f-reply",
            )

        val call =
            evaluate(
                content = "राहुल को कॉल करो",
                trace = "trace-stage337f-call",
            )

        assertEquals(
            listOf("राहुल", "वहीं रुको"),
            send.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
        assertEquals(
            listOf("राहुल", "मैं आ रहा हूँ"),
            reply.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
        assertEquals(
            "राहुल",
            call.semantics
                ?.arguments
                ?.single()
                ?.value,
        )
    }

    @Test
    fun `Hindi media request preserves unresolved object reference`() {
        val play =
            evaluate(
                content = "वह गाना चलाओ",
                trace = "trace-stage337f-play",
            )

        val pause =
            evaluate(
                content = "संगीत रोक दो",
                trace = "trace-stage337f-pause",
            )

        assertEquals(
            "play",
            play.semantics?.predicate,
        )
        assertEquals(
            "वह गाना",
            play.semantics
                ?.arguments
                ?.single()
                ?.value,
        )

        assertEquals(
            "pause",
            pause.semantics?.predicate,
        )
        assertEquals(
            "media",
            pause.semantics?.target,
        )
    }

    @Test
    fun `Hindi general information question preserves question target without answering`() {
        val who =
            evaluate(
                content = "एडा लवलेस कौन हैं",
                trace = "trace-stage337f-who",
            )

        val about =
            evaluate(
                content = "क्वांटम कंप्यूटिंग के बारे में बताओ",
                trace = "trace-stage337f-about",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            who.semantics?.intent,
        )
        assertEquals(
            "एडा लवलेस",
            who.semantics?.target,
        )
        assertEquals(
            "क्वांटम कंप्यूटिंग",
            about.semantics?.target,
        )
    }

    @Test
    fun `unmatched Devanagari remains unsupported and does not invent Hindi`() {
        val understanding =
            evaluate(
                content = "आज आकाश सुंदर दिसते",
                trace = "trace-stage337f-unmatched-devanagari",
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertNull(
            understanding.semantics,
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
    fun `Marathi-only open wording does not become Hindi merely from Devanagari`() {
        val understanding =
            evaluate(
                content = "सेटिंग उघडा",
                trace = "trace-stage337f-marathi-boundary",
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
        )
        assertNull(
            understanding.semantics,
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
    fun `mixed Hindi and Latin input remains outside bounded Hindi policy`() {
        val understanding =
            evaluate(
                content = "Devil सेटिंग खोलो",
                trace = "trace-stage337f-mixed",
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            understanding.state,
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
    fun `equivalent text and voice Hindi preserve same semantic and language policy`() {
        val text =
            evaluate(
                source = ContextSource.TEXT,
                content = "आवाज़ कम करो",
                trace = "trace-stage337f-text",
            )

        val voice =
            evaluate(
                source = ContextSource.VOICE,
                content = "आवाज़ कम करो",
                trace = "trace-stage337f-voice",
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
        content: String,
        trace: String,
        source: ContextSource = ContextSource.TEXT,
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
                                                            1_754_100_337_006L,
                                                        ),
                                            ),
                                        content = content,
                                    ),
                                state =
                                    ConversationIntakeState.ACCEPTED,
                                rationale =
                                    "Stage 337F accepted conversation intake.",
                            ),
                    ),
            ),
        )
}
