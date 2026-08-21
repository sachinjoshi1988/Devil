package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage179ScreenUnderstandingTest {

    @Test
    fun `screen element normalizes supplied accessibility metadata`() {
        val element =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "  Settings  ",
                contentDescription = "  Open settings  ",
            )

        assertEquals(0, element.position)
        assertEquals("Settings", element.text)
        assertEquals(
            "Open settings",
            element.contentDescription,
        )
    }

    @Test
    fun `screen element permits one supplied metadata field`() {
        val element =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "Camera",
                contentDescription = "   ",
            )

        assertEquals("Camera", element.text)
        assertNull(element.contentDescription)
    }

    @Test
    fun `screen element rejects absent readable metadata`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidScreenElementRecord.create(
                position = 0,
                text = " ",
                contentDescription = null,
            )
        }
    }

    @Test
    fun `available result preserves contiguous ordered elements`() {
        val first =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "First",
                contentDescription = null,
            )

        val second =
            AndroidScreenElementRecord.create(
                position = 1,
                text = null,
                contentDescription = "Second",
            )

        val result =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(first, second),
            )

        assertEquals(listOf(first, second), result.elements)
    }

    @Test
    fun `available result rejects noncontiguous positions`() {
        val element =
            AndroidScreenElementRecord.create(
                position = 1,
                text = "Settings",
                contentDescription = null,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.AVAILABLE,
                elements = listOf(element),
            )
        }
    }

    @Test
    fun `unavailable result cannot contain screen elements`() {
        val element =
            AndroidScreenElementRecord.create(
                position = 0,
                text = "Settings",
                contentDescription = null,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus
                        .SERVICE_UNAVAILABLE,
                elements = listOf(element),
            )
        }
    }

    @Test
    fun `coordinator preserves exact source result`() {
        val expected =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.AVAILABLE,
                elements =
                    listOf(
                        AndroidScreenElementRecord.create(
                            position = 0,
                            text = "Settings",
                            contentDescription = null,
                        ),
                    ),
            )

        val coordinator =
            AndroidScreenUnderstandingCoordinator(
                source =
                    AndroidScreenUnderstandingSource {
                        expected
                    },
            )

        assertSame(expected, coordinator.inspect())
    }
}
