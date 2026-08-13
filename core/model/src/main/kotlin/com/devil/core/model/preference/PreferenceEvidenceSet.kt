package com.devil.core.model.preference

/**
 * A bounded collection of independent evidence concerning one preference key.
 *
 * Every entry must refer to the same normalized preference key, and every entry
 * must originate from a distinct constitutional trace. This prevents one event
 * from being counted repeatedly as independent support.
 *
 * Values may disagree. Conflicting values are preserved rather than discarded
 * because disagreement is constitutionally relevant evidence.
 *
 * This set performs no preference learning and creates no Memory.
 */
@ConsistentCopyVisibility
data class PreferenceEvidenceSet private constructor(
    val key: String,
    val evidence: List<PreferenceEvidence>,
) {
    companion object {
        fun create(
            evidence: List<PreferenceEvidence>,
        ): PreferenceEvidenceSet {
            require(evidence.isNotEmpty()) {
                "Preference evidence set requires at least one evidence item."
            }

            val key = evidence.first().key

            require(
                evidence.all {
                    it.key == key
                },
            ) {
                "Preference evidence set may contain evidence for only one preference key."
            }

            require(
                evidence.map {
                    it.traceId
                }.distinct().size == evidence.size,
            ) {
                "Preference evidence set requires distinct trace identities for independent evidence."
            }

            return PreferenceEvidenceSet(
                key = key,
                evidence = evidence.toList(),
            )
        }
    }
}
