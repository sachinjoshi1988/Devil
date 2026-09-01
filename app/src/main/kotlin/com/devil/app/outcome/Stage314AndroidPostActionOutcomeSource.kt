package com.devil.app.outcome

import com.devil.app.diagnostic.NoOpStage314PostActionDiagnostic
import com.devil.app.diagnostic.Stage314PostActionDiagnostic
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.app.observation.Stage314AndroidPostActionObservationStore
import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.core.model.common.TraceId

/**
 * Stage 314 bounded post-action Android outcome source.
 *
 * This source is eligible only after the existing constitutional path supplies
 * genuine Android verification evidence.
 *
 * It independently requires the same trace/capability to retain:
 *
 * - the bounded expected post-action condition; and
 * - the genuine accessibility-derived post-action observation.
 *
 * Verification evidence alone is not automatically reinterpreted as Outcome.
 *
 * ESTABLISHED here means only that the bounded Stage 314 post-action condition
 * has sufficient preserved expectation, observation, and verification evidence.
 *
 * It does not claim:
 *
 * - task completion;
 * - plan completion;
 * - generic Android execution success;
 * - settings mutation;
 * - World Model update;
 * - Learning;
 * - Memory;
 * - persistence;
 * - or broader capability success.
 *
 * VERIFIED != OUTCOME.
 * OUTCOME_ESTABLISHED != TASK_COMPLETED.
 */
class Stage314AndroidPostActionOutcomeSource(
    private val expectationStore:
        Stage314AndroidPostActionExpectationStore,
    private val observationStore:
        Stage314AndroidPostActionObservationStore,
    private val presentationStore:
        Stage314VerifiedAndroidOutcomePresentationStore? = null,
    private val diagnostic:
        Stage314PostActionDiagnostic =
        NoOpStage314PostActionDiagnostic,
) : AndroidOutcomeSource {

    override fun establish(
        traceId: TraceId,
        verificationEvidence: AndroidVerificationEvidence,
    ): AndroidOutcomeResult {
        val capabilityId =
            verificationEvidence.capabilityId

        val expectation =
            expectationStore.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )
                ?: run {
                    diagnostic.outcome(
                        traceId = traceId,
                        capabilityId = capabilityId,
                        event = "EXPECTATION_MISSING",
                    )

                    return deferred(
                        traceId = traceId,
                    )
                }

        observationStore.current(
            traceId = traceId,
            capabilityId = capabilityId,
        )
            ?: run {
                diagnostic.outcome(
                    traceId = traceId,
                    capabilityId = capabilityId,
                    event = "OBSERVATION_MISSING",
                )

                return deferred(
                    traceId = traceId,
                )
            }

        diagnostic.outcome(
            traceId = traceId,
            capabilityId = capabilityId,
            event = "OUTCOME_ESTABLISHED",
        )

        val result =
            AndroidOutcomeResult.create(
                traceId = traceId,
                status =
                    AndroidOutcomeStatus.ESTABLISHED,
                evidence =
                    AndroidOutcomeEvidence.create(
                        capabilityId = capabilityId,
                        description =
                            "Established the bounded Stage 314 Android outcome after independent verification of the expected post-action accessibility condition: ${expectation.expectedVisibleText}",
                    ),
            )

        presentationStore?.bindEstablished(
            traceId = traceId,
            capabilityId = capabilityId,
            message = "Android action verified.",
        )

        return result
    }

    private fun deferred(
        traceId: TraceId,
    ): AndroidOutcomeResult {
        return AndroidOutcomeResult.create(
            traceId = traceId,
            status =
                AndroidOutcomeStatus.DEFERRED,
        )
    }
}
