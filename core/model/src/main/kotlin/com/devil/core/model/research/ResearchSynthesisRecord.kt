package com.devil.core.model.research

/**
 * Immutable Stage 112 representation of one bounded constitutional Research
 * synthesis result.
 *
 * The exact Stage 111 ResearchConfidenceAssessment remains attached so all
 * Stage 107 evidence, Stage 109 source assessment, Stage 110 corroboration and
 * conflict state, and Stage 111 confidence provenance remain intact.
 *
 * SYNTHESIZED requires:
 *
 * - Stage 110 corroboration == CORROBORATED;
 * - Stage 110 conflict == CONSISTENT;
 * - and one nonblank bounded synthesis description.
 *
 * DEFERRED contains no synthesis description.
 *
 * Stage 111 confidence is preserved but is deliberately not used as an
 * independent authority to override conflict or manufacture synthesis.
 *
 * Creating this record does not:
 *
 * - alter Research evidence;
 * - alter source assessments;
 * - rank or weight sources;
 * - infer source independence;
 * - resolve conflicting research;
 * - select a winning claim;
 * - establish factual truth;
 * - perform factual Verification;
 * - create universal consensus;
 * - mutate World Model state;
 * - perform Learning;
 * - create, approve, commit, persist, or recall Memory;
 * - grant authorization;
 * - execute an action;
 * - or establish verified success.
 *
 * RESEARCH_SYNTHESIS != TRUTH.
 * RESEARCH_SYNTHESIS != VERIFICATION.
 * RESEARCH_SYNTHESIS != CONSENSUS.
 * RESEARCH_SYNTHESIS != WORLD_MODEL.
 * RESEARCH_SYNTHESIS != LEARNING.
 * RESEARCH_SYNTHESIS != MEMORY.
 * HIGH_CONFIDENCE != CONFLICT_RESOLUTION.
 */
@ConsistentCopyVisibility
data class ResearchSynthesisRecord private constructor(
    val confidenceAssessment: ResearchConfidenceAssessment,
    val status: ResearchSynthesisStatus,
    val description: String?,
) {
    companion object {

        fun create(
            confidenceAssessment: ResearchConfidenceAssessment,
            status: ResearchSynthesisStatus,
            description: String? = null,
        ): ResearchSynthesisRecord {
            val normalizedDescription =
                description
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            val corroborationAssessment =
                confidenceAssessment.corroborationAssessment

            when (status) {
                ResearchSynthesisStatus.SYNTHESIZED -> {
                    require(
                        corroborationAssessment.corroboration ==
                            ResearchCorroborationStatus.CORROBORATED,
                    ) {
                        "Research synthesis requires explicitly CORROBORATED research."
                    }

                    require(
                        corroborationAssessment.conflict ==
                            ResearchConflictStatus.CONSISTENT,
                    ) {
                        "Research synthesis must not erase represented Research conflict."
                    }

                    require(normalizedDescription != null) {
                        "Synthesized Research requires one nonblank bounded description."
                    }
                }

                ResearchSynthesisStatus.DEFERRED -> {
                    require(normalizedDescription == null) {
                        "Deferred Research synthesis must not contain a synthesis description."
                    }
                }
            }

            return ResearchSynthesisRecord(
                confidenceAssessment = confidenceAssessment,
                status = status,
                description = normalizedDescription,
            )
        }
    }
}
