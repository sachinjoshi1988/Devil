package com.devil.app.vision

/**
 * Stage 41 bounded source for acquiring one Android visual frame.
 *
 * Implementations may approach Android camera APIs only for the explicit
 * requested camera identity.
 *
 * Implementations must not:
 *
 * - infer a camera target from conversation text;
 * - interpret pixels;
 * - perform face recognition;
 * - authenticate a subject;
 * - grant authorization;
 * - invoke UnifiedDevilRuntime;
 * - persist the frame;
 * - or establish successful task completion.
 */
fun interface AndroidVisionFrameSource {

    fun capture(
        request: AndroidVisionFrameRequest,
    ): AndroidVisionFrameCaptureResult
}
