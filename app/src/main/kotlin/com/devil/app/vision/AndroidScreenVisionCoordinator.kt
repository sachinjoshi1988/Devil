package com.devil.app.vision

import com.devil.app.accessibility.AndroidScreenUnderstandingResult
import com.devil.app.accessibility.AndroidScreenUnderstandingStatus

/**
 * Stage 209 bounded Screen Vision coordinator.
 *
 * It integrates one exact Stage 179 accessibility-derived Screen Understanding
 * result with one exact Stage 206 Image Understanding result.
 *
 * It does not:
 *
 * - inspect the live accessibility tree;
 * - capture a screenshot or camera frame;
 * - perform OCR;
 * - reconcile accessibility metadata with visual pixels;
 * - establish that visual description and accessibility metadata agree;
 * - resolve an actionable target;
 * - click, scroll, gesture, or execute another accessibility action;
 * - identify or authenticate a person;
 * - create Memory;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 210.
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
class AndroidScreenVisionCoordinator {

    fun integrate(
        screenUnderstanding: AndroidScreenUnderstandingResult,
        imageUnderstanding: AndroidImageUnderstandingResult,
    ): AndroidScreenVisionResult {
        val status =
            if (
                screenUnderstanding.status ==
                    AndroidScreenUnderstandingStatus.AVAILABLE &&
                imageUnderstanding.status ==
                    AndroidImageUnderstandingStatus.UNDERSTOOD
            ) {
                AndroidScreenVisionStatus.AVAILABLE
            } else {
                AndroidScreenVisionStatus.DEFERRED
            }

        return AndroidScreenVisionResult.create(
            status = status,
            screenUnderstanding = screenUnderstanding,
            imageUnderstanding = imageUnderstanding,
        )
    }
}
