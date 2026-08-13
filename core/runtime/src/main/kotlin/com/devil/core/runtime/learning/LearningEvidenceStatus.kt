package com.devil.core.runtime.learning

/**
 * Describes whether genuine bounded constitutional Learning evidence was
 * established after one constitutional World Model update result.
 *
 * ESTABLISHED means an authorized learning-evidence mechanism genuinely
 * produced bounded evidence supporting one possible constitutional Learning
 * evaluation.
 *
 * ESTABLISHED does not mean that:
 *
 * - learning occurred;
 * - Memory was proposed;
 * - Memory Authority approved anything;
 * - Memory was committed;
 * - Memory was persisted;
 * - the task completed;
 * - or the plan completed.
 *
 * DEFERRED means no justified Learning evidence was established.
 *
 * FAILED represents an operational Learning-evidence failure with one matching
 * error.
 *
 * WORLD_MODEL_UPDATE != LEARNING_EVIDENCE.
 * LEARNING_EVIDENCE != LEARNING.
 * LEARNING != MEMORY_PROPOSAL.
 */
enum class LearningEvidenceStatus {
    ESTABLISHED,
    DEFERRED,
    FAILED,
}
