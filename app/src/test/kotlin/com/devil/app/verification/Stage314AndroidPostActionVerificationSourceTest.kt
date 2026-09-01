package com.devil.app.verification

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.app.observation.AndroidObservationEvidence
import com.devil.app.observation.Stage314AndroidPostActionObservationStore
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage314AndroidPostActionVerificationSourceTest {

    @Test
    fun `matching actual visible text independently verifies expected condition`() {
        val fixture =
            fixture(
                expectedText =
                    "Settings, privacy, and permissions presentation",
                observedText =
                    "  SETTINGS,   PRIVACY, AND PERMISSIONS PRESENTATION ",
            )

        val result =
            fixture.source.verify(
                traceId = fixture.traceId,
                observationEvidence =
                    observationEvidence(
                        fixture.capabilityId,
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.VERIFIED,
            result.status,
        )

        assertEquals(
            fixture.capabilityId,
            result.evidence?.capabilityId,
        )
    }

    @Test
    fun `matching content description may independently verify expected condition`() {
        val fixture =
            fixture(
                expectedText =
                    "Settings, privacy, and permissions presentation",
                observedText = null,
                observedContentDescription =
                    "settings, privacy, and permissions presentation",
            )

        val result =
            fixture.source.verify(
                traceId = fixture.traceId,
                observationEvidence =
                    observationEvidence(
                        fixture.capabilityId,
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.VERIFIED,
            result.status,
        )
    }

    @Test
    fun `different observed screen remains deferred`() {
        val fixture =
            fixture(
                expectedText =
                    "Settings, privacy, and permissions presentation",
                observedText =
                    "MAIN CONVERSATION",
            )

        val result =
            fixture.source.verify(
                traceId = fixture.traceId,
                observationEvidence =
                    observationEvidence(
                        fixture.capabilityId,
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.evidence)
    }

    @Test
    fun `missing expectation remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-verification-no-expectation",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val observationStore =
            Stage314AndroidPostActionObservationStore()

        observationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            elements =
                listOf(
                    element(
                        text =
                            "Settings, privacy, and permissions presentation",
                    ),
                ),
        )

        val source =
            Stage314AndroidPostActionVerificationSource(
                expectationStore =
                    Stage314AndroidPostActionExpectationStore(),
                observationStore =
                    observationStore,
            )

        val result =
            source.verify(
                traceId = traceId,
                observationEvidence =
                    observationEvidence(
                        capabilityId,
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `missing actual observation remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-verification-no-observation",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText =
                "Settings, privacy, and permissions presentation",
        )

        val source =
            Stage314AndroidPostActionVerificationSource(
                expectationStore = expectationStore,
                observationStore =
                    Stage314AndroidPostActionObservationStore(),
            )

        val result =
            source.verify(
                traceId = traceId,
                observationEvidence =
                    observationEvidence(
                        capabilityId,
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `foreign trace cannot verify stored expectation and observation`() {
        val fixture =
            fixture(
                expectedText =
                    "Settings, privacy, and permissions presentation",
                observedText =
                    "Settings, privacy, and permissions presentation",
            )

        val result =
            fixture.source.verify(
                traceId =
                    TraceId.from(
                        "trace-stage-314-verification-foreign",
                    ),
                observationEvidence =
                    observationEvidence(
                        fixture.capabilityId,
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `foreign capability cannot verify stored expectation and observation`() {
        val fixture =
            fixture(
                expectedText =
                    "Settings, privacy, and permissions presentation",
                observedText =
                    "Settings, privacy, and permissions presentation",
            )

        val result =
            fixture.source.verify(
                traceId = fixture.traceId,
                observationEvidence =
                    observationEvidence(
                        CapabilityId.from(
                            "stage-314-foreign-capability",
                        ),
                    ),
            )

        assertEquals(
            AndroidVerificationStatus.DEFERRED,
            result.status,
        )
    }

    private fun fixture(
        expectedText: String,
        observedText: String?,
        observedContentDescription: String? = null,
    ): Fixture {
        val traceId =
            TraceId.from(
                "trace-stage-314-verification",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val observationStore =
            Stage314AndroidPostActionObservationStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = expectedText,
        )

        observationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            elements =
                listOf(
                    element(
                        text = observedText,
                        contentDescription =
                            observedContentDescription,
                    ),
                ),
        )

        return Fixture(
            traceId = traceId,
            capabilityId = capabilityId,
            source =
                Stage314AndroidPostActionVerificationSource(
                    expectationStore =
                        expectationStore,
                    observationStore =
                        observationStore,
                ),
        )
    }

    private fun observationEvidence(
        capabilityId: CapabilityId,
    ): AndroidObservationEvidence {
        return AndroidObservationEvidence.create(
            capabilityId = capabilityId,
            description =
                "Genuine bounded Stage 314 Android observation.",
        )
    }

    private fun element(
        text: String?,
        contentDescription: String? = null,
    ): AndroidScreenElementRecord {
        return AndroidScreenElementRecord.create(
            position = 0,
            text = text,
            contentDescription =
                contentDescription,
        )
    }

    private data class Fixture(
        val traceId: TraceId,
        val capabilityId: CapabilityId,
        val source:
            Stage314AndroidPostActionVerificationSource,
    )
}
