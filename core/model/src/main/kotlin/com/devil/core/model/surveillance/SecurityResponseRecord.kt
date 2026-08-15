package com.devil.core.model.surveillance

/**
 * Immutable Stage 91 representation of one bounded Security Response
 * preparation beneath one existing Stage 90 SecuritySurveillanceRecord.
 *
 * The exact Stage 90 surveillance record remains attached so provenance
 * remains explicit.
 *
 * Stage 91 adds only:
 *
 * - one explicitly supplied SecurityResponseAction;
 * - and one nonblank supplied rationale.
 *
 * This record deliberately contains no:
 *
 * - another Devil intelligence;
 * - another Brain;
 * - another Constitution;
 * - another Executive;
 * - another Planner;
 * - another Unified Devil Runtime;
 * - Security Response-specific Memory Authority;
 * - Security Response-specific Security Authority;
 * - verified person identity;
 * - verified watchlist identity;
 * - verified criminal status;
 * - guilt determination;
 * - authentication result;
 * - trust result;
 * - authorization result;
 * - threat determination;
 * - intrusion determination;
 * - emergency determination;
 * - constitutional Decision;
 * - Task;
 * - Plan;
 * - capability binding;
 * - capability readiness;
 * - execution approval;
 * - ExecutionRequest;
 * - notification instruction sent to a platform;
 * - alarm execution;
 * - lock execution;
 * - emergency-service communication;
 * - platform operation;
 * - network operation;
 * - constitutional Observation;
 * - Verification;
 * - Outcome;
 * - World Model mutation;
 * - constitutional Learning result;
 * - Memory commitment;
 * - or persistence authority.
 *
 * A Stage 90 watchlist candidate claim remains only a candidate claim when
 * preserved through this record.
 *
 * SECURITY_RESPONSE = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * SECURITY_RESPONSE != ANOTHER_INTELLIGENCE.
 * SURVEILLANCE_RECORD != THREAT_DECISION.
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 * SECURITY_RESPONSE_PREPARATION != AUTHORIZATION.
 * SECURITY_RESPONSE_PREPARATION != EXECUTION.
 */
@ConsistentCopyVisibility
data class SecurityResponseRecord private constructor(
    val surveillance: SecuritySurveillanceRecord,
    val action: SecurityResponseAction,
    val rationale: String,
) {
    companion object {

        fun create(
            surveillance: SecuritySurveillanceRecord,
            action: SecurityResponseAction,
            rationale: String,
        ): SecurityResponseRecord {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Security Response rationale must not be blank."
            }

            return SecurityResponseRecord(
                surveillance = surveillance,
                action = action,
                rationale = normalizedRationale,
            )
        }
    }
}
