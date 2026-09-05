package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingSemanticArgument
import com.devil.core.model.understanding.UnderstandingSemantics

/**
 * Stage 337G provider-neutral bounded Marathi semantic-understanding boundary.
 *
 * Implementations may establish only semantics supported by their explicit
 * Marathi policy. They do not authorize, execute, translate, transliterate,
 * resolve contacts/references, or establish verified language truth.
 *
 * DEVANAGARI != MARATHI.
 * MARATHI_POLICY_MATCH != LANGUAGE_VERIFIED.
 * SHARED_DEVANAGARI_EXPRESSION != LANGUAGE_IDENTIFIED.
 * MARATHI_UNDERSTOOD != AUTHORIZED.
 * MARATHI_UNDERSTOOD != EXECUTABLE.
 */
interface MarathiUnderstandingSemanticsResolver {
    fun resolve(
        content: String,
    ): UnderstandingSemantics?
}

/**
 * Deterministic Stage 337G Devanagari-Marathi semantics implementation.
 *
 * This implementation intentionally rejects mixed-script input and relies on
 * bounded Marathi-specific constructions rather than Devanagari script alone.
 *
 * Bare shared Devanagari expressions do not establish Marathi.
 *
 * Romanized Marathi, translation, transliteration, model inference, Android
 * APIs, capability selection, authorization, execution, and multilingual
 * voice activation are outside Stage 337G.
 */
class DefaultMarathiUnderstandingSemanticsResolver :
    MarathiUnderstandingSemanticsResolver {

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
                "कसा आहेस डेविल",
                "कशी आहेस डेविल",
                "कसे आहात डेविल",
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

        val aboutMatch =
            TELL_ME_ABOUT_PATTERN.matchEntire(normalized)

        if (aboutMatch != null) {
            val target =
                cleanCapturedValue(
                    aboutMatch.groupValues[1],
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
                            value =
                                normalizeDecimalDigits(
                                    setVolumeMatch.groupValues[1],
                                ),
                        ),
                        UnderstandingSemanticArgument.create(
                            name = "unit",
                            value = "percent",
                        ),
                    ),
            )
        }

        val alarmMatch =
            SET_ALARM_PATTERN.matchEntire(normalized)

        if (alarmMatch != null) {
            val timeExpression =
                cleanCapturedValue(
                    alarmMatch.groupValues[1],
                )

            if (timeExpression.isNotEmpty()) {
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

    private fun normalizeDecimalDigits(
        value: String,
    ): String {
        return buildString {
            value.forEach { character ->
                append(
                    when (character) {
                        '०' -> '0'
                        '१' -> '1'
                        '२' -> '2'
                        '३' -> '3'
                        '४' -> '4'
                        '५' -> '5'
                        '६' -> '6'
                        '७' -> '7'
                        '८' -> '8'
                        '९' -> '9'
                        else -> character
                    },
                )
            }
        }
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
                """^(?:कृपया\s+)?(.+?)\s+(?:उघडा|उघड)$""",
            )

        val BATTERY_QUERY_PATTERN =
            Regex(
                """^(?:माझी\s+)?बॅटरी(?:\s+(?:पातळी|टक्केवारी|टक्के))?\s+किती\s+(?:आहे|आहेत)$""",
            )

        val NOTIFICATION_QUERY_PATTERN =
            Regex(
                """^(?:माझ्या\s+)?(?:(नवीनतम|शेवटची|ताजी)\s+)?(?:सूचना|नोटिफिकेशन(?:्स)?)\s+(?:दाखवा|वाचा|सांगा)$""",
            )

        val TELL_ME_ABOUT_PATTERN =
            Regex(
                """^(.+?)\s*बद्दल\s+(?:सांगा|सांग)$""",
            )

        val GENERAL_INFORMATION_QUERY_PATTERN =
            Regex(
                """^(.+?)\s+(?:कोण|काय)\s+(?:आहे|आहेत)$""",
            )

        val DECREASE_VOLUME_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:आवाज|व्हॉल्यूम)\s+(?:कमी\s+करा|कमी\s+कर)$""",
            )

        val INCREASE_VOLUME_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:आवाज|व्हॉल्यूम)\s+(?:वाढवा|वाढव|मोठा\s+करा)$""",
            )

        val SET_VOLUME_PATTERN =
            Regex(
                """^(?:कृपया\s+)?(?:आवाज|व्हॉल्यूम)\s+([0-9०-९]{1,3})\s*(?:टक्के|%)\s+(?:करा|कर)$""",
            )

        val SET_ALARM_PATTERN =
            Regex(
                """^(.+?)\s+गजर\s+(?:लावा|सेट\s+करा)$""",
            )

        val REPLY_PATTERN =
            Regex(
                """^(.+?)ला\s+उत्तर\s+द्या\s*:\s*(.+)$""",
            )

        val SEND_MESSAGE_PATTERN =
            Regex(
                """^(.+?)ला\s+(?:संदेश|मेसेज)\s+पाठवा(?::\s*(.+))?$""",
            )

        val CALL_PATTERN =
            Regex(
                """^(.+?)ला\s+(?:फोन|कॉल)\s+करा$""",
            )

        val PLAY_PATTERN =
            Regex(
                """^(.+?)\s+(?:वाजवा|चालू\s+करा)$""",
            )

        val PAUSE_MEDIA_PATTERN =
            Regex(
                """^(?:गाणे|गाणं|संगीत|मीडिया)\s+(?:थांबवा|बंद\s+करा)$""",
            )

        val INFORMATIONAL_PATTERN =
            Regex(
                """^मी\s+.+\s+(?:पाहिले|वापरले|उघडले)$""",
            )

        val MULTIPLE_WHITESPACE_PATTERN =
            Regex("""\s+""")
    }
}
