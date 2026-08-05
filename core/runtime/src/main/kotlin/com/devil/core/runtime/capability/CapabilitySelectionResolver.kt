package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId

/**
 * Resolves at most one registered capability from a bounded constitutional
 * capability-selection request and registry result.
 *
 * This resolver must not fabricate capability registrations or selection
 * policy. It does not establish availability, health, authorization,
 * operating-system permission, readiness, execution, observation,
 * verification, or final outcome.
 */
interface CapabilitySelectionResolver {

    fun resolve(
        traceId: TraceId,
        request: CapabilitySelectionRequest,
        registry: CapabilityRegistryResult,
    ): CapabilitySelectionResolutionResult
}
