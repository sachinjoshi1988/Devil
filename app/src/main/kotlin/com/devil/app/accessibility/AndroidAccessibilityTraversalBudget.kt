package com.devil.app.accessibility

/**
 * Bounds one accessibility-node resolution traversal.
 *
 * The budget limits only the number of unique nodes that may be inspected
 * during one explicitly requested accessibility resolution attempt.
 *
 * Exhausting this budget grants no authority and establishes no execution,
 * observation, verification, or outcome. The resolver truthfully returns no
 * resolved node when another unique node cannot be inspected within the
 * configured bound.
 */
internal class AndroidAccessibilityTraversalBudget(
    private val maxVisitedNodes: Int =
        MAX_VISITED_NODES,
) {

    private var visitedNodeCount: Int = 0

    init {
        require(maxVisitedNodes > 0) {
            "Accessibility traversal budget must be positive."
        }
    }

    fun tryAcquireNodeInspection(): Boolean {
        if (visitedNodeCount >= maxVisitedNodes) {
            return false
        }

        visitedNodeCount += 1
        return true
    }

    companion object {
        internal const val MAX_VISITED_NODES: Int =
            1_024
    }
}
