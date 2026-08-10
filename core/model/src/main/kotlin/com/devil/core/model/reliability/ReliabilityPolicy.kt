package com.devil.core.model.reliability

/**
 * Stage 45 pure reliability and recovery-disposition policy.
 *
 * This policy evaluates supplied evidence only.
 *
 * It performs no I/O, no retry, no restart, no execution, no runtime invocation,
 * no capability-health mutation, no error clearing, no authorization evaluation,
 * no memory persistence, and no Outcome mutation.
 */
class ReliabilityPolicy {

    fun assess(
        evidence: RecoveryEvidence,
    ): ReliabilityAssessment {
        if (evidence.manualInterventionRequired) {
            return ReliabilityAssessment.create(
                evidence = evidence,
                disposition =
                    RecoveryDisposition.MANUAL_INTERVENTION_REQUIRED,
                rationale =
                    "The supplied reliability evidence explicitly requires manual intervention.",
            )
        }

        return when (evidence.condition) {
            ReliabilityCondition.HEALTHY ->
                ReliabilityAssessment.create(
                    evidence = evidence,
                    disposition =
                        RecoveryDisposition.RECOVERY_NOT_REQUIRED,
                    rationale =
                        "The supplied reliability evidence does not establish a recovery condition.",
                )

            ReliabilityCondition.DEGRADED ->
                if (evidence.recoveryPathKnown) {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.RECOVERY_ELIGIBLE,
                        rationale =
                            "The supplied degraded reliability evidence identifies a bounded recovery path for later authorized consideration.",
                    )
                } else {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.UNAVAILABLE,
                        rationale =
                            "The supplied degraded reliability evidence does not establish a known bounded recovery path.",
                    )
                }

            ReliabilityCondition.UNAVAILABLE ->
                if (evidence.recoveryPathKnown) {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.RECOVERY_ELIGIBLE,
                        rationale =
                            "The supplied unavailable reliability evidence identifies a bounded recovery path for later authorized consideration.",
                    )
                } else {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.UNAVAILABLE,
                        rationale =
                            "The supplied unavailable reliability evidence does not establish a known bounded recovery path.",
                    )
                }

            ReliabilityCondition.FAILED ->
                if (evidence.error == null) {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.UNAVAILABLE,
                        rationale =
                            "Failed reliability evidence lacks a constitutional error record and cannot safely establish a recovery disposition.",
                    )
                } else if (evidence.recoveryPathKnown) {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.RECOVERY_ELIGIBLE,
                        rationale =
                            "The supplied failure preserves constitutional error evidence and identifies a bounded recovery path for later authorized consideration.",
                    )
                } else {
                    ReliabilityAssessment.create(
                        evidence = evidence,
                        disposition =
                            RecoveryDisposition.NOT_RECOVERABLE,
                        rationale =
                            "The supplied failure preserves constitutional error evidence but establishes no bounded recovery path.",
                    )
                }
        }
    }
}
