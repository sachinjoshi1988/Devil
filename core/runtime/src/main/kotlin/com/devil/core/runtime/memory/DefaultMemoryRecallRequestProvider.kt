package com.devil.core.runtime.memory

import com.devil.core.model.memory.MemoryRecallRequest

/**
 * Default Stage 105 constitutional logical-memory recall request provider.
 *
 * A request is available only when the supplied Stage 104 result is ELIGIBLE and
 * contains its bounded MemoryRecallEligibilityRecord.
 *
 * A deferred Stage 104 result remains unavailable.
 *
 * A failed Stage 104 result preserves its matching upstream error.
 *
 * The provider preserves the exact Stage 104 eligibility record. It introduces no
 * independent MemoryId and therefore cannot redirect an eligibility decision from
 * one logical-memory item to another.
 *
 * This provider performs no logical-memory retrieval or recall.
 *
 * It does not:
 *
 * - read a database;
 * - read a filesystem;
 * - read Android storage;
 * - read cloud storage;
 * - invoke a network service;
 * - expose or disclose logical-memory content;
 * - derive privacy-disclosure permission;
 * - map MemorySensitivity to privacy-disclosure policy;
 * - create, commit, persist, delete, or mutate logical memory;
 * - modify the Unified Devil Runtime;
 * - modify Stage 49 constitutional ordering;
 * - execute an action;
 * - or establish verified success.
 *
 * RECALL_REQUEST_AVAILABLE != MEMORY_RECALL.
 * RECALL_REQUEST_AVAILABLE != STORAGE_READ.
 * RECALL_REQUEST_AVAILABLE != DISCLOSURE_PERMISSION.
 */
class DefaultMemoryRecallRequestProvider :
    MemoryRecallRequestProvider {

    override fun provide(
        eligibility: MemoryRecallEligibilityResult,
    ): MemoryRecallRequestResult {
        return when (eligibility.status) {
            MemoryRecallEligibilityStatus.ELIGIBLE ->
                MemoryRecallRequestResult.create(
                    traceId = eligibility.traceId,
                    status =
                        MemoryRecallRequestStatus.AVAILABLE,
                    request =
                        MemoryRecallRequest.create(
                            traceId = eligibility.traceId,
                            eligibility =
                                requireNotNull(
                                    eligibility.record,
                                ),
                        ),
                )

            MemoryRecallEligibilityStatus.DEFERRED ->
                MemoryRecallRequestResult.create(
                    traceId = eligibility.traceId,
                    status =
                        MemoryRecallRequestStatus.UNAVAILABLE,
                )

            MemoryRecallEligibilityStatus.FAILED ->
                MemoryRecallRequestResult.create(
                    traceId = eligibility.traceId,
                    status =
                        MemoryRecallRequestStatus.FAILED,
                    error =
                        requireNotNull(
                            eligibility.error,
                        ),
                )
        }
    }
}
