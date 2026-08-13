package com.devil.core.runtime.verification

/**
 * Describes whether genuine bounded verification evidence was established after
 * one constitutional ObservationResult.
 *
 * VERIFIED means an authorized verification embodiment genuinely produced
 * bounded evidence for the observed capability.
 *
 * VERIFIED does not mean that:
 *
 * - a final constitutional Outcome was established;
 * - the task completed;
 * - the plan completed;
 * - World Model state changed;
 * - learning occurred;
 * - or memory was committed.
 *
 * DEFERRED means no justified verification evidence was established.
 *
 * FAILED represents an operational verification-evidence failure with one
 * matching error.
 *
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
enum class VerificationEvidenceStatus {
    VERIFIED,
    DEFERRED,
    FAILED,
}
