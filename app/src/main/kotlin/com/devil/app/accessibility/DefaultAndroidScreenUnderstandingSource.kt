package com.devil.app.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import java.util.ArrayDeque
import java.util.IdentityHashMap

/**
 * Default Stage 179 Android accessibility-derived screen-understanding source.
 *
 * This source reads only the currently connected DevilAccessibilityService and
 * its active accessibility root.
 *
 * Traversal is bounded and preserves only explicit node text and content
 * description metadata.
 *
 * It does not:
 *
 * - infer user intent or semantic meaning;
 * - resolve an execution target;
 * - click, scroll, gesture, or perform another accessibility action;
 * - grant Devil authorization;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 180 Reliable Target Resolution.
 *
 * SCREEN_METADATA != USER_INTENT.
 * SCREEN_ELEMENT != EXECUTION_TARGET.
 * ACCESSIBILITY_TREE != OBSERVATION_PROOF.
 */
class DefaultAndroidScreenUnderstandingSource(
    private val serviceProvider: () -> DevilAccessibilityService? = {
        DevilAccessibilityServiceRegistry.current()
    },
) : AndroidScreenUnderstandingSource {

    override fun inspect(): AndroidScreenUnderstandingResult {
        val service =
            serviceProvider()
                ?: return AndroidScreenUnderstandingResult.create(
                    status =
                        AndroidScreenUnderstandingStatus
                            .SERVICE_UNAVAILABLE,
                )

        val root =
            service.rootInActiveWindow
                ?: return AndroidScreenUnderstandingResult.create(
                    status =
                        AndroidScreenUnderstandingStatus
                            .SCREEN_UNAVAILABLE,
                )

        return AndroidScreenUnderstandingResult.create(
            status = AndroidScreenUnderstandingStatus.AVAILABLE,
            elements = inspectRoot(root),
        )
    }

    private fun inspectRoot(
        root: AccessibilityNodeInfo,
    ): List<AndroidScreenElementRecord> {
        val queue =
            ArrayDeque<AccessibilityNodeInfo>()

        val visited =
            IdentityHashMap<AccessibilityNodeInfo, Boolean>()

        val traversalBudget =
            AndroidAccessibilityTraversalBudget()

        val elements =
            mutableListOf<AndroidScreenElementRecord>()

        queue.add(root)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            if (visited.put(node, true) != null) {
                continue
            }

            if (!traversalBudget.tryAcquireNodeInspection()) {
                break
            }

            val text =
                node.text
                    ?.toString()
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            val contentDescription =
                node.contentDescription
                    ?.toString()
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            if (
                text != null ||
                contentDescription != null
            ) {
                elements.add(
                    AndroidScreenElementRecord.create(
                        position = elements.size,
                        text = text,
                        contentDescription =
                            contentDescription,
                    ),
                )
            }

            for (index in 0 until node.childCount) {
                val child = node.getChild(index)

                if (child != null) {
                    queue.add(child)
                }
            }
        }

        return elements.toList()
    }
}
