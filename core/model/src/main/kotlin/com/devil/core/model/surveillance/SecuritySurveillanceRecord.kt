package com.devil.core.model.surveillance

/**
 * Immutable Stage 90 representation of one bounded Security Surveillance record.
 *
 * The record preserves only explicitly supplied surveillance-domain information:
 *
 * - one SecuritySurveillanceSource;
 * - one SecuritySurveillanceSignal belonging to that same source;
 * - and optionally one externally supplied SecurityWatchlistMatchClaim.
 *
 * A watchlist claim is deliberately optional because ordinary surveillance
 * signals must not be forced into biometric or identity processing.
 *
 * This record deliberately contains no:
 *
 * - another Devil intelligence;
 * - another Brain;
 * - another Constitution;
 * - another Executive;
 * - another Planner;
 * - another Unified Devil Runtime;
 * - surveillance-specific Memory Authority;
 * - surveillance-specific Security Authority;
 * - capability binding;
 * - active camera;
 * - CCTV connection;
 * - network-camera connection;
 * - live stream;
 * - image bytes;
 * - video bytes;
 * - biometric template;
 * - face embedding;
 * - biometric comparison algorithm;
 * - person identity;
 * - verified watchlist identity;
 * - verified criminal status;
 * - authentication result;
 * - trust result;
 * - authorization result;
 * - threat classification;
 * - intrusion classification;
 * - emergency classification;
 * - constitutional Decision;
 * - Task;
 * - Plan;
 * - execution request;
 * - alarm instruction;
 * - lock instruction;
 * - notification instruction;
 * - emergency-service instruction;
 * - constitutional Observation;
 * - Verification;
 * - Outcome;
 * - World Model mutation;
 * - constitutional Learning result;
 * - Memory commitment;
 * - persistence authority;
 * - or Stage 91 Security Response.
 *
 * SECURITY_SURVEILLANCE = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * SECURITY_SURVEILLANCE != ANOTHER_INTELLIGENCE.
 * SURVEILLANCE_RECORD != THREAT_DECISION.
 * SURVEILLANCE_RECORD != SECURITY_RESPONSE.
 * SURVEILLANCE_RECORD != EXECUTION.
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 */
@ConsistentCopyVisibility
data class SecuritySurveillanceRecord private constructor(
    val source: SecuritySurveillanceSource,
    val signal: SecuritySurveillanceSignal,
    val watchlistMatchClaim: SecurityWatchlistMatchClaim?,
) {
    companion object {

        fun create(
            source: SecuritySurveillanceSource,
            signal: SecuritySurveillanceSignal,
            watchlistMatchClaim: SecurityWatchlistMatchClaim? = null,
        ): SecuritySurveillanceRecord {
            require(signal.source == source) {
                "Security-surveillance record source and signal source must match."
            }

            return SecuritySurveillanceRecord(
                source = source,
                signal = signal,
                watchlistMatchClaim = watchlistMatchClaim,
            )
        }
    }
}
