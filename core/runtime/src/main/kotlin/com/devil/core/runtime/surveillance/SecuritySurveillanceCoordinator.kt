package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecuritySurveillanceRecord
import com.devil.core.model.surveillance.SecuritySurveillanceSignal
import com.devil.core.model.surveillance.SecuritySurveillanceSource
import com.devil.core.model.surveillance.SecurityWatchlistMatchClaim

/**
 * Stage 90 bounded Security Surveillance Foundation coordinator.
 *
 * This coordinator prepares one platform-neutral surveillance record from explicitly
 * supplied structured surveillance-domain input.
 *
 * Inputs are:
 *
 * - constitutional TraceId;
 * - explicit surveillance source identity;
 * - explicit surveillance source type;
 * - explicit supplied event time;
 * - explicit supplied signal description;
 * - and optionally one complete externally supplied watchlist candidate-match claim.
 *
 * The optional candidate claim consists of:
 *
 * - opaque external reference identity;
 * - external source-system identity;
 * - externally supplied confidence permille.
 *
 * Stage 90 does not calculate that match.
 *
 * It does not infer unavailable surveillance information.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Unified Devil Runtime;
 * - create surveillance-specific Memory or Security authorities;
 * - access Android camera APIs;
 * - open cameras;
 * - capture images;
 * - connect to CCTV;
 * - connect to IP or network cameras;
 * - implement RTSP;
 * - implement ONVIF;
 * - establish a live video stream;
 * - analyze pixels;
 * - detect faces;
 * - create biometric templates;
 * - create face embeddings;
 * - compare biometric templates;
 * - perform face recognition;
 * - query criminal databases;
 * - query watchlists;
 * - identify objects;
 * - recognize faces;
 * - infer a person's identity;
 * - determine that a person is a criminal;
 * - authenticate a person;
 * - establish trust;
 * - grant authorization;
 * - infer intent;
 * - infer suspicious behavior;
 * - classify a threat;
 * - classify an intrusion;
 * - classify an emergency;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register, select, authorize, or activate capabilities;
 * - invoke UnifiedDevilRuntime;
 * - create execution requests;
 * - execute actions;
 * - trigger alarms;
 * - lock or unlock devices;
 * - contact emergency services;
 * - send security notifications;
 * - perform Stage 91 Security Response;
 * - establish constitutional Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - propose, commit, or persist Memory;
 * - persist surveillance or biometric state;
 * - or communicate with platform, network, biometric, or watchlist APIs.
 *
 * CAMERA_FRAME != IDENTITY.
 * SURVEILLANCE_SIGNAL != CONSTITUTIONAL_OBSERVATION.
 * SIGNAL != VERIFIED_REALITY.
 * SIGNAL != THREAT.
 * WATCHLIST_MATCH_CLAIM != VERIFIED_IDENTITY.
 * WATCHLIST_MATCH_CLAIM != CRIMINAL_STATUS.
 * WATCHLIST_MATCH_CLAIM != THREAT.
 * THREAT != AUTHORIZATION.
 * SURVEILLANCE != SECURITY_RESPONSE.
 * SECURITY_RESPONSE != EXECUTION_AUTHORITY.
 * SECURITY_SURVEILLANCE_DOMAIN != ANOTHER_INTELLIGENCE.
 */
class SecuritySurveillanceCoordinator {

    fun prepare(
        traceId: TraceId,
        sourceId: String,
        sourceType: String,
        occurredAtEpochMilliseconds: Long,
        description: String,
        externalWatchlistReferenceId: String? = null,
        externalWatchlistSourceSystem: String? = null,
        externalWatchlistConfidencePermille: Int? = null,
    ): SecuritySurveillancePreparationResult {
        if (
            sourceId.isBlank() ||
            sourceType.isBlank() ||
            occurredAtEpochMilliseconds < 0L ||
            description.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val suppliedWatchlistFieldCount =
            listOf(
                externalWatchlistReferenceId,
                externalWatchlistSourceSystem,
                externalWatchlistConfidencePermille,
            ).count { it != null }

        if (
            suppliedWatchlistFieldCount != 0 &&
            suppliedWatchlistFieldCount != 3
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val source =
            SecuritySurveillanceSource.create(
                sourceId = sourceId,
                sourceType = sourceType,
            )

        val signal =
            SecuritySurveillanceSignal.create(
                source = source,
                occurredAtEpochMilliseconds =
                    occurredAtEpochMilliseconds,
                description = description,
            )

        val watchlistMatchClaim =
            if (suppliedWatchlistFieldCount == 3) {
                val referenceId =
                    requireNotNull(
                        externalWatchlistReferenceId,
                    )

                val sourceSystem =
                    requireNotNull(
                        externalWatchlistSourceSystem,
                    )

                val confidencePermille =
                    requireNotNull(
                        externalWatchlistConfidencePermille,
                    )

                if (
                    referenceId.isBlank() ||
                    sourceSystem.isBlank() ||
                    confidencePermille !in 0..1000
                ) {
                    return deferred(
                        traceId = traceId,
                    )
                }

                SecurityWatchlistMatchClaim.create(
                    referenceId = referenceId,
                    sourceSystem = sourceSystem,
                    confidencePermille = confidencePermille,
                )
            } else {
                null
            }

        val record =
            SecuritySurveillanceRecord.create(
                source = source,
                signal = signal,
                watchlistMatchClaim = watchlistMatchClaim,
            )

        return SecuritySurveillancePreparationResult.create(
            traceId = traceId,
            status =
                SecuritySurveillancePreparationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): SecuritySurveillancePreparationResult {
        return SecuritySurveillancePreparationResult.create(
            traceId = traceId,
            status =
                SecuritySurveillancePreparationStatus.DEFERRED,
        )
    }
}
