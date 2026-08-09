package com.devil.app.accessibility

import android.view.accessibility.AccessibilityNodeInfo
import java.util.ArrayDeque
import java.util.IdentityHashMap

/**
 * Stage 38 bounded accessibility-node resolver.
 *
 * Resolution is based only on the explicitly supplied target.
 *
 * It does not infer intent or choose between semantic alternatives.
 *
 * A textual match is not an execution approval.
 *
 * A resolved node is not proof that clicking it will produce the intended
 * effect.
 */
class AndroidAccessibilityNodeResolver {

    fun resolveClickableNode(
        root: AccessibilityNodeInfo,
        target: AndroidAccessibilityTarget,
    ): AccessibilityNodeInfo? {
        val queue =
            ArrayDeque<AccessibilityNodeInfo>()

        val visited =
            IdentityHashMap<AccessibilityNodeInfo, Boolean>()

        queue.add(root)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            if (
                visited.put(
                    node,
                    true,
                ) != null
            ) {
                continue
            }

            if (matches(node, target)) {
                val actionable =
                    firstClickableAncestor(
                        node,
                    )

                if (actionable != null) {
                    return actionable
                }
            }

            for (
                index in
                    0 until node.childCount
            ) {
                val child =
                    node.getChild(index)

                if (child != null) {
                    queue.add(child)
                }
            }
        }

        return null
    }

    internal fun matchesText(
        candidateText: String?,
        target: AndroidAccessibilityTarget,
    ): Boolean {
        if (candidateText.isNullOrBlank()) {
            return false
        }

        return AndroidAccessibilityTarget.normalize(
            candidateText,
        ) == target.normalizedText
    }

    private fun matches(
        node: AccessibilityNodeInfo,
        target: AndroidAccessibilityTarget,
    ): Boolean {
        return matchesText(
            candidateText =
                node.text?.toString(),
            target = target,
        ) ||
            matchesText(
                candidateText =
                    node.contentDescription?.toString(),
                target = target,
            )
    }

    private fun firstClickableAncestor(
        node: AccessibilityNodeInfo,
    ): AccessibilityNodeInfo? {
        var current:
            AccessibilityNodeInfo? =
            node

        while (current != null) {
            if (
                current.isClickable &&
                current.isEnabled
            ) {
                return current
            }

            current = current.parent
        }

        return null
    }
}
