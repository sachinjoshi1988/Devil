package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.model.embodiment.PcEmbodimentEvidence

/**
 * Stable Stage 83 result of one bounded PC-embodiment assessment.
 *
 * The result always preserves the existing Stage 81 embodiment being assessed.
 *
 * PC requires genuine bounded PC evidence.
 *
 * NON_PC means the existing Stage 81 platform identity is not the PC platform
 * family and therefore must not carry PC-specific evidence.
 *
 * DEFERRED means the embodiment is represented as PC-class but genuine bounded
 * PC evidence is unavailable.
 *
 * NON_PC and DEFERRED must not contain fabricated or contradictory PC evidence.
 *
 * The result establishes no new Devil identity, Brain, runtime, trust,
 * authentication, authorization, session, capability, permission, execution,
 * Observation, Verification, Outcome, Learning, Memory, or persistence
 * authority.
 *
 * PC_ASSESSMENT != DEVIL_IDENTITY.
 * PC_ASSESSMENT != AUTHORITY.
 * PC_ASSESSMENT != EXECUTION.
 */
@ConsistentCopyVisibility
data class PcEmbodimentAssessmentResult private constructor(
    val traceId: TraceId,
    val status: PcEmbodimentAssessmentStatus,
    val embodiment: EmbodimentRecord,
    val evidence: PcEmbodimentEvidence?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: PcEmbodimentAssessmentStatus,
            embodiment: EmbodimentRecord,
            evidence: PcEmbodimentEvidence? = null,
        ): PcEmbodimentAssessmentResult {
            when (status) {
                PcEmbodimentAssessmentStatus.PC -> {
                    require(evidence != null) {
                        "PC embodiment results require genuine bounded PC evidence."
                    }
                }

                PcEmbodimentAssessmentStatus.NON_PC -> {
                    require(evidence == null) {
                        "Non-PC embodiment results must not contain PC evidence."
                    }
                }

                PcEmbodimentAssessmentStatus.DEFERRED -> {
                    require(evidence == null) {
                        "Deferred PC embodiment results must not contain evidence."
                    }
                }
            }

            return PcEmbodimentAssessmentResult(
                traceId = traceId,
                status = status,
                embodiment = embodiment,
                evidence = evidence,
            )
        }
    }
}
