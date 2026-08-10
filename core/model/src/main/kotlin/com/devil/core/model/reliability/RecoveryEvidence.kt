package com.devil.core.model.reliability

import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Immutable Stage 45 bounded evidence supplied for one reliability assessment.
 *
 * The evidence may preserve:
 *
 * - one reliability condition;
 * - one optional constitutional error record;
 * - one optional capability-health observation;
 * - whether a bounded recovery path is known to exist;
 * - whether explicit manual intervention is required.
 *
 * This record does not:
 *
 * - reinterpret or mutate UniversalErrorRecord;
 * - change CapabilityHealthState;
 * - authorize retries;
 * - execute recovery;
 * - clear failures;
 * - claim recovery success;
 * - create logical memory;
 * - or alter verified Outcome.
 */
@ConsistentCopyVisibility
data class RecoveryEvidence private constructor(
    val condition: ReliabilityCondition,
    val error: UniversalErrorRecord?,
    val capabilityHealth: CapabilityHealthState?,
    val recoveryPathKnown: Boolean,
    val manualInterventionRequired: Boolean,
) {
    companion object {

        fun create(
            condition: ReliabilityCondition,
            error: UniversalErrorRecord? = null,
            capabilityHealth: CapabilityHealthState? = null,
            recoveryPathKnown: Boolean = false,
            manualInterventionRequired: Boolean = false,
        ): RecoveryEvidence {
            require(
                condition == ReliabilityCondition.FAILED || error == null,
            ) {
                "A constitutional error record may be attached only to FAILED reliability evidence."
            }

            require(
                !manualInterventionRequired || condition != ReliabilityCondition.HEALTHY,
            ) {
                "Healthy reliability evidence must not require manual intervention."
            }

            return RecoveryEvidence(
                condition = condition,
                error = error,
                capabilityHealth = capabilityHealth,
                recoveryPathKnown = recoveryPathKnown,
                manualInterventionRequired = manualInterventionRequired,
            )
        }
    }
}
