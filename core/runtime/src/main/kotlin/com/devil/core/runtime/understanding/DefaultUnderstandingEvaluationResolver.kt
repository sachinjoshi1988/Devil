package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import java.util.Locale

/**
 * Default bounded structured-understanding evaluation resolver.
 *
 * Stage 56 begins a deliberately small semantic policy.
 *
 * The resolver recognizes only meanings explicitly represented by the current
 * deterministic policy. Unrecognized text remains UNSUPPORTED rather than
 * being guessed, fabricated, or converted into an action.
 *
 * Understanding remains separate from constitutional Decision. An actionable
 * semantic result therefore does not authorize work or cause execution.
 *
 * This resolver performs no conversation intake, memory creation, decision
 * selection, task creation, planning, capability authorization, execution,
 * observation, verification, or Outcome production.
 */
class DefaultUnderstandingEvaluationResolver :
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
            return UnderstandingRecord.create(
                context = input.context,
                state = UnderstandingState.COMPLETE,
                summary = "User expressed a greeting.",
                semantics = semantics,
            )
        }

        openTargetSemantics(content)?.let { semantics ->
            return UnderstandingRecord.create(
                context = input.context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "User requested opening the target: ${semantics.target}.",
                semantics = semantics,
            )
        }

        informationalSemantics(normalized)?.let { semantics ->
            return UnderstandingRecord.create(
                context = input.context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "User provided a non-actionable informational statement.",
                semantics = semantics,
            )
        }

        return UnderstandingRecord.create(
            context = input.context,
            state = UnderstandingState.UNSUPPORTED,
            summary =
                "No bounded language-understanding policy matched the supplied input.",
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

    private fun openTargetSemantics(
        content: String,
    ): UnderstandingSemantics? {
        val match =
            OPEN_TARGET_PATTERN.matchEntire(
                content.trim(),
            ) ?: return null

        val target =
            match.groupValues[1]
                .trim()
                .trimEnd('.', '!', '?')
                .trim()

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

    private companion object {

        val OPEN_TARGET_PATTERN =
            Regex(
                pattern =
                    """(?i)^(?:please\s+)?open\s+(.+?)(?:\s+please)?[.!?]?$""",
            )

        val INFORMATIONAL_PATTERN =
            Regex(
                pattern =
                    """^i\s+(watched|used|opened|visited)\s+.+$""",
            )

        val MULTIPLE_WHITESPACE_PATTERN =
            Regex("""\s+""")
    }
}
