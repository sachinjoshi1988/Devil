package com.devil.app.diagnostic

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.app.observation.AndroidObservationEvidence
import com.devil.app.observation.Stage314AndroidPostActionObservationStore
import com.devil.app.outcome.AndroidOutcomeStatus
import com.devil.app.outcome.Stage314AndroidPostActionOutcomeSource
import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.app.verification.AndroidVerificationStatus
import com.devil.app.verification.Stage314AndroidPostActionVerificationSource
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage314PostActionDiagnosticIntegrationTest {

    @Test
    fun `verified condition records diagnostic without changing verification result`() {
        val fixture = fixture()
        val diagnostic = RecordingDiagnostic()

        val result =
            Stage314AndroidPostActionVerificationSource(
                expectationStore = fixture.expectationStore,
                observationStore = fixture.observationStore,
                diagnostic = diagnostic,
            ).verify(
                traceId = fixture.traceId,
                observationEvidence =
                    AndroidObservationEvidence.create(
                        capabilityId = fixture.capabilityId,
                        description =
                            "Genuine bounded Stage 314 Android observation.",
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.VERIFIED,
            result.status,
        )

        assertEquals(
            listOf("EXPECTED_CONDITION_VERIFIED"),
            diagnostic.verificationEvents,
        )

        assertEquals(
            fixture.expectedVisibleText,
            diagnostic.verificationExpectedVisibleTexts.single(),
        )
    }

    @Test
    fun `established outcome records diagnostic without changing outcome result`() {
        val fixture = fixture()
        val diagnostic = RecordingDiagnostic()

        val result =
            Stage314AndroidPostActionOutcomeSource(
                expectationStore = fixture.expectationStore,
                observationStore = fixture.observationStore,
                diagnostic = diagnostic,
            ).establish(
                traceId = fixture.traceId,
                verificationEvidence =
                    AndroidVerificationEvidence.create(
                        capabilityId = fixture.capabilityId,
                        description =
                            "Genuine independently verified Stage 314 Android effect.",
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.ESTABLISHED,
            result.status,
        )

        assertEquals(
            listOf("OUTCOME_ESTABLISHED"),
            diagnostic.outcomeEvents,
        )
    }

    private fun fixture(): Fixture {
        val traceId =
            TraceId.from(
                "trace-stage-314-diagnostic-integration",
            )

        val capabilityId =
            CapabilityId.from(
                "android-accessibility-click-visible-text",
            )

        val expectedVisibleText =
            "Settings, privacy, and permissions presentation"

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val observationStore =
            Stage314AndroidPostActionObservationStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = expectedVisibleText,
        )

        observationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            elements =
                listOf(
                    AndroidScreenElementRecord.create(
                        position = 0,
                        text = expectedVisibleText,
                        contentDescription = null,
                    ),
                ),
        )

        return Fixture(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = expectedVisibleText,
            expectationStore = expectationStore,
            observationStore = observationStore,
        )
    }

    private class RecordingDiagnostic :
        Stage314PostActionDiagnostic {

        val verificationEvents =
            mutableListOf<String>()

        val verificationExpectedVisibleTexts =
            mutableListOf<String?>()

        val outcomeEvents =
            mutableListOf<String>()

        override fun observation(
            traceId: TraceId,
            capabilityId: CapabilityId,
            event: String,
            elements: List<AndroidScreenElementRecord>,
        ) = Unit

        override fun verification(
            traceId: TraceId,
            capabilityId: CapabilityId,
            event: String,
            expectedVisibleText: String?,
        ) {
            verificationEvents += event
            verificationExpectedVisibleTexts +=
                expectedVisibleText
        }

        override fun outcome(
            traceId: TraceId,
            capabilityId: CapabilityId,
            event: String,
        ) {
            outcomeEvents += event
        }
    }

    private data class Fixture(
        val traceId: TraceId,
        val capabilityId: CapabilityId,
        val expectedVisibleText: String,
        val expectationStore:
            Stage314AndroidPostActionExpectationStore,
        val observationStore:
            Stage314AndroidPostActionObservationStore,
    )
}
