package com.devil.app.vision

/**
 * Stage 41 bounded coordinator for one explicitly requested Android visual
 * frame.
 *
 * Flow:
 *
 * AndroidVisionFrameRequest
 * -> AndroidVisionFrameSource
 * -> AndroidVisionFrameCaptureResult
 * -> AndroidVisionFramePerceptionResult.
 *
 * This coordinator does not:
 *
 * - parse conversation text;
 * - choose a camera from prose;
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - interpret pixels;
 * - recognize objects or faces;
 * - identify or authenticate a person;
 * - grant authorization;
 * - persist frame bytes;
 * - execute another capability;
 * - verify an effect;
 * - or establish an Outcome.
 *
 * Frame perception
 * != visual understanding
 * != identity
 * != authorization
 * != memory
 * != verified success.
 */
class AndroidVisionFramePerceptionCoordinator(
    private val frameSource: AndroidVisionFrameSource,
) {

    fun perceive(
        request: AndroidVisionFrameRequest,
    ): AndroidVisionFramePerceptionResult {
        return AndroidVisionFramePerceptionResult.fromCapture(
            captureResult =
                frameSource.capture(
                    request = request,
                ),
        )
    }
}
