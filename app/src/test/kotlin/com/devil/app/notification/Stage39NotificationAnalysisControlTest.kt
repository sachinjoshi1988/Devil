package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Stage39NotificationAnalysisControlTest {

    @Test
    fun `perception record reaches bounded analysis exactly once`() {
        val record =
            AndroidNotificationRecord.create(
                eventType =
                    AndroidNotificationEventType.POSTED,
                packageName = "com.example.stage39",
                notificationKey =
                    "stage39-analysis-control",
                postedAtEpochMilliseconds =
                    1_754_000_393_000L,
                category = "email",
                text = "A notification exists.",
            )

        var perceptionCalls = 0
        var analysisCalls = 0

        val analysisCoordinator =
            AndroidNotificationAnalysisCoordinator(
                listener =
                    AndroidNotificationAnalysisListener {
                        analysisCalls += 1
                    },
            )

        val perceptionCoordinator =
            AndroidNotificationPerceptionCoordinator(
                listener =
                    AndroidNotificationPerceptionListener {
                        perceived ->
                        perceptionCalls += 1
                        assertSame(
                            record,
                            perceived,
                        )
                    },
                analysisCoordinator =
                    analysisCoordinator,
            )

        val result =
            perceptionCoordinator.accept(
                record = record,
            )

        assertEquals(
            1,
            perceptionCalls,
        )

        assertEquals(
            1,
            analysisCalls,
        )

        assertEquals(
            AndroidNotificationAnalysisStatus.ANALYZED,
            result.status,
        )

        assertEquals(
            AndroidNotificationClassification.EMAIL,
            result.safety
                .classification
                .classification,
        )
    }
}
