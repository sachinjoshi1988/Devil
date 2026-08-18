package com.devil.core.runtime.research

import com.devil.core.model.research.ResearchConfidence
import com.devil.core.model.research.ResearchConfidenceAssessment
import com.devil.core.model.research.ResearchCorroborationAssessment

/**
 * Stage 111 platform-independent coordinator for bounded Research confidence.
 *
 * The coordinator preserves one exact Stage 110
 * ResearchCorroborationAssessment together with an explicitly supplied bounded
 * ResearchConfidence.
 *
 * It does not calculate confidence from source count, source trust,
 * authenticity, freshness, corroboration, conflict, majority agreement, text,
 * or any other heuristic.
 *
 * It does not establish truth, factual Verification, conflict resolution,
 * consensus, synthesis, World Model state, Learning, Memory, authorization,
 * execution, or verified Outcome.
 *
 * RESEARCH_CONFIDENCE != FACT_VERIFICATION.
 * RESEARCH_CONFIDENCE != CONSENSUS.
 * RESEARCH_CONFIDENCE != SYNTHESIS.
 */
class ResearchConfidenceCoordinator {

    fun establish(
        corroborationAssessment: ResearchCorroborationAssessment,
        confidence: ResearchConfidence,
    ): ResearchConfidenceAssessment {
        return ResearchConfidenceAssessment.create(
            corroborationAssessment = corroborationAssessment,
            confidence = confidence,
        )
    }
}
