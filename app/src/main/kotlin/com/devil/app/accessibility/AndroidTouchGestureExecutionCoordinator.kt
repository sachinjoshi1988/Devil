package com.devil.app.accessibility

/**
 * Stage 181 bounded Touch & Gesture Execution coordinator.
 *
 * The coordinator consumes one exact Stage 180 Reliable Target Resolution
 * result.
 *
 * A genuinely RESOLVED Stage 180 target is converted only into the currently
 * supported bounded CLICK_VISIBLE_TEXT Android accessibility request.
 *
 * Non-resolved Stage 180 states remain DEFERRED.
 *
 * This coordinator does not:
 *
 * - inspect a live AccessibilityNodeInfo tree;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - perform the accessibility action;
 * - call AccessibilityNodeInfo.performAction;
 * - dispatch gestures;
 * - observe or verify an effect;
 * - establish Outcome;
 * - implement Stage 182 Navigation Intelligence.
 *
 * TARGET_RESOLUTION != EXECUTION.
 * ACTION_REQUEST_PREPARED != ACTION_ATTEMPTED.
 */
class AndroidTouchGestureExecutionCoordinator {

    fun prepare(
        targetResolution: AndroidReliableTargetResolutionResult,
    ): AndroidTouchGestureExecutionResult {
        if (
            targetResolution.status !=
            AndroidReliableTargetResolutionStatus.RESOLVED
        ) {
            return AndroidTouchGestureExecutionResult.create(
                status = AndroidTouchGestureExecutionStatus.DEFERRED,
                targetResolution = targetResolution,
            )
        }

        val request =
            AndroidAccessibilityActionRequest(
                actionType =
                    AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT,
                target = targetResolution.target,
            )

        return AndroidTouchGestureExecutionResult.create(
            status = AndroidTouchGestureExecutionStatus.READY,
            targetResolution = targetResolution,
            actionRequest = request,
        )
    }
}
