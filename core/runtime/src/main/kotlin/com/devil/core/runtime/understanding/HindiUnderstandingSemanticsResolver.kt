package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingSemanticArgument
import com.devil.core.model.understanding.UnderstandingSemantics

/**
 * Stage 337F provider-neutral bounded Hindi semantic-understanding boundary.
 *
 * Implementations may establish only semantics supported by their explicit
 * Hindi policy. They do not authorize, execute, translate, transliterate,
 * resolve contacts/references, or establish verified language truth.
 *
 * DEVANAGARI != HINDI.
 * HINDI_POLICY_MATCH != LANGUAGE_VERIFIED.
 * HINDI_UNDERSTOOD != AUTHORIZED.
 * HINDI_UNDERSTOOD != EXECUTABLE.
 */
interface HindiUnderstandingSemanticsResolver {
    fun resolve(
        content: String,
    ): UnderstandingSemantics?
}

/**
 * Deterministic Stage 337F Devanagari-Hindi semantics implementation.
 *
 * This bounded implementation intentionally rejects mixed-script input.
 * Shared Devanagari expressions that do not uniquely identify Hindi are also
 * rejected rather than guessed.
 * Romanized Hindi / Hinglish, Marathi, translation, transliteration, model
 * inference, Android APIs, capability selection, authorization, and execution
 * are outside Stage 337F.
 */
class DefaultHindiUnderstandingSemanticsResolver :
    HindiUnderstandingSemanticsResolver {

    override fun resolve(
        content: String,
    ): UnderstandingSemantics? {
        if (!isDevanagariText(content)) {
            return null
        }

        val normalized = normalize(content)

        greetingSemantics(normalized)?.let { return it }
        openTargetSemantics(normalized)?.let { return it }
        informationQuerySemantics(normalized)?.let { return it }
        actionRequestSemantics(normalized)?.let { return it }
        informationalSemantics(normalized)?.let { return it }

        return null
    }

    private fun greetingSemantics(
        normalized: String,
    ): UnderstandingSemantics? {
        if (
            normalized !in
            setOf(
                "नमस्ते",
                "नमस्ते डेविल",
            )
        ) {
            return null
        }

        return UnderstandingSemantics.create(
            intent = UnderstandingIntent.GREETING,
            actionability =
                UnderstandingActionability.NON_ACTIONABLE,
            meaning = "greeting",
        )
    }

    private fun openTargetSemantics(
        normalized: String,
    ): UnderstandingSemantics? {
        val match =
            OPEN_TARGET_PATTERN.matchEntire(normalized)
                ?: return null

        val capturedTarget =
            cleanCapturedValue(
                match.groupValues[1],
            )

        if (capturedTarget.isEmpty()) {
            return null
        }

        val target =
            when (capturedTarget) {
                "सेटिंग",
                "सेटिंग्स",
                -> "settings"

                else -> capturedTarget
            }

        return UnderstandingSemantics.create(
            intent = UnderstandingIntent.OPEN_TARGET,
            actionability =
                UnderstandingActionability.ACTIONABLE,
            meaning = "open target",
            target = target,
        )
    }

    private fun informationQuerySemantics(
        normalized: String,
    ): UnderstandingSemantics? {
        if (BATTERY_QUERY_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "query battery level",
                target = "battery level",
                predicate = "query",
            )
        }

        val notificationMatch =
            NOTIFICATION_QUERY_PATTERN.matchEntire(normalized)

        if (notificationMatch != null) {
            val latest =
                notificationMatch.groupValues[1]
                    .isNotBlank()

            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning =
                    if (latest) {
                        "query latest notification"
                    } else {
                        "query notifications"
                    },
                target =
                    if (latest) {
                        "latest notification"
                    } else {
                        "notifications"
                    },
                predicate = "query",
            )
        }

        val tellMeAboutMatch =
            TELL_ME_ABOUT_PATTERN.matchEntire(normalized)

        if (tellMeAboutMatch != null) {
            val target =
                cleanCapturedValue(
                    tellMeAboutMatch.groupValues[1],
                )

            if (target.isNotEmpty()) {
                return UnderstandingSemantics.create(
                    intent = UnderstandingIntent.INFORMATION_QUERY,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "query information",
                    target = target,
                    predicate = "query",
                )
            }
        }

        val generalQuestionMatch =
            GENERAL_INFORMATION_QUERY_PATTERN
                .matchEntire(normalized)

        if (generalQuestionMatch != null) {
            val target =
                cleanCapturedValue(
                    generalQuestionMatch.groupValues[1],
                )

            if (target.isNotEmpty()) {
                return UnderstandingSemantics.create(
                    intent = UnderstandingIntent.INFORMATION_QUERY,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "query information",
                    target = target,
                    predicate = "query",
                )
            }
        }

        return null
    }

    private fun actionRequestSemantics(
        normalized: String,
    ): UnderstandingSemantics? {
        if (DECREASE_VOLUME_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "decrease volume",
                target = "volume",
                predicate = "decrease",
            )
        }

        if (INCREASE_VOLUME_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "increase volume",
                target = "volume",
                predicate = "increase",
            )
        }

        val setVolumeMatch =
            SET_VOLUME_PATTERN.matchEntire(normalized)

        if (setVolumeMatch != null) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "set volume",
                target = "volume",
                predicate = "set",
                arguments =
                    listOf(
                        UnderstandingSemanticArgument.create(
                            name = "value",
                            value = setVolumeMatch.groupValues[1],
                        ),
                        UnderstandingSemanticArgument.create(
                            name = "unit",
                            value = "percent",
                        ),
                    ),
            )
        }

        alarmTimeExpression(normalized)?.let { timeExpression ->
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "set alarm",
                target = "alarm",
                predicate = "set",
                arguments =
                    listOf(
                        UnderstandingSemanticArgument.create(
                            name = "time_expression",
                            value = timeExpression,
                        ),
                    ),
            )
        }

        val replyMatch =
            REPLY_PATTERN.matchEntire(normalized)

        if (replyMatch != null) {
            val recipientReference =
                cleanCapturedValue(
                    replyMatch.groupValues[1],
                )
            val messageContent =
                cleanCapturedValue(
                    replyMatch.groupValues[2],
                )

            if (
                recipientReference.isNotEmpty() &&
                messageContent.isNotEmpty()
            ) {
                return UnderstandingSemantics.create(
                    intent = UnderstandingIntent.ACTION_REQUEST,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "reply message",
                    target = "message",
                    predicate = "reply",
                    arguments =
                        listOf(
                            UnderstandingSemanticArgument.create(
                                name = "recipient_reference",
                                value = recipientReference,
                            ),
                            UnderstandingSemanticArgument.create(
                                name = "content",
                                value = messageContent,
                            ),
                        ),
                )
            }
        }

        val sendMessageMatch =
            SEND_MESSAGE_PATTERN.matchEntire(normalized)

        if (sendMessageMatch != null) {
            val recipientReference =
                cleanCapturedValue(
                    sendMessageMatch.groupValues[1],
                )

            val messageContent =
                sendMessageMatch.groupValues[2]
                    .trim()
                    .takeIf { it.isNotEmpty() }

            if (recipientReference.isNotEmpty()) {
                val arguments =
                    buildList {
                        add(
                            UnderstandingSemanticArgument.create(
                                name = "recipient_reference",
                                value = recipientReference,
                            ),
                        )

                        messageContent?.let { contentValue ->
                            add(
                                UnderstandingSemanticArgument.create(
                                    name = "content",
                                    value = contentValue,
                                ),
                            )
                        }
                    }

                return UnderstandingSemantics.create(
                    intent = UnderstandingIntent.ACTION_REQUEST,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "send message",
                    target = "message",
                    predicate = "send",
                    arguments = arguments,
                )
            }
        }

        val callMatch =
            CALL_PATTERN.matchEntire(normalized)

        if (callMatch != null) {
            val recipientReference =
                cleanCapturedValue(
                    callMatch.groupValues[1],
                )

            if (recipientReference.isNotEmpty()) {
                return UnderstandingSemantics.create(
                    intent = UnderstandingIntent.ACTION_REQUEST,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "call contact",
                    target = "contact",
                    predicate = "call",
                    arguments =
                        listOf(
                            UnderstandingSemanticArgument.create(
                                name = "recipient_reference",
                                value = recipientReference,
                            ),
                        ),
                )
            }
        }

        val playMatch =
            PLAY_PATTERN.matchEntire(normalized)

        if (playMatch != null) {
            val objectReference =
                cleanCapturedValue(
                    playMatch.groupValues[1],
                )

            if (objectReference.isNotEmpty()) {
                return UnderstandingSemantics.create(
                    intent = UnderstandingIntent.ACTION_REQUEST,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "play media",
                    target = "media",
                    predicate = "play",
                    arguments =
                        listOf(
                            UnderstandingSemanticArgument.create(
                                name = "object_reference",
                                value = objectReference,
                            ),
                        ),
                )
            }
        }

        if (PAUSE_MEDIA_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "pause media",
                target = "media",
                predicate = "pause",
            )
        }

        return null
    }

    private fun informationalSemantics(
        normalized: String,
    ): UnderstandingSemantics? {
        if (!INFORMATIONAL_PATTERN.matches(normalized)) {
            return null
        }

        return UnderstandingSemantics.create(
            intent = UnderstandingIntent.INFORMATIONAL,
            actionability =
                UnderstandingActionability.NON_ACTIONABLE,
            meaning = "informational statement",
        )
    }

    private fun alarmTimeExpression(
        normalized: String,
    ): String? {
        val prefixMatch =
            ALARM_PREFIX_PATTERN.matchEntire(normalized)

        if (prefixMatch != null) {
            return cleanCapturedValue(
                prefixMatch.groupValues[1],
            ).takeIf { it.isNotEmpty() }
        }

        val suffixMatch =
            ALARM_SUFFIX_PATTERN.matchEntire(normalized)

        if (suffixMatch != null) {
            return cleanCapturedValue(
                suffixMatch.groupValues[1],
            ).takeIf { it.isNotEmpty() }
        }

        return null
    }

    private fun normalize(
        content: String,
    ): String {
        return content
            .trim()
            .trimEnd('.', '!', '?', '।')
            .trim()
            .replace(
                MULTIPLE_WHITESPACE_PATTERN,
                " ",
            )
    }

    private fun cleanCapturedValue(
        value: String,
    ): String {
        return value
            .trim()
            .trimEnd('.', '!', '?', '।')
            .trim()
    }

    private fun isDevanagariText(
        content: String,
    ): Boolean {
        var sawDevanagariLetter = false
        var offset = 0

        while (offset < content.length) {
            val codePoint = content.codePointAt(offset)
            offset += Character.charCount(codePoint)

            if (!Character.isLetter(codePoint)) {
                continue
            }

            if (
                Character.UnicodeScript.of(codePoint) !=
                Character.UnicodeScript.DEVANAGARI
            ) {
                return false
            }

            sawDevanagariLetter = true
        }

        return sawDevanagariLetter
    }

    private companion object {
        val OPEN_TARGET_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:मेरी\s+)?(.+?)\s+(?:खोलो|खोलिए)$""",
            )

        val BATTERY_QUERY_PATTERN =
            Regex(
                """^(?:मेरी\s+)?बैटरी(?:\s+(?:लेवल|प्रतिशत|परसेंट))?\s+(?:कितनी|कितना)\s+(?:है|हैं)$""",
            )

        val NOTIFICATION_QUERY_PATTERN =
            Regex(
                """^(?:(?:मेरे|मेरा)\s+)?(?:(लेटेस्ट|नवीनतम|आखिरी)\s+)?नोटिफिकेशन(?:्स)?\s+(?:दिखाओ|पढ़ो|पढ़िए|सुनाओ)$""",
            )

        val TELL_ME_ABOUT_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(.+?)\s+के\s+बारे\s+में\s+बताओ$""",
            )

        val GENERAL_INFORMATION_QUERY_PATTERN =
            Regex(
                """^(.+?)\s+(?:कौन|क्या)\s+(?:है|हैं)$""",
            )

        val DECREASE_VOLUME_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:आवाज़|वॉल्यूम)\s+(?:कम\s+करो|कम\s+कीजिए|घटाओ|घटाइए)$""",
            )

        val INCREASE_VOLUME_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:आवाज़|वॉल्यूम)\s+(?:बढ़ाओ|बढ़ाइए|तेज़\s+करो|तेज\s+करो)$""",
            )

        val SET_VOLUME_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:आवाज़|वॉल्यूम)\s+(\d{1,3})\s*(?:प्रतिशत|परसेंट|%)\s+(?:करो|कर\s+दो|सेट\s+करो)$""",
            )

        val ALARM_PREFIX_PATTERN =
            Regex(
                """^अलार्म\s+(.+?)\s+के\s+लिए\s+(?:लगाओ|सेट\s+करो)$""",
            )

        val ALARM_SUFFIX_PATTERN =
            Regex(
                """^(.+?)\s+(?:का|के\s+लिए)\s+अलार्म\s+(?:लगाओ|सेट\s+करो)$""",
            )

        val REPLY_PATTERN =
            Regex(
                """^(.+?)\s+को\s+जवाब\s+दो\s*:\s*(.+)$""",
            )

        val SEND_MESSAGE_PATTERN =
            Regex(
                """^(.+?)\s+को\s+(?:मैसेज|संदेश)\s+भेजो(?::\s*(.+))?$""",
            )

        val CALL_PATTERN =
            Regex(
                """^(.+?)\s+को\s+कॉल\s+करो$""",
            )

        val PLAY_PATTERN =
            Regex(
                """^(.+?)\s+(?:चलाओ|बजाओ)$""",
            )

        val PAUSE_MEDIA_PATTERN =
            Regex(
                """^(?:गाना|संगीत|म्यूजिक|मीडिया)\s+(?:रोक\s+दो|रोकिए|पॉज़\s+करो)$""",
            )

        val INFORMATIONAL_PATTERN =
            Regex(
                """^मैंने\s+.+\s+(?:देखा|खोला|इस्तेमाल\s+किया)$""",
            )

        val MULTIPLE_WHITESPACE_PATTERN =
            Regex("""\s+""")
    }
}
