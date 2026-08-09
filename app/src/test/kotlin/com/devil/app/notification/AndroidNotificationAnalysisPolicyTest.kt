package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class AndroidNotificationAnalysisPolicyTest {

    @Test
    fun `eligible posted notification may receive bounded analyzed status`() {
        val record =
            record(
                eventType = AndroidNotificationEventType.POSTED,
                title = "Message",
            )

        val safety =
            AndroidNotificationSafetyPolicy()
                .evaluate(record)

        val result =
            AndroidNotificationAnalysisPolicy()
                .analyze(
                    record = record,
                    safety = safety,
                )

        assertEquals(
            AndroidNotificationAnalysisStatus.ANALYZED,
            result.status,
        )

        assertSame(
            record,
            result.record,
        )

        assertEquals(
            AndroidNotificationSafetyDisposition.ELIGIBLE_FOR_LATER_ANALYSIS,
            result.safety.disposition,
        )
    }

    @Test
    fun `perception-only notification cannot become analyzed`() {
        val record =
            record(
                eventType = AndroidNotificationEventType.REMOVED,
                title = "Removed",
            )

        val safety =
            AndroidNotificationSafetyPolicy()
                .evaluate(record)

        val result =
            AndroidNotificationAnalysisPolicy()
                .analyze(
                    record = record,
                    safety = safety,
                )

        assertEquals(
            AndroidNotificationAnalysisStatus.PERCEPTION_ONLY,
            result.status,
        )

        assertEquals(
            AndroidNotificationSafetyDisposition.PERCEPTION_ONLY,
            result.safety.disposition,
        )
    }

    private fun record(
        eventType: AndroidNotificationEventType,
        title: String?,
    ): AndroidNotificationRecord {
        return AndroidNotificationRecord.create(
            eventType = eventType,
            packageName = "com.example.app",
            notificationKey = "notification-analysis-policy-test",
            postedAtEpochMilliseconds = 1_754_000_390_000L,
            category = "msg",
            title = title,
        )
    }
}
