package com.devil.app.notification

/**
 * Stage 39 bounded Android notification event type.
 *
 * POSTED means Android reported that one notification became available.
 *
 * REMOVED means Android reported that one previously visible notification was
 * removed.
 *
 * Neither state establishes sender identity, content truth, conversational
 * intent, importance, authorization, memory eligibility, execution approval,
 * observation proof, verification, or task success.
 */
enum class AndroidNotificationEventType {
    POSTED,
    REMOVED,
}
