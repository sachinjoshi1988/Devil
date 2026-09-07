package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemanticArgument
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import java.util.Locale

/**
 * Stage 337C/337F/337G bounded deterministic English, Hindi, and Marathi assistant-language understanding resolver.
 *
 * This resolver establishes bounded structured English, Hindi, and Marathi meaning only.
 *
 * It does not authenticate or authorize an owner, select a constitutional
 * decision, create a task or plan, select or execute a capability, establish
 * Observation / Verification / Outcome, resolve ambiguous references by
 * guessing, invoke a model, or create persistent Memory.
 *
 * English, bounded Hindi, and bounded Marathi understanding remain provider-independent and available without
 * paid cloud-model access.
 *
 * LANGUAGE_INTERPRETED != UNDERSTANDING_AUTHORITY.
 * INTENT_RECOGNIZED != DECISION_SELECTED.
 * INTENT_RECOGNIZED != CAPABILITY_SELECTED.
 * ACTIONABLE != AUTHORIZED.
 * ACTIONABLE != EXECUTABLE.
 * UNRESOLVED_REFERENCE != GUESSED_REFERENCE.
 * SEMANTIC_CANDIDATE != VERIFIED_USER_INTENT.
 * MODEL != UNDERSTANDING_AUTHORITY.
 */
class DefaultUnderstandingEvaluationResolver(
    private val languageEvidenceResolver: UnderstandingLanguageEvidenceResolver =
        DefaultUnderstandingLanguageEvidenceResolver(),
    private val hindiUnderstandingSemanticsResolver: HindiUnderstandingSemanticsResolver =
        DefaultHindiUnderstandingSemanticsResolver(),
    private val marathiUnderstandingSemanticsResolver: MarathiUnderstandingSemanticsResolver =
        DefaultMarathiUnderstandingSemanticsResolver(),
) :
    UnderstandingEvaluationResolver {

    override fun evaluate(
        request: UnderstandingEvaluationRequest,
    ): UnderstandingRecord {
        val input =
            request.conversationIntake
                .record
                .input

        val content = input.content
        val normalized = normalizeForMatching(content)

        greetingSemantics(normalized)?.let { semantics ->
            return complete(
                request = request,
                summary = "User expressed a greeting.",
                semantics = semantics,
                boundedLanguageTag = "en",
            )
        }

        openTargetSemantics(content)?.let { semantics ->
            return complete(
                request = request,
                summary =
                    "User requested opening the target: ${semantics.target}.",
                semantics = semantics,
                boundedLanguageTag = "en",
            )
        }

        informationQuerySemantics(content, normalized)?.let { semantics ->
            return complete(
                request = request,
                summary =
                    "User expressed an information query about: ${semantics.target}.",
                semantics = semantics,
                boundedLanguageTag = "en",
            )
        }

        actionRequestSemantics(content, normalized)?.let { semantics ->
            return complete(
                request = request,
                summary =
                    "User expressed an action request: ${semantics.meaning}.",
                semantics = semantics,
                boundedLanguageTag = "en",
            )
        }

        informationalSemantics(normalized)?.let { semantics ->
            return complete(
                request = request,
                summary =
                    "User provided a non-actionable informational statement.",
                semantics = semantics,
                boundedLanguageTag = "en",
            )
        }


        hindiUnderstandingSemanticsResolver
            .resolve(content)
            ?.let { semantics ->
                val summary =
                    when (semantics.intent) {
                        UnderstandingIntent.GREETING ->
                            "User expressed a greeting."

                        UnderstandingIntent.OPEN_TARGET ->
                            "User requested opening the target: ${semantics.target}."

                        UnderstandingIntent.INFORMATION_QUERY ->
                            "User expressed an information query about: ${semantics.target}."

                        UnderstandingIntent.ACTION_REQUEST ->
                            "User expressed an action request: ${semantics.meaning}."

                        UnderstandingIntent.INFORMATIONAL ->
                            "User provided a non-actionable informational statement."
                    }

                return complete(
                    request = request,
                    summary = summary,
                    semantics = semantics,
                    boundedLanguageTag = "hi",
                )
            }


        marathiUnderstandingSemanticsResolver
            .resolve(content)
            ?.let { semantics ->
                val summary =
                    when (semantics.intent) {
                        UnderstandingIntent.GREETING ->
                            "User expressed a greeting."

                        UnderstandingIntent.OPEN_TARGET ->
                            "User requested opening the target: ${semantics.target}."

                        UnderstandingIntent.INFORMATION_QUERY ->
                            "User expressed an information query about: ${semantics.target}."

                        UnderstandingIntent.ACTION_REQUEST ->
                            "User expressed an action request: ${semantics.meaning}."

                        UnderstandingIntent.INFORMATIONAL ->
                            "User provided a non-actionable informational statement."
                    }

                return complete(
                    request = request,
                    summary = summary,
                    semantics = semantics,
                    boundedLanguageTag = "mr",
                )
            }

        return UnderstandingRecord.create(
            context = input.context,
            state = UnderstandingState.UNSUPPORTED,
            summary =
                "No bounded language-understanding policy matched the supplied input.",
            languageEvidence =
                languageEvidenceResolver.resolve(
                    content = content,
                ),
        )
    }

    private fun complete(
        request: UnderstandingEvaluationRequest,
        summary: String,
        semantics: UnderstandingSemantics,
        boundedLanguageTag: String,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context =
                request.conversationIntake
                    .record
                    .input
                    .context,
            state = UnderstandingState.COMPLETE,
            summary = summary,
            semantics = semantics,
            languageEvidence =
                languageEvidenceResolver.resolve(
                    content =
                        request.conversationIntake
                            .record
                            .input
                            .content,
                    boundedLanguageTag = boundedLanguageTag,
                ),
        )
    }

    private fun greetingSemantics(
        normalized: String,
    ): UnderstandingSemantics? {
        val greetingMatched =
            normalized in
                setOf(
                    "hello",
                    "hello devil",
                    "hi",
                    "hi devil",
                    "hey",
                    "hey devil",
                )

        if (!greetingMatched) {
            return null
        }

        return UnderstandingSemantics.create(
            intent = UnderstandingIntent.GREETING,
            actionability =
                UnderstandingActionability.NON_ACTIONABLE,
            meaning = "greeting",
        )
    }

    /**
     * OPEN_TARGET remains a compatibility intent for the already-established
     * open-target path. Natural polite English wrappers may be interpreted,
     * but no additional execution authority is created here.
     */
    private fun openTargetSemantics(
        content: String,
    ): UnderstandingSemantics? {
        val match =
            OPEN_TARGET_PATTERN.matchEntire(
                content.trim(),
            ) ?: return null

        val target =
            cleanCapturedValue(
                match.groupValues[1],
            )

        if (target.isEmpty()) {
            return null
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
        content: String,
        normalized: String,
    ): UnderstandingSemantics? {
        if (DEVICE_MODEL_QUERY_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "query device model",
                target = "device model",
                predicate = "query",
            )
        }

        if (ANDROID_VERSION_QUERY_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "query android version",
                target = "android version",
                predicate = "query",
            )
        }

        if (DEVICE_SUMMARY_QUERY_PATTERN.matches(normalized)) {
            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "query device summary",
                target = "device summary",
                predicate = "query",
            )
        }

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
            TELL_ME_ABOUT_PATTERN.matchEntire(
                content.trim(),
            )

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
            GENERAL_INFORMATION_QUERY_PATTERN.matchEntire(
                content.trim(),
            )

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
        content: String,
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
            val value =
                setVolumeMatch.groupValues[1]

            val unit =
                setVolumeMatch.groupValues[2]
                    .takeIf { it.isNotBlank() }
                    ?.let { capturedUnit ->
                        if (capturedUnit == "%") {
                            "percent"
                        } else {
                            capturedUnit
                        }
                    }

            val arguments =
                buildList {
                    add(
                        UnderstandingSemanticArgument.create(
                            name = "value",
                            value = value,
                        ),
                    )

                    unit?.let { normalizedUnit ->
                        add(
                            UnderstandingSemanticArgument.create(
                                name = "unit",
                                value = normalizedUnit,
                            ),
                        )
                    }
                }

            return UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "set volume",
                target = "volume",
                predicate = "set",
                arguments = arguments,
            )
        }

        val alarmMatch =
            SET_ALARM_PATTERN.matchEntire(
                content.trim(),
            )

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
            REPLY_PATTERN.matchEntire(
                content.trim(),
            )

        if (replyMatch != null) {
            val recipientReference =
                replyMatch.groupValues[1]
                    .trim()

            val messageContent =
                replyMatch.groupValues[2]
                    .trim()

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
            SEND_MESSAGE_PATTERN.matchEntire(
                content.trim(),
            )

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
            CALL_PATTERN.matchEntire(
                content.trim(),
            )

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
            PLAY_PATTERN.matchEntire(
                content.trim(),
            )

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

    private fun normalizeForMatching(
        content: String,
    ): String {
        return content
            .trim()
            .lowercase(Locale.ROOT)
            .trimEnd('.', '!', '?')
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
            .trimEnd('.', '!', '?')
            .trim()
    }

    private companion object {

        val OPEN_TARGET_PATTERN =
            Regex(
                pattern =
                    """(?i)^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?open\s+(?:my\s+)?(.+?)(?:\s+please)?[.!?]?$""",
            )

        val DEVICE_MODEL_QUERY_PATTERN =
            Regex(
                """^(?:what(?:['’]s|\s+is)\s+my\s+device\s+model|what\s+phone\s+is\s+this)$""",
            )

        val ANDROID_VERSION_QUERY_PATTERN =
            Regex(
                """^what\s+android\s+version\s+am\s+i\s+using$""",
            )

        val DEVICE_SUMMARY_QUERY_PATTERN =
            Regex(
                """^tell\s+me\s+about\s+this\s+device$""",
            )

        val BATTERY_QUERY_PATTERN =
            Regex(
                """^(?:what(?:['’]s|\s+is))\s+(?:my\s+|the\s+)?battery(?:\s+(?:level|percentage|percent|status))?$""",
            )

        val NOTIFICATION_QUERY_PATTERN =
            Regex(
                """^(?:show|read)\s+(?:me\s+)?(?:my\s+|the\s+)?(latest\s+)?notifications?$""",
            )

        val TELL_ME_ABOUT_PATTERN =
            Regex(
                """(?i)^(?:please\s+)?tell\s+me\s+about\s+(.+?)[.!?]?$""",
            )

        val GENERAL_INFORMATION_QUERY_PATTERN =
            Regex(
                """(?i)^(?:what(?:['’]s|\s+is)|who\s+is|where\s+is|when\s+is)\s+(.+?)[?!.]?$""",
            )

        val DECREASE_VOLUME_PATTERN =
            Regex(
                """^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?(?:lower|decrease|reduce|turn\s+down)\s+(?:the\s+|my\s+)?volume$""",
            )

        val INCREASE_VOLUME_PATTERN =
            Regex(
                """^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?(?:raise|increase|turn\s+up)\s+(?:the\s+|my\s+)?volume$""",
            )

        val SET_VOLUME_PATTERN =
            Regex(
                """^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?set\s+(?:the\s+|my\s+)?volume\s+to\s+(\d{1,3})(?:\s*(percent|%))?$""",
            )

        val SET_ALARM_PATTERN =
            Regex(
                """(?i)^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?set\s+(?:an?\s+|the\s+)?alarm\s+for\s+(.+?)[.!?]?$""",
            )

        val REPLY_PATTERN =
            Regex(
                """(?i)^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?reply\s+to\s+(.+?)\s*:\s*(.+)$""",
            )

        val SEND_MESSAGE_PATTERN =
            Regex(
                """(?i)^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?send\s+(?:a\s+)?message\s+to\s+(.+?)(?::\s*(.+))?[.!?]?$""",
            )

        val CALL_PATTERN =
            Regex(
                """(?i)^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?call\s+(.+?)[.!?]?$""",
            )

        val PLAY_PATTERN =
            Regex(
                """(?i)^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?play\s+(.+?)[.!?]?$""",
            )

        val PAUSE_MEDIA_PATTERN =
            Regex(
                """^(?:(?:please\s+)|(?:(?:can|could|would)\s+you\s+(?:please\s+)?))?pause(?:\s+(?:the\s+)?(?:music|media|song|audio))?$""",
            )

        val INFORMATIONAL_PATTERN =
            Regex(
                """^i\s+(watched|used|opened|visited)\s+.+$""",
            )

        val MULTIPLE_WHITESPACE_PATTERN =
            Regex("""\s+""")
    }
}
