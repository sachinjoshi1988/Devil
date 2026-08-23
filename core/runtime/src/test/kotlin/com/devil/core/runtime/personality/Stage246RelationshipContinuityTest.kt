package com.devil.core.runtime.personality

import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.model.owner.OwnerRelationshipType
import com.devil.core.model.personality.DevilCorePersonalityRecord
import com.devil.core.model.personality.PersonalityFoundationRecord
import com.devil.core.model.personality.RelationshipContinuityRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage246RelationshipContinuityTest {

    @Test
    fun `valid supplied context establishes Stage 246 relationship continuity`() {
        val corePersonality = corePersonality()
        val relationship = relationship()

        val result =
            RelationshipContinuityCoordinator()
                .establish(
                    corePersonality = corePersonality,
                    relationship = relationship,
                    continuityDescription =
                        "  Preserve bounded companionship continuity across interactions.  ",
                )

        assertEquals(
            RelationshipContinuityStatus.ESTABLISHED,
            result.status,
        )

        val continuity = assertNotNull(result.continuity)

        assertSame(corePersonality, continuity.corePersonality)
        assertSame(relationship, continuity.relationship)
        assertEquals(
            "Preserve bounded companionship continuity across interactions.",
            continuity.continuityDescription,
        )
    }

    @Test
    fun `missing core personality keeps Stage 246 deferred`() {
        val result =
            RelationshipContinuityCoordinator()
                .establish(
                    corePersonality = null,
                    relationship = relationship(),
                    continuityDescription =
                        "Bounded relationship continuity.",
                )

        assertEquals(
            RelationshipContinuityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.continuity)
    }

    @Test
    fun `missing relationship keeps Stage 246 deferred`() {
        val result =
            RelationshipContinuityCoordinator()
                .establish(
                    corePersonality = corePersonality(),
                    relationship = null,
                    continuityDescription =
                        "Bounded relationship continuity.",
                )

        assertEquals(
            RelationshipContinuityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.continuity)
    }

    @Test
    fun `blank continuity description keeps Stage 246 deferred`() {
        val result =
            RelationshipContinuityCoordinator()
                .establish(
                    corePersonality = corePersonality(),
                    relationship = relationship(),
                    continuityDescription = "   ",
                )

        assertEquals(
            RelationshipContinuityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.continuity)
    }

    @Test
    fun `null continuity description keeps Stage 246 deferred`() {
        val result =
            RelationshipContinuityCoordinator()
                .establish(
                    corePersonality = corePersonality(),
                    relationship = relationship(),
                    continuityDescription = null,
                )

        assertEquals(
            RelationshipContinuityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.continuity)
    }

    @Test
    fun `relationship continuity record normalizes supplied description`() {
        val record =
            RelationshipContinuityRecord.create(
                corePersonality = corePersonality(),
                relationship = relationship(),
                continuityDescription =
                    "  Normalized relationship continuity.  ",
            )

        assertEquals(
            "Normalized relationship continuity.",
            record.continuityDescription,
        )
    }

    @Test
    fun `relationship continuity record rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            RelationshipContinuityRecord.create(
                corePersonality = corePersonality(),
                relationship = relationship(),
                continuityDescription = "   ",
            )
        }
    }

    @Test
    fun `relationship continuity preserves exact supplied references`() {
        val corePersonality = corePersonality()
        val relationship = relationship()

        val record =
            RelationshipContinuityRecord.create(
                corePersonality = corePersonality,
                relationship = relationship,
                continuityDescription =
                    "Exact supplied relationship continuity.",
            )

        assertSame(corePersonality, record.corePersonality)
        assertSame(relationship, record.relationship)
    }

    @Test
    fun `established result preserves exact continuity reference`() {
        val continuity =
            RelationshipContinuityRecord.create(
                corePersonality = corePersonality(),
                relationship = relationship(),
                continuityDescription =
                    "Exact Stage 246 continuity.",
            )

        val result =
            RelationshipContinuityResult.create(
                status = RelationshipContinuityStatus.ESTABLISHED,
                continuity = continuity,
            )

        assertEquals(
            RelationshipContinuityStatus.ESTABLISHED,
            result.status,
        )
        assertSame(continuity, result.continuity)
    }

    @Test
    fun `established result requires continuity record`() {
        assertFailsWith<IllegalArgumentException> {
            RelationshipContinuityResult.create(
                status = RelationshipContinuityStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain continuity record`() {
        val continuity =
            RelationshipContinuityRecord.create(
                corePersonality = corePersonality(),
                relationship = relationship(),
                continuityDescription =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            RelationshipContinuityResult.create(
                status = RelationshipContinuityStatus.DEFERRED,
                continuity = continuity,
            )
        }
    }

    @Test
    fun `deferred result contains no relationship continuity`() {
        val result =
            RelationshipContinuityResult.create(
                status = RelationshipContinuityStatus.DEFERRED,
            )

        assertEquals(
            RelationshipContinuityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.continuity)
    }

    private fun corePersonality(): DevilCorePersonalityRecord {
        return DevilCorePersonalityRecord.create(
            personalityFoundation =
                PersonalityFoundationRecord.create(
                    personalityIdentityDescription =
                        "One constitutionally governed Devil personality foundation.",
                    constitutionalRoleDescription =
                        "Personality shapes bounded presentation without becoming authority.",
                ),
            coreCharacterDescription =
                "Stable Devil core personality.",
            interactionPrinciplesDescription =
                "Core personality remains subordinate to Devil's Constitution.",
        )
    }

    private fun relationship(): OwnerRelationship {
        return OwnerRelationship.create(
            ownerIdentityId = IdentityId.from("owner-stage-246"),
            subjectIdentityId = IdentityId.from("subject-stage-246"),
            type = OwnerRelationshipType.OTHER,
        )
    }
}
