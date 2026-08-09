package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class AndroidNotificationAnalysisCoordinatorTest {

    @Test
    fun `coordinator preserves record through bounded safety and analysis path`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName = "com.example.messaging",
                notificationKey =
                    "notification-analysis-coordinator-test",
                postedAtEpochMilliseconds =
                    1_754_000_391_000L,
                category = "msg",
                title = "New message",
                text = "Hello",
            )

        var observed:
            AndroidNotificationAnalysisResult? = null

        val coordinator =
            AndroidNotificationAnalysisCoordinator(
                listener =
                    AndroidNotificationAnalysisListener {
                        result ->
                        observed = result
                    },
            )

        val result =
            coordinator.analyze(
                record = record,
            )

        assertEquals(
            AndroidNotificationAnalysisStatus.ANALYZED,
            result.status,
        )

        assertSame(
            record,
            result.record,
        )

        assertSame(
            result,
            observed,
        )

        assertEquals(
            AndroidNotificationClassification.MESSAGE,
            result.safety
                .classification
                .classification,
        )
    }

    @Test
    fun `removed notification remains perception only`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.REMOVED,
                packageName = "com.example.messaging",
                notificationKey =
                    "notification-analysis-removed-test",
                postedAtEpochMilliseconds =
                    1_754_000_392_000L,
                category = "msg",
                title = "Removed message",
            )

        val result =
            AndroidNotificationAnalysisCoordinator()
                .analyze(
                    record = record,
                )

        assertEquals(
            AndroidNotificationAnalysisStatus.PERCEPTION_ONLY,
            result.status,
        )
    }
}
