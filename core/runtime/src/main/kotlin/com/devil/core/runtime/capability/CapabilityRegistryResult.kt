package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the result of obtaining registered capability contracts.
 *
 * An available result contains one or more unique registered capability
 * contracts. An unavailable result contains neither capabilities nor error.
 * A failed result contains a matching error.
 *
 * This result does not register capabilities, select a capability, establish
 * availability or health, grant authorization, check operating-system
 * permission, execute actions, observe results, verify outcomes, or report
 * final outcomes.
 */
@ConsistentCopyVisibility
data class CapabilityRegistryResult private constructor(
    val traceId: TraceId,
    val status: CapabilityRegistryStatus,
    val capabilities: List<CapabilityContract>,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: CapabilityRegistryStatus,
            capabilities: List<CapabilityContract> = emptyList(),
            error: UniversalErrorRecord? = null,
        ): CapabilityRegistryResult {
            val preservedCapabilities = capabilities.toList()

            when (status) {
                CapabilityRegistryStatus.AVAILABLE -> {
                    require(
                        preservedCapabilities.isNotEmpty() &&
                            error == null,
                    ) {
                        "Available capability registry results require at least one capability and must not contain an error."
                    }

                    require(
                        preservedCapabilities
                            .map { it.capabilityId }
                            .distinct()
                            .size == preservedCapabilities.size,
                    ) {
                        "Available capability registry results must not contain duplicate capability identities."
                    }
                }

                CapabilityRegistryStatus.UNAVAILABLE -> {
                    require(
                        preservedCapabilities.isEmpty() &&
                            error == null,
                    ) {
                        "Unavailable capability registry results must not contain capabilities or error."
                    }
                }

                CapabilityRegistryStatus.FAILED -> {
                    require(
                        preservedCapabilities.isEmpty() &&
                            error != null,
                    ) {
                        "Failed capability registry results require an error and must not contain capabilities."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Capability registry result and error must use the same trace identity."
            }

            return CapabilityRegistryResult(
                traceId = traceId,
                status = status,
                capabilities = preservedCapabilities,
                error = error,
            )
        }
    }
}
