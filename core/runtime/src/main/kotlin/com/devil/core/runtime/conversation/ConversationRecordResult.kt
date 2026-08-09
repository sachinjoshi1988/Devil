package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationRecord
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the stable operational result of bounded conversation-record
 * formation.
 *
 * A produced result contains one ConversationRecord whose preserved intake
 * belongs to the same constitutional trace.
 *
 * A deferred result contains neither record nor error.
 *
 * A failed result contains one matching error and no record.
 *
 * Producing this result does not persist, restore, or durably store
 * conversation state. It does not establish multi-turn ordering, create
 * conversation identity, create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
@ConsistentCopyVisibility
data class ConversationRecordResult private constructor(
    val traceId: TraceId,
    val status: ConversationRecordStatus,
    val record: ConversationRecord?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationRecordStatus,
            record: ConversationRecord? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationRecordResult {
            when (status) {
                ConversationRecordStatus.PRODUCED -> {
                    require(record != null && error == null) {
                        "Produced conversation-record results require a record and must not contain an error."
                    }
                }

                ConversationRecordStatus.DEFERRED -> {
                    require(record == null && error == null) {
                        "Deferred conversation-record results must not contain a record or error."
                    }
                }

                ConversationRecordStatus.FAILED -> {
                    require(record == null && error != null) {
                        "Failed conversation-record results require an error and must not contain a record."
                    }
                }
            }

            require(
                record == null ||
                    record.intake.record.input.context.traceId == traceId,
            ) {
                "Conversation-record result and record must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Conversation-record result and error must use the same trace identity."
            }

            return ConversationRecordResult(
                traceId = traceId,
                status = status,
                record = record,
                error = error,
            )
        }
    }
}
