package com.devil.core.runtime.memory

/**
 * Stage 104 bounded status for constitutional logical-memory recall eligibility.
 *
 * ELIGIBLE means one MemoryRecallEligibilityRecord was formed only after:
 *
 * - Stage 103 memory continuity was ESTABLISHED;
 * - constitutional authorization was explicitly AUTHORIZED;
 * - Stage 100 owner / multi-user context was ESTABLISHED;
 * - every supplied result used the same constitutional trace identity;
 * - and the preserved memory subject matched the established current subject.
 *
 * DEFERRED means those bounded prerequisites are not currently established.
 *
 * FAILED preserves one genuine matching upstream failure.
 *
 * ELIGIBLE does not mean logical memory was retrieved, read from storage,
 * exposed, disclosed, recalled, persisted, deleted, synchronized, replicated,
 * decrypted, or otherwise made available to a caller.
 *
 * RECALL_ELIGIBLE != MEMORY_RECALL.
 * RECALL_ELIGIBLE != STORAGE_READ.
 * RECALL_ELIGIBLE != DISCLOSURE_PERMISSION.
 * AUTHORIZATION != PRIVACY_DISCLOSURE_PERMISSION.
 */
enum class MemoryRecallEligibilityStatus {
    ELIGIBLE,
    DEFERRED,
    FAILED,
}
