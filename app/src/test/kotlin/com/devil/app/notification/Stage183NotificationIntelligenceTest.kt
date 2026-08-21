package com.devil.app.notification

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Stage183NotificationIntelligenceTest {

    @Test
    fun `analyzed Stage 39 result becomes available with exact provenance`() {
        val analysis =
            analyzedResult()

        val result =
            AndroidNotificationIntelligenceCoordinator()
                .integrate(analysis)

        assertEquals(
            AndroidNotificationIntelligenceStatus.AVAILABLE,
            result.status,
        )
        assertEquals(analysis, result.analysis)
    }

    @Test
    fun `perception only Stage 39 result remains deferred with exact provenance`() {
        val analysis =
            perceptionOnlyResult()

        val result =
            AndroidNotificationIntelligenceCoordinator()
                .integrate(analysis)

        assertEquals(
            AndroidNotificationIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertEquals(analysis, result.analysis)
    }

    @Test
    fun `available result rejects perception only analysis`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidNotificationIntelligenceResult.create(
                status = AndroidNotificationIntelligenceStatus.AVAILABLE,
                analysis = perceptionOnlyResult(),
            )
        }
    }

    @Test
    fun `deferred result rejects analyzed notification`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidNotificationIntelligenceResult.create(
                status = AndroidNotificationIntelligenceStatus.DEFERRED,
                analysis = analyzedResult(),
            )
        }
    }

    private fun analyzedResult(): AndroidNotificationAnalysisResult {
        val record =
            AndroidNotificationRecord.create(
                eventType = AndroidNotificationEventType.POSTED,
                packageName = "com.example.messages",
                notificationKey = "notification-stage-183-analyzed",
                postedAtEpochMilliseconds = 100L,
                category = "msg",
                title = "Example",
                text = "Bounded notification text",
            )

        val safety =
            AndroidNotificationSafetyPolicy()
                .evaluate(record)

        return AndroidNotificationAnalysisPolicy()
            .analyze(
                record = record,
                safety = safety,
            )
    }

    private fun perceptionOnlyResult(): AndroidNotificationAnalysisResult {
        val record =
            AndroidNotificationRecord.create(
                eventType = AndroidNotificationEventType.REMOVED,
                packageName = "com.example.messages",
                notificationKey = "notification-stage-183-perception",
                postedAtEpochMilliseconds = 200L,
                category = "msg",
            )

        val safety =
            AndroidNotificationSafetyPolicy()
                .evaluate(record)

        return AndroidNotificationAnalysisPolicy()
            .analyze(
                record = record,
                safety = safety,
            )
    }
}
