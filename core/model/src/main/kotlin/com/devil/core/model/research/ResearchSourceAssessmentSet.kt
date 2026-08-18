package com.devil.core.model.research

/**
 * Immutable Stage 109 collection of bounded source assessments corresponding to
 * one existing Stage 107 ResearchEvidenceSet.
 *
 * Every evidence item in the source evidence set must have exactly one matching
 * assessment and no assessment may introduce another evidence object.
 *
 * Different sources may receive different assessments.
 *
 * The set does not rank sources, resolve disagreement, calculate consensus,
 * establish factual truth, perform verification, or synthesize conclusions.
 *
 * SOURCE_ASSESSMENT_SET != SOURCE_RANKING.
 * SOURCE_ASSESSMENT_SET != CONSENSUS.
 * SOURCE_ASSESSMENT_SET != SYNTHESIS.
 * SOURCE_ASSESSMENT_SET != VERIFIED_FACT.
 */
@ConsistentCopyVisibility
data class ResearchSourceAssessmentSet private constructor(
    val evidenceSet: ResearchEvidenceSet,
    val assessments: List<ResearchSourceAssessment>,
) {
    companion object {

        fun create(
            evidenceSet: ResearchEvidenceSet,
            assessments: List<ResearchSourceAssessment>,
        ): ResearchSourceAssessmentSet {
            require(
                assessments.size == evidenceSet.evidence.size,
            ) {
                "Research source assessment set requires exactly one assessment for every evidence item."
            }

            require(
                assessments.map { it.evidence }.toSet().size ==
                    assessments.size,
            ) {
                "Research source assessment set must not assess the same evidence item more than once."
            }

            require(
                assessments.all { assessment ->
                    evidenceSet.evidence.any { evidence ->
                        evidence === assessment.evidence
                    }
                },
            ) {
                "Research source assessment set may contain only exact evidence objects preserved by the supplied evidence set."
            }

            require(
                evidenceSet.evidence.all { evidence ->
                    assessments.any { assessment ->
                        assessment.evidence === evidence
                    }
                },
            ) {
                "Every research evidence item requires one matching source assessment."
            }

            return ResearchSourceAssessmentSet(
                evidenceSet = evidenceSet,
                assessments = assessments.toList(),
            )
        }
    }
}
