package com.devil.core.runtime.conversation

/**
 * Default Stage 25 bounded conversation-record authority coordinator.
 *
 * This authority:
 *
 * - obtains one bounded ConversationRecordRequest,
 * - obtains one genuine ConversationId when an approved mechanism provides one,
 * - delegates bounded ConversationRecord formation to ConversationRecordResolver,
 * - and returns one stable ConversationRecordResult.
 *
 * The default conversation identity provider remains truthfully unavailable until
 * an approved production conversation-identity mechanism exists. Therefore the
 * default authority currently defers rather than fabricating conversation identity.
 *
 * This coordinator does not reinterpret conversation-intake state, decide whether
 * constitutional processing may continue, generate conversation identity, establish
 * multi-turn ordering, persist or restore conversation state, create Android
 * presentation state, create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
class DefaultConversationRecordAuthority(
    private val requestProvider: ConversationRecordRequestProvider =
        DefaultConversationRecordRequestProvider(),
    private val identityProvider: ConversationIdentityProvider =
        DefaultConversationIdentityProvider(),
    private val resolver: ConversationRecordResolver =
        DefaultConversationRecordResolver(),
) : ConversationRecordAuthority {

    override fun record(
        conversationIntake: ConversationIntakeAuthorityResult,
    ): ConversationRecordResult {
        val requestResult =
            requestProvider.provide(
                conversationIntake = conversationIntake,
            )

        require(
            requestResult.traceId == conversationIntake.traceId,
        ) {
            "Conversation intake and conversation-record request result must use the same trace identity."
        }

        return when (requestResult.status) {
            ConversationRecordRequestStatus.AVAILABLE -> {
                val request =
                    requireNotNull(
                        requestResult.request,
                    )

                val identityResult =
                    identityProvider.provide(
                        traceId = requestResult.traceId,
                        input = request.intake.record.input,
                    )

                require(
                    identityResult.traceId == requestResult.traceId,
                ) {
                    "Conversation-record request and conversation identity result must use the same trace identity."
                }

                when (identityResult.status) {
                    ConversationIdentityProvisionStatus.AVAILABLE -> {
                        val record =
                            resolver.create(
                                intake = request.intake,
                                conversationId =
                                    requireNotNull(
                                        identityResult.conversationId,
                                    ),
                            )

                        require(
                            record.intake.record.input.context.traceId ==
                                requestResult.traceId,
                        ) {
                            "Conversation-record request and resolved record must use the same trace identity."
                        }

                        ConversationRecordResult.create(
                            traceId = requestResult.traceId,
                            status = ConversationRecordStatus.PRODUCED,
                            record = record,
                        )
                    }

                    ConversationIdentityProvisionStatus.UNAVAILABLE ->
                        ConversationRecordResult.create(
                            traceId = requestResult.traceId,
                            status = ConversationRecordStatus.DEFERRED,
                        )

                    ConversationIdentityProvisionStatus.FAILED ->
                        ConversationRecordResult.create(
                            traceId = requestResult.traceId,
                            status = ConversationRecordStatus.FAILED,
                            error =
                                requireNotNull(
                                    identityResult.error,
                                ),
                        )
                }
            }

            ConversationRecordRequestStatus.UNAVAILABLE ->
                ConversationRecordResult.create(
                    traceId = requestResult.traceId,
                    status = ConversationRecordStatus.DEFERRED,
                )

            ConversationRecordRequestStatus.FAILED ->
                ConversationRecordResult.create(
                    traceId = requestResult.traceId,
                    status = ConversationRecordStatus.FAILED,
                    error =
                        requireNotNull(
                            requestResult.error,
                        ),
                )
        }
    }
}
