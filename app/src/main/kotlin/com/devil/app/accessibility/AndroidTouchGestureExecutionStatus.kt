package com.devil.app.accessibility

/**
 * Stage 181 bounded Touch & Gesture Execution preparation status.
 *
 * READY means one exact Stage 180 resolved target can be represented as the
 * currently supported bounded Android accessibility click request.
 *
 * DEFERRED means no executable gesture request was prepared.
 *
 * READY != CONSTITUTIONAL_EXECUTION_APPROVED.
 * REQUEST_PREPARED != ACTION_ATTEMPTED.
 */
enum class AndroidTouchGestureExecutionStatus {
    READY,
    DEFERRED,
}
