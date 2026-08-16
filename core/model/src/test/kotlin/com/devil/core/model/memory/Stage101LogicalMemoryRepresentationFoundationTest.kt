package com.devil.core.model.memory

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage101LogicalMemoryRepresentationFoundationTest {

    @Test
    fun `logical memory representation preserves explicitly supplied bounded metadata`() {
        val memoryId =
            MemoryId.from(
                "memory-stage101-representation-001",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage101-memory-001",
            )

        val confidence =
            MemoryConfidence.from(
                87,
            )

        val source =
            MemorySource.create(
                sourceId = "source-stage101-owner-input",
                sourceType = "owner-supplied",
            )

        val reason =
            OwnerVisibleMemoryReason.from(
                "Explicitly supplied preference eligible for later constitutional review.",
            )

        val representation =
            LogicalMemoryRepresentation.create(
                memoryId = memoryId,
                subjectIdentityId = subjectIdentityId,
                memoryClass = MemoryClass.PREFERENCE,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = confidence,
                retention = MemoryRetention.LONG_TERM,
                source = source,
                ownerVisibleReason = reason,
                content = "  Preferred response style is concise.  ",
            )

        assertSame(
            memoryId,
            representation.memoryId,
        )

        assertSame(
            subjectIdentityId,
            representation.subjectIdentityId,
        )

        assertEquals(
            MemoryClass.PREFERENCE,
            representation.memoryClass,
        )

        assertEquals(
            MemorySensitivity.PRIVATE,
            representation.sensitivity,
        )

        assertSame(
            confidence,
            representation.confidence,
        )

        assertEquals(
            MemoryRetention.LONG_TERM,
            representation.retention,
        )

        assertSame(
            source,
            representation.source,
        )

        assertSame(
            reason,
            representation.ownerVisibleReason,
        )

        assertEquals(
            "Preferred response style is concise.",
            representation.content,
        )
    }

    @Test
    fun `logical memory representation rejects blank content`() {
        assertFailsWith<IllegalArgumentException> {
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage101-blank-content",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage101-blank-content",
                    ),
                memoryClass = MemoryClass.SEMANTIC,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(70),
                retention = MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId = "source-stage101",
                        sourceType = "explicit",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Bounded test reason.",
                    ),
                content = "   ",
            )
        }
    }

    @Test
    fun `memory class taxonomy remains the locked ten classes`() {
        assertEquals(
            listOf(
                MemoryClass.WORKING,
                MemoryClass.CONVERSATION,
                MemoryClass.PERSONAL,
                MemoryClass.RELATIONSHIP,
                MemoryClass.PREFERENCE,
                MemoryClass.EPISODIC,
                MemoryClass.SEMANTIC,
                MemoryClass.PROCEDURAL,
                MemoryClass.DEVICE,
                MemoryClass.SECURITY,
            ),
            MemoryClass.entries,
        )
    }

    @Test
    fun `memory sensitivity taxonomy remains bounded and memory specific`() {
        assertEquals(
            listOf(
                MemorySensitivity.PUBLIC,
                MemorySensitivity.PRIVATE,
                MemorySensitivity.SENSITIVE,
                MemorySensitivity.HIGHLY_SENSITIVE,
            ),
            MemorySensitivity.entries,
        )
    }

    @Test
    fun `memory retention taxonomy remains bounded`() {
        assertEquals(
            listOf(
                MemoryRetention.SESSION,
                MemoryRetention.SHORT_TERM,
                MemoryRetention.LONG_TERM,
                MemoryRetention.UNTIL_DELETED,
            ),
            MemoryRetention.entries,
        )
    }

    @Test
    fun `representation preserves subject binding without transforming identity`() {
        val subjectIdentityId =
            IdentityId.from(
                "subject-stage101-descriptive-binding",
            )

        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage101-subject-binding",
                    ),
                subjectIdentityId = subjectIdentityId,
                memoryClass = MemoryClass.RELATIONSHIP,
                sensitivity = MemorySensitivity.SENSITIVE,
                confidence = MemoryConfidence.from(60),
                retention = MemoryRetention.SHORT_TERM,
                source =
                    MemorySource.create(
                        sourceId = "source-stage101-relationship",
                        sourceType = "explicit-context",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Explicit relationship context supplied for bounded representation.",
                    ),
                content =
                    "Descriptive relationship context only.",
            )

        assertSame(
            subjectIdentityId,
            representation.subjectIdentityId,
        )
    }
}
