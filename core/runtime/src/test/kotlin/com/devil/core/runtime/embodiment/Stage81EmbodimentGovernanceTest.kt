package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage81EmbodimentGovernanceTest {

    @Test
    fun `one bounded embodiment may be represented without creating another intelligence`() {
        val traceId =
            TraceId.from(
                "trace-stage81-embodiment-001",
            )

        val result =
            EmbodimentRepresentationCoordinator().represent(
                traceId = traceId,
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:android-primary",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "android",
                    ),
                description =
                    "Primary Android embodiment of the unified Devil runtime.",
            )

        assertEquals(
            traceId,
            result.traceId,
        )
        assertEquals(
            EmbodimentRepresentationStatus.REPRESENTED,
            result.status,
        )

        val embodiment =
            requireNotNull(result.embodiment)

        assertEquals(
            "embodiment:android-primary",
            embodiment.embodimentId.value,
        )
        assertEquals(
            "android",
            embodiment.platformId.value,
        )
        assertEquals(
            "Primary Android embodiment of the unified Devil runtime.",
            embodiment.description,
        )
    }

    @Test
    fun `embodiment identity is normalized but must not be blank`() {
        assertEquals(
            "embodiment:android-primary",
            EmbodimentId.from(
                "  embodiment:android-primary  ",
            ).value,
        )

        assertFailsWith<IllegalArgumentException> {
            EmbodimentId.from("   ")
        }
    }

    @Test
    fun `platform identity is extensible and normalized without claiming implementation`() {
        assertEquals(
            "future-platform",
            EmbodimentPlatformId.from(
                "  future-platform  ",
            ).value,
        )

        assertFailsWith<IllegalArgumentException> {
            EmbodimentPlatformId.from("   ")
        }
    }

    @Test
    fun `embodiment description is normalized and required`() {
        val record =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:test",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "test-platform",
                    ),
                description =
                    "  Bounded test embodiment.  ",
            )

        assertEquals(
            "Bounded test embodiment.",
            record.description,
        )

        assertFailsWith<IllegalArgumentException> {
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:test",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "test-platform",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `blank description remains deferred rather than fabricating embodiment`() {
        val traceId =
            TraceId.from(
                "trace-stage81-embodiment-002",
            )

        val result =
            EmbodimentRepresentationCoordinator().represent(
                traceId = traceId,
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:deferred",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "android",
                    ),
                description = "   ",
            )

        assertEquals(
            EmbodimentRepresentationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.embodiment)
    }

    @Test
    fun `represented result requires one embodiment`() {
        assertFailsWith<IllegalArgumentException> {
            EmbodimentRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage81-result-001",
                    ),
                status =
                    EmbodimentRepresentationStatus.REPRESENTED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle an embodiment`() {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:smuggled",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "android",
                    ),
                description =
                    "Bounded embodiment that must not appear in a deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            EmbodimentRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage81-result-002",
                    ),
                status =
                    EmbodimentRepresentationStatus.DEFERRED,
                embodiment = embodiment,
            )
        }
    }
}
