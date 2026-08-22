package com.devil.app.vision

/**
 * Stage 205 bounded Vision Integration V2 coordinator.
 *
 * It integrates only an already-established Stage 41 frame-perception result.
 *
 * It does not:
 *
 * - capture a camera frame;
 * - interpret image contents;
 * - identify people, faces, or objects;
 * - perform OCR;
 * - authenticate from visual evidence;
 * - invoke external vision/model providers;
 * - persist frame bytes;
 * - create Memory;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 206 Image Understanding.
 *
 * VISION_INTEGRATED != IMAGE_UNDERSTOOD.
 * FRAME_CAPTURED != SEMANTIC_UNDERSTANDING.
 * FRAME_CAPTURED != IDENTITY.
 * VISION_AVAILABLE != AUTHENTICATION.
 * VISUAL_INPUT != VERIFIED_OUTCOME.
 */
class AndroidVisionIntegrationV2Coordinator {

    fun integrate(
        perceptionResult: AndroidVisionFramePerceptionResult,
    ): AndroidVisionIntegrationV2Result {
        return if (
            perceptionResult.status ==
                AndroidVisionFrameCaptureStatus.CAPTURED
        ) {
            AndroidVisionIntegrationV2Result.create(
                status = AndroidVisionIntegrationV2Status.AVAILABLE,
                perceptionResult = perceptionResult,
                frame = perceptionResult.frame,
            )
        } else {
            AndroidVisionIntegrationV2Result.create(
                status = AndroidVisionIntegrationV2Status.DEFERRED,
                perceptionResult = perceptionResult,
            )
        }
    }
}
