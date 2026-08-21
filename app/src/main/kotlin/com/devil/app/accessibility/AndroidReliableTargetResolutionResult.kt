package com.devil.app.accessibility

/**
 * Stage 180 bounded Reliable Target Resolution result.
 *
 * A RESOLVED result preserves exactly one Stage 179 screen element.
 *
 * NOT_FOUND and SCREEN_UNAVAILABLE preserve no resolved element.
 *
 * This contract does not perform Android actions and does not establish that
 * resolved screen metadata corresponds to a currently actionable accessibility node.
 *
 * RESOLVED_SCREEN_METADATA != ACTIONABLE_NODE.
 * TARGET_RESOLUTION != EXECUTION.
 */
@ConsistentCopyVisibility
data class AndroidReliableTargetResolutionResult private constructor(
    val status: AndroidReliableTargetResolutionStatus,
    val screenUnderstanding: AndroidScreenUnderstandingResult,
    val target: AndroidAccessibilityTarget,
    val resolvedElement: AndroidScreenElementRecord?,
) {
    companion object {
        fun create(
            status: AndroidReliableTargetResolutionStatus,
            screenUnderstanding: AndroidScreenUnderstandingResult,
            target: AndroidAccessibilityTarget,
            resolvedElement: AndroidScreenElementRecord? = null,
        ): AndroidReliableTargetResolutionResult {
            when (status) {
                AndroidReliableTargetResolutionStatus.RESOLVED ->
                    require(resolvedElement != null) {
                        "Resolved Android target-resolution results require one resolved screen element."
                    }

                AndroidReliableTargetResolutionStatus.NOT_FOUND,
                AndroidReliableTargetResolutionStatus.SCREEN_UNAVAILABLE,
                ->
                    require(resolvedElement == null) {
                        "Non-resolved Android target-resolution results must not contain a resolved screen element."
                    }
            }

            if (status == AndroidReliableTargetResolutionStatus.RESOLVED) {
                require(
                    screenUnderstanding.status ==
                        AndroidScreenUnderstandingStatus.AVAILABLE,
                ) {
                    "Resolved Android target-resolution results require available screen understanding."
                }

                require(
                    screenUnderstanding.elements.any {
                        it == resolvedElement
                    },
                ) {
                    "Resolved Android target-resolution element must originate from the supplied Stage 179 screen understanding."
                }
            }

            if (
                status ==
                AndroidReliableTargetResolutionStatus.SCREEN_UNAVAILABLE
            ) {
                require(
                    screenUnderstanding.status !=
                        AndroidScreenUnderstandingStatus.AVAILABLE,
                ) {
                    "Screen-unavailable target resolution requires unavailable Stage 179 screen understanding."
                }
            }

            return AndroidReliableTargetResolutionResult(
                status = status,
                screenUnderstanding = screenUnderstanding,
                target = target,
                resolvedElement = resolvedElement,
            )
        }
    }
}
