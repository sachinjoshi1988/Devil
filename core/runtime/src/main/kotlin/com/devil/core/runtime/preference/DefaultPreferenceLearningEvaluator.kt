package com.devil.core.runtime.preference

import com.devil.core.model.preference.PreferenceEvidenceSet

/**
 * Deterministic bounded preference-evidence evaluator.
 *
 * Evidence is counted by candidate value. Because PreferenceEvidenceSet already
 * requires distinct trace identities, each count represents one independent
 * constitutional trace.
 *
 * Confidence is calculated as:
 *
 * supporting evidence for strongest candidate / total supplied evidence
 *
 * No hidden threshold is embedded here. Qualification uses the explicit
 * PreferenceLearningCriteria supplied by the caller.
 *
 * One occurrence can never qualify because PreferenceLearningCriteria forbids
 * minimumIndependentEvidence below two.
 */
class DefaultPreferenceLearningEvaluator :
    PreferenceLearningEvaluator {

    override fun evaluate(
        evidenceSet: PreferenceEvidenceSet,
        criteria: PreferenceLearningCriteria,
    ): PreferenceLearningResult {
        val grouped =
            evidenceSet.evidence
                .groupBy {
                    it.value
                }

        val strongestSupport =
            grouped.values
                .maxOf {
                    it.size
                }

        val strongestValues =
            grouped
                .filterValues {
                    it.size == strongestSupport
                }
                .keys

        val totalEvidence =
            evidenceSet.evidence.size

        val confidence =
            strongestSupport.toDouble() /
                totalEvidence.toDouble()

        if (strongestValues.size != 1) {
            return PreferenceLearningResult.create(
                key = evidenceSet.key,
                status =
                    PreferenceLearningStatus.AMBIGUOUS,
                candidateValue = null,
                confidence = confidence,
                supportingEvidenceCount =
                    strongestSupport,
                totalEvidenceCount =
                    totalEvidence,
            )
        }

        val candidateValue =
            strongestValues.single()

        val qualifies =
            strongestSupport >=
                criteria.minimumIndependentEvidence &&
                confidence >=
                criteria.minimumConfidence

        return PreferenceLearningResult.create(
            key = evidenceSet.key,
            status =
                if (qualifies) {
                    PreferenceLearningStatus.QUALIFIED
                } else {
                    PreferenceLearningStatus.INSUFFICIENT_EVIDENCE
                },
            candidateValue = candidateValue,
            confidence = confidence,
            supportingEvidenceCount =
                strongestSupport,
            totalEvidenceCount =
                totalEvidence,
        )
    }
}
