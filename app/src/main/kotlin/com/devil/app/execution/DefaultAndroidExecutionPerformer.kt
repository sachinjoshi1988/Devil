package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionSource
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DefaultAndroidAccessibilityActionSource
import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Default Android execution performer.
 *
 * Stage 30 established this as a fail-closed platform execution boundary.
 *
 * Stage 38 adds the first explicitly registered Android action implementation:
 *
 * Android Accessibility Click Visible Text.
 *
 * Stage 314 may additionally mark the exact genuine ATTEMPTED boundary for the
 * bounded post-action accessibility evidence bridge.
 *
 * ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != COMPLETED.
 */
class DefaultAndroidExecutionPerformer(
    directiveProvider: AndroidExecutionDirectiveProvider =
        DefaultAndroidExecutionDirectiveProvider(),
    accessibilitySource: AndroidAccessibilityActionSource =
        DefaultAndroidAccessibilityActionSource(),
    private val accessibilityChangeReadinessStore:
        Stage314AndroidAccessibilityChangeReadinessStore? =
        null,
) : AndroidExecutionPerformer {

    private val accessibilityPerformer =
        AndroidAccessibilityExecutionPerformer(
            directiveProvider = directiveProvider,
            accessibilitySource = accessibilitySource,
        )

    override fun perform(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionAttemptResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Android execution performer trace and request must use the same trace identity."
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

        val result =
            accessibilityPerformer.perform(
                traceId = traceId,
                request = request,
            )

        if (
            result.status ==
            AndroidExecutionAttemptStatus.ATTEMPTED
        ) {
            val attemptedCapabilityId =
                requireNotNull(result.capabilityId) {
                    "Attempted Android execution requires capability identity."
                }

            accessibilityChangeReadinessStore
                ?.markExecutionAttempted(
                    traceId = traceId,
                    capabilityId = attemptedCapabilityId,
                )
        }

        return result
    }
}
