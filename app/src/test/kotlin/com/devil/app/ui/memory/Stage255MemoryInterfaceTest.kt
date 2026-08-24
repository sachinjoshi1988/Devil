package com.devil.app.ui.memory

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 255 Memory Interface governance tests.
 *
 * Stage 255 is presentation only.
 */
class Stage255MemoryInterfaceTest {

    @Test
    fun `memory interface uses locked Devil identity asset`() {
        val source =
            memoryInterfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )
    }

    @Test
    fun `memory interface presents bounded memory metadata`() {
        val source =
            memoryInterfaceSource()

        assertTrue(source.contains("MEMORY VAULT"))
        assertTrue(source.contains("CLASS"))
        assertTrue(source.contains("SENSITIVITY"))
        assertTrue(source.contains("CONFIDENCE"))
        assertTrue(source.contains("RETENTION"))
        assertTrue(source.contains("SOURCE"))
        assertTrue(source.contains("WHY DEVIL MAY REMEMBER THIS"))
    }

    @Test
    fun `memory interface preserves Memory Authority boundaries`() {
        val source =
            memoryInterfaceSource()

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != MEMORY_AUTHORITY.",
            ),
        )

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != MEMORY_AUTHORITY_APPROVAL.",
            ),
        )

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != MEMORY_COMMITMENT.",
            ),
        )

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != MEMORY_PERSISTENCE.",
            ),
        )

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != MEMORY_RECALL.",
            ),
        )

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != DELETION_EXECUTION.",
            ),
        )

        assertTrue(
            source.contains(
                "MEMORY_INTERFACE != VERIFICATION.",
            ),
        )
    }

    @Test
    fun `memory interface does not invoke memory execution architecture`() {
        val source =
            memoryInterfaceSource()

        assertFalse(
            source.contains(
                "MemoryAuthorityCoordinator",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryPersistenceCoordinator",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryRecallCoordinator",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryDeletionCoordinator",
            ),
        )

        assertFalse(
            source.contains(
                ".persist(",
            ),
        )

        assertFalse(
            source.contains(
                ".delete(",
            ),
        )

        assertFalse(
            source.contains(
                ".recall(",
            ),
        )
    }

    @Test
    fun `missing supplied metadata remains explicitly unavailable`() {
        val source =
            memoryInterfaceSource()

        assertTrue(
            source.contains(
                "?: \"Unavailable\"",
            ),
        )

        assertTrue(
            source.contains(
                "No owner-visible reason supplied.",
            ),
        )
    }

    @Test
    fun `Stage 255 does not implement Stage 256 task automation interface`() {
        val source =
            memoryInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 255 does not implement Stage 256 Task & Automation Interface.",
            ),
        )

        assertFalse(
            source.contains(
                "TaskAutomation",
            ),
        )

        assertFalse(
            source.contains(
                "ScheduledTask",
            ),
        )
    }

    private fun memoryInterfaceSource(): String {
        val candidates =
            listOf(
                "app/src/main/kotlin/com/devil/app/ui/memory/DevilMemoryInterface.kt",
                "src/main/kotlin/com/devil/app/ui/memory/DevilMemoryInterface.kt",
            )

        val file =
            candidates
                .map(::File)
                .firstOrNull(File::isFile)

        requireNotNull(file) {
            "Unable to locate Stage 255 DevilMemoryInterface source."
        }

        return file.readText()
    }
}

/*
 * Stage 255 integration checks.
 *
 * These deliberately inspect presentation wiring only.
 */
