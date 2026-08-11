package com.devil.app.vision

/**
 * Resource policy for one transient Android vision frame.
 *
 * These limits bound only the platform resources that may be accepted for one
 * explicitly requested still-frame capture.
 *
 * They grant no authority and establish no interpretation, observation,
 * verification, memory, or Outcome.
 */
internal object AndroidVisionFrameResourcePolicy {

    internal const val MAX_FRAME_PIXEL_COUNT: Long =
        2_500_000L

    internal const val TARGET_FRAME_PIXEL_COUNT: Long =
        2_000_000L

    internal const val MAX_ENCODED_FRAME_BYTES: Int =
        8 * 1024 * 1024

    fun acceptsDimensions(
        width: Int,
        height: Int,
    ): Boolean {
        if (width <= 0 || height <= 0) {
            return false
        }

        return width.toLong() * height.toLong() <=
            MAX_FRAME_PIXEL_COUNT
    }

    fun acceptsEncodedByteCount(
        byteCount: Int,
    ): Boolean {
        return byteCount in 1..MAX_ENCODED_FRAME_BYTES
    }
}
