package com.devil.core.runtime.research

import com.devil.core.model.research.ResearchConflictStatus
import com.devil.core.model.research.ResearchCorroborationAssessment
import com.devil.core.model.research.ResearchCorroborationStatus
import com.devil.core.model.research.ResearchSourceAssessmentSet

/**
 * Stage 110 platform-independent coordinator for bounded Research corroboration
 * and conflict assessment.
 *
 * The coordinator preserves one exact Stage 109 ResearchSourceAssessmentSet and
 * explicitly supplied corroboration/conflict states.
 *
 * It does not inspect descriptions to manufacture agreement, infer majority
 * truth, rank sources, resolve conflicting claims, calculate confidence,
 * establish factual Verification, or synthesize conclusions.
 *
 * RESEARCH_CORROBORATION != FACT_VERIFICATION.
 * CONFLICT_ASSESSMENT != CONFLICT_RESOLUTION.
 * CORROBORATION != CONSENSUS.
 * CORROBORATION != SYNTHESIS.
 */
class ResearchCorroborationCoordinator {

    fun establish(
        sourceAssessmentSet: ResearchSourceAssessmentSet,
        corroboration: ResearchCorroborationStatus,
        conflict: ResearchConflictStatus,
    ): ResearchCorroborationAssessment {
        return ResearchCorroborationAssessment.create(
            sourceAssessmentSet = sourceAssessmentSet,
            corroboration = corroboration,
            conflict = conflict,
        )
    }
}
