package com.devil.core.runtime.education

/**
 * Stage 131 bounded Adaptive Language Curriculum preparation status.
 *
 * PREPARED means one structurally valid curriculum-preparation context was
 * produced from an existing Stage 120 Language Education session plus explicitly
 * supplied curriculum focus and adaptation rationale.
 *
 * PREPARED does not mean:
 *
 * - learner proficiency was inferred;
 * - progress or mastery was assessed;
 * - curriculum was adapted from verified learner evidence;
 * - lessons were generated;
 * - curriculum was executed;
 * - Strategy Adaptation occurred;
 * - execution occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful adaptive-curriculum context was produced.
 */
enum class AdaptiveLanguageCurriculumPreparationStatus {
    PREPARED,
    DEFERRED,
}
