package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.TraceId

/**
 * Supplies registered capability contracts for one bounded constitutional
 * capability-selection request.
 *
 * A registry exposes existing registrations only. It must not fabricate,
 * select, authorize, activate, execute, observe, or verify capabilities.
 */
interface CapabilityRegistry {

    fun obtain(
        traceId: TraceId,
        request: CapabilitySelectionRequest,
    ): CapabilityRegistryResult
}
