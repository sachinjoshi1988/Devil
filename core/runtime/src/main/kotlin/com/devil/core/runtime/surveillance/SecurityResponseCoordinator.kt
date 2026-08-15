package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecurityResponseAction
import com.devil.core.model.surveillance.SecurityResponseRecord
import com.devil.core.model.surveillance.SecuritySurveillanceRecord

/**
 * Stage 91 bounded Security Response Foundation coordinator.
 *
 * This coordinator prepares one platform-neutral Security Response record from:
 *
 * - constitutional TraceId;
 * - one existing Stage 90 SecuritySurveillanceRecord;
 * - one explicitly supplied response action;
 * - and one explicitly supplied rationale.
 *
 * The coordinator does not determine which response Devil should choose.
 *
 * In particular, it does not transform:
 *
 * surveillance signal
 * -> threat determination
 * -> authorization
 * -> execution.
 *
 * Those are separate constitutional boundaries.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Unified Devil Runtime;
 * - create Security Response-specific Memory or Security authorities;
 * - inspect camera pixels;
 * - perform face detection;
 * - perform face recognition;
 * - create biometric templates;
 * - compare face embeddings;
 * - query criminal databases;
 * - query watchlists;
 * - reinterpret a Stage 90 watchlist candidate claim as verified identity;
 * - determine that a person is a criminal;
 * - determine guilt;
 * - determine threat status;
 * - determine intrusion status;
 * - determine emergency status;
 * - authenticate anyone;
 * - establish trust;
 * - grant authorization;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register, select, authorize, or activate capabilities;
 * - establish capability availability, health, or readiness;
 * - invoke UnifiedDevilRuntime;
 * - create ExecutionRequests;
 * - execute actions;
 * - send notifications;
 * - trigger alarms;
 * - operate locks;
 * - contact emergency services;
 * - call Android APIs;
 * - call CCTV APIs;
 * - call network APIs;
 * - implement RTSP;
 * - implement ONVIF;
 * - communicate with biometric or watchlist APIs;
 * - establish constitutional Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - propose, commit, or persist Memory;
 * - or persist Security Response state.
 *
 * SURVEILLANCE_RECORD != THREAT_DECISION.
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 * THREAT_INFORMATION != AUTHORIZATION.
 * SECURITY_RESPONSE_PREPARED != AUTHORIZED.
 * SECURITY_RESPONSE_PREPARED != EXECUTION_REQUEST.
 * SECURITY_RESPONSE_PREPARED != EXECUTED.
 * RESPONSE != EXECUTION_AUTHORITY.
 */
class SecurityResponseCoordinator {

    fun prepare(
        traceId: TraceId,
        surveillance: SecuritySurveillanceRecord,
        action: String,
        rationale: String,
    ): SecurityResponsePreparationResult {
        if (
            action.isBlank() ||
            rationale.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val response =
            SecurityResponseRecord.create(
                surveillance = surveillance,
                action =
                    SecurityResponseAction.from(
                        action,
                    ),
                rationale = rationale,
            )

        return SecurityResponsePreparationResult.create(
            traceId = traceId,
            status =
                SecurityResponsePreparationStatus.PREPARED,
            record = response,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): SecurityResponsePreparationResult {
        return SecurityResponsePreparationResult.create(
            traceId = traceId,
            status =
                SecurityResponsePreparationStatus.DEFERRED,
        )
    }
}
