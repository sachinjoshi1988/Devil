package com.devil.app.diagnostic

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId

/**
 * Stage 314 diagnostic boundary for bounded post-action O/V/O evidence.
 *
 * Implementations may record already-established diagnostic facts only.
 *
 * They must not:
 *
 * - alter execution;
 * - delay or retry observation;
 * - synthesize screen evidence;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - or grant constitutional authority.
 *
 * DIAGNOSTIC_RECORDED != OBSERVED.
 * DIAGNOSTIC_RECORDED != VERIFIED.
 * DIAGNOSTIC_RECORDED != OUTCOME_ESTABLISHED.
 */
interface Stage314PostActionDiagnostic {

    fun observation(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        elements: List<AndroidScreenElementRecord> = emptyList(),
    )

    fun verification(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        expectedVisibleText: String? = null,
    )

    fun outcome(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
    )
}

/**
 * Default diagnostic implementation.
 *
 * Production behavior is intentionally no-op unless an Android diagnostic
 * recorder is explicitly injected by composition.
 */
object NoOpStage314PostActionDiagnostic :
    Stage314PostActionDiagnostic {

    override fun observation(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        elements: List<AndroidScreenElementRecord>,
    ) = Unit

    override fun verification(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        expectedVisibleText: String?,
    ) = Unit

    override fun outcome(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
    ) = Unit
}
