package com.devil.app.accessibility

/**
 * Stage 180 bounded Reliable Target Resolution coordinator.
 *
 * The coordinator compares one explicit accessibility target against one exact
 * Stage 179 Screen Understanding result.
 *
 * Matching uses the existing AndroidAccessibilityTarget normalization semantics.
 *
 * The coordinator resolves only when exactly one Stage 179 element matches.
 * Zero matches and ambiguous multiple matches remain unresolved.
 *
 * It does not:
 *
 * - inspect a live AccessibilityNodeInfo tree;
 * - click, scroll, gesture, or perform another Android action;
 * - infer user intent;
 * - establish node clickability or actionability;
 * - grant Devil authorization;
 * - create an ExecutionRequest;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 181 Touch & Gesture Execution.
 *
 * RESOLVED_SCREEN_METADATA != ACTIONABLE_NODE.
 * TARGET_RESOLUTION != EXECUTION.
 */
class AndroidReliableTargetResolutionCoordinator {

    fun resolve(
        screenUnderstanding: AndroidScreenUnderstandingResult,
        target: AndroidAccessibilityTarget,
    ): AndroidReliableTargetResolutionResult {
        if (
            screenUnderstanding.status !=
            AndroidScreenUnderstandingStatus.AVAILABLE
        ) {
            return AndroidReliableTargetResolutionResult.create(
                status =
                    AndroidReliableTargetResolutionStatus
                        .SCREEN_UNAVAILABLE,
                screenUnderstanding = screenUnderstanding,
                target = target,
            )
        }

        val matches =
            screenUnderstanding.elements.filter { element ->
                matches(
                    candidate = element.text,
                    target = target,
                ) ||
                    matches(
                        candidate = element.contentDescription,
                        target = target,
                    )
            }

        val resolvedElement =
            matches.singleOrNull()

        return if (resolvedElement != null) {
            AndroidReliableTargetResolutionResult.create(
                status =
                    AndroidReliableTargetResolutionStatus.RESOLVED,
                screenUnderstanding = screenUnderstanding,
                target = target,
                resolvedElement = resolvedElement,
            )
        } else {
            AndroidReliableTargetResolutionResult.create(
                status =
                    AndroidReliableTargetResolutionStatus.NOT_FOUND,
                screenUnderstanding = screenUnderstanding,
                target = target,
            )
        }
    }

    private fun matches(
        candidate: String?,
        target: AndroidAccessibilityTarget,
    ): Boolean {
        if (candidate.isNullOrBlank()) {
            return false
        }

        return AndroidAccessibilityTarget.normalize(candidate) ==
            target.normalizedText
    }
}
