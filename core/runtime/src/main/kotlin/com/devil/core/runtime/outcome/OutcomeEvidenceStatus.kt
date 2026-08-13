package com.devil.core.runtime.outcome

/**
 * Describes whether genuine bounded constitutional outcome evidence was
 * established after verification.
 *
 * ESTABLISHED means an authorized outcome embodiment genuinely produced bounded
 * evidence supporting one constitutional outcome determination.
 *
 * ESTABLISHED does not mean that:
 *
 * - the task completed;
 * - the plan completed;
 * - World Model state changed;
 * - learning occurred;
 * - memory was committed;
 * - or memory was persisted.
 *
 * DEFERRED means no justified outcome evidence was established.
 *
 * FAILED represents an operational outcome-evidence failure with one matching
 * error.
 *
 * VERIFIED != OUTCOME_EVIDENCE.
 * OUTCOME_EVIDENCE != OUTCOME.
 * OUTCOME != COMPLETED.
 */
enum class OutcomeEvidenceStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
