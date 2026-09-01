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
 * Stage 314 adds debug-only-at-runtime diagnostic recording around the existing
 * execution decisions. Diagnostic recording does not alter execution behavior.
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
                ?: run {
                    Stage314AccessibilityExecutionDiagnosticRecorder.record(
                        context = service,
                        event = "ROOT_UNAVAILABLE",
                        target = request.target,
                    )

                    return AndroidAccessibilityActionResult
                        .targetNotFound()
                }

        return try {
            when (request.actionType) {
                AndroidAccessibilityActionType.CLICK_VISIBLE_TEXT ->
                    performClick(
                        context = service,
                        root = root,
                        target = request.target,
                    )
            }
        } catch (
            throwable: RuntimeException,
        ) {
            Stage314AccessibilityExecutionDiagnosticRecorder.record(
                context = service,
                event = "ACTION_FAILED",
                target = request.target,
            )

            AndroidAccessibilityActionResult.failed(
                errorCode =
                    "ANDROID_ACCESSIBILITY_ACTION_FAILED",
            )
        }
    }

    private fun performClick(
        context: DevilAccessibilityService,
        root: AccessibilityNodeInfo,
        target: AndroidAccessibilityTarget,
    ): AndroidAccessibilityActionResult {
        val node =
            nodeResolver.resolveClickableNode(
                root = root,
                target = target,
            )
                ?: run {
                    Stage314AccessibilityExecutionDiagnosticRecorder.record(
                        context = context,
                        screenUnderstanding =
                            DefaultAndroidScreenUnderstandingSource(
                                serviceProvider = { context },
                            ).inspect(),
                        event = "TARGET_NOT_FOUND",
                        target = target,
                    )

                    return AndroidAccessibilityActionResult
                        .targetNotFound()
                }

        val attempted =
            node.performAction(
                AccessibilityNodeInfo.ACTION_CLICK,
            )

        return if (attempted) {
            Stage314AccessibilityExecutionDiagnosticRecorder.record(
                context = context,
                event = "CLICK_ATTEMPTED",
                target = target,
            )

            AndroidAccessibilityActionResult.attempted()
        } else {
            Stage314AccessibilityExecutionDiagnosticRecorder.record(
                context = context,
                event = "CLICK_REJECTED",
                target = target,
            )

            AndroidAccessibilityActionResult.failed(
                errorCode =
                    "ANDROID_ACCESSIBILITY_CLICK_REJECTED",
            )
        }
    }
}
