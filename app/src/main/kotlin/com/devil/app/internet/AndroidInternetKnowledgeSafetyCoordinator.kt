package com.devil.app.internet

/**
 * Stage 42 coordinator for bounded Internet retrieval followed by structural
 * external-content safety assessment.
 *
 * Flow:
 *
 * AndroidInternetKnowledgeRequest
 * -> AndroidInternetKnowledgeCoordinator
 * -> AndroidInternetKnowledgeResult
 * -> AndroidInternetKnowledgeSafetyPolicy
 * -> AndroidInternetKnowledgeSafetyResult.
 *
 * This coordinator does not:
 *
 * - create another Brain;
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - infer user intent;
 * - authenticate an Internet source;
 * - treat external prose as instructions;
 * - grant trust;
 * - grant authorization;
 * - persist external content;
 * - create memory;
 * - execute an action;
 * - or establish a verified Outcome.
 *
 * External content remains data.
 */
class AndroidInternetKnowledgeSafetyCoordinator(
    private val knowledgeCoordinator:
        AndroidInternetKnowledgeCoordinator,
    private val safetyPolicy:
        AndroidInternetKnowledgeSafetyPolicy =
        AndroidInternetKnowledgeSafetyPolicy(),
) {

    fun retrieveAndAssess(
        request: AndroidInternetKnowledgeRequest,
    ): AndroidInternetKnowledgeSafetyResult {
        val retrievalResult =
            knowledgeCoordinator.retrieve(
                request = request,
            )

        return safetyPolicy.evaluate(
            request = request,
            retrievalResult = retrievalResult,
        )
    }
}
