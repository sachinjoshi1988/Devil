package com.devil.app.verification

import com.devil.app.accessibility.AndroidAccessibilityTarget
import com.devil.app.diagnostic.NoOpStage314PostActionDiagnostic
import com.devil.app.diagnostic.Stage314PostActionDiagnostic
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.app.observation.AndroidObservationEvidence
import com.devil.app.observation.Stage314AndroidPostActionObservationStore
import com.devil.core.model.common.TraceId

/**
 * Stage 314 bounded post-action verification source.
 *
 * Verification requires:
 *
 * - one genuine constitutional Android observation;
 * - one matching trace-bound expected post-action condition;
 * - one matching trace-bound accessibility-derived screen observation;
 * - and actual visible accessibility metadata matching that expectation.
 *
 * This source performs no Android action and no new screen inspection.
 *
 * EXPECTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class Stage314AndroidPostActionVerificationSource(
    private val expectationStore:
        Stage314AndroidPostActionExpectationStore,
    private val observationStore:
        Stage314AndroidPostActionObservationStore,
    private val diagnostic:
        Stage314PostActionDiagnostic =
        NoOpStage314PostActionDiagnostic,
) : AndroidVerificationSource {

    override fun verify(
        traceId: TraceId,
        observationEvidence: AndroidObservationEvidence,
    ): AndroidVerificationResult {
        val capabilityId =
            observationEvidence.capabilityId

        val expectation =
            expectationStore.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )
                ?: run {
                    diagnostic.verification(
                        traceId = traceId,
                        capabilityId = capabilityId,
                        event = "EXPECTATION_MISSING",
                    )

                    return deferred(
                        traceId = traceId,
                    )
                }

        val observation =
            observationStore.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )
                ?: run {
                    diagnostic.verification(
                        traceId = traceId,
                        capabilityId = capabilityId,
                        event = "OBSERVATION_MISSING",
                        expectedVisibleText =
                            expectation.expectedVisibleText,
                    )

                    return deferred(
                        traceId = traceId,
                    )
                }

        val expectedNormalizedText =
            AndroidAccessibilityTarget.normalize(
                expectation.expectedVisibleText,
            )

        val expectedConditionPresent =
            observation.elements.any { element ->
                sequenceOf(
                    element.text,
                    element.contentDescription,
                )
                    .filterNotNull()
                    .any { observedValue ->
                        AndroidAccessibilityTarget.normalize(
                            observedValue,
                        ) == expectedNormalizedText
                    }
            }

        if (!expectedConditionPresent) {
            diagnostic.verification(
                traceId = traceId,
                capabilityId = capabilityId,
                event = "EXPECTED_CONDITION_MISSING",
                expectedVisibleText =
                    expectation.expectedVisibleText,
            )

            return deferred(
                traceId = traceId,
            )
        }

        diagnostic.verification(
            traceId = traceId,
            capabilityId = capabilityId,
            event = "EXPECTED_CONDITION_VERIFIED",
            expectedVisibleText =
                expectation.expectedVisibleText,
        )

        return AndroidVerificationResult.create(
            traceId = traceId,
            status =
                AndroidVerificationStatus.VERIFIED,
            evidence =
                AndroidVerificationEvidence.create(
                    capabilityId = capabilityId,
                    description =
                        "Verified the expected Stage 314 post-action accessibility condition: ${expectation.expectedVisibleText}",
                ),
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): AndroidVerificationResult {
        return AndroidVerificationResult.create(
            traceId = traceId,
            status =
                AndroidVerificationStatus.DEFERRED,
        )
    }
}
