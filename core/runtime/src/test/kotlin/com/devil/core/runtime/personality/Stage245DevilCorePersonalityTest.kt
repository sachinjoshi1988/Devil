package com.devil.core.runtime.personality

import com.devil.core.model.personality.DevilCorePersonalityRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage245DevilCorePersonalityTest {

    private fun establishedFoundation(): PersonalityFoundationResult {
        return PersonalityFoundationCoordinator()
            .establish(
                personalityIdentityDescription =
                    "One constitutionally governed Devil personality foundation.",
                constitutionalRoleDescription =
                    "Personality shapes bounded presentation without becoming authority.",
            )
    }

    @Test
    fun `valid Stage 244 foundation and bounded metadata establish Stage 245 core personality`() {
        val stage244 = establishedFoundation()

        val result =
            DevilCorePersonalityCoordinator()
                .establish(
                    personalityFoundation = stage244,
                    coreCharacterDescription =
                        "  Stable bounded Devil core character.  ",
                    interactionPrinciplesDescription =
                        "  Consistent bounded interaction principles.  ",
                )

        assertEquals(
            DevilCorePersonalityStatus.ESTABLISHED,
            result.status,
        )

        val corePersonality = assertNotNull(result.corePersonality)

        assertSame(
            stage244.foundation,
            corePersonality.personalityFoundation,
        )

        assertEquals(
            "Stable bounded Devil core character.",
            corePersonality.coreCharacterDescription,
        )

        assertEquals(
            "Consistent bounded interaction principles.",
            corePersonality.interactionPrinciplesDescription,
        )
    }

    @Test
    fun `deferred Stage 244 foundation keeps Stage 245 deferred`() {
        val stage244 =
            PersonalityFoundationCoordinator()
                .establish(
                    personalityIdentityDescription = null,
                    constitutionalRoleDescription = null,
                )

        val result =
            DevilCorePersonalityCoordinator()
                .establish(
                    personalityFoundation = stage244,
                    coreCharacterDescription =
                        "Bounded core character.",
                    interactionPrinciplesDescription =
                        "Bounded interaction principles.",
                )

        assertEquals(
            DevilCorePersonalityStatus.DEFERRED,
            result.status,
        )

        assertNull(result.corePersonality)
    }

    @Test
    fun `blank core character keeps Stage 245 deferred`() {
        val result =
            DevilCorePersonalityCoordinator()
                .establish(
                    personalityFoundation = establishedFoundation(),
                    coreCharacterDescription = "   ",
                    interactionPrinciplesDescription =
                        "Bounded interaction principles.",
                )

        assertEquals(
            DevilCorePersonalityStatus.DEFERRED,
            result.status,
        )

        assertNull(result.corePersonality)
    }

    @Test
    fun `blank interaction principles keep Stage 245 deferred`() {
        val result =
            DevilCorePersonalityCoordinator()
                .establish(
                    personalityFoundation = establishedFoundation(),
                    coreCharacterDescription =
                        "Bounded core character.",
                    interactionPrinciplesDescription = "   ",
                )

        assertEquals(
            DevilCorePersonalityStatus.DEFERRED,
            result.status,
        )

        assertNull(result.corePersonality)
    }

    @Test
    fun `null Stage 245 metadata keeps Stage 245 deferred`() {
        val result =
            DevilCorePersonalityCoordinator()
                .establish(
                    personalityFoundation = establishedFoundation(),
                    coreCharacterDescription = null,
                    interactionPrinciplesDescription = null,
                )

        assertEquals(
            DevilCorePersonalityStatus.DEFERRED,
            result.status,
        )

        assertNull(result.corePersonality)
    }

    @Test
    fun `core personality record normalizes supplied metadata`() {
        val foundation =
            assertNotNull(establishedFoundation().foundation)

        val record =
            DevilCorePersonalityRecord.create(
                personalityFoundation = foundation,
                coreCharacterDescription =
                    "  Normalized core character.  ",
                interactionPrinciplesDescription =
                    "  Normalized interaction principles.  ",
            )

        assertSame(
            foundation,
            record.personalityFoundation,
        )

        assertEquals(
            "Normalized core character.",
            record.coreCharacterDescription,
        )

        assertEquals(
            "Normalized interaction principles.",
            record.interactionPrinciplesDescription,
        )
    }

    @Test
    fun `core personality record rejects blank core character`() {
        val foundation =
            assertNotNull(establishedFoundation().foundation)

        assertFailsWith<IllegalArgumentException> {
            DevilCorePersonalityRecord.create(
                personalityFoundation = foundation,
                coreCharacterDescription = "   ",
                interactionPrinciplesDescription =
                    "Bounded interaction principles.",
            )
        }
    }

    @Test
    fun `core personality record rejects blank interaction principles`() {
        val foundation =
            assertNotNull(establishedFoundation().foundation)

        assertFailsWith<IllegalArgumentException> {
            DevilCorePersonalityRecord.create(
                personalityFoundation = foundation,
                coreCharacterDescription =
                    "Bounded core character.",
                interactionPrinciplesDescription = "   ",
            )
        }
    }

    @Test
    fun `established result preserves exact Stage 245 core personality reference`() {
        val foundation =
            assertNotNull(establishedFoundation().foundation)

        val corePersonality =
            DevilCorePersonalityRecord.create(
                personalityFoundation = foundation,
                coreCharacterDescription =
                    "Exact Stage 245 core character.",
                interactionPrinciplesDescription =
                    "Exact Stage 245 interaction principles.",
            )

        val result =
            DevilCorePersonalityResult.create(
                status = DevilCorePersonalityStatus.ESTABLISHED,
                corePersonality = corePersonality,
            )

        assertEquals(
            DevilCorePersonalityStatus.ESTABLISHED,
            result.status,
        )

        assertSame(
            corePersonality,
            result.corePersonality,
        )
    }

    @Test
    fun `established result requires Stage 245 core personality record`() {
        assertFailsWith<IllegalArgumentException> {
            DevilCorePersonalityResult.create(
                status = DevilCorePersonalityStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain Stage 245 core personality record`() {
        val foundation =
            assertNotNull(establishedFoundation().foundation)

        val corePersonality =
            DevilCorePersonalityRecord.create(
                personalityFoundation = foundation,
                coreCharacterDescription =
                    "Must not appear in deferred result.",
                interactionPrinciplesDescription =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            DevilCorePersonalityResult.create(
                status = DevilCorePersonalityStatus.DEFERRED,
                corePersonality = corePersonality,
            )
        }
    }

    @Test
    fun `deferred result contains no Stage 245 core personality`() {
        val result =
            DevilCorePersonalityResult.create(
                status = DevilCorePersonalityStatus.DEFERRED,
            )

        assertEquals(
            DevilCorePersonalityStatus.DEFERRED,
            result.status,
        )

        assertNull(result.corePersonality)
    }
}
