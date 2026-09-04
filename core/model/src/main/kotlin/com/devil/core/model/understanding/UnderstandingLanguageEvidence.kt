package com.devil.core.model.understanding

/**
 * Stage 337E provider-neutral script classification for Understanding evidence.
 *
 * Script evidence is deliberately weaker than language identity.
 *
 * SCRIPT_RECOGNIZED != LANGUAGE_IDENTIFIED.
 * DEVANAGARI != HINDI.
 * DEVANAGARI != MARATHI.
 */
enum class UnderstandingScript {
    LATIN,
    DEVANAGARI,
    MIXED,
    OTHER,
    UNKNOWN,
}

/**
 * Stage 337E provenance status for one bounded language-evidence claim.
 *
 * UNKNOWN means no language identity was established.
 * DECLARED means a language tag was explicitly supplied by an authoritative
 * caller; declaration is not verification.
 * DETECTED means one bounded language policy established the supplied tag;
 * detection is not verification.
 *
 * LANGUAGE_DETECTED != LANGUAGE_VERIFIED.
 * LANGUAGE_DECLARED != LANGUAGE_VERIFIED.
 */
enum class UnderstandingLanguageEvidenceStatus {
    UNKNOWN,
    DECLARED,
    DETECTED,
}

/**
 * Immutable Stage 337E language/script evidence attached to Understanding.
 *
 * This evidence does not translate, transliterate, infer intent, authenticate,
 * authorize, select capabilities, execute actions, or establish verified truth.
 *
 * UNKNOWN evidence cannot carry a language tag.
 * DECLARED and DETECTED evidence require one nonblank normalized language tag.
 */
@ConsistentCopyVisibility
data class UnderstandingLanguageEvidence private constructor(
    val status: UnderstandingLanguageEvidenceStatus,
    val script: UnderstandingScript,
    val languageTag: String?,
) {
    companion object {
        fun create(
            status: UnderstandingLanguageEvidenceStatus,
            script: UnderstandingScript,
            languageTag: String? = null,
        ): UnderstandingLanguageEvidence {
            val normalizedLanguageTag =
                languageTag
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }
                    ?.lowercase()

            when (status) {
                UnderstandingLanguageEvidenceStatus.UNKNOWN -> {
                    require(normalizedLanguageTag == null) {
                        "Unknown language evidence must not contain a language tag."
                    }
                }

                UnderstandingLanguageEvidenceStatus.DECLARED,
                UnderstandingLanguageEvidenceStatus.DETECTED,
                -> {
                    require(normalizedLanguageTag != null) {
                        "$status language evidence requires one nonblank language tag."
                    }
                }
            }

            return UnderstandingLanguageEvidence(
                status = status,
                script = script,
                languageTag = normalizedLanguageTag,
            )
        }
    }
}
