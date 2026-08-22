package com.devil.app.device

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipCoordinator
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage213DeviceProtocolIntegrationTest {

    @Test
    fun `represented relationship becomes available and preserves exact provenance`() {
        val relationship =
            representedRelationship()

        val result =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = "  devil-device-protocol-v1  ",
                )

        assertEquals(
            AndroidDeviceProtocolIntegrationStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            relationship,
            result.relationshipRepresentation,
        )
        assertEquals(
            "devil-device-protocol-v1",
            result.protocolId,
        )
    }

    @Test
    fun `blank protocol identifier remains deferred`() {
        val relationship =
            representedRelationship()

        val result =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = "   ",
                )

        assertEquals(
            AndroidDeviceProtocolIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(
            relationship,
            result.relationshipRepresentation,
        )
        assertNull(result.protocolId)
    }

    @Test
    fun `missing protocol identifier remains deferred`() {
        val relationship =
            representedRelationship()

        val result =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = null,
                )

        assertEquals(
            AndroidDeviceProtocolIntegrationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.protocolId)
    }

    @Test
    fun `deferred Stage 84 relationship remains deferred`() {
        val deferredRelationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId = TraceId.from("trace-stage213-deferred"),
                status =
                    CrossDeviceRelationshipRepresentationStatus.DEFERRED,
            )

        val result =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = deferredRelationship,
                    protocolId = "devil-device-protocol-v1",
                )

        assertEquals(
            AndroidDeviceProtocolIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(
            deferredRelationship,
            result.relationshipRepresentation,
        )
        assertNull(result.protocolId)
    }

    @Test
    fun `available result requires represented Stage 84 relationship`() {
        val deferredRelationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId = TraceId.from("trace-stage213-invalid"),
                status =
                    CrossDeviceRelationshipRepresentationStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceProtocolIntegrationResult.create(
                status = AndroidDeviceProtocolIntegrationStatus.AVAILABLE,
                relationshipRepresentation = deferredRelationship,
                protocolId = "devil-device-protocol-v1",
            )
        }
    }

    @Test
    fun `available result rejects blank protocol identifier`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceProtocolIntegrationResult.create(
                status = AndroidDeviceProtocolIntegrationStatus.AVAILABLE,
                relationshipRepresentation = representedRelationship(),
                protocolId = "   ",
            )
        }
    }

    @Test
    fun `deferred result rejects protocol metadata`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidDeviceProtocolIntegrationResult.create(
                status = AndroidDeviceProtocolIntegrationStatus.DEFERRED,
                relationshipRepresentation = representedRelationship(),
                protocolId = "devil-device-protocol-v1",
            )
        }
    }

    private fun representedRelationship():
        CrossDeviceRelationshipRepresentationResult {
        return CrossDeviceRelationshipCoordinator()
            .represent(
                traceId = TraceId.from("trace-stage213"),
                sourceEmbodiment =
                    embodiment(
                        id = "embodiment:stage213:android",
                        platform = "android",
                    ),
                targetEmbodiment =
                    embodiment(
                        id = "embodiment:stage213:peer",
                        platform = "peer-platform",
                    ),
                description =
                    "Bounded Stage 213 device protocol relationship.",
            )
    }

    private fun embodiment(
        id: String,
        platform: String,
    ): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId = EmbodimentId.from(id),
            platformId = EmbodimentPlatformId.from(platform),
            description = "Stage 213 bounded embodiment.",
        )
    }
}
