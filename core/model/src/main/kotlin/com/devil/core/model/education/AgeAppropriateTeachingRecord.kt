package com.devil.core.model.education

/**
 * Immutable Stage 145 representation of one bounded Age-Appropriate Teaching
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 144 Guardian Policy Foundation context;
 * - one explicitly supplied nonblank teaching level;
 * - one explicitly supplied nonblank teaching approach;
 * - one explicitly supplied nonblank teaching objective.
 *
 * Stage 145 adapts only bounded education-domain teaching context.
 *
 * It does not:
 *
 * - infer chronological age;
 * - infer developmental maturity;
 * - classify a subject as CHILD;
 * - authenticate a child or guardian;
 * - establish guardian authority;
 * - obtain guardian approval;
 * - replace Stage 44 child/guardian policy;
 * - weaken or bypass Stage 144 guardian-policy provenance;
 * - generate or execute lessons or homework;
 * - grant constitutional authorization;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - or implement Stage 146 Child Privacy Boundary.
 *
 * AGE_APPROPRIATE_TEACHING != AGE_INFERENCE.
 * AGE_APPROPRIATE_TEACHING != CHILD_CLASSIFICATION.
 * AGE_APPROPRIATE_TEACHING != GUARDIAN_APPROVAL.
 * AGE_APPROPRIATE_TEACHING != EDUCATION_DELIVERED.
 */
@ConsistentCopyVisibility
data class AgeAppropriateTeachingRecord private constructor(
    val guardianEducationPolicy: GuardianEducationPolicyRecord,
    val teachingLevel: String,
    val teachingApproach: String,
    val teachingObjective: String,
) {
    companion object {

        fun create(
            guardianEducationPolicy: GuardianEducationPolicyRecord,
            teachingLevel: String,
            teachingApproach: String,
            teachingObjective: String,
        ): AgeAppropriateTeachingRecord {
            val normalizedTeachingLevel =
                teachingLevel.trim()

            val normalizedTeachingApproach =
                teachingApproach.trim()

            val normalizedTeachingObjective =
                teachingObjective.trim()

            require(normalizedTeachingLevel.isNotEmpty()) {
                "Age-Appropriate Teaching level must not be blank."
            }

            require(normalizedTeachingApproach.isNotEmpty()) {
                "Age-Appropriate Teaching approach must not be blank."
            }

            require(normalizedTeachingObjective.isNotEmpty()) {
                "Age-Appropriate Teaching objective must not be blank."
            }

            return AgeAppropriateTeachingRecord(
                guardianEducationPolicy = guardianEducationPolicy,
                teachingLevel = normalizedTeachingLevel,
                teachingApproach = normalizedTeachingApproach,
                teachingObjective = normalizedTeachingObjective,
            )
        }
    }
}
