package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionSource
import com.devil.app.accessibility.AndroidAccessibilityActionStatus
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DefaultAndroidAccessibilityActionSource
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest

/**
 * Stage 38 bounded Android accessibility execution performer.
 *
 * This performer may be reached only after DefaultAndroidExecutionAdapter has
 * already established:
 *
 * - constitutional Execution APPROVED;
 * - matching capability identity;
 * - Android capability AVAILABLE;
 * - Android capability health READY;
 * - and Android permission NOT_REQUIRED or GRANTED.
 *
 * The performer additionally requires one explicit matching typed
 * AndroidExecutionDirective.
 *
 * It never derives an accessibility target from plan summaries, task summaries,
 * decision summaries, capability names, capability descriptions, or raw
 * conversation text.
 *
 * Accessibility ATTEMPTED != observed effect.
 * Accessibility ATTEMPTED != verified outcome.
 * Accessibility ATTEMPTED != task completion.
 */
class AndroidAccessibilityExecutionPerformer(
    private val directiveProvider: AndroidExecutionDirectiveProvider,
    private val accessibilitySource: AndroidAccessibilityActionSource =
        DefaultAndroidAccessibilityActionSource(),
    private val failureTimeProvider: () -> DevilTimestamp = {
        DevilTimestamp.fromEpochMilliseconds(
            System.currentTimeMillis(),
        )
    },
) : AndroidExecutionPerformer {

    override fun perform(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionAttemptResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Android accessibility execution performer trace and request must use the same trace identity."
        }

        if (
            !AndroidAccessibilityCapability.matches(
                request.capability,
            )
        ) {
            return AndroidExecutionAttemptResult.create(
                traceId = traceId,
                status =
                    AndroidExecutionAttemptStatus.DEFERRED,
            )
        }

        val directive =
            directiveProvider.provide(
                traceId = traceId,
                request = request,
            )
                ?: return AndroidExecutionAttemptResult.create(
                    traceId = traceId,
                    status =
                        AndroidExecutionAttemptStatus.DEFERRED,
                )

        require(directive.traceId == traceId) {
            "Android execution directive and execution request must use the same trace identity."
        }

        require(
            directive.capabilityId ==
                request.capability.capabilityId,
        ) {
            "Android execution directive and execution request must use the same capability identity."
        }

        val accessibilityRequest =
            directive.accessibilityRequest

        val result =
            accessibilitySource.perform(
                request = accessibilityRequest,
            )

        return when (result.status) {
            AndroidAccessibilityActionStatus.ATTEMPTED ->
                AndroidExecutionAttemptResult.create(
                    traceId = traceId,
                    status =
                        AndroidExecutionAttemptStatus.ATTEMPTED,
                    capabilityId =
                        request.capability.capabilityId,
                )

            AndroidAccessibilityActionStatus.TARGET_NOT_FOUND,
            AndroidAccessibilityActionStatus.SERVICE_UNAVAILABLE,
            ->
                AndroidExecutionAttemptResult.create(
                    traceId = traceId,
                    status =
                        AndroidExecutionAttemptStatus.DEFERRED,
                )

            AndroidAccessibilityActionStatus.FAILED ->
                AndroidExecutionAttemptResult.create(
                    traceId = traceId,
                    status =
                        AndroidExecutionAttemptStatus.FAILED,
                    error =
                        UniversalErrorRecord.create(
                            errorCode =
                                ErrorCode.from(
                                    requireNotNull(
                                        result.errorCode,
                                    ),
                                ),
                            traceId = traceId,
                            occurredAt =
                                failureTimeProvider(),
                            summary =
                                "Bounded Android accessibility execution failed.",
                        ),
                )
        }
    }
}
