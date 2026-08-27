package com.devil.core.runtime.modelprovider.conversation

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConversationalResponseCoordinatorTest {

    @Test
    fun `accepted constitutional conversation intake may reach inference boundary`() {
        val traceId =
            TraceId.from(
                "trace-stage313-response-001",
            )

        val conversationIntake =
            producedIntake(
                traceId = traceId,
                state = ConversationIntakeState.ACCEPTED,
            )

        var invoked = false

        val coordinator =
            ConversationalResponseCoordinator(
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        invoked = true

                        assertEquals(
                            traceId,
                            request.traceId,
                        )

                        assertEquals(
                            "Hello Devil.",
                            request.content,
                        )

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Hello. How can I help?",
                        )
                    },
            )

        val result =
            coordinator.generate(
                conversationIntake =
                    conversationIntake,
                content = "  Hello Devil.  ",
            )

        assertTrue(invoked)

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            ConversationalModelInferenceStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            "Hello. How can I help?",
            result.generatedOutput,
        )
    }

    @Test
    fun `rejected or deferred constitutional intake cannot invoke model inference`() {
        listOf(
            ConversationIntakeState.REJECTED,
            ConversationIntakeState.DEFERRED,
        ).forEachIndexed { index, state ->
            val traceId =
                TraceId.from(
                    "trace-stage313-response-blocked-$index",
                )

            var invoked = false

            val coordinator =
                ConversationalResponseCoordinator(
                    inferencePort =
                        ConversationalModelInferencePort { request ->
                            invoked = true

                            ConversationalModelInferenceResult.available(
                                traceId = request.traceId,
                                generatedOutput =
                                    "Must not be generated.",
                            )
                        },
                )

            assertFailsWith<IllegalArgumentException> {
                coordinator.generate(
                    conversationIntake =
                        producedIntake(
                            traceId = traceId,
                            state = state,
                        ),
                    content = "Blocked request.",
                )
            }

            assertFalse(
                invoked,
                "Inference must not run for conversation intake state $state.",
            )
        }
    }

    @Test
    fun `non produced authority result cannot invoke model inference`() {
        val traceId =
            TraceId.from(
                "trace-stage313-response-002",
            )

        var invoked = false

        val coordinator =
            ConversationalResponseCoordinator(
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        invoked = true

                        ConversationalModelInferenceResult.available(
                            traceId = request.traceId,
                            generatedOutput =
                                "Must not be generated.",
                        )
                    },
            )

        val deferred =
            ConversationIntakeAuthorityResult.create(
                traceId = traceId,
                status =
                    ConversationIntakeAuthorityStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.generate(
                conversationIntake = deferred,
                content = "Blocked before intake.",
            )
        }

        assertFalse(invoked)
    }

    @Test
    fun `inference result must preserve constitutional trace identity`() {
        val intakeTraceId =
            TraceId.from(
                "trace-stage313-response-003",
            )

        val differentTraceId =
            TraceId.from(
                "trace-stage313-response-004",
            )

        val coordinator =
            ConversationalResponseCoordinator(
                inferencePort =
                    ConversationalModelInferencePort {
                        ConversationalModelInferenceResult.available(
                            traceId = differentTraceId,
                            generatedOutput =
                                "Wrong trace output.",
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.generate(
                conversationIntake =
                    producedIntake(
                        traceId = intakeTraceId,
                        state =
                            ConversationIntakeState.ACCEPTED,
                    ),
                content = "Preserve my trace.",
            )
        }
    }

    @Test
    fun `accepted intake preserves unavailable model result without fabrication`() {
        val traceId =
            TraceId.from(
                "trace-stage313-response-005",
            )

        val coordinator =
            ConversationalResponseCoordinator(
                inferencePort =
                    ConversationalModelInferencePort { request ->
                        ConversationalModelInferenceResult.unavailable(
                            traceId = request.traceId,
                        )
                    },
            )

        val result =
            coordinator.generate(
                conversationIntake =
                    producedIntake(
                        traceId = traceId,
                        state =
                            ConversationIntakeState.ACCEPTED,
                    ),
                content = "Generate something.",
            )

        assertEquals(
            ConversationalModelInferenceStatus.UNAVAILABLE,
            result.status,
        )
    }

    private fun producedIntake(
        traceId: TraceId,
        state: ConversationIntakeState,
    ): ConversationIntakeAuthorityResult {
        val input =
            ConversationInput.create(
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
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_313_000L,
                            ),
                    ),
                content =
                    "Stage 313 conversational response test input.",
            )

        val intake =
            ConversationIntakeResult.create(
                record =
                    ConversationIntakeRecord.create(
                        input = input,
                        state = state,
                        rationale =
                            "Stage 313 bounded test intake.",
                    ),
            )

        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status =
                ConversationIntakeAuthorityStatus.PRODUCED,
            intake = intake,
        )
    }
}
