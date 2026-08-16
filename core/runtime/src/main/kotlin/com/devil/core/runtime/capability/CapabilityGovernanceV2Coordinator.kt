package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Stage 98 — Capability Governance V2.
 *
 * Represents whether the bounded capability-governance prerequisites established
 * for this stage are satisfied.
 *
 * SATISFIED means only that:
 *
 * - Capability Selection selected one existing registered CapabilityContract;
 * - explicit capability availability is AVAILABLE; and
 * - explicit capability health is READY.
 *
 * SATISFIED does not establish constitutional authorization, operating-system
 * permission, Executive readiness, execution approval, capability activation,
 * execution, observation, verification, Outcome, autonomy, or success.
 *
 * DEFERRED means the bounded prerequisites are not currently satisfied.
 *
 * FAILED preserves a matching upstream capability-selection failure.
 */
enum class CapabilityGovernanceV2Status {
    SATISFIED,
    DEFERRED,
    FAILED,
}

/**
 * Immutable evidence-preserving Stage 98 capability-governance record.
 *
 * This record preserves one already-selected registered capability together with
 * explicit availability and health states supplied to this core boundary.
 *
 * Creation requires AVAILABLE and READY because only that combination satisfies
 * the bounded Stage 98 governance prerequisite.
 *
 * This record does not establish where availability or health evidence came
 * from and must never fabricate either state.
 *
 * CAPABILITY_GOVERNANCE_SATISFIED != AUTHORIZATION.
 * CAPABILITY_GOVERNANCE_SATISFIED != EXECUTIVE_READINESS.
 * CAPABILITY_GOVERNANCE_SATISFIED != EXECUTION_APPROVAL.
 * CAPABILITY_GOVERNANCE_SATISFIED != EXECUTION.
 */
@ConsistentCopyVisibility
data class CapabilityGovernanceV2Record private constructor(
    val capability: CapabilityContract,
    val availability: CapabilityAvailabilityState,
    val health: CapabilityHealthState,
) {
    companion object {

        fun create(
            capability: CapabilityContract,
            availability: CapabilityAvailabilityState,
            health: CapabilityHealthState,
        ): CapabilityGovernanceV2Record {
            require(
                availability == CapabilityAvailabilityState.AVAILABLE,
            ) {
                "Capability Governance V2 requires explicit AVAILABLE capability state."
            }

            require(
                health == CapabilityHealthState.READY,
            ) {
                "Capability Governance V2 requires explicit READY capability health."
            }

            return CapabilityGovernanceV2Record(
                capability = capability,
                availability = availability,
                health = health,
            )
        }
    }
}

/**
 * Structured Stage 98 Capability Governance V2 result.
 *
 * A satisfied result contains exactly one bounded governance record.
 * A deferred result contains neither record nor error.
 * A failed result contains one matching upstream error.
 */
@ConsistentCopyVisibility
data class CapabilityGovernanceV2Result private constructor(
    val traceId: TraceId,
    val status: CapabilityGovernanceV2Status,
    val record: CapabilityGovernanceV2Record?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CapabilityGovernanceV2Status,
            record: CapabilityGovernanceV2Record? = null,
            error: UniversalErrorRecord? = null,
        ): CapabilityGovernanceV2Result {
            when (status) {
                CapabilityGovernanceV2Status.SATISFIED -> {
                    require(
                        record != null &&
                            error == null,
                    ) {
                        "Satisfied Capability Governance V2 results require one record and must not contain an error."
                    }
                }

                CapabilityGovernanceV2Status.DEFERRED -> {
                    require(
                        record == null &&
                            error == null,
                    ) {
                        "Deferred Capability Governance V2 results must not contain a record or error."
                    }
                }

                CapabilityGovernanceV2Status.FAILED -> {
                    require(
                        record == null &&
                            error != null,
                    ) {
                        "Failed Capability Governance V2 results require an error and must not contain a record."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Capability Governance V2 result and error must use the same trace identity."
            }

            return CapabilityGovernanceV2Result(
                traceId = traceId,
                status = status,
                record = record,
                error = error,
            )
        }
    }
}

/**
 * Stage 98 bounded Capability Governance V2 coordinator.
 *
 * This coordinator consumes an existing CapabilitySelectionResult and explicit
 * capability availability and health states.
 *
 * It does not:
 *
 * - register or select a capability;
 * - invent capability availability or health;
 * - inspect Android or another platform;
 * - interpret operating-system permission;
 * - grant constitutional authorization;
 * - establish Executive readiness;
 * - create or alter a Decision, Task, or Plan;
 * - create an ExecutionRequest;
 * - approve execution;
 * - activate or execute a capability;
 * - observe or verify effects;
 * - establish Outcome;
 * - grant Controlled Autonomy;
 * - or continue work autonomously.
 *
 * Registered != Available != Authorized != Ready != Executed.
 *
 * Capability health READY != Executive readiness READY.
 *
 * STAGE_98_CAPABILITY_GOVERNANCE != CAPABILITY_AUTHORITY_REPLACEMENT.
 */
class CapabilityGovernanceV2Coordinator {

    fun assess(
        traceId: TraceId,
        capabilitySelection: CapabilitySelectionResult,
        availability: CapabilityAvailabilityState,
        health: CapabilityHealthState,
    ): CapabilityGovernanceV2Result {
        require(
            capabilitySelection.traceId == traceId,
        ) {
            "Capability Governance V2 trace and capability selection result must use the same trace identity."
        }

        return when (capabilitySelection.status) {
            CapabilitySelectionStatus.SELECTED -> {
                if (
                    availability !=
                    CapabilityAvailabilityState.AVAILABLE ||
                    health != CapabilityHealthState.READY
                ) {
                    CapabilityGovernanceV2Result.create(
                        traceId = traceId,
                        status =
                            CapabilityGovernanceV2Status.DEFERRED,
                    )
                } else {
                    CapabilityGovernanceV2Result.create(
                        traceId = traceId,
                        status =
                            CapabilityGovernanceV2Status.SATISFIED,
                        record =
                            CapabilityGovernanceV2Record.create(
                                capability =
                                    requireNotNull(
                                        capabilitySelection.capability,
                                    ),
                                availability = availability,
                                health = health,
                            ),
                    )
                }
            }

            CapabilitySelectionStatus.DEFERRED ->
                CapabilityGovernanceV2Result.create(
                    traceId = traceId,
                    status =
                        CapabilityGovernanceV2Status.DEFERRED,
                )

            CapabilitySelectionStatus.FAILED ->
                CapabilityGovernanceV2Result.create(
                    traceId = traceId,
                    status =
                        CapabilityGovernanceV2Status.FAILED,
                    error =
                        requireNotNull(
                            capabilitySelection.error,
                        ),
                )
        }
    }
}
