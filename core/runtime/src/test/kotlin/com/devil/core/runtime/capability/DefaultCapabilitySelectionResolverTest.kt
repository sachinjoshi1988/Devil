package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultCapabilitySelectionResolverTest {

    @Test
    fun `resolve returns unavailable without fabricating selection policy`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-001",
        )
        val resolver: CapabilitySelectionResolver =
            DefaultCapabilitySelectionResolver()

        val result = resolver.resolve(
            traceId = traceId,
            request = createRequest(traceId),
            registry = CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.AVAILABLE,
                capabilities = listOf(createCapability()),
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `resolve does not select the only registered capability`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-002",
        )

        val result = DefaultCapabilitySelectionResolver().resolve(
            traceId = traceId,
            request = createRequest(traceId),
            registry = CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.AVAILABLE,
                capabilities = listOf(createCapability()),
            ),
        )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
    }

    @Test
    fun `resolve preserves unavailable registry result`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-003",
        )

        val result = DefaultCapabilitySelectionResolver().resolve(
            traceId = traceId,
            request = createRequest(traceId),
            registry = CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.UNAVAILABLE,
            ),
        )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `resolve preserves failed registry error`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-004",
        )
        val error = createError(traceId)

        val result = DefaultCapabilitySelectionResolver().resolve(
            traceId = traceId,
            request = createRequest(traceId),
            registry = CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(
            CapabilitySelectionResolutionStatus.FAILED,
            result.status,
        )
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `resolve rejects request from a different trace`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-005",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request = createRequest(
                    TraceId.from(
                        "trace-default-capability-request-other",
                    ),
                ),
                registry = CapabilityRegistryResult.create(
                    traceId = traceId,
                    status = CapabilityRegistryStatus.UNAVAILABLE,
                ),
            )
        }
    }

    @Test
    fun `resolve rejects registry result from a different trace`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request = createRequest(traceId),
                registry = CapabilityRegistryResult.create(
                    traceId = TraceId.from(
                        "trace-default-capability-registry-other",
                    ),
                    status = CapabilityRegistryStatus.UNAVAILABLE,
                ),
            )
        }
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createRequest(
        traceId: TraceId,
    ): CapabilitySelectionRequest {
        return CapabilitySelectionRequest.create(
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-default-capability-resolver-001",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-default-capability-resolver-001",
                    ),
                    decision = DecisionRecord.create(
                        understanding =
                            UnderstandingRecord.create(
                                context =
                                    ContextEnvelope.create(
                                        traceId = traceId,
                                        schemaVersion =
                                            SchemaVersion.from(1),
                                        source =
                                            ContextSource.TEXT,
                                        trustLevel =
                                            ContextTrustLevel.VERIFIED,
                                        securityLevel =
                                            ContextSecurityLevel.RESTRICTED,
                                        observedAt =
                                            DevilTimestamp
                                                .fromEpochMilliseconds(
                                                    1_754_000_091_000L,
                                                ),
                                    ),
                                state =
                                    UnderstandingState.COMPLETE,
                                summary =
                                    "Bounded understanding was produced.",
                            ),
                        state = DecisionState.SELECTED,
                        summary =
                            "A constitutional decision was selected.",
                    ),
                    state = TaskState.CREATED,
                    summary =
                        "A bounded constitutional task was created.",
                ),
                state = PlanState.CREATED,
                summary =
                    "Use the constitutionally approved capability path.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "CAPABILITY_REGISTRY_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_091_500L,
                ),
            summary = "Capability registry access failed.",
        )
    }
}
