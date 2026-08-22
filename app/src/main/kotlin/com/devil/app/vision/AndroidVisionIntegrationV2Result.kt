package com.devil.app.vision

/**
 * Stage 205 bounded Vision Integration V2 result.
 *
 * AVAILABLE preserves the exact Stage 41 frame-perception result and the exact
 * captured frame it already contains.
 *
 * DEFERRED preserves the exact Stage 41 perception result and no frame.
 *
 * VISION_INTEGRATED != IMAGE_UNDERSTOOD.
 * FRAME_CAPTURED != SEMANTIC_UNDERSTANDING.
 * FRAME_CAPTURED != IDENTITY.
 * VISION_AVAILABLE != AUTHENTICATION.
 * VISUAL_INPUT != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class AndroidVisionIntegrationV2Result private constructor(
    val status: AndroidVisionIntegrationV2Status,
    val perceptionResult: AndroidVisionFramePerceptionResult,
    val frame: AndroidVisionFrame?,
) {
    companion object {
        fun create(
            status: AndroidVisionIntegrationV2Status,
            perceptionResult: AndroidVisionFramePerceptionResult,
            frame: AndroidVisionFrame? = null,
        ): AndroidVisionIntegrationV2Result {
            when (status) {
                AndroidVisionIntegrationV2Status.AVAILABLE -> {
                    require(
                        perceptionResult.status ==
                            AndroidVisionFrameCaptureStatus.CAPTURED,
                    ) {
                        "Available Stage 205 vision integration requires a captured Stage 41 perception result."
                    }

                    val perceivedFrame =
                        requireNotNull(perceptionResult.frame) {
                            "Captured Stage 41 perception result requires one frame."
                        }

                    require(frame === perceivedFrame) {
                        "Stage 205 must preserve the exact Stage 41 captured frame provenance."
                    }
                }

                AndroidVisionIntegrationV2Status.DEFERRED -> {
                    require(
                        perceptionResult.status !=
                            AndroidVisionFrameCaptureStatus.CAPTURED,
                    ) {
                        "Deferred Stage 205 vision integration must not contain a captured Stage 41 perception result."
                    }

                    require(frame == null) {
                        "Deferred Stage 205 vision integration must not contain a frame."
                    }
                }
            }

            return AndroidVisionIntegrationV2Result(
                status = status,
                perceptionResult = perceptionResult,
                frame = frame,
            )
        }
    }
}
