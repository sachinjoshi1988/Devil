package com.devil.core.runtime.education

/**
 * Stage 132 bounded Language Progress & Assessment preparation status.
 *
 * PREPARED means one structurally valid educational assessment context was
 * prepared from an existing Stage 120 Language Education session plus explicitly
 * supplied assessment focus, learner-evidence description, and interpretation.
 *
 * PREPARED does not mean:
 *
 * - constitutional Observation was established;
 * - constitutional Verification occurred;
 * - Outcome was established;
 * - learner mastery was established;
 * - standardized or global proficiency was verified;
 * - curriculum was automatically adapted;
 * - execution occurred;
 * - constitutional Learning occurred;
 * - or Memory was committed.
 *
 * DEFERRED means no truthful Language Progress & Assessment context was produced.
 */
enum class LanguageProgressAssessmentPreparationStatus {
    PREPARED,
    DEFERRED,
}
