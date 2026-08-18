package com.devil.core.model.task

import com.devil.core.model.authorization.AuthorizationEvaluationState
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertSame

class CompoundWorkExecutiveReentryStage119Test {

    @Test
    fun `request preserves exact capability reentry provenance and capability`() {
        val capabilityReentry = capabilityReentryRecord()
        val capability =
            CapabilityContract.create(
                capabilityId = CapabilityId.from("stage119-capability"),
                category = CapabilityCategory.ACTION,
                name = "Stage 119 capability",
                description = "Selected capability for Executive re-entry.",
            )

        val record =
            CompoundWorkExecutiveReentryRecord.create(
                request =
                    CompoundWorkExecutiveReentryRequest.create(
                        capabilityReentry = capabilityReentry,
                        capability = capability,
                    ),
            )

        assertSame(capabilityReentry, record.request.capabilityReentry)
        assertSame(capability, record.request.capability)
        assertSame(
            capabilityReentry.request.plan,
            record.request.capabilityReentry.request.plan,
        )
        assertSame(
            capabilityReentry.request.plan.task,
            record.request.capabilityReentry.request.plan.task,
        )
        assertSame(
            capabilityReentry
                .request
                .planReentry
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .freshDecision,
            record.request
                .capabilityReentry
                .request
                .planReentry
                .request
                .taskReentry
                .request
                .reconsideration
                .request
                .freshDecision,
        )
    }

    private fun capabilityReentryRecord(): CompoundWorkCapabilityReentryRecord {
        val currentTrace = TraceId.from("stage119-current")
        val originalTrace = TraceId.from("stage119-original")

        val originalDecision = decision(originalTrace, "Original Decision")
        val freshDecision = decision(currentTrace, "Fresh Decision")

        val predecessor =
            CompoundWorkStep.create(
                position = 1,
                summary = "Completed predecessor.",
            )

        val step =
            CompoundWorkStep.create(
                position = 2,
                summary = "Stage 119 step",
            )

        val originalRequest =
            CompoundWorkRequest.create(
                decision = originalDecision,
                steps = listOf(predecessor, step),
            )

        val continuation =
            CompoundWorkContinuationRecord.create(
                request = originalRequest,
                step = step,
            )

        val reconsideration =
            CompoundWorkReconsiderationRecord.create(
                request =
                    CompoundWorkReconsiderationRequest.create(
                        continuation = continuation,
                        freshDecision = freshDecision,
                    ),
            )

        val taskReentry =
            CompoundWorkTaskReentryRecord.create(
                request =
                    CompoundWorkTaskReentryRequest.create(
                        reconsideration = reconsideration,
                        authorizationState =
                            AuthorizationEvaluationState.AUTHORIZED,
                    ),
            )

        val task =
            TaskRecord.create(
                taskId = TaskId.from("stage119-task"),
                decision = freshDecision,
                state = TaskState.CREATED,
                summary = "Stage 119 task",
            )

        val planReentry =
            CompoundWorkPlanReentryRecord.create(
                request =
                    CompoundWorkPlanReentryRequest.create(
                        taskReentry = taskReentry,
                        task = task,
                    ),
            )

        val plan =
            PlanRecord.create(
                planId = PlanId.from("stage119-plan"),
                task = task,
                state = PlanState.CREATED,
                summary = "Stage 119 plan",
            )

        return CompoundWorkCapabilityReentryRecord.create(
            request =
                CompoundWorkCapabilityReentryRequest.create(
                    planReentry = planReentry,
                    plan = plan,
                ),
        )
    }

    private fun decision(
        traceId: TraceId,
        summary: String,
    ): DecisionRecord {

        val context =
            ContextEnvelope.create(
                traceId = traceId,
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_119_000L,
                    ),
            )

        val understanding =
            UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary = summary,
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }
}
