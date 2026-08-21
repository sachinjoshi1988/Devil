package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage180ReliableTargetResolutionTest {

    @Test
    fun `unique normalized match resolves exact Stage 179 element`() {
        val first =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "Settings",
                contentDescription = null,
            )

        val second =
            AndroidScreenElementRecord.create(
                position = 1,
                text = "Recent",
                contentDescription = null,
            )

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(first, second),
            )

        val target =
            AndroidAccessibilityTarget.fromText(
                "  SETTINGS  ",
            )

        val result =
            AndroidReliableTargetResolutionCoordinator()
                .resolve(
                    screenUnderstanding = screen,
                    target = target,
                )

        assertEquals(
            AndroidReliableTargetResolutionStatus.RESOLVED,
            result.status,
        )
        assertEquals(screen, result.screenUnderstanding)
        assertEquals(target, result.target)
        assertEquals(first, result.resolvedElement)
    }

    @Test
    fun `content description can provide bounded match`() {
        val element =
            AndroidScreenElementRecord.create(
                position = 0,
                text = null,
                contentDescription = "Open Settings",
            )

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(element),
            )

        val result =
            AndroidReliableTargetResolutionCoordinator()
                .resolve(
                    screenUnderstanding = screen,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "open   settings",
                        ),
                )

        assertEquals(
            AndroidReliableTargetResolutionStatus.RESOLVED,
            result.status,
        )
        assertEquals(element, result.resolvedElement)
    }

    @Test
    fun `zero matches remains not found`() {
        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements =
                    listOf(
                        AndroidScreenElementRecord.create(
                            position = 0,
                            text = "Home",
                            contentDescription = null,
                        ),
                    ),
            )

        val result =
            AndroidReliableTargetResolutionCoordinator()
                .resolve(
                    screenUnderstanding = screen,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Settings",
                        ),
                )

        assertEquals(
            AndroidReliableTargetResolutionStatus.NOT_FOUND,
            result.status,
        )
        assertNull(result.resolvedElement)
    }

    @Test
    fun `ambiguous multiple matches do not fabricate reliable resolution`() {
        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements =
                    listOf(
                        AndroidScreenElementRecord.create(
                            position = 0,
                            text = "Send",
                            contentDescription = null,
                        ),
                        AndroidScreenElementRecord.create(
                            position = 1,
                            text = null,
                            contentDescription = "SEND",
                        ),
                    ),
            )

        val result =
            AndroidReliableTargetResolutionCoordinator()
                .resolve(
                    screenUnderstanding = screen,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Send",
                        ),
                )

        assertEquals(
            AndroidReliableTargetResolutionStatus.NOT_FOUND,
            result.status,
        )
        assertNull(result.resolvedElement)
    }

    @Test
    fun `unavailable Stage 179 screen remains screen unavailable`() {
        val screen =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus
                        .SERVICE_UNAVAILABLE,
            )

        val result =
            AndroidReliableTargetResolutionCoordinator()
                .resolve(
                    screenUnderstanding = screen,
                    target =
                        AndroidAccessibilityTarget.fromText(
                            "Settings",
                        ),
                )

        assertEquals(
            AndroidReliableTargetResolutionStatus.SCREEN_UNAVAILABLE,
            result.status,
        )
        assertEquals(screen, result.screenUnderstanding)
        assertNull(result.resolvedElement)
    }

    @Test
    fun `resolved result rejects element outside supplied Stage 179 provenance`() {
        val supplied =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "Settings",
                contentDescription = null,
            )

        val foreign =
            AndroidScreenElementRecord.create(
                position = 1,
                text = "Settings",
                contentDescription = null,
            )

        val screen =
            AndroidScreenUnderstandingResult.create(
                status = AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(supplied),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidReliableTargetResolutionResult.create(
                status =
                    AndroidReliableTargetResolutionStatus.RESOLVED,
                screenUnderstanding = screen,
                target =
                    AndroidAccessibilityTarget.fromText(
                        "Settings",
                    ),
                resolvedElement = foreign,
            )
        }
    }
}
