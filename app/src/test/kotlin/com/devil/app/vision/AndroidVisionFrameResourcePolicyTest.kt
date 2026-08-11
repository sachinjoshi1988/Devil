package com.devil.app.vision

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidVisionFrameResourcePolicyTest {

    @Test
    fun `dimensions at configured pixel ceiling are accepted`() {
        assertTrue(
            AndroidVisionFrameResourcePolicy.acceptsDimensions(
                width = 2_500,
                height = 1_000,
            ),
        )
    }

    @Test
    fun `dimensions above configured pixel ceiling are rejected`() {
        assertFalse(
            AndroidVisionFrameResourcePolicy.acceptsDimensions(
                width = 2_501,
                height = 1_000,
            ),
        )
    }

    @Test
    fun `non-positive dimensions are rejected`() {
        assertFalse(
            AndroidVisionFrameResourcePolicy.acceptsDimensions(
                width = 0,
                height = 1_000,
            ),
        )

        assertFalse(
            AndroidVisionFrameResourcePolicy.acceptsDimensions(
                width = 1_000,
                height = 0,
            ),
        )
    }

    @Test
    fun `encoded frame at configured byte ceiling is accepted`() {
        assertTrue(
            AndroidVisionFrameResourcePolicy.acceptsEncodedByteCount(
                AndroidVisionFrameResourcePolicy.MAX_ENCODED_FRAME_BYTES,
            ),
        )
    }

    @Test
    fun `encoded frame above configured byte ceiling is rejected`() {
        assertFalse(
            AndroidVisionFrameResourcePolicy.acceptsEncodedByteCount(
                AndroidVisionFrameResourcePolicy.MAX_ENCODED_FRAME_BYTES + 1,
            ),
        )
    }

    @Test
    fun `empty encoded frame is rejected`() {
        assertFalse(
            AndroidVisionFrameResourcePolicy.acceptsEncodedByteCount(
                0,
            ),
        )
    }
}
