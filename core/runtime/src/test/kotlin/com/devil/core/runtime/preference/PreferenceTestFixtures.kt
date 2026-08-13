package com.devil.core.runtime.preference

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
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
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.learning.LearningRequest
import com.devil.core.model.observation.ObservationRequest
import com.devil.core.model.outcome.OutcomeRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.preference.PreferenceLearningCandidate
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.model.verification.VerificationRequest
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

object PreferenceTestFixtures {

    fun learningRequest(): LearningRequest {
        return LearningRequest.create(
            worldModelUpdate =
                WorldModelUpdateRequest.create(
                    outcome =
                        OutcomeRequest.create(
                            verification =
                                VerificationRequest.create(
                                    observation =
                                        ObservationRequest.create(
                                            execution =
                                                ExecutionRequest.create(
                                                    plan =
                                                        PlanRecord.create(
                                                            planId =
                                                                PlanId.from(
                                                                    "plan-stage72-preference-001",
                                                                ),
                                                            task =
                                                                TaskRecord.create(
                                                                    taskId =
                                                                        TaskId.from(
                                                                            "task-stage72-preference-001",
                                                                        ),
                                                                    decision =
                                                                        DecisionRecord.create(
                                                                            understanding =
                                                                                UnderstandingRecord.create(
                                                                                    context =
                                                                                        ContextEnvelope.create(
                                                                                            traceId =
                                                                                                TraceId.from(
                                                                                                    "trace-stage72-preference-learning-001",
                                                                                                ),
                                                                                            schemaVersion =
                                                                                                SchemaVersion.from(
                                                                                                    1,
                                                                                                ),
                                                                                            source =
                                                                                                ContextSource.TEXT,
                                                                                            trustLevel =
                                                                                                ContextTrustLevel.VERIFIED,
                                                                                            securityLevel =
                                                                                                ContextSecurityLevel.RESTRICTED,
                                                                                            observedAt =
                                                                                                DevilTimestamp.fromEpochMilliseconds(
                                                                                                    1_754_000_200_000L,
                                                                                                ),
                                                                                        ),
                                                                                    state =
                                                                                        UnderstandingState.COMPLETE,
                                                                                    summary =
                                                                                        "Use Google Maps.",
                                                                                ),
                                                                            state =
                                                                                DecisionState.SELECTED,
                                                                            summary =
                                                                                "Use Google Maps.",
                                                                        ),
                                                                    state =
                                                                        TaskState.CREATED,
                                                                    summary =
                                                                        "Use Google Maps.",
                                                                ),
                                                            state =
                                                                PlanState.CREATED,
                                                            summary =
                                                                "Use the approved Maps capability.",
                                                        ),
                                                    capability =
                                                        CapabilityContract.create(
                                                            capabilityId =
                                                                CapabilityId.from(
                                                                    "capability-maps",
                                                                ),
                                                            category =
                                                                CapabilityCategory.ACTION,
                                                            name =
                                                                "Maps",
                                                            description =
                                                                "Performs one bounded Maps action.",
                                                        ),
                                                ),
                                        ),
                                ),
                        ),
                ),
        )
    }

    fun candidate(): PreferenceLearningCandidate {
        val first =
            TraceId.from(
                "trace-stage72-preference-evidence-001",
            )

        val second =
            TraceId.from(
                "trace-stage72-preference-evidence-002",
            )

        val third =
            TraceId.from(
                "trace-stage72-preference-evidence-003",
            )

        return PreferenceLearningCandidate.create(
            key = "usual-map-app",
            value = "Google Maps",
            confidence = 2.0 / 3.0,
            supportingEvidenceCount = 2,
            totalEvidenceCount = 3,
            supportingTraceIds =
                listOf(
                    first,
                    second,
                ),
            evidenceTraceIds =
                listOf(
                    first,
                    second,
                    third,
                ),
        )
    }
}
