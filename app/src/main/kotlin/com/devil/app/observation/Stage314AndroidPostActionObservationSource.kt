package com.devil.app.observation

import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
import com.devil.app.diagnostic.NoOpStage314PostActionDiagnostic
import com.devil.app.diagnostic.Stage314PostActionDiagnostic
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Stage 314 bounded Android post-action observation source.
 *
 * This source is eligible only when the genuine runtime trace and capability
 * already have a matching Stage 314 post-action expectation.
 *
 * It consumes one bounded stable accessibility snapshot captured from genuine
 * Android accessibility events after the genuine execution attempt.
 *
 * It does not re-read Android accessibility state from the runtime worker
 * thread. It also does not compare the captured metadata with the expected
 * condition. That comparison remains Verification's responsibility.
 *
 * No sleep, polling, retry, inferred success, synthetic screen state, or
 * destination-specific readiness rule is used.
 *
 * SNAPSHOT_READY != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class Stage314AndroidPostActionObservationSource(
    private val expectationStore:
        Stage314AndroidPostActionExpectationStore,
    private val observationStore:
        Stage314AndroidPostActionObservationStore,
    private val accessibilityChangeReadinessStore:
        Stage314AndroidAccessibilityChangeReadinessStore,
    private val diagnostic:
        Stage314PostActionDiagnostic =
        NoOpStage314PostActionDiagnostic,
    private val readinessTimeoutMilliseconds: Long =
        2_000L,
) : AndroidObservationSource {

    init {
        require(readinessTimeoutMilliseconds > 0L) {
            "Stage 314 post-action readiness timeout must be positive."
        }
    }

    override fun observe(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): AndroidObservationResult {
        expectationStore.current(
            traceId = traceId,
            capabilityId = capabilityId,
        ) ?: run {
            diagnostic.observation(
                traceId = traceId,
                capabilityId = capabilityId,
                event = "EXPECTATION_MISSING",
            )

            return AndroidObservationResult.create(
                traceId = traceId,
                status = AndroidObservationStatus.DEFERRED,
            )
        }

        val elements =
            try {
                accessibilityChangeReadinessStore
                    .awaitStableAccessibilitySnapshot(
                        traceId = traceId,
                        capabilityId = capabilityId,
                        timeoutMilliseconds =
                            readinessTimeoutMilliseconds,
                    )
            } finally {
                accessibilityChangeReadinessStore.clear(
                    traceId = traceId,
                    capabilityId = capabilityId,
                )
            }

        if (elements == null) {
            diagnostic.observation(
                traceId = traceId,
                capabilityId = capabilityId,
                event = "ACCESSIBILITY_SNAPSHOT_NOT_READY",
            )

            return AndroidObservationResult.create(
                traceId = traceId,
                status = AndroidObservationStatus.DEFERRED,
            )
        }

        observationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            elements = elements,
        )

        diagnostic.observation(
            traceId = traceId,
            capabilityId = capabilityId,
            event = "SCREEN_AVAILABLE_OBSERVED",
            elements = elements,
        )

        return AndroidObservationResult.create(
            traceId = traceId,
            status = AndroidObservationStatus.OBSERVED,
            evidence =
                AndroidObservationEvidence.create(
                    capabilityId = capabilityId,
                    description =
                        "Observed ${elements.size} accessibility-derived screen elements captured after the genuine Android execution attempt.",
                ),
        )
    }
}
