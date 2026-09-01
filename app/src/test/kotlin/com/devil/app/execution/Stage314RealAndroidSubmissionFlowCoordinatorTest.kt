package com.devil.app.execution

import com.devil.app.accessibility.AndroidAccessibilityActionType
import com.devil.app.accessibility.AndroidAccessibilityCapability
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.execution.ExecutionRequest
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

class Stage314RealAndroidSubmissionFlowCoordinatorTest {

    @Test
    fun `open settings arms explicit directive during delegated submission`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-submission-arm",
            )

        val request =
            createExecutionRequest(traceId)

        val store =
            AndroidRealExecutionDirectiveStore()

        var delegateCalls = 0
        var consumedDuringDelegation = false

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            delegateCalls += 1

                            val directive =
                                store.provide(
                                    traceId = traceId,
                                    request = request,
                                )

                            assertEquals(
                                AndroidAccessibilityActionType
                                    .CLICK_VISIBLE_TEXT,
                                directive
                                    ?.accessibilityRequest
                                    ?.actionType,
                            )

                            assertEquals(
                                "Settings",
                                directive
                                    ?.accessibilityRequest
                                    ?.target
                                    ?.text,
                            )

                            consumedDuringDelegation =
                                directive != null

                            return state
                        }
                    },
                directiveStore = store,
            )

        coordinator.submit(
            state =
                ConversationUiState(
                    draft = "  Open Settings  ",
                ),
        )

        assertEquals(1, delegateCalls)
        assertTrue(consumedDuringDelegation)

        assertNull(
            store.provide(
                traceId = traceId,
                request = request,
            ),
        )
    }

    @Test
    fun `unused Stage 314 directive is always cleared after delegation`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-submission-clear",
            )

        val request =
            createExecutionRequest(traceId)

        val store =
            AndroidRealExecutionDirectiveStore()

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            return state
                        }
                    },
                directiveStore = store,
            )

        coordinator.submit(
            state =
                ConversationUiState(
                    draft = "Open Settings",
                ),
        )

        assertNull(
            store.provide(
                traceId = traceId,
                request = request,
            ),
        )
    }

    @Test
    fun `unrelated draft never arms Stage 314 action`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-submission-unrelated",
            )

        val request =
            createExecutionRequest(traceId)

        val store =
            AndroidRealExecutionDirectiveStore()

        var delegateCalls = 0

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            delegateCalls += 1

                            return state
                        }
                    },
                directiveStore = store,
            )

        coordinator.submit(
            state =
                ConversationUiState(
                    draft = "Open Calculator",
                ),
        )

        assertEquals(1, delegateCalls)

        assertNull(
            store.provide(
                traceId = traceId,
                request = request,
            ),
        )
    }

    @Test
    fun `blank draft never arms Stage 314 action`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-submission-blank",
            )

        val store =
            AndroidRealExecutionDirectiveStore()

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            return state
                        }
                    },
                directiveStore = store,
            )

        coordinator.submit(
            state =
                ConversationUiState(
                    draft = "   ",
                ),
        )

        assertNull(
            store.provide(
                traceId = traceId,
                request = createExecutionRequest(traceId),
            ),
        )
    }

    @Test
    fun `already submitting state never arms Stage 314 action`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-submission-active",
            )

        val store =
            AndroidRealExecutionDirectiveStore()

        val coordinator =
            Stage314RealAndroidSubmissionFlowCoordinator(
                submissionCoordinator =
                    object : ConversationSubmissionFlowCoordinator {
                        override fun submit(
                            state: ConversationUiState,
                        ): ConversationUiState {
                            return state
                        }
                    },
                directiveStore = store,
            )

        coordinator.submit(
            state =
                ConversationUiState(
                    draft = "Open Settings",
                    isSubmitting = true,
                ),
        )

        assertNull(
            store.provide(
                traceId = traceId,
                request = createExecutionRequest(traceId),
            ),
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        val context =
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
                    DevilTimestamp.fromEpochMilliseconds(
                        1_756_000_314_200L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Stage 314 governed Android real-device submission.",
            )

        val decision =
            DecisionRecord.create(
                understanding = understanding,
                state =
                    DecisionState.SELECTED,
                summary =
                    "Use the explicitly selected Android accessibility capability.",
            )

        val task =
            TaskRecord.create(
                taskId =
                    TaskId.from(
                        "task-stage-314-submission",
                    ),
                decision = decision,
                state =
                    TaskState.CREATED,
                summary =
                    "Perform one bounded Stage 314 Android accessibility action.",
            )

        val plan =
            PlanRecord.create(
                planId =
                    PlanId.from(
                        "plan-stage-314-submission",
                    ),
                task = task,
                state =
                    PlanState.CREATED,
                summary =
                    "Use the existing constitutional execution path.",
            )

        return ExecutionRequest.create(
            plan = plan,
            capability =
                AndroidAccessibilityCapability.contract,
        )
    }
}
