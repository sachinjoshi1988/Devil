package com.devil.core.runtime.education

/**
 * Stage 150 bounded Guardian Learning Summary preparation status.
 *
 * PREPARED means one structurally valid guardian-facing Education Domain
 * summary context was prepared.
 *
 * It does not mean:
 *
 * - a guardian was authenticated;
 * - guardian authority was established;
 * - disclosure was authorized or occurred;
 * - learner mastery was verified;
 * - a summary was transmitted;
 * - Memory was persisted;
 * - or execution occurred.
 *
 * DEFERRED means no truthful Guardian Learning Summary context was produced.
 */
enum class GuardianLearningSummaryPreparationStatus {
    PREPARED,
    DEFERRED,
}
