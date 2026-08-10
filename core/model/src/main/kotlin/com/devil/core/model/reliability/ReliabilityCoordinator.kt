package com.devil.core.model.reliability

/**
 * Stage 45 bounded coordinator for reliability assessment.
 *
 * Flow:
 *
 * RecoveryEvidence
 * -> ReliabilityPolicy
 * -> ReliabilityAssessment.
 *
 * This coordinator is not another Brain, Executive, Planner, Security Authority,
 * Authorization Authority, Memory Authority, capability-health authority,
 * execution path, or recovery executor.
 *
 * RECOVERY_ELIGIBLE is advisory recovery disposition only.
 *
 * The coordinator does not retry anything.
 */
class ReliabilityCoordinator(
    private val policy: ReliabilityPolicy =
        ReliabilityPolicy(),
) {

    fun assess(
        evidence: RecoveryEvidence,
    ): ReliabilityAssessment {
        return policy.assess(
            evidence = evidence,
        )
    }
}
