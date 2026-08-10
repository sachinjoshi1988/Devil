package com.devil.app.vision

/**
 * Immutable Stage 41 representation of one transient Android visual frame.
 *
 * cameraId is Android platform camera identity only.
 *
 * capturedAtEpochMilliseconds is the bounded capture timestamp supplied by the
 * approved frame source.
 *
 * encodedBytes contains one transient encoded visual frame.
 *
 * Frame bytes:
 *
 * != trusted reality
 * != semantic understanding
 * != person identity
 * != biometric authentication
 * != conversation input
 * != memory
 * != authorization
 * != verified Outcome.
 *
 * Stage 41 does not persist this object.
 */
class AndroidVisionFrame private constructor(
    val cameraId: String,
    val format: AndroidVisionFrameFormat,
    val capturedAtEpochMilliseconds: Long,
    val width: Int,
    val height: Int,
    encodedBytes: ByteArray,
) {

    private val protectedBytes: ByteArray =
        encodedBytes.copyOf()

    val byteCount: Int
        get() = protectedBytes.size

    fun encodedBytesCopy(): ByteArray {
        return protectedBytes.copyOf()
    }

    companion object {

        fun create(
            cameraId: String,
            format: AndroidVisionFrameFormat,
            capturedAtEpochMilliseconds: Long,
            width: Int,
            height: Int,
            encodedBytes: ByteArray,
        ): AndroidVisionFrame {
            val normalizedCameraId =
                cameraId.trim()

            require(normalizedCameraId.isNotEmpty()) {
                "Android vision frame camera identity must not be blank."
            }

            require(capturedAtEpochMilliseconds >= 0L) {
                "Android vision frame capture time must not be negative."
            }

            require(width > 0) {
                "Android vision frame width must be positive."
            }

            require(height > 0) {
                "Android vision frame height must be positive."
            }

            require(encodedBytes.isNotEmpty()) {
                "Android vision frame bytes must not be empty."
            }

            return AndroidVisionFrame(
                cameraId = normalizedCameraId,
                format = format,
                capturedAtEpochMilliseconds =
                    capturedAtEpochMilliseconds,
                width = width,
                height = height,
                encodedBytes = encodedBytes,
            )
        }
    }
}
