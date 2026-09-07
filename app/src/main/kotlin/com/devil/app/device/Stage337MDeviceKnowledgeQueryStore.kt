package com.devil.app.device

import com.devil.core.model.common.TraceId
import java.util.LinkedHashMap

/**
 * Process-local Stage 337M correlation record for bounded Device Knowledge.
 *
 * A record may contain:
 *
 * - only a trace claim, when structured semantics identified Device Knowledge
 *   but no supported Stage 40 query was selected; or
 * - one typed Stage 40 query after exact Device Knowledge capability selection.
 *
 * A null queryType therefore means "this trace belongs to the bounded
 * Device Knowledge response domain, but no supported local fact query was
 * established."
 *
 * This record is correlation evidence only.
 *
 * QUERY_RECORD != CAPABILITY_SELECTION_AUTHORITY.
 * QUERY_RECORD != AUTHORIZATION.
 * QUERY_RECORD != DEVICE_FACT.
 * QUERY_RECORD != EXECUTION.
 * QUERY_RECORD != MEMORY.
 * DEVICE_KNOWLEDGE_CLAIM != DEVICE_FACT.
 * DEVICE_KNOWLEDGE_CLAIM != CAPABILITY_SELECTED.
 */
data class Stage337MDeviceKnowledgeQueryRecord(
    val queryType: AndroidDeviceKnowledgeQueryType?,
)

/**
 * Small process-local one-shot Stage337M Device Knowledge correlation store.
 *
 * No conversation text, model output, Android fact, owner identity,
 * authorization evidence, World Model state, or Memory is stored here.
 */
class Stage337MDeviceKnowledgeQueryStore(
    private val maxEntries: Int = 64,
) {

    init {
        require(maxEntries > 0) {
            "Stage337M Device Knowledge query store capacity must be positive."
        }
    }

    private val records =
        LinkedHashMap<
            TraceId,
            Stage337MDeviceKnowledgeQueryRecord,
        >()

    /**
     * Marks one genuine runtime trace as belonging to the structured bounded
     * Device Knowledge response domain.
     *
     * An already-established typed query is never downgraded to a bare claim.
     */
    @Synchronized
    fun claim(
        traceId: TraceId,
    ) {
        if (records[traceId]?.queryType != null) {
            return
        }

        records[traceId] =
            Stage337MDeviceKnowledgeQueryRecord(
                queryType = null,
            )

        trimToCapacity()
    }

    /**
     * Records one already-selected typed Stage40 query.
     *
     * This upgrades any earlier trace claim for the same TraceId.
     */
    @Synchronized
    fun record(
        traceId: TraceId,
        queryType: AndroidDeviceKnowledgeQueryType,
    ) {
        records[traceId] =
            Stage337MDeviceKnowledgeQueryRecord(
                queryType = queryType,
            )

        trimToCapacity()
    }

    /**
     * Consumes the complete one-shot Stage337M correlation record.
     */
    @Synchronized
    fun consumeRecord(
        traceId: TraceId,
    ): Stage337MDeviceKnowledgeQueryRecord? {
        return records.remove(traceId)
    }

    /**
     * Compatibility helper for callers interested only in an established
     * supported typed query.
     */
    @Synchronized
    fun consume(
        traceId: TraceId,
    ): AndroidDeviceKnowledgeQueryType? {
        return records.remove(traceId)?.queryType
    }

    @Synchronized
    internal fun size(): Int {
        return records.size
    }

    private fun trimToCapacity() {
        while (records.size > maxEntries) {
            val iterator =
                records.entries.iterator()

            if (!iterator.hasNext()) {
                return
            }

            iterator.next()
            iterator.remove()
        }
    }
}
