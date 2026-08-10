package com.devil.app.internet

/**
 * Stage 42 bounded coordinator for external Internet knowledge.
 *
 * Flow:
 *
 * AndroidInternetKnowledgeRequest
 * -> AndroidInternetKnowledgeSource
 * -> AndroidInternetKnowledgeResult.
 *
 * This coordinator is not another Brain, Planner, Executive, Conversation
 * Domain, Security Authority, Memory Authority, or autonomous browser.
 *
 * It performs no semantic reinterpretation of external content.
 *
 * It does not invoke UnifiedDevilRuntime.
 *
 * It does not convert external content into ConversationInput.
 *
 * It does not execute actions or persist retrieved content.
 */
class AndroidInternetKnowledgeCoordinator(
    private val source: AndroidInternetKnowledgeSource,
) {

    fun retrieve(
        request: AndroidInternetKnowledgeRequest,
    ): AndroidInternetKnowledgeResult {
        return source.retrieve(
            request = request,
        )
    }
}
