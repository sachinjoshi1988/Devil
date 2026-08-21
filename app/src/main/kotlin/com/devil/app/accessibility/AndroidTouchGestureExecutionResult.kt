package com.devil.app.accessibility

/**
 * Stage 181 bounded Touch & Gesture Execution result.
 *
 * READY preserves one exact Stage 180 Reliable Target Resolution result
 * together with one bounded Android accessibility action request.
 *
 * DEFERRED preserves the exact Stage 180 result and no action request.
 *
 * This result does not:
 *
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - perform an Android accessibility action;
 * - establish that an action was attempted;
 * - observe or verify an effect;
 * - establish Outcome;
 * - implement Stage 182 Navigation Intelligence.
 *
 * TARGET_RESOLVED != EXECUTION_APPROVED.
 * ACTION_REQUEST_PREPARED != ACTION_ATTEMPTED.
 * ACTION_ATTEMPTED != OBSERVED_EFFECT.
 */
@ConsistentCopyVisibility
data class AndroidTouchGestureExecutionResult private constructor(
    val status: AndroidTouchGestureExecutionStatus,
    val targetResolution: AndroidReliableTargetResolutionResult,
    val actionRequest: AndroidAccessibilityActionRequest?,
) {
    companion object {

        fun create(
            status: AndroidTouchGestureExecutionStatus,
            targetResolution: AndroidReliableTargetResolutionResult,
            actionRequest: AndroidAccessibilityActionRequest? = null,
        ): AndroidTouchGestureExecutionResult {
            when (status) {
                AndroidTouchGestureExecutionStatus.READY ->
                    require(actionRequest != null) {
                        "Ready Android touch-and-gesture execution results require one action request."
                    }

                AndroidTouchGestureExecutionStatus.DEFERRED ->
                    require(actionRequest == null) {
                        "Deferred Android touch-and-gesture execution results must not contain an action request."
                    }
            }

            if (status == AndroidTouchGestureExecutionStatus.READY) {
                require(
                    targetResolution.status ==
                        AndroidReliableTargetResolutionStatus.RESOLVED,
                ) {
                    "Ready Android touch-and-gesture execution requires resolved Stage 180 target resolution."
                }

                require(
                    actionRequest?.target ==
                        targetResolution.target,
                ) {
                    "Prepared Android accessibility action request must preserve the exact Stage 180 target."
                }

                require(
                    actionRequest?.actionType ==
                        AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT,
                ) {
                    "Stage 181 currently prepares only the bounded click-visible-text accessibility action."
                }
            }

            return AndroidTouchGestureExecutionResult(
                status = status,
                targetResolution = targetResolution,
                actionRequest = actionRequest,
            )
        }
    }
}
