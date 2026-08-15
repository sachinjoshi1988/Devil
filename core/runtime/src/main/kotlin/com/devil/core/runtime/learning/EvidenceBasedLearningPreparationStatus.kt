package com.devil.core.runtime.learning

/**
 * Stage 92 bounded Evidence-Based Learning V2 preparation status.
 *
 * PREPARED means one structurally valid EvidenceBasedLearningRecord was created
 * from one existing evidence-backed WorldModelRepresentation and one explicitly
 * supplied bounded proposition.
 *
 * PREPARED does not mean:
 *
 * - constitutional Learning occurred;
 * - the proposition became truth;
 * - new evidence was established;
 * - the World Model changed;
 * - Failure Learning occurred;
 * - strategy changed;
 * - a Decision changed;
 * - authorization changed;
 * - execution occurred;
 * - Controlled Autonomy was granted;
 * - a Memory Proposal exists;
 * - Memory Authority approved anything;
 * - Memory was committed;
 * - or learning was persisted.
 *
 * DEFERRED means no truthful bounded Evidence-Based Learning V2 record was
 * produced.
 *
 * PREPARED != LEARNED.
 * PREPARED != MEMORY_PROPOSED.
 * PREPARED != MEMORY_COMMITTED.
 * PREPARED != STRATEGY_ADAPTED.
 * PREPARED != AUTONOMY_GRANTED.
 */
enum class EvidenceBasedLearningPreparationStatus {
    PREPARED,
    DEFERRED,
}
