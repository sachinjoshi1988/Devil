package com.devil.core.model.surveillance

/**
 * Immutable Stage 90 representation of one explicitly supplied external
 * security-watchlist candidate-match claim.
 *
 * This contract deliberately preserves a claim supplied by a future authorized
 * surveillance or matching embodiment without allowing Stage 90 to perform
 * biometric identification itself.
 *
 * referenceId is an opaque external reference. It is not a Devil IdentityId and
 * must not be interpreted by this contract as a person's verified identity.
 *
 * sourceSystem identifies the external system that supplied the candidate claim.
 *
 * confidencePermille preserves the externally supplied bounded confidence value:
 *
 * 0    = 0.0%
 * 1000 = 100.0%
 *
 * Even confidencePermille == 1000 does not establish verified identity,
 * criminal status, guilt, threat status, authentication, authorization, or
 * response authority.
 *
 * Stage 90 does not:
 *
 * - capture a face;
 * - detect a face;
 * - create a biometric template;
 * - create a face embedding;
 * - compare biometric templates;
 * - perform face recognition;
 * - identify a person;
 * - query a criminal database;
 * - query a watchlist;
 * - determine that a person is a criminal;
 * - authenticate a subject;
 * - establish trust;
 * - establish authorization;
 * - classify a threat;
 * - establish constitutional Observation;
 * - establish Verification;
 * - establish Outcome;
 * - authorize Security Response;
 * - execute an alarm or defensive action;
 * - perform constitutional Learning;
 * - commit Memory;
 * - or persist biometric state.
 *
 * The future external adapter supplying this claim must itself be separately
 * governed, authorized, auditable, and privacy constrained.
 *
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != AUTHENTICATION.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 * WATCHLIST_MATCH_CLAIM != THREAT.
 * WATCHLIST_MATCH_CLAIM != AUTHORIZATION.
 * WATCHLIST_MATCH_CLAIM != SECURITY_RESPONSE.
 * WATCHLIST_MATCH_CLAIM != EXECUTION_AUTHORITY.
 */
@ConsistentCopyVisibility
data class SecurityWatchlistMatchClaim private constructor(
    val referenceId: String,
    val sourceSystem: String,
    val confidencePermille: Int,
) {
    companion object {

        fun create(
            referenceId: String,
            sourceSystem: String,
            confidencePermille: Int,
        ): SecurityWatchlistMatchClaim {
            val normalizedReferenceId =
                referenceId.trim()

            val normalizedSourceSystem =
                sourceSystem.trim()

            require(normalizedReferenceId.isNotEmpty()) {
                "Security watchlist reference identity must not be blank."
            }

            require(normalizedSourceSystem.isNotEmpty()) {
                "Security watchlist source system must not be blank."
            }

            require(confidencePermille in 0..1000) {
                "Security watchlist candidate confidence must be between 0 and 1000 permille."
            }

            return SecurityWatchlistMatchClaim(
                referenceId = normalizedReferenceId,
                sourceSystem = normalizedSourceSystem,
                confidencePermille = confidencePermille,
            )
        }
    }
}
