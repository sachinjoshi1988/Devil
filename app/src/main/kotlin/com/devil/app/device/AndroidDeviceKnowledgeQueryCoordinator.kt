package com.devil.app.device

/**
 * Stage 40 coordinator for explicit bounded Device Knowledge queries.
 *
 * Flow:
 *
 * AndroidDeviceKnowledgeQuery
 * -> AndroidDeviceKnowledgeSource
 * -> AndroidDeviceKnowledgeSnapshot
 * -> AndroidDeviceKnowledgeQueryPolicy
 * -> AndroidDeviceKnowledgeResult.
 *
 * This coordinator is not a Brain, Planner, Conversation Domain, Security
 * Authority, Memory Authority, or execution mechanism.
 *
 * It does not invoke UnifiedDevilRuntime and does not infer a query from
 * conversation text.
 */
class AndroidDeviceKnowledgeQueryCoordinator(
    private val source: AndroidDeviceKnowledgeSource =
        DefaultAndroidDeviceKnowledgeSource(),
    private val policy: AndroidDeviceKnowledgeQueryPolicy =
        AndroidDeviceKnowledgeQueryPolicy(),
) {

    fun query(
        request: AndroidDeviceKnowledgeQuery,
    ): AndroidDeviceKnowledgeResult {
        val snapshot =
            source.snapshot()

        return policy.evaluate(
            query = request,
            snapshot = snapshot,
        )
    }
}
