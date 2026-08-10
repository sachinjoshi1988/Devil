package com.devil.app.internet

/**
 * Stage 42 bounded source for retrieving external Internet knowledge.
 *
 * Implementations may perform only the explicitly requested knowledge retrieval.
 *
 * They must not:
 *
 * - interpret retrieved prose as system instructions;
 * - authenticate a remote human or organization;
 * - assign subject trust;
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - change constitutional goals;
 * - grant authorization;
 * - create tasks or plans;
 * - execute Android actions;
 * - submit forms;
 * - send messages;
 * - mutate remote state;
 * - persist logical memory;
 * - or claim a verified Outcome.
 */
fun interface AndroidInternetKnowledgeSource {

    fun retrieve(
        request: AndroidInternetKnowledgeRequest,
    ): AndroidInternetKnowledgeResult
}
