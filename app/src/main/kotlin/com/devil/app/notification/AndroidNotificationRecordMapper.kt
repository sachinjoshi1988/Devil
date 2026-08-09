package com.devil.app.notification

/**
 * Stage 39 mapper from already-extracted Android notification platform values
 * into one immutable AndroidNotificationRecord.
 *
 * Keeping primitive platform extraction separate from the record contract makes
 * normalization deterministic and testable.
 *
 * This mapper performs no semantic interpretation.
 */
class AndroidNotificationRecordMapper {

    fun map(
        eventType: AndroidNotificationEventType,
        packageName: String,
        notificationKey: String,
        postedAtEpochMilliseconds: Long,
        category: String?,
        title: CharSequence?,
        text: CharSequence?,
        subText: CharSequence?,
    ): AndroidNotificationRecord {
        return AndroidNotificationRecord.create(
            eventType = eventType,
            packageName = packageName,
            notificationKey = notificationKey,
            postedAtEpochMilliseconds =
                postedAtEpochMilliseconds,
            category = category,
            title = title?.toString(),
            text = text?.toString(),
            subText = subText?.toString(),
        )
    }
}
