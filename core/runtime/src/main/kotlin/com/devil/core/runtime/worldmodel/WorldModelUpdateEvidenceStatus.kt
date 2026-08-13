package com.devil.core.runtime.worldmodel

/**
 * Describes whether genuine bounded constitutional World Model update evidence
 * was established after one constitutional Outcome.
 *
 * ESTABLISHED means an authorized World Model evidence mechanism genuinely
 * produced bounded evidence supporting one possible constitutional World Model
 * update evaluation.
 *
 * ESTABLISHED does not mean that:
 *
 * - the World Model changed;
 * - the task completed;
 * - the plan completed;
 * - learning occurred;
 * - memory was proposed;
 * - memory was committed;
 * - or memory was persisted.
 *
 * DEFERRED means no justified World Model update evidence was established.
 *
 * FAILED represents an operational World Model update-evidence failure with one
 * matching error.
 *
 * OUTCOME != WORLD_MODEL_UPDATE_EVIDENCE.
 * WORLD_MODEL_UPDATE_EVIDENCE != WORLD_MODEL_UPDATE.
 * WORLD_MODEL_UPDATE != WORLD_STATE_CHANGED.
 */
enum class WorldModelUpdateEvidenceStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
