package com.devil.core.runtime.task

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
import com.devil.core.model.task.TaskCreationRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTaskAuthorityTest {

    @Test
    fun `createTask creates bounded task using default trace-derived identity`() {
        val context = createContext(
            "trace-task-default-001",
        )

        val result = DefaultTaskAuthority().createTask(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                createUnderstanding(context),
            decision = createDecision(context),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            TaskAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(
            TaskId.from("task:trace-task-default-001"),
            result.task?.taskId,
        )
        assertEquals(
            TaskState.CREATED,
            result.task?.state,
        )
        assertEquals(
            DecisionState.SELECTED,
            result.task?.decision?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `createTask coordinates selected decision through identity resolver and mapper`() {
        val context = createContext(
            "trace-task-default-002",
        )
        val taskId = TaskId.from(
            "task-default-authority-002",
        )
        val authority = DefaultTaskAuthority(
            taskIdentityProvider = object :
                TaskIdentityProvider {
                override fun provide(
                    traceId: TraceId,
                    request: TaskCreationRequest,
                ): TaskIdentityProvisionResult {
                    return TaskIdentityProvisionResult.create(
                        traceId = traceId,
                        status =
                            TaskIdentityProvisionStatus.AVAILABLE,
                        taskId = taskId,
                    )
                }
            },
        )

        val result = authority.createTask(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                createUnderstanding(context),
            decision = createDecision(context),
        )

        assertEquals(
            TaskAuthorityStatus.CREATED,
            result.status,
        )
        assertEquals(taskId, result.task?.taskId)
        assertEquals(
            TaskState.CREATED,
            result.task?.state,
        )
        assertEquals(
            DecisionState.SELECTED,
            result.task?.decision?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `createTask preserves failed request error`() {
        val context = createContext(
            "trace-task-default-003",
        )
        val error = createError(
            context.traceId,
            "TASK_CREATION_REQUEST_FAILED",
        )
        val authority = DefaultTaskAuthority(
            requestProvider = object :
                TaskCreationRequestProvider {
                override fun provide(
                    decision: DecisionAuthorityResult,
                ): TaskCreationRequestResult {
                    return TaskCreationRequestResult.create(
                        traceId = context.traceId,
                        status =
                            TaskCreationRequestStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = authority.createTask(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                createUnderstanding(context),
            decision = createDecision(context),
        )

        assertEquals(
            TaskAuthorityStatus.FAILED,
            result.status,
        )
        assertNull(result.task)
        assertEquals(error, result.error)
    }

    @Test
    fun `createTask preserves failed task identity error`() {
        val context = createContext(
            "trace-task-default-004",
        )
        val error = createError(
            context.traceId,
            "TASK_IDENTITY_PROVISION_FAILED",
        )
        val authority = DefaultTaskAuthority(
            taskIdentityProvider = object :
                TaskIdentityProvider {
                override fun provide(
                    traceId: TraceId,
                    request: TaskCreationRequest,
                ): TaskIdentityProvisionResult {
                    return TaskIdentityProvisionResult.create(
                        traceId = traceId,
                        status =
                            TaskIdentityProvisionStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = authority.createTask(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                createUnderstanding(context),
            decision = createDecision(context),
        )

        assertEquals(
            TaskAuthorityStatus.FAILED,
            result.status,
        )
        assertNull(result.task)
        assertEquals(error, result.error)
    }

    @Test
    fun `createTask rejects identity result from a different trace`() {
        val context = createContext(
            "trace-task-default-005",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultTaskAuthority().createTask(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-task-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
            )
        }
    }

    @Test
    fun `createTask rejects trust result from a different trace`() {
        val context = createContext(
            "trace-task-default-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultTaskAuthority().createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from(
                        "trace-task-trust-other",
                    ),
                ),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
            )
        }
    }

    @Test
    fun `createTask rejects authorization result from a different trace`() {
        val context = createContext(
            "trace-task-default-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultTaskAuthority().createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from(
                        "trace-task-authorization-other",
                    ),
                ),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
            )
        }
    }

    @Test
    fun `createTask rejects understanding result from a different trace`() {
        val context = createContext(
            "trace-task-default-008",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultTaskAuthority().createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-task-understanding-other",
                        ),
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
                decision = createDecision(context),
            )
        }
    }

    @Test
    fun `createTask rejects decision result from a different trace`() {
        val context = createContext(
            "trace-task-default-009",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultTaskAuthority().createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = DecisionAuthorityResult.create(
                    traceId = TraceId.from(
                        "trace-task-decision-other",
                    ),
                    status =
                        DecisionAuthorityStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `createTask rejects request result from a different trace`() {
        val context = createContext(
            "trace-task-default-010",
        )
        val authority = DefaultTaskAuthority(
            requestProvider = object :
                TaskCreationRequestProvider {
                override fun provide(
                    decision: DecisionAuthorityResult,
                ): TaskCreationRequestResult {
                    return TaskCreationRequestResult.create(
                        traceId = TraceId.from(
                            "trace-task-request-other",
                        ),
                        status =
                            TaskCreationRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
            )
        }
    }

    @Test
    fun `createTask rejects task identity result from a different trace`() {
        val context = createContext(
            "trace-task-default-011",
        )
        val authority = DefaultTaskAuthority(
            taskIdentityProvider = object :
                TaskIdentityProvider {
                override fun provide(
                    traceId: TraceId,
                    request: TaskCreationRequest,
                ): TaskIdentityProvisionResult {
                    return TaskIdentityProvisionResult.create(
                        traceId = TraceId.from(
                            "trace-task-identity-result-other",
                        ),
                        status =
                            TaskIdentityProvisionStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
            )
        }
    }

    @Test
    fun `createTask rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-task-default-012",
        )
        val authority = DefaultTaskAuthority(
            taskIdentityProvider = object :
                TaskIdentityProvider {
                override fun provide(
                    traceId: TraceId,
                    request: TaskCreationRequest,
                ): TaskIdentityProvisionResult {
                    return TaskIdentityProvisionResult.create(
                        traceId = traceId,
                        status =
                            TaskIdentityProvisionStatus.AVAILABLE,
                        taskId = TaskId.from(
                            "task-default-authority-012",
                        ),
                    )
                }
            },
            resultMapper = object :
                TaskCreationResultMapper {
                override fun map(
                    traceId: TraceId,
                    task: TaskRecord,
                ): TaskAuthorityResult {
                    return TaskAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-task-mapper-other",
                        ),
                        status =
                            TaskAuthorityStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.createTask(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createUnderstanding(context),
                decision = createDecision(context),
            )
        }
    }

    private fun createIdentity(
        traceId: TraceId,
    ): IdentityResult {
        return IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.UNRESOLVED,
        )
    }

    private fun createTrust(
        traceId: TraceId,
    ): TrustResult {
        return TrustResult.create(
            traceId = traceId,
            status = TrustStatus.DEFERRED,
        )
    }

    private fun createAuthorization(
        traceId: TraceId,
    ): AuthorizationResult {
        return AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.DEFERRED,
        )
    }

    private fun createUnderstanding(
        context: ContextEnvelope,
    ): UnderstandingAuthorityResult {
        return UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status =
                UnderstandingAuthorityStatus.PRODUCED,
            understanding = UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "Bounded understanding was produced.",
            ),
        )
    }

    private fun createDecision(
        context: ContextEnvelope,
    ): DecisionAuthorityResult {
        return DecisionAuthorityResult.create(
            traceId = context.traceId,
            status = DecisionAuthorityStatus.PRODUCED,
            decision = DecisionRecord.create(
                understanding = UnderstandingRecord.create(
                    context = context,
                    state = UnderstandingState.COMPLETE,
                    summary =
                        "Bounded understanding was produced.",
                ),
                state = DecisionState.SELECTED,
                summary =
                    "Bounded constitutional decision was selected.",
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
                    1_754_000_084_500L,
                ),
            summary =
                "Bounded task authority dependency failed.",
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_084_000L,
                ),
        )
    }
}
