package com.devil.app.notification

/**
 * Immutable Stage 39 Android notification perception record.
 *
 * This record preserves only bounded data explicitly supplied by the Android
 * notification-listener boundary.
 *
 * packageName identifies the Android package that posted the notification.
 * It does not authenticate a human sender.
 *
 * notificationKey is Android platform identity for the notification instance.
 *
 * category preserves optional Android-supplied notification category metadata.
 * Category metadata is descriptive only and is not trusted semantic truth.
 *
 * title, text, and subText are optional presentation fields supplied by Android.
 * Their presence does not establish truth, intent, priority, or authorization.
 *
 * postedAtEpochMilliseconds preserves Android-reported posting time.
 *
 * Captured notification data:
 *
 * != authenticated sender
 * != trusted content
 * != Devil command
 * != conversation input
 * != memory commitment
 * != authorization
 * != execution request
 * != verified outcome.
 */
@ConsistentCopyVisibility
data class AndroidNotificationRecord private constructor(
    val eventType: AndroidNotificationEventType,
    val packageName: String,
    val notificationKey: String,
    val postedAtEpochMilliseconds: Long,
    val category: String?,
    val title: String?,
    val text: String?,
    val subText: String?,
) {
    companion object {

        fun create(
            eventType: AndroidNotificationEventType,
            packageName: String,
            notificationKey: String,
            postedAtEpochMilliseconds: Long,
            category: String? = null,
            title: String? = null,
            text: String? = null,
            subText: String? = null,
        ): AndroidNotificationRecord {
            val normalizedPackageName =
                packageName.trim()

            val normalizedNotificationKey =
                notificationKey.trim()

            require(normalizedPackageName.isNotEmpty()) {
                "Android notification package name must not be blank."
            }

            require(normalizedNotificationKey.isNotEmpty()) {
                "Android notification key must not be blank."
            }

            require(postedAtEpochMilliseconds >= 0L) {
                "Android notification posting time must not be negative."
            }

            return AndroidNotificationRecord(
                eventType = eventType,
                packageName = normalizedPackageName,
                notificationKey = normalizedNotificationKey,
                postedAtEpochMilliseconds =
                    postedAtEpochMilliseconds,
                category = normalizeOptionalText(category),
                title = normalizeOptionalText(title),
                text = normalizeOptionalText(text),
                subText = normalizeOptionalText(subText),
            )
        }

        private fun normalizeOptionalText(
            value: String?,
        ): String? {
            return value
                ?.trim()
                ?.takeIf {
                    it.isNotEmpty()
                }
        }
    }
}
