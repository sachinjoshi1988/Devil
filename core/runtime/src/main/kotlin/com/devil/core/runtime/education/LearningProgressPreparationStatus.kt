package com.devil.core.runtime.education

/**
 * Stage 149 bounded Learning Progress preparation status.
 *
 * PREPARED means one structurally valid LearningProgressRecord was prepared
 * from an existing Stage 148 Study Companion context and explicitly supplied
 * progress evidence metadata.
 *
 * PREPARED does not mean:
 *
 * - learner evidence was independently observed;
 * - mastery was verified;
 * - global proficiency was established;
 * - constitutional Verification occurred;
 * - a verified Outcome exists;
 * - progress was persisted;
 * - constitutional Learning occurred;
 * - or Stage 150 Guardian Learning Summary was produced.
 *
 * DEFERRED means no truthful Learning Progress context was produced.
 */
enum class LearningProgressPreparationStatus {
    PREPARED,
    DEFERRED,
}
