package com.devil.core.runtime.personality

import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.model.owner.OwnerRelationshipType
import com.devil.core.model.personality.AdaptiveCommunicationStyleRecord
import com.devil.core.model.personality.DevilCorePersonalityRecord
import com.devil.core.model.personality.PersonalityFoundationRecord
import com.devil.core.model.personality.RelationshipContinuityRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage247AdaptiveCommunicationStyleTest {

    @Test
    fun `valid Stage 246 continuity establishes adaptive communication style`() {
        val continuity = relationshipContinuity()

        val result =
            AdaptiveCommunicationStyleCoordinator()
                .establish(
                    relationshipContinuity = continuity,
                    communicationStyleDescription =
                        "  Warm direct and context-aware communication.  ",
                    adaptationRationale =
                        "  Adapt presentation while preserving Devil's constitutional personality.  ",
                )

        assertEquals(
            AdaptiveCommunicationStyleStatus.ESTABLISHED,
            result.status,
        )

        val style = assertNotNull(result.style)

        assertSame(
            continuity,
            style.relationshipContinuity,
        )

        assertSame(
            continuity.corePersonality,
            style.relationshipContinuity.corePersonality,
        )

        assertSame(
            continuity.relationship,
            style.relationshipContinuity.relationship,
        )

        assertEquals(
            "Warm direct and context-aware communication.",
            style.communicationStyleDescription,
        )

        assertEquals(
            "Adapt presentation while preserving Devil's constitutional personality.",
            style.adaptationRationale,
        )
    }

    @Test
    fun `missing Stage 246 continuity keeps Stage 247 deferred`() {
        val result =
            AdaptiveCommunicationStyleCoordinator()
                .establish(
                    relationshipContinuity = null,
                    communicationStyleDescription =
                        "Bounded communication style.",
                    adaptationRationale =
                        "Bounded adaptation rationale.",
                )

        assertEquals(
            AdaptiveCommunicationStyleStatus.DEFERRED,
            result.status,
        )
        assertNull(result.style)
    }

    @Test
    fun `blank communication style keeps Stage 247 deferred`() {
        val result =
            AdaptiveCommunicationStyleCoordinator()
                .establish(
                    relationshipContinuity = relationshipContinuity(),
                    communicationStyleDescription = "   ",
                    adaptationRationale =
                        "Bounded adaptation rationale.",
                )

        assertEquals(
            AdaptiveCommunicationStyleStatus.DEFERRED,
            result.status,
        )
        assertNull(result.style)
    }

    @Test
    fun `blank adaptation rationale keeps Stage 247 deferred`() {
        val result =
            AdaptiveCommunicationStyleCoordinator()
                .establish(
                    relationshipContinuity = relationshipContinuity(),
                    communicationStyleDescription =
                        "Bounded communication style.",
                    adaptationRationale = "   ",
                )

        assertEquals(
            AdaptiveCommunicationStyleStatus.DEFERRED,
            result.status,
        )
        assertNull(result.style)
    }

    @Test
    fun `adaptive communication style record normalizes metadata`() {
        val record =
            AdaptiveCommunicationStyleRecord.create(
                relationshipContinuity = relationshipContinuity(),
                communicationStyleDescription =
                    "  Normalized communication style.  ",
                adaptationRationale =
                    "  Normalized adaptation rationale.  ",
            )

        assertEquals(
            "Normalized communication style.",
            record.communicationStyleDescription,
        )

        assertEquals(
            "Normalized adaptation rationale.",
            record.adaptationRationale,
        )
    }

    @Test
    fun `adaptive communication style record rejects blank style`() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveCommunicationStyleRecord.create(
                relationshipContinuity = relationshipContinuity(),
                communicationStyleDescription = "   ",
                adaptationRationale =
                    "Bounded adaptation rationale.",
            )
        }
    }

    @Test
    fun `adaptive communication style record rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveCommunicationStyleRecord.create(
                relationshipContinuity = relationshipContinuity(),
                communicationStyleDescription =
                    "Bounded communication style.",
                adaptationRationale = "   ",
            )
        }
    }

    @Test
    fun `adaptive communication style preserves exact Stage 246 reference`() {
        val continuity = relationshipContinuity()

        val record =
            AdaptiveCommunicationStyleRecord.create(
                relationshipContinuity = continuity,
                communicationStyleDescription =
                    "Exact Stage 247 communication style.",
                adaptationRationale =
                    "Exact Stage 247 rationale.",
            )

        assertSame(
            continuity,
            record.relationshipContinuity,
        )
    }

    @Test
    fun `established result preserves exact style reference`() {
        val style =
            AdaptiveCommunicationStyleRecord.create(
                relationshipContinuity = relationshipContinuity(),
                communicationStyleDescription =
                    "Exact Stage 247 style.",
                adaptationRationale =
                    "Exact Stage 247 rationale.",
            )

        val result =
            AdaptiveCommunicationStyleResult.create(
                status =
                    AdaptiveCommunicationStyleStatus.ESTABLISHED,
                style = style,
            )

        assertSame(
            style,
            result.style,
        )
    }

    @Test
    fun `established result requires style record`() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveCommunicationStyleResult.create(
                status =
                    AdaptiveCommunicationStyleStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain style record`() {
        val style =
            AdaptiveCommunicationStyleRecord.create(
                relationshipContinuity = relationshipContinuity(),
                communicationStyleDescription =
                    "Must not appear in deferred result.",
                adaptationRationale =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            AdaptiveCommunicationStyleResult.create(
                status =
                    AdaptiveCommunicationStyleStatus.DEFERRED,
                style = style,
            )
        }
    }

    @Test
    fun `deferred result contains no adaptive communication style`() {
        val result =
            AdaptiveCommunicationStyleResult.create(
                status =
                    AdaptiveCommunicationStyleStatus.DEFERRED,
            )

        assertEquals(
            AdaptiveCommunicationStyleStatus.DEFERRED,
            result.status,
        )
        assertNull(result.style)
    }

    private fun relationshipContinuity(): RelationshipContinuityRecord {
        return RelationshipContinuityRecord.create(
            corePersonality = corePersonality(),
            relationship = relationship(),
            continuityDescription =
                "Bounded Stage 247 relationship continuity.",
        )
    }

    private fun corePersonality(): DevilCorePersonalityRecord {
        return DevilCorePersonalityRecord.create(
            personalityFoundation =
                PersonalityFoundationRecord.create(
                    personalityIdentityDescription =
                        "One constitutionally governed Devil personality foundation.",
                    constitutionalRoleDescription =
                        "Personality shapes presentation without becoming authority.",
                ),
            coreCharacterDescription =
                "Stable Devil core personality.",
            interactionPrinciplesDescription =
                "Core personality remains subordinate to Devil's Constitution.",
        )
    }

    private fun relationship(): OwnerRelationship {
        return OwnerRelationship.create(
            ownerIdentityId =
                IdentityId.from("owner-stage-247"),
            subjectIdentityId =
                IdentityId.from("subject-stage-247"),
            type = OwnerRelationshipType.OTHER,
        )
    }
}
