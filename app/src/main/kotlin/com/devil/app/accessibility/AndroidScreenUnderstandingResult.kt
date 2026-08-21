package com.devil.app.accessibility

/**
 * Stage 179 bounded Screen Understanding result.
 *
 * AVAILABLE preserves one ordered collection of accessibility-derived elements.
 *
 * Non-available results preserve no screen elements.
 *
 * Screen understanding does not establish user intent, target resolution,
 * execution authority, Observation, Verification, or Outcome.
 */
@ConsistentCopyVisibility
data class AndroidScreenUnderstandingResult private constructor(
    val status: AndroidScreenUnderstandingStatus,
    val elements: List<AndroidScreenElementRecord>,
) {
    companion object {

        fun create(
            status: AndroidScreenUnderstandingStatus,
            elements: List<AndroidScreenElementRecord> = emptyList(),
        ): AndroidScreenUnderstandingResult {
            val preservedElements = elements.toList()

            when (status) {
                AndroidScreenUnderstandingStatus.AVAILABLE -> {
                    require(
                        preservedElements.map { it.position } ==
                            preservedElements.indices.toList(),
                    ) {
                        "Available Android screen-understanding elements must use contiguous zero-based ordered positions."
                    }
                }

                AndroidScreenUnderstandingStatus.SERVICE_UNAVAILABLE,
                AndroidScreenUnderstandingStatus.SCREEN_UNAVAILABLE,
                -> {
                    require(preservedElements.isEmpty()) {
                        "Unavailable Android screen-understanding results must not contain screen elements."
                    }
                }
            }

            return AndroidScreenUnderstandingResult(
                status = status,
                elements = preservedElements,
            )
        }
    }
}
