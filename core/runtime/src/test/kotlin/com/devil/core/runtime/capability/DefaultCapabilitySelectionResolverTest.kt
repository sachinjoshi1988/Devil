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
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultCapabilitySelectionResolverTest {

    @Test
    fun `resolve selects registered camera capability for open camera semantics`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-001",
        )
        val capability = createCameraCapability()

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "camera",
                    ),
                registry =
                    availableRegistry(
                        traceId = traceId,
                        capability = capability,
                    ),
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.RESOLVED,
            result.status,
        )
        assertEquals(capability, result.capability)
        assertNull(result.error)
    }

    @Test
    fun `resolve accepts bounded article form of camera target`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-002",
        )
        val capability = createCameraCapability()

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "the camera",
                    ),
                registry =
                    availableRegistry(
                        traceId = traceId,
                        capability = capability,
                    ),
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.RESOLVED,
            result.status,
        )
        assertEquals(capability, result.capability)
    }

    @Test
    fun `resolve does not guess capability for unknown target`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-003",
        )

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "unknown target",
                    ),
                registry =
                    availableRegistry(
                        traceId = traceId,
                        capability = createCameraCapability(),
                    ),
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
    }

    @Test
    fun `resolve does not select action capability for greeting semantics`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-004",
        )

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.GREETING,
                        actionability =
                            UnderstandingActionability.NON_ACTIONABLE,
                    ),
                registry =
                    availableRegistry(
                        traceId = traceId,
                        capability = createCameraCapability(),
                    ),
            )

        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
    }

    @Test
    fun `resolve defers when complete understanding lacks structured semantics`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-005",
        )

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequestWithoutSemantics(traceId),
                registry =
                    availableRegistry(
                        traceId = traceId,
                        capability = createCameraCapability(),
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
            "trace-default-capability-resolver-006",
        )

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "camera",
                    ),
                registry =
                    CapabilityRegistryResult.create(
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
            "trace-default-capability-resolver-007",
        )
        val error = createError(traceId)

        val result =
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "camera",
                    ),
                registry =
                    CapabilityRegistryResult.create(
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
            "trace-default-capability-resolver-008",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId =
                            TraceId.from(
                                "trace-default-capability-request-other",
                            ),
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "camera",
                    ),
                registry =
                    CapabilityRegistryResult.create(
                        traceId = traceId,
                        status = CapabilityRegistryStatus.UNAVAILABLE,
                    ),
            )
        }
    }

    @Test
    fun `resolve rejects registry result from a different trace`() {
        val traceId = TraceId.from(
            "trace-default-capability-resolver-009",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionResolver().resolve(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                        intent = UnderstandingIntent.OPEN_TARGET,
                        actionability =
                            UnderstandingActionability.ACTIONABLE,
                        target = "camera",
                    ),
                registry =
                    CapabilityRegistryResult.create(
                        traceId =
                            TraceId.from(
                                "trace-default-capability-registry-other",
                            ),
                        status = CapabilityRegistryStatus.UNAVAILABLE,
                    ),
            )
        }
    }

    private fun createCameraCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-camera",
                ),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Represents the bounded registered capability for opening or addressing the camera target.",
        )
    }

    private fun availableRegistry(
        traceId: TraceId,
        capability: CapabilityContract,
    ): CapabilityRegistryResult {
        return CapabilityRegistryResult.create(
            traceId = traceId,
            status = CapabilityRegistryStatus.AVAILABLE,
            capabilities = listOf(capability),
        )
    }

    private fun createRequest(
        traceId: TraceId,
        intent: UnderstandingIntent,
        actionability: UnderstandingActionability,
        target: String? = null,
    ): CapabilitySelectionRequest {
        return createPlanRequest(
            traceId = traceId,
            semantics =
                UnderstandingSemantics.create(
                    intent = intent,
                    actionability = actionability,
                    meaning =
                        when (intent) {
                            UnderstandingIntent.GREETING ->
                                "greeting"

                            UnderstandingIntent.OPEN_TARGET ->
                                "open target"

                            UnderstandingIntent.INFORMATIONAL ->
                                "informational statement"
                        },
                    target = target,
                ),
        )
    }

    private fun createRequestWithoutSemantics(
        traceId: TraceId,
    ): CapabilitySelectionRequest {
        return createPlanRequest(
            traceId = traceId,
            semantics = null,
        )
    }

    private fun createPlanRequest(
        traceId: TraceId,
        semantics: UnderstandingSemantics?,
    ): CapabilitySelectionRequest {
        return CapabilitySelectionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-default-capability-resolver-001",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-default-capability-resolver-001",
                                ),
                            decision =
                                DecisionRecord.create(
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
                                            semantics = semantics,
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
                        "Prepare the bounded plan for capability selection.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CAPABILITY_REGISTRY_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_091_500L,
                ),
            summary =
                "Capability registry failed.",
        )
    }
}
