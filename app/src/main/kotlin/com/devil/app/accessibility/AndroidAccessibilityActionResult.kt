package com.devil.app.accessibility

/**
 * Preserves the immediate Android-side result of one bounded accessibility
 * platform request.
 *
 * ATTEMPTED contains no success claim. It means only that Android accepted the
 * accessibility node action request.
 *
 * TARGET_NOT_FOUND and SERVICE_UNAVAILABLE are truthful non-attempt states.
 *
 * FAILED preserves one bounded operational error code.
 */
@ConsistentCopyVisibility
data class AndroidAccessibilityActionResult private constructor(
    val status: AndroidAccessibilityActionStatus,
    val errorCode: String?,
) {
    companion object {

        fun attempted(): AndroidAccessibilityActionResult {
            return AndroidAccessibilityActionResult(
                status =
                    AndroidAccessibilityActionStatus.ATTEMPTED,
                errorCode = null,
            )
        }

        fun targetNotFound(): AndroidAccessibilityActionResult {
            return AndroidAccessibilityActionResult(
                status =
                    AndroidAccessibilityActionStatus.TARGET_NOT_FOUND,
                errorCode = null,
            )
        }

        fun serviceUnavailable(): AndroidAccessibilityActionResult {
            return AndroidAccessibilityActionResult(
                status =
                    AndroidAccessibilityActionStatus.SERVICE_UNAVAILABLE,
                errorCode = null,
            )
        }

        fun failed(
            errorCode: String,
        ): AndroidAccessibilityActionResult {
            val normalizedErrorCode =
                errorCode.trim()

            require(normalizedErrorCode.isNotEmpty()) {
                "Android accessibility error code must not be blank."
            }

            return AndroidAccessibilityActionResult(
                status =
                    AndroidAccessibilityActionStatus.FAILED,
                errorCode = normalizedErrorCode,
            )
        }
    }
}
