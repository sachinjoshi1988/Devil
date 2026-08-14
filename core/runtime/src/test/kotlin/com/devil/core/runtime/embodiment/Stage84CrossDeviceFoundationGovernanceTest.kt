package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage84CrossDeviceFoundationGovernanceTest {

    @Test
    fun `two distinct embodiments may form bounded cross device relationship`() {
        val source =
            embodiment(
                id = "embodiment:android-primary",
                platform = "android",
                description =
                    "Primary Android embodiment of the unified Devil runtime.",
            )

        val target =
            embodiment(
                id = "embodiment:pc-primary",
                platform = "pc",
                description =
                    "Primary PC embodiment of the unified Devil runtime.",
            )

        val traceId =
            TraceId.from(
                "trace-stage84-cross-device-001",
            )

        val result =
            CrossDeviceRelationshipCoordinator().represent(
                traceId = traceId,
                sourceEmbodiment = source,
                targetEmbodiment = target,
                description =
                    "Bounded Android-to-PC architectural relationship.",
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
            result.status,
        )

        val relationship =
            requireNotNull(result.relationship)

        assertEquals(
            source.embodimentId,
            relationship.sourceEmbodimentId,
        )

        assertEquals(
            target.embodimentId,
            relationship.targetEmbodimentId,
        )

        assertEquals(
            "Bounded Android-to-PC architectural relationship.",
            relationship.description,
        )
    }

    @Test
    fun `cross device relationship is platform agnostic`() {
        val result =
            CrossDeviceRelationshipCoordinator().represent(
                traceId =
                    TraceId.from(
                        "trace-stage84-cross-device-002",
                    ),
                sourceEmbodiment =
                    embodiment(
                        id = "embodiment:future-a",
                        platform = "future-platform-a",
                        description =
                            "Future embodiment A.",
                    ),
                targetEmbodiment =
                    embodiment(
                        id = "embodiment:future-b",
                        platform = "future-platform-b",
                        description =
                            "Future embodiment B.",
                    ),
                description =
                    "Future cross-device relationship.",
            )

        assertEquals(
            CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
            result.status,
        )
    }

    @Test
    fun `same embodiment cannot form cross device relationship with itself`() {
        val embodiment =
            embodiment(
                id = "embodiment:android-primary",
                platform = "android",
                description =
                    "Primary Android embodiment.",
            )

        val result =
            CrossDeviceRelationshipCoordinator().represent(
                traceId =
                    TraceId.from(
                        "trace-stage84-cross-device-003",
                    ),
                sourceEmbodiment = embodiment,
                targetEmbodiment = embodiment,
                description =
                    "Invalid self relationship.",
            )

        assertEquals(
            CrossDeviceRelationshipRepresentationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.relationship)
    }

    @Test
    fun `blank relationship description remains deferred`() {
        val result =
            CrossDeviceRelationshipCoordinator().represent(
                traceId =
                    TraceId.from(
                        "trace-stage84-cross-device-004",
                    ),
                sourceEmbodiment =
                    embodiment(
                        id = "embodiment:android-primary",
                        platform = "android",
                        description =
                            "Primary Android embodiment.",
                    ),
                targetEmbodiment =
                    embodiment(
                        id = "embodiment:pc-primary",
                        platform = "pc",
                        description =
                            "Primary PC embodiment.",
                    ),
                description = "   ",
            )

        assertEquals(
            CrossDeviceRelationshipRepresentationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.relationship)
    }

    @Test
    fun `relationship record rejects identical embodiment identities`() {
        val embodimentId =
            EmbodimentId.from(
                "embodiment:same",
            )

        assertFailsWith<IllegalArgumentException> {
            CrossDeviceRelationshipRecord.create(
                sourceEmbodimentId = embodimentId,
                targetEmbodimentId = embodimentId,
                description =
                    "Invalid self relationship.",
            )
        }
    }

    @Test
    fun `relationship record normalizes description`() {
        val relationship =
            CrossDeviceRelationshipRecord.create(
                sourceEmbodimentId =
                    EmbodimentId.from(
                        "embodiment:a",
                    ),
                targetEmbodimentId =
                    EmbodimentId.from(
                        "embodiment:b",
                    ),
                description =
                    "  Bounded relationship.  ",
            )

        assertEquals(
            "Bounded relationship.",
            relationship.description,
        )
    }

    @Test
    fun `relationship record rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            CrossDeviceRelationshipRecord.create(
                sourceEmbodimentId =
                    EmbodimentId.from(
                        "embodiment:a",
                    ),
                targetEmbodimentId =
                    EmbodimentId.from(
                        "embodiment:b",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `represented result requires relationship`() {
        assertFailsWith<IllegalArgumentException> {
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage84-result-001",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle relationship`() {
        val relationship =
            CrossDeviceRelationshipRecord.create(
                sourceEmbodimentId =
                    EmbodimentId.from(
                        "embodiment:a",
                    ),
                targetEmbodimentId =
                    EmbodimentId.from(
                        "embodiment:b",
                    ),
                description =
                    "Bounded relationship.",
            )

        assertFailsWith<IllegalArgumentException> {
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage84-result-002",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.DEFERRED,
                relationship = relationship,
            )
        }
    }

    private fun embodiment(
        id: String,
        platform: String,
        description: String,
    ): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId =
                EmbodimentId.from(id),
            platformId =
                EmbodimentPlatformId.from(platform),
            description = description,
        )
    }
}
