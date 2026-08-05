package com.devil.core.runtime.capability

import com.devil.core.model.common.TraceId

/**
 * Translates one bounded capability-selection resolution result into the stable
 * operational Capability Selection result contract.
 *
 * This mapper does not resolve or fabricate capabilities, establish availability
 * or health, grant authorization, check operating-system permission, execute
 * actions, observe results, verify outcomes, or report final outcomes.
 */
interface CapabilitySelectionResultMapper {

    fun map(
        traceId: TraceId,
        resolution: CapabilitySelectionResolutionResult,
    ): CapabilitySelectionResult
}
