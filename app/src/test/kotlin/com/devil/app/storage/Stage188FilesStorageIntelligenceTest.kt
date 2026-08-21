package com.devil.app.storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage188FilesStorageIntelligenceTest {

    @Test
    fun `metadata normalizes supplied path`() {
        val metadata =
            AndroidFileMetadataRecord.create(
                path = "  /storage/example.txt  ",
                sizeBytes = 42L,
                regularFile = true,
            )

        assertEquals("/storage/example.txt", metadata.path)
        assertEquals(42L, metadata.sizeBytes)
        assertEquals(true, metadata.regularFile)
    }

    @Test
    fun `blank path and negative size are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidFileMetadataRecord.create(path = "   ")
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidFileMetadataRecord.create(
                path = "/storage/example.txt",
                sizeBytes = -1L,
            )
        }
    }

    @Test
    fun `supplied metadata becomes available unchanged`() {
        val metadata =
            AndroidFileMetadataRecord.create(
                path = "/storage/example.txt",
            )

        val result =
            AndroidStorageIntelligenceCoordinator()
                .integrate(metadata)

        assertEquals(AndroidStorageIntelligenceStatus.AVAILABLE, result.status)
        assertEquals(metadata, result.metadata)
    }

    @Test
    fun `absent metadata remains deferred`() {
        val result =
            AndroidStorageIntelligenceCoordinator()
                .integrate(null)

        assertEquals(AndroidStorageIntelligenceStatus.DEFERRED, result.status)
        assertNull(result.metadata)
    }

    @Test
    fun `result invariants are enforced`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidStorageIntelligenceResult.create(
                status = AndroidStorageIntelligenceStatus.AVAILABLE,
            )
        }

        val metadata =
            AndroidFileMetadataRecord.create(
                path = "/storage/example.txt",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidStorageIntelligenceResult.create(
                status = AndroidStorageIntelligenceStatus.DEFERRED,
                metadata = metadata,
            )
        }
    }
}
