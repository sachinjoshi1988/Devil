package com.devil.app.execution

import com.devil.app.capability.AndroidCapabilityState
import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.permission.AndroidPermissionAssessment
import com.devil.app.permission.AndroidPermissionAssessmentStatus
import com.devil.app.permission.AndroidPermissionAuthorityAdapter
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
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
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidExecutionAttemptPortTest {

    @Test
    fun `deferred constitutional execution does not inspect Android embodiment`() {
        val traceId =
            TraceId.from(
                "trace-stage-64-deferred",
            )

        var stateCalls = 0
        var permissionCalls = 0
        var executionCalls = 0

        val port =
            createPort(
                stateCall = {
                    stateCalls += 1
                },
                permissionCall = {
                    permissionCalls += 1
                },
                executionCall = {
                    executionCalls += 1
                },
            )

        val result =
            port.attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.DEFERRED,
                    ),
            )

        assertEquals(ExecutionAttemptStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
        assertEquals(0, stateCalls)
        assertEquals(0, permissionCalls)
        assertEquals(0, executionCalls)
    }

    @Test
    fun `failed constitutional execution preserves error without Android access`() {
        val traceId =
            TraceId.from(
                "trace-stage-64-failed",
            )
        val error =
            createError(
                traceId = traceId,
                code = "STAGE_64_EXECUTION_FAILED",
            )

        var stateCalls = 0
        var permissionCalls = 0
        var executionCalls = 0

        val port =
            createPort(
                stateCall = {
                    stateCalls += 1
                },
                permissionCall = {
                    permissionCalls += 1
                },
                executionCall = {
                    executionCalls += 1
                },
            )

        val result =
            port.attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(ExecutionAttemptStatus.FAILED, result.status)
        assertEquals(error, result.error)
        assertNull(result.request)
        assertEquals(0, stateCalls)
        assertEquals(0, permissionCalls)
        assertEquals(0, executionCalls)
    }

    @Test
    fun `genuine Android attempt becomes neutral constitutional attempted result`() {
        val traceId =
            TraceId.from(
                "trace-stage-64-attempted",
            )
        val request = createRequest(traceId)

        var stateCalls = 0
        var permissionCalls = 0
        var executionCalls = 0

        val port =
            DefaultAndroidExecutionAttemptPort(
                capabilityStateProvider =
                    AndroidCapabilityStateProvider { capability ->
                        stateCalls += 1

                        AndroidCapabilityState.create(
                            capability = capability,
                            availability =
                                CapabilityAvailabilityState.AVAILABLE,
                            health =
                                CapabilityHealthState.READY,
                        )
                    },
                permissionAuthorityAdapter =
                    AndroidPermissionAuthorityAdapter { capability ->
                        permissionCalls += 1

                        AndroidPermissionAssessment.create(
                            capabilityId = capability.capabilityId,
                            status =
                                AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                        )
                    },
                executionAdapter =
                    AndroidExecutionAdapter {
                            execution,
                            capabilityState,
                            permissionAssessment,
                        ->
                        executionCalls += 1

                        assertEquals(
                            request.capability.capabilityId,
                            capabilityState.capability.capabilityId,
                        )
                        assertEquals(
                            request.capability.capabilityId,
                            permissionAssessment.capabilityId,
                        )

                        AndroidExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                request.capability.capabilityId,
                        )
                    },
            )

        val result =
            port.attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.APPROVED,
                        request = request,
                    ),
            )

        assertEquals(1, stateCalls)
        assertEquals(1, permissionCalls)
        assertEquals(1, executionCalls)

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.ATTEMPTED, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `Android deferred execution remains constitutionally deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage-64-android-deferred",
            )
        val request = createRequest(traceId)

        val port =
            DefaultAndroidExecutionAttemptPort(
                capabilityStateProvider =
                    readyStateProvider(),
                permissionAuthorityAdapter =
                    permissionFreeAdapter(),
                executionAdapter =
                    AndroidExecutionAdapter { execution, _, _ ->
                        AndroidExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status =
                                AndroidExecutionAttemptStatus.DEFERRED,
                        )
                    },
            )

        val result =
            port.attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.APPROVED,
                        request = request,
                    ),
            )

        assertEquals(ExecutionAttemptStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `Android failed execution preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-stage-64-android-failed",
            )
        val request = createRequest(traceId)
        val error =
            createError(
                traceId = traceId,
                code = "STAGE_64_ANDROID_FAILED",
            )

        val port =
            DefaultAndroidExecutionAttemptPort(
                capabilityStateProvider =
                    readyStateProvider(),
                permissionAuthorityAdapter =
                    permissionFreeAdapter(),
                executionAdapter =
                    AndroidExecutionAdapter { execution, _, _ ->
                        AndroidExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status =
                                AndroidExecutionAttemptStatus.FAILED,
                            error = error,
                        )
                    },
            )

        val result =
            port.attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.APPROVED,
                        request = request,
                    ),
            )

        assertEquals(ExecutionAttemptStatus.FAILED, result.status)
        assertEquals(error, result.error)
        assertNull(result.request)
    }

    @Test
    fun `attempted Android capability must match constitutional capability`() {
        val traceId =
            TraceId.from(
                "trace-stage-64-capability-mismatch",
            )
        val request = createRequest(traceId)

        val port =
            DefaultAndroidExecutionAttemptPort(
                capabilityStateProvider =
                    readyStateProvider(),
                permissionAuthorityAdapter =
                    permissionFreeAdapter(),
                executionAdapter =
                    AndroidExecutionAdapter { execution, _, _ ->
                        AndroidExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                CapabilityId.from(
                                    "capability-stage-64-other",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.APPROVED,
                        request = request,
                    ),
            )
        }
    }

    private fun createPort(
        stateCall: () -> Unit,
        permissionCall: () -> Unit,
        executionCall: () -> Unit,
    ): DefaultAndroidExecutionAttemptPort {
        return DefaultAndroidExecutionAttemptPort(
            capabilityStateProvider =
                AndroidCapabilityStateProvider { capability ->
                    stateCall()

                    AndroidCapabilityState.create(
                        capability = capability,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    )
                },
            permissionAuthorityAdapter =
                AndroidPermissionAuthorityAdapter { capability ->
                    permissionCall()

                    AndroidPermissionAssessment.create(
                        capabilityId = capability.capabilityId,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    )
                },
            executionAdapter =
                AndroidExecutionAdapter { execution, _, _ ->
                    executionCall()

                    AndroidExecutionAttemptResult.create(
                        traceId = execution.traceId,
                        status =
                            AndroidExecutionAttemptStatus.DEFERRED,
                    )
                },
        )
    }

    private fun readyStateProvider(): AndroidCapabilityStateProvider {
        return AndroidCapabilityStateProvider { capability ->
            AndroidCapabilityState.create(
                capability = capability,
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )
        }
    }

    private fun permissionFreeAdapter(): AndroidPermissionAuthorityAdapter {
        return AndroidPermissionAuthorityAdapter { capability ->
            AndroidPermissionAssessment.create(
                capabilityId = capability.capabilityId,
                status =
                    AndroidPermissionAssessmentStatus.NOT_REQUIRED,
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        val context =
            ContextEnvelope.create(
                traceId = traceId,
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_640_000L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "Stage 64 bounded understanding.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state = DecisionState.SELECTED,
                summary =
                    "Stage 64 constitutional decision.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-64",
                    ),
                decision = decision,
                state = TaskState.CREATED,
                summary =
                    "Stage 64 constitutional task.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage-64",
                    ),
                task = task,
                state = PlanState.CREATED,
                summary =
                    "Stage 64 constitutional plan.",
            )

        return ExecutionRequest.create(
            plan = plan,
            capability =
                CapabilityContract.create(
                    capabilityId =
                        CapabilityId.from(
                            "capability-stage-64",
                        ),
                    category =
                        CapabilityCategory.ACTION,
                    name =
                        "Stage 64 Android Action",
                    description =
                        "Represents one bounded Android execution attempt.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_640_500L,
                ),
            summary =
                "Stage 64 bounded execution failure.",
        )
    }
}
