package com.devil.core.runtime.research

import com.devil.core.model.research.ResearchSourceAssessment
import com.devil.core.model.research.ResearchSourceAssessmentSet

/**
 * Stage 109 platform-independent Research source-assessment coordinator.
 *
 * The coordinator accepts one Stage 108 evaluation result together with
 * explicitly supplied bounded source assessments.
 *
 * It does not manufacture authenticity, trust, or freshness values.
 *
 * The current Stage 108 default evaluation is UNAVAILABLE, so callers cannot
 * treat repository-default research evidence as automatically assessable.
 *
 * RESEARCH_SOURCE_ASSESSMENT != FACT_VERIFICATION.
 * RESEARCH_SOURCE_ASSESSMENT != SOURCE_RANKING.
 * RESEARCH_SOURCE_ASSESSMENT != SYNTHESIS.
 */
class ResearchSourceAssessmentCoordinator {

    fun establish(
        evaluation: ResearchEvidenceEvaluationResult,
        assessments: List<ResearchSourceAssessment>,
    ): ResearchSourceAssessmentSet {
        require(
            evaluation.status ==
                ResearchEvidenceEvaluationStatus.ASSESSABLE,
        ) {
            "Research source assessment requires an ASSESSABLE Stage 108 evaluation."
        }

        return ResearchSourceAssessmentSet.create(
            evidenceSet = evaluation.evidenceSet,
            assessments = assessments,
        )
    }
}
