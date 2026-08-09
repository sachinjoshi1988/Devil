package com.devil.app.execution

import com.devil.app.capability.AndroidCapabilityState
import com.devil.app.permission.AndroidPermissionAssessment
import com.devil.app.permission.AndroidPermissionAssessmentStatus
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
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidExecutionAdapterTest {

    @Test
    fun `approved available healthy permission-free capability may reach performer`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-001",
            )
        val request = createRequest(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1

                        assertEquals(traceId, performerTrace)
                        assertEquals(request, performerRequest)

                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )

        assertEquals(1, calls)
        assertEquals(AndroidExecutionAttemptStatus.ATTEMPTED, result.status)
        assertEquals(request.capability.capabilityId, result.capabilityId)
        assertNull(result.error)
    }

    @Test
    fun `granted Android permission may reach performer but does not itself create approval`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-002",
            )
        val request = createRequest(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1
                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    AndroidPermissionAssessment.create(
                        capabilityId =
                            request.capability.capabilityId,
                        status =
                            AndroidPermissionAssessmentStatus.GRANTED,
                        requiredPermissions =
                            listOf(
                                "android.permission.CAMERA",
                            ),
                    ),
            )

        assertEquals(1, calls)
        assertEquals(AndroidExecutionAttemptStatus.ATTEMPTED, result.status)
    }

    @Test
    fun `denied Android permission defers without invoking performer`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-003",
            )
        val request = createRequest(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1
                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    AndroidPermissionAssessment.create(
                        capabilityId =
                            request.capability.capabilityId,
                        status =
                            AndroidPermissionAssessmentStatus.DENIED,
                        requiredPermissions =
                            listOf(
                                "android.permission.CAMERA",
                            ),
                    ),
            )

        assertEquals(0, calls)
        assertEquals(AndroidExecutionAttemptStatus.DEFERRED, result.status)
    }

    @Test
    fun `unavailable capability defers without invoking performer`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-004",
            )
        val request = createRequest(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1
                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.UNAVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )

        assertEquals(0, calls)
        assertEquals(AndroidExecutionAttemptStatus.DEFERRED, result.status)
    }

    @Test
    fun `non-ready capability health defers without invoking performer`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-005",
            )
        val request = createRequest(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1
                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.UNAVAILABLE,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )

        assertEquals(0, calls)
        assertEquals(AndroidExecutionAttemptStatus.DEFERRED, result.status)
    }

    @Test
    fun `deferred core execution never reaches Android performer`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-006",
            )
        val request = createRequest(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1
                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.DEFERRED,
                    ),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )

        assertEquals(0, calls)
        assertEquals(AndroidExecutionAttemptStatus.DEFERRED, result.status)
    }

    @Test
    fun `failed core execution preserves constitutional failure without performer invocation`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-007",
            )
        val request = createRequest(traceId)
        val error = createError(traceId)
        var calls = 0

        val adapter =
            DefaultAndroidExecutionAdapter(
                performer =
                    AndroidExecutionPerformer { performerTrace, performerRequest ->
                        calls += 1
                        AndroidExecutionAttemptResult.create(
                            traceId = performerTrace,
                            status =
                                AndroidExecutionAttemptStatus.ATTEMPTED,
                            capabilityId =
                                performerRequest.capability.capabilityId,
                        )
                    },
            )

        val result =
            adapter.execute(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.FAILED,
                        error = error,
                    ),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )

        assertEquals(0, calls)
        assertEquals(AndroidExecutionAttemptStatus.FAILED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `approved execution rejects capability state for different capability`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-008",
            )
        val request = createRequest(traceId)

        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidExecutionAdapter().execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    AndroidCapabilityState.create(
                        capability =
                            createCapability(
                                "capability-android-execution-other",
                            ),
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )
        }
    }

    @Test
    fun `default performer defers instead of fabricating Android action`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-adapter-009",
            )
        val request = createRequest(traceId)

        val result =
            DefaultAndroidExecutionAdapter().execute(
                execution = approvedExecution(traceId, request),
                capabilityState =
                    capabilityState(
                        request = request,
                        availability =
                            CapabilityAvailabilityState.AVAILABLE,
                        health =
                            CapabilityHealthState.READY,
                    ),
                permissionAssessment =
                    permissionAssessment(
                        request = request,
                        status =
                            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
                    ),
            )

        assertEquals(AndroidExecutionAttemptStatus.DEFERRED, result.status)
    }

    private fun approvedExecution(
        traceId: TraceId,
        request: ExecutionRequest,
    ): ExecutionResult {
        return ExecutionResult.create(
            traceId = traceId,
            status = ExecutionStatus.APPROVED,
            request = request,
        )
    }

    private fun capabilityState(
        request: ExecutionRequest,
        availability: CapabilityAvailabilityState,
        health: CapabilityHealthState,
    ): AndroidCapabilityState {
        return AndroidCapabilityState.create(
            capability = request.capability,
            availability = availability,
            health = health,
        )
    }

    private fun permissionAssessment(
        request: ExecutionRequest,
        status: AndroidPermissionAssessmentStatus,
    ): AndroidPermissionAssessment {
        return AndroidPermissionAssessment.create(
            capabilityId = request.capability.capabilityId,
            status = status,
        )
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-android-execution-adapter",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-android-execution-adapter",
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
                                                        ContextSource.TEST,
                                                    trustLevel =
                                                        ContextTrustLevel.VERIFIED,
                                                    securityLevel =
                                                        ContextSecurityLevel.RESTRICTED,
                                                    observedAt =
                                                        DevilTimestamp
                                                            .fromEpochMilliseconds(
                                                                1_754_000_230_000L,
                                                            ),
                                                ),
                                            state =
                                                UnderstandingState.COMPLETE,
                                            summary =
                                                "Bounded understanding was produced.",
                                        ),
                                    state =
                                        DecisionState.SELECTED,
                                    summary =
                                        "A constitutional decision was selected.",
                                ),
                            state = TaskState.CREATED,
                            summary =
                                "A bounded execution task was created.",
                        ),
                    state = PlanState.CREATED,
                    summary =
                        "Use one bounded Android execution adapter.",
                ),
            capability =
                createCapability(
                    "capability-android-execution-test",
                ),
        )
    }

    private fun createCapability(
        id: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(id),
            category = CapabilityCategory.ACTION,
            name = "Android Execution Test Capability",
            description =
                "Represents one bounded execution-adapter test capability.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CONSTITUTIONAL_EXECUTION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_230_500L,
                ),
            summary =
                "Constitutional execution evaluation failed.",
        )
    }
}
