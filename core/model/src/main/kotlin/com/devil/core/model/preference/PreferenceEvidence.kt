package com.devil.core.model.preference

import com.devil.core.model.common.TraceId

/**
 * One bounded piece of evidence concerning a possible stable user preference.
 *
 * The evidence identifies what preference key was observed, the candidate value
 * observed for that key, and the constitutional trace from which that evidence
 * originated.
 *
 * This object does not establish a preference, create Learning, create a Memory
 * Proposal, invoke Memory Authority, commit Memory, persist Memory, mutate world
 * state, grant authorization, or perform an action.
 *
 * One PreferenceEvidence instance is never sufficient by itself to establish a
 * learned preference.
 */
@ConsistentCopyVisibility
data class PreferenceEvidence private constructor(
    val traceId: TraceId,
    val key: String,
    val value: String,
) {
    companion object {
        fun create(
            traceId: TraceId,
            key: String,
            value: String,
        ): PreferenceEvidence {
            val normalizedKey = key.trim()
            val normalizedValue = value.trim()

            require(normalizedKey.isNotEmpty()) {
                "Preference evidence requires a nonblank preference key."
            }

            require(normalizedValue.isNotEmpty()) {
                "Preference evidence requires a nonblank candidate value."
            }

            return PreferenceEvidence(
                traceId = traceId,
                key = normalizedKey,
                value = normalizedValue,
            )
        }
    }
}
