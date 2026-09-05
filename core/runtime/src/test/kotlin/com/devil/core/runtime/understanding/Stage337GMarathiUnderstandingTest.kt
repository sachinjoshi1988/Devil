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
 * Stage 337G bounded Marathi Understanding proof.
 *
 * DEVANAGARI != MARATHI.
 * MARATHI_POLICY_MATCH != LANGUAGE_VERIFIED.
 * SHARED_DEVANAGARI_EXPRESSION != LANGUAGE_IDENTIFIED.
 * MARATHI_UNDERSTOOD != AUTHORIZED.
 * MARATHI_UNDERSTOOD != CAPABILITY_AVAILABLE.
 * MARATHI_UNDERSTOOD != EXECUTABLE.
 * UNRESOLVED_REFERENCE != GUESSED_REFERENCE.
 * TRANSLITERATED != TRANSLATED.
 */
class Stage337GMarathiUnderstandingTest {

    private val resolver =
        DefaultUnderstandingEvaluationResolver()

    @Test
    fun `Marathi conversational greeting establishes detected Marathi evidence`() {
        val understanding =
            evaluate(
                content = "कसा आहेस डेविल",
                trace = "trace-stage337g-greeting",
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
            "mr",
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `Marathi open settings reuses existing open target semantics`() {
        val understanding =
            evaluate(
                content = "सेटिंग उघडा",
                trace = "trace-stage337g-open-settings",
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
            "mr",
            understanding.languageEvidence.languageTag,
        )
    }

    @Test
    fun `Marathi volume decrease and increase reuse canonical action semantics`() {
        val decrease =
            evaluate(
                content = "आवाज कमी करा",
                trace = "trace-stage337g-volume-down",
            )

        val increase =
            evaluate(
                content = "आवाज वाढवा",
                trace = "trace-stage337g-volume-up",
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
    fun `Marathi set volume accepts Devanagari digits and canonicalizes value`() {
        val understanding =
            evaluate(
                content = "आवाज ३० टक्के करा",
                trace = "trace-stage337g-set-volume",
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
    fun `Marathi battery and notification questions reuse information semantics`() {
        val battery =
            evaluate(
                content = "माझी बॅटरी किती आहे",
                trace = "trace-stage337g-battery",
            )

        val notification =
            evaluate(
                content = "माझ्या नवीनतम सूचना दाखवा",
                trace = "trace-stage337g-notification",
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
    fun `Marathi alarm preserves unresolved time expression`() {
        val understanding =
            evaluate(
                content = "सकाळी सात वाजता गजर लावा",
                trace = "trace-stage337g-alarm",
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
            "सकाळी सात वाजता",
            understanding.semantics
                ?.arguments
                ?.single()
                ?.value,
        )
    }

    @Test
    fun `Marathi messaging reply and call preserve unresolved references`() {
        val send =
            evaluate(
                content = "राहुलला संदेश पाठवा: तिथे थांब",
                trace = "trace-stage337g-send",
            )

        val reply =
            evaluate(
                content = "राहुलला उत्तर द्या: मी येतोय",
                trace = "trace-stage337g-reply",
            )

        val call =
            evaluate(
                content = "राहुलला फोन करा",
                trace = "trace-stage337g-call",
            )

        assertEquals(
            listOf("राहुल", "तिथे थांब"),
            send.semantics
                ?.arguments
                ?.map { argument -> argument.value },
        )
        assertEquals(
            listOf("राहुल", "मी येतोय"),
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
    fun `Marathi media request preserves unresolved object reference`() {
        val play =
            evaluate(
                content = "ते गाणे वाजवा",
                trace = "trace-stage337g-play",
            )

        val pause =
            evaluate(
                content = "संगीत थांबवा",
                trace = "trace-stage337g-pause",
            )

        assertEquals(
            "play",
            play.semantics?.predicate,
        )
        assertEquals(
            "ते गाणे",
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
    fun `Marathi information questions preserve targets without answering`() {
        val who =
            evaluate(
                content = "एडा लवलेस कोण आहे",
                trace = "trace-stage337g-who",
            )

        val about =
            evaluate(
                content = "क्वांटम कंप्यूटिंगबद्दल सांगा",
                trace = "trace-stage337g-about",
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
    fun `Hindi and Marathi open settings remain distinct bounded language policies`() {
        val hindi =
            evaluate(
                content = "सेटिंग खोलो",
                trace = "trace-stage337g-hindi-open",
            )

        val marathi =
            evaluate(
                content = "सेटिंग उघडा",
                trace = "trace-stage337g-marathi-open",
            )

        assertEquals(
            "hi",
            hindi.languageEvidence.languageTag,
        )
        assertEquals(
            "mr",
            marathi.languageEvidence.languageTag,
        )
        assertEquals(
            hindi.semantics?.intent,
            marathi.semantics?.intent,
        )
        assertEquals(
            hindi.semantics?.target,
            marathi.semantics?.target,
        )
    }

    @Test
    fun `shared Namaskar greeting remains language unknown rather than guessed`() {
        val understanding =
            evaluate(
                content = "नमस्कार",
                trace = "trace-stage337g-shared-namaskar",
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
    fun `unmatched Devanagari remains language unknown`() {
        val understanding =
            evaluate(
                content = "कखग घचज",
                trace = "trace-stage337g-unmatched",
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
    fun `mixed Latin and Marathi remains outside bounded Marathi policy`() {
        val understanding =
            evaluate(
                content = "Devil सेटिंग उघडा",
                trace = "trace-stage337g-mixed",
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
    fun `equivalent text and voice Marathi preserve same semantic and language policy`() {
        val text =
            evaluate(
                source = ContextSource.TEXT,
                content = "आवाज कमी करा",
                trace = "trace-stage337g-text",
            )

        val voice =
            evaluate(
                source = ContextSource.VOICE,
                content = "आवाज कमी करा",
                trace = "trace-stage337g-voice",
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
                                                            1_754_200_337_007L,
                                                        ),
                                            ),
                                        content = content,
                                    ),
                                state =
                                    ConversationIntakeState.ACCEPTED,
                                rationale =
                                    "Stage 337G accepted conversation intake.",
                            ),
                    ),
            ),
        )
}
