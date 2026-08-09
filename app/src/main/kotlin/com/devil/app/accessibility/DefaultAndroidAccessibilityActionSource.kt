package com.devil.app.accessibility

import android.view.accessibility.AccessibilityNodeInfo

/**
 * Default Stage 38 Android accessibility action source.
 *
 * The source uses only the currently connected DevilAccessibilityService and
 * one explicitly supplied bounded action request.
 *
 * Stage 38 supports CLICK_VISIBLE_TEXT only.
 *
 * The action source never converts Android accessibility availability into
 * Devil authorization.
 *
 * It reports ATTEMPTED only when AccessibilityNodeInfo.performAction returns
 * true.
 *
 * ATTEMPTED does not mean the intended effect occurred.
 */
class DefaultAndroidAccessibilityActionSource(
    private val serviceProvider:
        () -> DevilAccessibilityService? = {
            DevilAccessibilityServiceRegistry.current()
        },
    private val nodeResolver:
        AndroidAccessibilityNodeResolver =
        AndroidAccessibilityNodeResolver(),
) : AndroidAccessibilityActionSource {

    override fun perform(
        request: AndroidAccessibilityActionRequest,
    ): AndroidAccessibilityActionResult {
        val service =
            serviceProvider()
                ?: return AndroidAccessibilityActionResult
                    .serviceUnavailable()

        val root =
            service.rootInActiveWindow
                ?: return AndroidAccessibilityActionResult
                    .targetNotFound()

        return try {
            when (request.actionType) {
                AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT ->
                    performClick(
                        root = root,
                        target = request.target,
                    )
            }
        } catch (
            throwable: RuntimeException,
        ) {
            AndroidAccessibilityActionResult.failed(
                errorCode =
                    "ANDROID_ACCESSIBILITY_ACTION_FAILED",
            )
        }
    }

    private fun performClick(
        root: AccessibilityNodeInfo,
        target: AndroidAccessibilityTarget,
    ): AndroidAccessibilityActionResult {
        val node =
            nodeResolver.resolveClickableNode(
                root = root,
                target = target,
            )
                ?: return AndroidAccessibilityActionResult
                    .targetNotFound()

        val attempted =
            node.performAction(
                AccessibilityNodeInfo.ACTION_CLICK,
            )

        return if (attempted) {
            AndroidAccessibilityActionResult.attempted()
        } else {
            AndroidAccessibilityActionResult.failed(
                errorCode =
                    "ANDROID_ACCESSIBILITY_CLICK_REJECTED",
            )
        }
    }
}
