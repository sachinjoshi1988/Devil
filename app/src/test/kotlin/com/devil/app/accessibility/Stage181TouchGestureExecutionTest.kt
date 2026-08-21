package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage181TouchGestureExecutionTest {

    @Test
    fun `resolved Stage 180 target prepares bounded click request`() {
        val target =
            AndroidAccessibilityTarget.fromText("Settings")

        val element =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "Settings",
                contentDescription = null,
            )

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(element),
            )

        val resolution =
            AndroidReliableTargetResolutionResult.create(
                status = AndroidReliableTargetResolutionStatus.RESOLVED,
                screenUnderstanding = screen,
                target = target,
                resolvedElement = element,
            )

        val result =
            AndroidTouchGestureExecutionCoordinator()
                .prepare(resolution)

        assertEquals(
            AndroidTouchGestureExecutionStatus.READY,
            result.status,
        )
        assertEquals(resolution, result.targetResolution)
        assertEquals(
            AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT,
            result.actionRequest?.actionType,
        )
        assertEquals(target, result.actionRequest?.target)
    }

    @Test
    fun `not found Stage 180 target remains deferred`() {
        val target =
            AndroidAccessibilityTarget.fromText("Settings")

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
            )

        val resolution =
            AndroidReliableTargetResolutionResult.create(
                status = AndroidReliableTargetResolutionStatus.NOT_FOUND,
                screenUnderstanding = screen,
                target = target,
            )

        val result =
            AndroidTouchGestureExecutionCoordinator()
                .prepare(resolution)

        assertEquals(
            AndroidTouchGestureExecutionStatus.DEFERRED,
            result.status,
        )
        assertEquals(resolution, result.targetResolution)
        assertNull(result.actionRequest)
    }

    @Test
    fun `screen unavailable Stage 180 target remains deferred`() {
        val target =
            AndroidAccessibilityTarget.fromText("Settings")

        val screen =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus
                        .SERVICE_UNAVAILABLE,
            )

        val resolution =
            AndroidReliableTargetResolutionResult.create(
                status =
                    AndroidReliableTargetResolutionStatus
                        .SCREEN_UNAVAILABLE,
                screenUnderstanding = screen,
                target = target,
            )

        val result =
            AndroidTouchGestureExecutionCoordinator()
                .prepare(resolution)

        assertEquals(
            AndroidTouchGestureExecutionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.actionRequest)
    }

    @Test
    fun `ready result requires resolved Stage 180 provenance`() {
        val target =
            AndroidAccessibilityTarget.fromText("Settings")

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
            )

        val resolution =
            AndroidReliableTargetResolutionResult.create(
                status = AndroidReliableTargetResolutionStatus.NOT_FOUND,
                screenUnderstanding = screen,
                target = target,
            )

        val request =
            AndroidAccessibilityActionRequest(
                actionType =
                    AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT,
                target = target,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidTouchGestureExecutionResult.create(
                status = AndroidTouchGestureExecutionStatus.READY,
                targetResolution = resolution,
                actionRequest = request,
            )
        }
    }

    @Test
    fun `ready result requires exact Stage 180 target`() {
        val target =
            AndroidAccessibilityTarget.fromText("Settings")

        val element =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "Settings",
                contentDescription = null,
            )

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(element),
            )

        val resolution =
            AndroidReliableTargetResolutionResult.create(
                status = AndroidReliableTargetResolutionStatus.RESOLVED,
                screenUnderstanding = screen,
                target = target,
                resolvedElement = element,
            )

        val foreignRequest =
            AndroidAccessibilityActionRequest(
                actionType =
                    AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT,
                target =
                    AndroidAccessibilityTarget.fromText("Recent"),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidTouchGestureExecutionResult.create(
                status = AndroidTouchGestureExecutionStatus.READY,
                targetResolution = resolution,
                actionRequest = foreignRequest,
            )
        }
    }
}
