package com.devil.app.notification

/**
 * Stage 183 bounded Notifications Intelligence status.
 *
 * AVAILABLE means one existing Stage 39 ANALYZED notification result is
 * available to the bounded Stage 183 intelligence layer.
 *
 * DEFERRED means the supplied Stage 39 result remains perception-only.
 *
 * NOTIFICATION_INTELLIGENCE_AVAILABLE != TRUSTED_CONTENT.
 * NOTIFICATION_INTELLIGENCE_AVAILABLE != DEVIL_AUTHORIZATION.
 */
enum class AndroidNotificationIntelligenceStatus {
    AVAILABLE,
    DEFERRED,
}
