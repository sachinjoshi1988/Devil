package com.devil.app.device

import com.devil.app.capability.AndroidCapabilityState
import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.conversation.ConversationEntryId
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationUiState
import com.devil.app.modelprovider.conversation.AndroidConversationIntakeEvidenceStore
import com.devil.app.modelprovider.conversation.AndroidConversationalResponseCompositionCoordinator
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferencePort
import com.devil.core.runtime.modelprovider.conversation.ConversationalModelInferenceResult
import com.devil.core.runtime.modelprovider.conversation.ConversationalResponseCoordinator
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Stage337M local-first response-composition proof.
 *
 * LOCAL_DEVICE_FACT != MODEL_OUTPUT.
 * DEVICE_KNOWLEDGE_CLAIM != DEVICE_FACT.
 * BATTERY_QUERY != BATTERY_FACT.
 * PAID_PROVIDER_UNAVAILABLE != DEVICE_KNOWLEDGE_UNAVAILABLE.
 * KNOWLEDGE_PRESENTATION != RUNTIME_STATUS.
 * KNOWLEDGE_PRESENTATION != VERIFIED_OUTCOME.
 */
class Stage337MDeviceKnowledgeResponseCompositionTest {

    @Test
    fun `supported claimed device query appends genuine local knowledge and suppresses model`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-local-model",
            )

        val queryStore =
            Stage337MDeviceKnowledgeQueryStore().apply {
                claim(traceId)
                record(
                    traceId = traceId,
                    queryType =
                        AndroidDeviceKnowledgeQueryType.DEVICE_MODEL,
                )
            }

        val intakeStore =
            acceptedStore(
                traceId = traceId,
                content = "What is my device model?",
            )

        var inferenceCalls = 0

        val coordinator =
            coordinator(
                queryStore = queryStore,
                intakeStore = intakeStore,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "This model output must not compete with local device state.",
                        )
                    },
            )

        val result =
            coordinator.generateAndAppend(
                state = ConversationUiState(),
                runtimeTraceId = traceId,
            )

        assertEquals(0, inferenceCalls)
        assertEquals(1, result.entries.size)

        val knowledge =
            result.entries.single()

        assertEquals(
            ConversationEntryRole.KNOWLEDGE,
            knowledge.role,
        )
        assertEquals(
            traceId,
            knowledge.traceId,
        )
        assertEquals(
            "Xiaomi Redmi Note 12.",
            knowledge.content,
        )
    }

    @Test
    fun `unsupported claimed battery query suppresses model without inventing fact`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-local-battery",
            )

        val queryStore =
            Stage337MDeviceKnowledgeQueryStore().apply {
                claim(traceId)
            }

        val intakeStore =
            acceptedStore(
                traceId = traceId,
                content = "What's my battery level?",
            )

        var inferenceCalls = 0

        val result =
            coordinator(
                queryStore = queryStore,
                intakeStore = intakeStore,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Battery is 75 percent.",
                        )
                    },
            ).generateAndAppend(
                state = ConversationUiState(),
                runtimeTraceId = traceId,
            )

        assertEquals(0, inferenceCalls)
        assertEquals(
            emptyList(),
            result.entries,
        )
    }

    @Test
    fun `claimed supported query with unavailable capability fails closed without model fallback`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-local-unavailable",
            )

        val queryStore =
            Stage337MDeviceKnowledgeQueryStore().apply {
                claim(traceId)
                record(
                    traceId = traceId,
                    queryType =
                        AndroidDeviceKnowledgeQueryType.ANDROID_VERSION,
                )
            }

        val intakeStore =
            acceptedStore(
                traceId = traceId,
                content =
                    "What Android version am I using?",
            )

        var inferenceCalls = 0

        val result =
            coordinator(
                queryStore = queryStore,
                intakeStore = intakeStore,
                availability =
                    CapabilityAvailabilityState.UNAVAILABLE,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Android version guessed by model.",
                        )
                    },
            ).generateAndAppend(
                state = ConversationUiState(),
                runtimeTraceId = traceId,
            )

        assertEquals(0, inferenceCalls)
        assertEquals(
            emptyList(),
            result.entries,
        )
    }

    @Test
    fun `unclaimed general conversation preserves existing model response path`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-general-model",
            )

        val intakeStore =
            acceptedStore(
                traceId = traceId,
                content = "Who is Ada Lovelace?",
            )

        var inferenceCalls = 0

        val result =
            coordinator(
                queryStore =
                    Stage337MDeviceKnowledgeQueryStore(),
                intakeStore = intakeStore,
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        inferenceCalls += 1

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "General model response.",
                        )
                    },
            ).generateAndAppend(
                state = ConversationUiState(),
                runtimeTraceId = traceId,
            )

        assertEquals(1, inferenceCalls)
        assertEquals(1, result.entries.size)
        assertEquals(
            ConversationEntryRole.ASSISTANT,
            result.entries.single().role,
        )
        assertEquals(
            "General model response.",
            result.entries.single().content,
        )
    }

    private fun coordinator(
        queryStore: Stage337MDeviceKnowledgeQueryStore,
        intakeStore: AndroidConversationIntakeEvidenceStore,
        availability:
            CapabilityAvailabilityState =
            CapabilityAvailabilityState.AVAILABLE,
        health:
            CapabilityHealthState =
            CapabilityHealthState.READY,
        inferencePort: ConversationalModelInferencePort,
    ): AndroidConversationalResponseCompositionCoordinator {
        val interactionCoordinator =
            ConversationInteractionCoordinator()

        val entryIdProvider =
            object : ConversationEntryIdProvider {
                override fun provide(): ConversationEntryId {
                    return ConversationEntryId.from(
                        "stage337m-response-entry",
                    )
                }
            }

        val capabilityStateProvider =
            AndroidCapabilityStateProvider { capability ->
                AndroidCapabilityState.create(
                    capability = capability,
                    availability = availability,
                    health = health,
                )
            }

        val queryCoordinator =
            AndroidDeviceKnowledgeQueryCoordinator(
                source =
                    object : AndroidDeviceKnowledgeSource {
                        override fun snapshot():
                            AndroidDeviceKnowledgeSnapshot {
                            return AndroidDeviceKnowledgeSnapshot.create(
                                sdkInt = 34,
                                androidRelease = "14",
                                manufacturer = "Xiaomi",
                                model = "Redmi Note 12",
                                device = "topaz",
                                product = "topaz_in",
                            )
                        }
                    },
            )

        val localCoordinator =
            Stage337MDeviceKnowledgeResponseCompositionCoordinator(
                queryStore = queryStore,
                capabilityStateProvider =
                    capabilityStateProvider,
                queryCoordinator =
                    queryCoordinator,
                interactionCoordinator =
                    interactionCoordinator,
                entryIdProvider =
                    entryIdProvider,
            )

        return AndroidConversationalResponseCompositionCoordinator(
            intakeEvidenceStore = intakeStore,
            responseCoordinator =
                ConversationalResponseCoordinator(
                    inferencePort =
                        inferencePort,
                ),
            interactionCoordinator =
                interactionCoordinator,
            entryIdProvider =
                entryIdProvider,
            deviceKnowledgeResponseCompositionCoordinator =
                localCoordinator,
        )
    }

    private fun acceptedStore(
        traceId: TraceId,
        content: String,
    ): AndroidConversationIntakeEvidenceStore {
        val store =
            AndroidConversationIntakeEvidenceStore()

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
                        1_788_599_337_300L,
                    ),
            )

        val input =
            ConversationInput.create(
                context = context,
                content = content,
            )

        store.observe(
            ConversationIntakeAuthorityResult.create(
                traceId = traceId,
                status =
                    ConversationIntakeAuthorityStatus.PRODUCED,
                intake =
                    ConversationIntakeResult.create(
                        record =
                            ConversationIntakeRecord.create(
                                input = input,
                                state =
                                    ConversationIntakeState.ACCEPTED,
                                rationale =
                                    "Stage337M accepted bounded conversation intake.",
                            ),
                    ),
            ),
        )

        return store
    }
}
