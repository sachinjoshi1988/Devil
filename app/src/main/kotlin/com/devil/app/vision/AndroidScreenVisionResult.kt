package com.devil.app.vision

import com.devil.app.accessibility.AndroidScreenUnderstandingResult
import com.devil.app.accessibility.AndroidScreenUnderstandingStatus

/**
 * Stage 209 bounded Screen Vision result.
 *
 * AVAILABLE preserves the exact Stage 179 accessibility-derived screen
 * understanding result and the exact Stage 206 image-understanding result.
 *
 * DEFERRED preserves both exact upstream results without claiming integrated
 * screen vision availability.
 *
 * SCREEN_VISION != ACCESSIBILITY_TREE.
 * SCREEN_VISION != OCR.
 * SCREEN_VISION != TARGET_RESOLUTION.
 * SCREEN_VISION != EXECUTION.
 * VISUAL_DESCRIPTION != ACCESSIBILITY_METADATA.
 * SCREEN_ELEMENTS != VERIFIED_PIXELS.
 * SCREEN_VISION != AUTHENTICATION.
 * SCREEN_VISION != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class AndroidScreenVisionResult private constructor(
    val status: AndroidScreenVisionStatus,
    val screenUnderstanding: AndroidScreenUnderstandingResult,
    val imageUnderstanding: AndroidImageUnderstandingResult,
) {
    companion object {
        fun create(
            status: AndroidScreenVisionStatus,
            screenUnderstanding: AndroidScreenUnderstandingResult,
            imageUnderstanding: AndroidImageUnderstandingResult,
        ): AndroidScreenVisionResult {
            when (status) {
                AndroidScreenVisionStatus.AVAILABLE -> {
                    require(
                        screenUnderstanding.status ==
                            AndroidScreenUnderstandingStatus.AVAILABLE,
                    ) {
                        "Available Stage 209 screen vision requires available Stage 179 screen understanding."
                    }

                    require(
                        imageUnderstanding.status ==
                            AndroidImageUnderstandingStatus.UNDERSTOOD,
                    ) {
                        "Available Stage 209 screen vision requires understood Stage 206 image understanding."
                    }
                }

                AndroidScreenVisionStatus.DEFERRED -> Unit
            }

            return AndroidScreenVisionResult(
                status = status,
                screenUnderstanding = screenUnderstanding,
                imageUnderstanding = imageUnderstanding,
            )
        }
    }
}
