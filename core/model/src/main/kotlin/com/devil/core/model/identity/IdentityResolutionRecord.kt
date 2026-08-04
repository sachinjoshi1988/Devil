package com.devil.core.model.identity

/**
 * Records the structured outcome of one identity-resolution process.
 *
 * A resolved record contains a selection belonging to the supplied candidate
 * set. Unresolved and ambiguous records contain no selection. This record does
 * not authenticate the subject, prove ownership, evaluate trust, grant
 * authorization, or permit an action.
 */
@ConsistentCopyVisibility
data class IdentityResolutionRecord private constructor(
    val candidateSet: IdentityResolutionCandidateSet,
    val state: IdentityResolutionState,
    val selection: IdentityResolutionSelection?,
    val rationale: String,
) {
    companion object {
        fun create(
            candidateSet: IdentityResolutionCandidateSet,
            state: IdentityResolutionState,
            selection: IdentityResolutionSelection? = null,
            rationale: String,
        ): IdentityResolutionRecord {
            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Identity resolution record rationale must not be blank."
            }

            when (state) {
                IdentityResolutionState.RESOLVED -> {
                    require(selection != null) {
                        "Resolved identity records require a selection."
                    }

                    require(
                        candidateSet.candidates.any { candidate ->
                            candidate.identityId == selection.candidate.identityId
                        },
                    ) {
                        "Resolved identity selection must belong to the candidate set."
                    }
                }

                IdentityResolutionState.UNRESOLVED,
                IdentityResolutionState.AMBIGUOUS,
                -> {
                    require(selection == null) {
                        "Unresolved and ambiguous identity records must not contain a selection."
                    }
                }
            }

            return IdentityResolutionRecord(
                candidateSet = candidateSet,
                state = state,
                selection = selection,
                rationale = normalizedRationale,
            )
        }
    }
}
