package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionRequest
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Stage 38 Android-side typed execution directive.
 *
 * The constitutional ExecutionRequest establishes the selected capability.
 *
 * This directive preserves the already-established Android embodiment details
 * required to attempt that capability.
 *
 * Stage 38 currently supports one bounded directive payload:
 *
 * accessibilityRequest
 *
 * The directive does not interpret conversation text, select a capability,
 * grant authorization, establish Executive readiness, approve execution,
 * grant Android permission, observe an effect, verify an outcome, or establish
 * successful task completion.
 *
 * Dynamic accessibility target data must never be reconstructed from plan,
 * task, decision, capability, or conversation summaries inside the Android
 * execution performer.
 */
data class AndroidExecutionDirective(
    val traceId: TraceId,
    val capabilityId: CapabilityId,
    val accessibilityRequest: AndroidAccessibilityActionRequest,
)
