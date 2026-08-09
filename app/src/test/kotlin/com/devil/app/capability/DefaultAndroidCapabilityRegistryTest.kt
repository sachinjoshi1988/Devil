package com.devil.app.capability

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
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.capability.CapabilityRegistryStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidCapabilityRegistryTest {

    @Test
    fun `obtain returns unavailable when no Android capabilities are registered`() {
        val traceId =
            TraceId.from(
                "trace-android-capability-registry-001",
            )
        val registry: AndroidCapabilityRegistry =
            DefaultAndroidCapabilityRegistry()

        val result =
            registry.obtain(
                traceId = traceId,
                request = createRequest(traceId),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilityRegistryStatus.UNAVAILABLE,
            result.status,
        )
        assertEquals(
            emptyList(),
            result.capabilities,
        )
        assertNull(result.error)
    }

    @Test
    fun `obtain preserves explicitly registered Android capability contracts`() {
        val traceId =
            TraceId.from(
                "trace-android-capability-registry-002",
            )
        val registrations =
            listOf(
                createCapability(
                    id = "android-capability-test-input",
                    category = CapabilityCategory.INPUT,
                    name = "Android Test Input",
                ),
                createCapability(
                    id = "android-capability-test-action",
                    category = CapabilityCategory.ACTION,
                    name = "Android Test Action",
                ),
            )

        val registry =
            DefaultAndroidCapabilityRegistry(
                registrationSource =
                    AndroidCapabilityRegistrationSource {
                        registrations
                    },
            )

        val result =
            registry.obtain(
                traceId = traceId,
                request = createRequest(traceId),
            )

        assertEquals(
            CapabilityRegistryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            registrations,
            result.capabilities,
        )
        assertNull(result.error)
    }

    @Test
    fun `registration does not add availability health authorization or permission state`() {
        val traceId =
            TraceId.from(
                "trace-android-capability-registry-003",
            )
        val capability =
            createCapability(
                id = "android-capability-registration-only",
                category = CapabilityCategory.ACTION,
                name = "Registration Only Capability",
            )

        val result =
            DefaultAndroidCapabilityRegistry(
                registrationSource =
                    AndroidCapabilityRegistrationSource {
                        listOf(capability)
                    },
            ).obtain(
                traceId = traceId,
                request = createRequest(traceId),
            )

        assertEquals(
            CapabilityRegistryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            capability,
            result.capabilities.single(),
        )
        assertEquals(
            "android-capability-registration-only",
            result.capabilities.single().capabilityId.value,
        )
    }

    @Test
    fun `obtain rejects duplicate Android capability identities`() {
        val traceId =
            TraceId.from(
                "trace-android-capability-registry-004",
            )
        val capability =
            createCapability(
                id = "android-capability-duplicate",
                category = CapabilityCategory.ACTION,
                name = "Duplicate Capability",
            )

        val registry =
            DefaultAndroidCapabilityRegistry(
                registrationSource =
                    AndroidCapabilityRegistrationSource {
                        listOf(
                            capability,
                            capability,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            registry.obtain(
                traceId = traceId,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `obtain rejects capability selection request from another trace`() {
        val registry =
            DefaultAndroidCapabilityRegistry()

        assertFailsWith<IllegalArgumentException> {
            registry.obtain(
                traceId =
                    TraceId.from(
                        "trace-android-capability-registry-005",
                    ),
                request =
                    createRequest(
                        TraceId.from(
                            "trace-android-capability-registry-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `registry takes a bounded snapshot of supplied registrations`() {
        val traceId =
            TraceId.from(
                "trace-android-capability-registry-006",
            )
        val mutableRegistrations =
            mutableListOf(
                createCapability(
                    id = "android-capability-snapshot",
                    category = CapabilityCategory.KNOWLEDGE,
                    name = "Snapshot Capability",
                ),
            )

        val result =
            DefaultAndroidCapabilityRegistry(
                registrationSource =
                    AndroidCapabilityRegistrationSource {
                        mutableRegistrations
                    },
            ).obtain(
                traceId = traceId,
                request = createRequest(traceId),
            )

        mutableRegistrations.clear()

        assertEquals(
            CapabilityRegistryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            1,
            result.capabilities.size,
        )
        assertEquals(
            "android-capability-snapshot",
            result.capabilities.single().capabilityId.value,
        )
    }

    private fun createCapability(
        id: String,
        category: CapabilityCategory,
        name: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(id),
            category = category,
            name = name,
            description =
                "Represents one bounded Android registry test capability without availability, authorization, permission, or execution meaning.",
        )
    }

    private fun createRequest(
        traceId: TraceId,
    ): CapabilitySelectionRequest {
        return CapabilitySelectionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-android-capability-registry",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-android-capability-registry",
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
                                                                1_754_000_208_000L,
                                                            ),
                                                ),
                                            state =
                                                UnderstandingState.COMPLETE,
                                            summary =
                                                "Bounded Android capability registry test understanding.",
                                        ),
                                    state =
                                        DecisionState.SELECTED,
                                    summary =
                                        "Preserve the bounded capability-selection path.",
                                ),
                            state =
                                TaskState.CREATED,
                            summary =
                                "Prepare bounded Android capability registry evaluation.",
                        ),
                    state = PlanState.CREATED,
                    summary =
                        "Use only explicitly registered Android capability contracts.",
                ),
        )
    }
}
