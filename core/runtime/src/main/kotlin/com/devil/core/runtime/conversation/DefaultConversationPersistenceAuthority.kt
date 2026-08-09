package com.devil.core.runtime.conversation

/**
 * Default Stage 25 constitutional conversation-persistence coordinator.
 *
 * This authority:
 *
 * - prepares one bounded ConversationPersistenceRequest when a conversation
 *   record exists,
 * - delegates constitutional persistence evaluation,
 * - and maps the evaluation into the stable ConversationPersistenceResult.
 *
 * The default evaluator remains truthfully unavailable until an approved
 * production conversation-persistence policy and durable mechanism exist.
 * Therefore the default authority currently defers instead of fabricating
 * persistence eligibility.
 *
 * This coordinator performs no database, filesystem, cloud, Android-platform,
 * network, or external-communication operation.
 *
 * It does not persist, restore, durably store, order, replicate, encrypt,
 * delete, expose, or recall conversation state and does not create logical
 * memory.
 */
class DefaultConversationPersistenceAuthority(
    private val requestProvider: ConversationPersistenceRequestProvider =
        DefaultConversationPersistenceRequestProvider(),
    private val evaluator: ConversationPersistenceEvaluator =
        DefaultConversationPersistenceEvaluator(),
    private val resultMapper: ConversationPersistenceResultMapper =
        DefaultConversationPersistenceResultMapper(),
) : ConversationPersistenceAuthority {

    override fun evaluatePersistence(
        conversationRecord: ConversationRecordResult,
    ): ConversationPersistenceResult {
        val requestResult =
            requestProvider.provide(
                conversationRecord = conversationRecord,
            )

        require(
            requestResult.traceId == conversationRecord.traceId,
        ) {
            "Conversation record and conversation persistence request result must use the same trace identity."
        }

        return when (requestResult.status) {
            ConversationPersistenceRequestStatus.AVAILABLE -> {
                val evaluation =
                    evaluator.evaluate(
                        traceId = requestResult.traceId,
                        request =
                            requireNotNull(
                                requestResult.request,
                            ),
                    )

                require(
                    evaluation.traceId == requestResult.traceId,
                ) {
                    "Conversation persistence request and evaluation result must use the same trace identity."
                }

                val result =
                    resultMapper.map(
                        traceId = requestResult.traceId,
                        evaluation = evaluation,
                    )

                require(
                    result.traceId == requestResult.traceId,
                ) {
                    "Conversation persistence request and mapped persistence result must use the same trace identity."
                }

                result
            }

            ConversationPersistenceRequestStatus.UNAVAILABLE ->
                ConversationPersistenceResult.create(
                    traceId = requestResult.traceId,
                    status = ConversationPersistenceStatus.DEFERRED,
                )

            ConversationPersistenceRequestStatus.FAILED ->
                ConversationPersistenceResult.create(
                    traceId = requestResult.traceId,
                    status = ConversationPersistenceStatus.FAILED,
                    error =
                        requireNotNull(
                            requestResult.error,
                        ),
                )
        }
    }
}
