package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionSource
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.accessibility.DefaultAndroidAccessibilityActionSource
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
 * The performer remains a router rather than an understanding, planning, or
 * authorization component.
 *
 * A supported capability may reach its bounded performer only when an explicit
 * typed Android execution directive already exists.
 *
 * No directive means DEFERRED.
 *
 * Unsupported capability means DEFERRED.
 *
 * The performer must never infer an action or target from:
 *
 * - conversation text;
 * - Understanding summary;
 * - Decision summary;
 * - Task summary;
 * - Plan summary;
 * - capability name;
 * - capability description;
 * - or accessibility-tree contents.
 *
 * Execution APPROVED != platform action attempted.
 * Attempted != Observed.
 * Observed != Verified.
 * Verified != Completed.
 */
class DefaultAndroidExecutionPerformer(
    directiveProvider: AndroidExecutionDirectiveProvider =
        DefaultAndroidExecutionDirectiveProvider(),
    accessibilitySource: AndroidAccessibilityActionSource =
        DefaultAndroidAccessibilityActionSource(),
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

        return if (
            AndroidAccessibilityCapability.matches(
                request.capability,
            )
        ) {
            accessibilityPerformer.perform(
                traceId = traceId,
                request = request,
            )
        } else {
            AndroidExecutionAttemptResult.create(
                traceId = traceId,
                status =
                    AndroidExecutionAttemptStatus.DEFERRED,
            )
        }
    }
}
