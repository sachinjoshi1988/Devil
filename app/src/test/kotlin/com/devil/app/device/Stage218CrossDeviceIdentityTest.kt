package com.devil.app.device

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.identity.IdentityId
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage218CrossDeviceIdentityTest {

    @Test
    fun `represented relationship and identity become available with exact provenance`() {
        val relationship =
            representedRelationship()

        val identity =
            IdentityId.from(
                "identity:stage218:subject",
            )

        val result =
            AndroidCrossDeviceIdentityCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    identityId = identity,
                )

        assertEquals(
            AndroidCrossDeviceIdentityStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            relationship,
            result.relationshipRepresentation,
        )
        assertSame(
            identity,
            result.identityId,
        )
    }

    @Test
    fun `missing identity keeps represented relationship deferred`() {
        val relationship =
            representedRelationship()

        val result =
            AndroidCrossDeviceIdentityCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    identityId = null,
                )

        assertEquals(
            AndroidCrossDeviceIdentityStatus.DEFERRED,
            result.status,
        )
        assertSame(
            relationship,
            result.relationshipRepresentation,
        )
        assertEquals(
            null,
            result.identityId,
        )
    }

    @Test
    fun `deferred Stage 84 relationship keeps cross device identity deferred`() {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage218-deferred",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.DEFERRED,
            )

        val result =
            AndroidCrossDeviceIdentityCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    identityId =
                        IdentityId.from(
                            "identity:stage218:deferred",
                        ),
                )

        assertEquals(
            AndroidCrossDeviceIdentityStatus.DEFERRED,
            result.status,
        )
        assertSame(
            relationship,
            result.relationshipRepresentation,
        )
        assertEquals(
            null,
            result.identityId,
        )
    }

    @Test
    fun `available result requires represented Stage 84 relationship`() {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage218-invalid",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceIdentityResult.create(
                status = AndroidCrossDeviceIdentityStatus.AVAILABLE,
                relationshipRepresentation = relationship,
                identityId =
                    IdentityId.from(
                        "identity:stage218:invalid",
                    ),
            )
        }
    }

    @Test
    fun `available result requires identity`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceIdentityResult.create(
                status = AndroidCrossDeviceIdentityStatus.AVAILABLE,
                relationshipRepresentation = representedRelationship(),
                identityId = null,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle identity`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidCrossDeviceIdentityResult.create(
                status = AndroidCrossDeviceIdentityStatus.DEFERRED,
                relationshipRepresentation = representedRelationship(),
                identityId =
                    IdentityId.from(
                        "identity:stage218:smuggled",
                    ),
            )
        }
    }

    private fun representedRelationship():
        CrossDeviceRelationshipRepresentationResult {
        return CrossDeviceRelationshipRepresentationResult.create(
            traceId =
                TraceId.from(
                    "trace-stage218-represented",
                ),
            status =
                CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
            relationship =
                CrossDeviceRelationshipRecord.create(
                    sourceEmbodimentId =
                        EmbodimentId.from(
                            "embodiment:stage218:source",
                        ),
                    targetEmbodimentId =
                        EmbodimentId.from(
                            "embodiment:stage218:target",
                        ),
                    description =
                        "Bounded Stage 218 cross-device relationship.",
                ),
        )
    }
}
