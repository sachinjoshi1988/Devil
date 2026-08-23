package com.devil.core.runtime.personality

import com.devil.core.model.personality.PersonalityFoundationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage244PersonalityFoundationTest {

    @Test
    fun `valid bounded metadata establishes Stage 244 personality foundation`() {
        val result =
            PersonalityFoundationCoordinator()
                .establish(
                    personalityIdentityDescription =
                        "  One constitutionally governed Devil personality foundation.  ",
                    constitutionalRoleDescription =
                        "  Personality shapes bounded presentation without becoming authority.  ",
                )

        assertEquals(
            PersonalityFoundationStatus.ESTABLISHED,
            result.status,
        )

        val foundation = assertNotNull(result.foundation)

        assertEquals(
            "One constitutionally governed Devil personality foundation.",
            foundation.personalityIdentityDescription,
        )

        assertEquals(
            "Personality shapes bounded presentation without becoming authority.",
            foundation.constitutionalRoleDescription,
        )
    }

    @Test
    fun `blank personality identity keeps Stage 244 deferred`() {
        val result =
            PersonalityFoundationCoordinator()
                .establish(
                    personalityIdentityDescription = "   ",
                    constitutionalRoleDescription =
                        "Bounded constitutional personality role.",
                )

        assertEquals(
            PersonalityFoundationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.foundation)
    }

    @Test
    fun `blank constitutional role keeps Stage 244 deferred`() {
        val result =
            PersonalityFoundationCoordinator()
                .establish(
                    personalityIdentityDescription =
                        "Bounded Devil personality identity.",
                    constitutionalRoleDescription = "   ",
                )

        assertEquals(
            PersonalityFoundationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.foundation)
    }

    @Test
    fun `null personality inputs keep Stage 244 deferred`() {
        val result =
            PersonalityFoundationCoordinator()
                .establish(
                    personalityIdentityDescription = null,
                    constitutionalRoleDescription = null,
                )

        assertEquals(
            PersonalityFoundationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.foundation)
    }

    @Test
    fun `personality foundation record normalizes supplied metadata`() {
        val foundation =
            PersonalityFoundationRecord.create(
                personalityIdentityDescription =
                    "  Normalized Devil personality identity.  ",
                constitutionalRoleDescription =
                    "  Normalized constitutional personality role.  ",
            )

        assertEquals(
            "Normalized Devil personality identity.",
            foundation.personalityIdentityDescription,
        )

        assertEquals(
            "Normalized constitutional personality role.",
            foundation.constitutionalRoleDescription,
        )
    }

    @Test
    fun `personality foundation record rejects blank personality identity`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalityFoundationRecord.create(
                personalityIdentityDescription = "   ",
                constitutionalRoleDescription =
                    "Bounded constitutional personality role.",
            )
        }
    }

    @Test
    fun `personality foundation record rejects blank constitutional role`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalityFoundationRecord.create(
                personalityIdentityDescription =
                    "Bounded Devil personality identity.",
                constitutionalRoleDescription = "   ",
            )
        }
    }

    @Test
    fun `established result preserves exact personality foundation reference`() {
        val foundation =
            PersonalityFoundationRecord.create(
                personalityIdentityDescription =
                    "Exact Stage 244 personality foundation.",
                constitutionalRoleDescription =
                    "Exact Stage 244 constitutional personality role.",
            )

        val result =
            PersonalityFoundationResult.create(
                status = PersonalityFoundationStatus.ESTABLISHED,
                foundation = foundation,
            )

        assertEquals(
            PersonalityFoundationStatus.ESTABLISHED,
            result.status,
        )

        assertSame(
            foundation,
            result.foundation,
        )
    }

    @Test
    fun `established result requires personality foundation record`() {
        assertFailsWith<IllegalArgumentException> {
            PersonalityFoundationResult.create(
                status = PersonalityFoundationStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain personality foundation record`() {
        val foundation =
            PersonalityFoundationRecord.create(
                personalityIdentityDescription =
                    "Must not appear in deferred result.",
                constitutionalRoleDescription =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            PersonalityFoundationResult.create(
                status = PersonalityFoundationStatus.DEFERRED,
                foundation = foundation,
            )
        }
    }

    @Test
    fun `deferred result contains no personality foundation`() {
        val result =
            PersonalityFoundationResult.create(
                status = PersonalityFoundationStatus.DEFERRED,
            )

        assertEquals(
            PersonalityFoundationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.foundation)
    }
}
