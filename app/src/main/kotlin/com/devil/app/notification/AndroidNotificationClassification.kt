package com.devil.app.notification

/**
 * Stage 39 bounded classification derived only from Android-supplied notification
 * category metadata.
 *
 * Classification is descriptive metadata only.
 *
 * MESSAGE does not authenticate a sender.
 * CALL does not prove a genuine incoming call.
 * SECURITY does not prove a genuine security event.
 * FINANCIAL does not prove a genuine financial transaction.
 *
 * Classification
 * != truth
 * != trust
 * != importance
 * != authorization
 * != conversational intent
 * != permission to speak
 * != permission to persist
 * != permission to execute.
 */
enum class AndroidNotificationClassification {
    MESSAGE,
    CALL,
    EMAIL,
    EVENT,
    REMINDER,
    ALARM,
    NAVIGATION,
    TRANSPORT,
    PROGRESS,
    SERVICE,
    STATUS,
    SYSTEM,
    SOCIAL,
    RECOMMENDATION,
    PROMOTION,
    ERROR,
    LOCATION,
    TIMER,
    FINANCIAL,
    SECURITY,
    OTHER,
    UNKNOWN,
}
