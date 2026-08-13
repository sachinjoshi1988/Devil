package com.devil.core.model.preference

import com.devil.core.model.common.TraceId

/**
 * One bounded candidate produced only after preference evidence has already
 * qualified through the preference-learning assessment boundary.
 *
 * The candidate preserves:
 *
 * - the exact preference key;
 * - the selected candidate value;
 * - calculated evidence confidence;
 * - supporting-evidence count;
 * - total-evidence count;
 * - supporting constitutional trace identities;
 * - every constitutional trace considered by the assessment.
 *
 * Preserving this candidate does not establish Learning, create Memory Proposal
 * evidence, create a Memory Proposal, invoke Memory Authority, approve Memory,
 * commit Memory, persist Memory, assign memory class, sensitivity, retention
 * policy, storage destination, owner-visible reason, or authorization.
 *
 * PREFERENCE_CANDIDATE != MEMORY.
 */
@ConsistentCopyVisibility
data class PreferenceLearningCandidate private constructor(
    val key: String,
    val value: String,
    val confidence: Double,
    val supportingEvidenceCount: Int,
    val totalEvidenceCount: Int,
    val supportingTraceIds: List<TraceId>,
    val evidenceTraceIds: List<TraceId>,
) {
    companion object {
        fun create(
            key: String,
            value: String,
            confidence: Double,
            supportingEvidenceCount: Int,
            totalEvidenceCount: Int,
            supportingTraceIds: List<TraceId>,
            evidenceTraceIds: List<TraceId>,
        ): PreferenceLearningCandidate {
            val normalizedKey = key.trim()
            val normalizedValue = value.trim()

            require(normalizedKey.isNotEmpty()) {
                "Preference learning candidate requires a nonblank key."
            }

            require(normalizedValue.isNotEmpty()) {
                "Preference learning candidate requires a nonblank value."
            }

            require(
                confidence > 0.5 &&
                    confidence <= 1.0,
            ) {
                "Qualified preference candidate confidence must be greater than 0.5 and at most 1.0."
            }

            require(supportingEvidenceCount >= 2) {
                "Qualified preference candidate requires repeated independent supporting evidence."
            }

            require(
                totalEvidenceCount >= supportingEvidenceCount,
            ) {
                "Preference candidate total evidence cannot be smaller than supporting evidence."
            }

            require(
                supportingTraceIds.size ==
                    supportingEvidenceCount,
            ) {
                "Supporting trace count must match supporting evidence count."
            }

            require(
                evidenceTraceIds.size ==
                    totalEvidenceCount,
            ) {
                "Evidence trace count must match total evidence count."
            }

            require(
                supportingTraceIds.distinct().size ==
                    supportingTraceIds.size,
            ) {
                "Supporting preference evidence traces must be distinct."
            }

            require(
                evidenceTraceIds.distinct().size ==
                    evidenceTraceIds.size,
            ) {
                "Preference evidence traces must be distinct."
            }

            require(
                supportingTraceIds.all {
                    it in evidenceTraceIds
                },
            ) {
                "Every supporting preference trace must belong to the complete evidence trace set."
            }

            return PreferenceLearningCandidate(
                key = normalizedKey,
                value = normalizedValue,
                confidence = confidence,
                supportingEvidenceCount =
                    supportingEvidenceCount,
                totalEvidenceCount =
                    totalEvidenceCount,
                supportingTraceIds =
                    supportingTraceIds.toList(),
                evidenceTraceIds =
                    evidenceTraceIds.toList(),
            )
        }
    }
}
