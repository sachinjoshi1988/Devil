package com.devil.core.runtime.preference

import com.devil.core.model.preference.PreferenceEvidenceSet
import com.devil.core.model.preference.PreferenceLearningCandidate

/**
 * Default deterministic qualified-preference candidate provider.
 *
 * This provider independently verifies the structural facts preserved by the
 * PreferenceLearningResult against the original PreferenceEvidenceSet before it
 * exposes an AVAILABLE candidate.
 *
 * It does not reinterpret constitutional Learning or Memory policy.
 */
class DefaultPreferenceLearningCandidateProvider :
    PreferenceLearningCandidateProvider {

    override fun provide(
        evidenceSet: PreferenceEvidenceSet,
        learningResult: PreferenceLearningResult,
    ): PreferenceLearningCandidateResult {
        require(learningResult.key == evidenceSet.key) {
            "Preference learning result and evidence set must use the same preference key."
        }

        require(
            learningResult.totalEvidenceCount ==
                evidenceSet.evidence.size,
        ) {
            "Preference learning result total evidence count must match the supplied evidence set."
        }

        val grouped =
            evidenceSet.evidence.groupBy {
                it.value
            }

        val strongestSupport =
            grouped.values.maxOf {
                it.size
            }

        val strongestValues =
            grouped
                .filterValues {
                    it.size == strongestSupport
                }
                .keys

        val expectedConfidence =
            strongestSupport.toDouble() /
                evidenceSet.evidence.size.toDouble()

        require(
            learningResult.supportingEvidenceCount ==
                strongestSupport,
        ) {
            "Preference learning result support count must match the supplied evidence."
        }

        require(
            learningResult.confidence ==
                expectedConfidence,
        ) {
            "Preference learning result confidence must match the supplied evidence."
        }

        if (strongestValues.size != 1) {
            require(
                learningResult.status ==
                    PreferenceLearningStatus.AMBIGUOUS,
            ) {
                "Tied preference evidence must remain ambiguous."
            }

            require(learningResult.candidateValue == null) {
                "Ambiguous preference learning must not select a candidate value."
            }

            return PreferenceLearningCandidateResult.create(
                status =
                    PreferenceLearningCandidateStatus.UNAVAILABLE,
            )
        }

        val strongestValue =
            strongestValues.single()

        require(
            learningResult.candidateValue ==
                strongestValue,
        ) {
            "Preference learning candidate value must match the strongest supplied evidence."
        }

        if (
            learningResult.status !=
            PreferenceLearningStatus.QUALIFIED
        ) {
            return PreferenceLearningCandidateResult.create(
                status =
                    PreferenceLearningCandidateStatus.UNAVAILABLE,
            )
        }

        val supportingEvidence =
            evidenceSet.evidence.filter {
                it.value == strongestValue
            }

        require(supportingEvidence.size >= 2) {
            "Qualified preference candidate requires repeated independent supporting evidence."
        }

        val candidate =
            PreferenceLearningCandidate.create(
                key = evidenceSet.key,
                value = strongestValue,
                confidence =
                    learningResult.confidence,
                supportingEvidenceCount =
                    supportingEvidence.size,
                totalEvidenceCount =
                    evidenceSet.evidence.size,
                supportingTraceIds =
                    supportingEvidence.map {
                        it.traceId
                    },
                evidenceTraceIds =
                    evidenceSet.evidence.map {
                        it.traceId
                    },
            )

        return PreferenceLearningCandidateResult.create(
            status =
                PreferenceLearningCandidateStatus.AVAILABLE,
            candidate = candidate,
        )
    }
}
