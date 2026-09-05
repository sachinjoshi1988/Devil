package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingLanguageEvidence
import com.devil.core.model.understanding.UnderstandingLanguageEvidenceStatus
import com.devil.core.model.understanding.UnderstandingScript

/**
 * Stage 337E provider-neutral language-evidence boundary.
 *
 * The caller may supply a bounded language tag only after some independent
 * bounded language-understanding policy established it. This resolver does not
 * infer Hindi, Marathi, English, or any other language merely from script.
 *
 * SCRIPT_RECOGNIZED != LANGUAGE_IDENTIFIED.
 * DEVANAGARI != HINDI.
 * DEVANAGARI != MARATHI.
 * LANGUAGE_IDENTIFIED != UNDERSTANDING_COMPLETE.
 */
interface UnderstandingLanguageEvidenceResolver {
    fun resolve(
        content: String,
        boundedLanguageTag: String? = null,
    ): UnderstandingLanguageEvidence
}

/**
 * Deterministic Stage 337E/337F provider-neutral language-evidence implementation.
 *
 * Digits, punctuation, symbols, and whitespace do not establish a script.
 * A nonblank language tag becomes DETECTED only when an independent bounded
 * semantic policy supplied that tag and the text contains one non-mixed observed script.
 *
 * Script classification itself never creates a language identity.
 *
 * This implementation has no Android, education, model-provider, translation,
 * transliteration, authorization, execution, or Memory dependency.
 */
class DefaultUnderstandingLanguageEvidenceResolver :
    UnderstandingLanguageEvidenceResolver {

    override fun resolve(
        content: String,
        boundedLanguageTag: String?,
    ): UnderstandingLanguageEvidence {
        val script = scriptOf(content)

        val normalizedBoundedLanguageTag =
            boundedLanguageTag
                ?.trim()
                ?.takeIf { it.isNotEmpty() }

        if (
            normalizedBoundedLanguageTag != null &&
            script != UnderstandingScript.UNKNOWN &&
            script != UnderstandingScript.MIXED
        ) {
            return UnderstandingLanguageEvidence.create(
                status = UnderstandingLanguageEvidenceStatus.DETECTED,
                script = script,
                languageTag = normalizedBoundedLanguageTag,
            )
        }

        return UnderstandingLanguageEvidence.create(
            status = UnderstandingLanguageEvidenceStatus.UNKNOWN,
            script = script,
        )
    }

    private fun scriptOf(
        content: String,
    ): UnderstandingScript {
        var sawLatin = false
        var sawDevanagari = false
        var sawOther = false

        var offset = 0

        while (offset < content.length) {
            val codePoint = content.codePointAt(offset)
            offset += Character.charCount(codePoint)

            if (!Character.isLetter(codePoint)) {
                continue
            }

            when (Character.UnicodeScript.of(codePoint)) {
                Character.UnicodeScript.LATIN -> {
                    sawLatin = true
                }

                Character.UnicodeScript.DEVANAGARI -> {
                    sawDevanagari = true
                }

                else -> {
                    sawOther = true
                }
            }
        }

        val observedScriptCount =
            listOf(
                sawLatin,
                sawDevanagari,
                sawOther,
            ).count { it }

        return when {
            observedScriptCount == 0 ->
                UnderstandingScript.UNKNOWN

            observedScriptCount > 1 ->
                UnderstandingScript.MIXED

            sawLatin ->
                UnderstandingScript.LATIN

            sawDevanagari ->
                UnderstandingScript.DEVANAGARI

            else ->
                UnderstandingScript.OTHER
        }
    }
}
