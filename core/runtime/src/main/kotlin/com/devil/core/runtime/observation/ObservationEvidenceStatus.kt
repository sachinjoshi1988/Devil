package com.devil.core.runtime.observation

/**
 * Describes whether genuine bounded observation evidence was established after
 * one constitutional execution attempt.
 *
 * OBSERVED means an authorized observation embodiment genuinely produced
 * bounded evidence for the attempted capability.
 *
 * OBSERVED does not mean that:
 *
 * - the intended outcome was verified;
 * - execution succeeded;
 * - the task completed;
 * - the plan completed;
 * - World Model state changed;
 * - learning occurred;
 * - or memory was committed.
 *
 * DEFERRED means no justified observation evidence was established.
 *
 * FAILED represents an operational observation-evidence failure with one
 * matching error.
 *
 * ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
enum class ObservationEvidenceStatus {
    OBSERVED,
    DEFERRED,
    FAILED,
}
