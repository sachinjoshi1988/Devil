package com.devil.app.verification

import com.devil.app.observation.AndroidObservationEvidence
import com.devil.app.observation.AndroidObservationResult
import com.devil.app.observation.AndroidObservationStatus
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.observation.ObservationStatus
import com.devil.core.runtime.verification.VerificationEvidencePort
import com.devil.core.runtime.verification.VerificationEvidenceResult
import com.devil.core.runtime.verification.VerificationEvidenceStatus

/**
 * Android implementation of the neutral constitutional VerificationEvidencePort.
 *
 * This boundary is reached only after the single Unified Devil Runtime has
 * established one genuine constitutional ObservationResult.
 *
 * OBSERVED is necessary but remains insufficient for VERIFIED.
 *
 * For one genuine OBSERVED result this port approaches the existing bounded
 * AndroidVerificationAdapter using only the preserved trace and observed
 * capability identity.
 *
 * Android verification evidence becomes neutral constitutional verification
 * evidence only when trace identity and capability identity remain consistent.
 *
 * This port grants no authority, establishes no final Outcome, updates no World
 * Model state, performs no Learning, commits no Memory, and creates no alternate
 * runtime, Brain, Executive, Planner, Security Authority, Verification Authority,
 * or Outcome Authority.
 *
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class DefaultAndroidVerificationEvidencePort(
    private val verificationAdapter: AndroidVerificationAdapter,
) : VerificationEvidencePort {

    override fun verify(
        observation: ObservationResult,
    ): VerificationEvidenceResult {
        return when (observation.status) {
            ObservationStatus.DEFERRED ->
                VerificationEvidenceResult.create(
                    traceId = observation.traceId,
                    status = VerificationEvidenceStatus.DEFERRED,
                )

            ObservationStatus.FAILED ->
                VerificationEvidenceResult.create(
                    traceId = observation.traceId,
                    status = VerificationEvidenceStatus.FAILED,
                    error = requireNotNull(observation.error),
                )

            ObservationStatus.OBSERVED -> {
                val request =
                    requireNotNull(observation.request)

                val capabilityId =
                    request.execution.capability.capabilityId

                val androidObservation =
                    AndroidObservationResult.create(
                        traceId = observation.traceId,
                        status = AndroidObservationStatus.OBSERVED,
                        evidence =
                            AndroidObservationEvidence.create(
                                capabilityId = capabilityId,
                                description =
                                    "Constitutionally established bounded observation evidence.",
                            ),
                    )

                val androidVerification =
                    verificationAdapter.verify(
                        observation = androidObservation,
                    )

                require(
                    androidVerification.traceId == observation.traceId,
                ) {
                    "Android verification and constitutional observation must use the same trace identity."
                }

                require(
                    androidVerification.evidence == null ||
                        androidVerification.evidence.capabilityId ==
                        capabilityId,
                ) {
                    "Android verification evidence and constitutional observation must refer to the same capability identity."
                }

                when (androidVerification.status) {
                    AndroidVerificationStatus.VERIFIED -> {
                        val evidence =
                            requireNotNull(
                                androidVerification.evidence,
                            )

                        VerificationEvidenceResult.create(
                            traceId = observation.traceId,
                            status =
                                VerificationEvidenceStatus.VERIFIED,
                            capabilityId =
                                evidence.capabilityId,
                            description =
                                evidence.description,
                        )
                    }

                    AndroidVerificationStatus.DEFERRED ->
                        VerificationEvidenceResult.create(
                            traceId = observation.traceId,
                            status =
                                VerificationEvidenceStatus.DEFERRED,
                        )

                    AndroidVerificationStatus.FAILED ->
                        VerificationEvidenceResult.create(
                            traceId = observation.traceId,
                            status =
                                VerificationEvidenceStatus.FAILED,
                            error =
                                requireNotNull(
                                    androidVerification.error,
                                ),
                        )
                }
            }
        }
    }
}
