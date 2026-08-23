package com.devil.core.runtime.personality

import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.model.owner.OwnerRelationshipType
import com.devil.core.model.personality.AdaptiveCommunicationStyleRecord
import com.devil.core.model.personality.DevilCorePersonalityRecord
import com.devil.core.model.personality.HumorSocialInteractionRecord
import com.devil.core.model.personality.PersonalityFoundationRecord
import com.devil.core.model.personality.RelationshipContinuityRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage248HumorSocialInteractionTest {

    @Test
    fun `valid Stage 247 style establishes bounded humor and social interaction`() {
        val style = adaptiveCommunicationStyle()

        val result =
            HumorSocialInteractionCoordinator()
                .establish(
                    adaptiveCommunicationStyle = style,
                    interactionDescription =
                        "  Playful socially aware humor that remains context appropriate.  ",
                    appropriatenessRationale =
                        "  Humor remains subordinate to relationship context and constitutional boundaries.  ",
                )

        assertEquals(
            HumorSocialInteractionStatus.ESTABLISHED,
            result.status,
        )

        val interaction = assertNotNull(result.interaction)

        assertSame(
            style,
            interaction.adaptiveCommunicationStyle,
        )

        assertSame(
            style.relationshipContinuity,
            interaction.adaptiveCommunicationStyle.relationshipContinuity,
        )

        assertEquals(
            "Playful socially aware humor that remains context appropriate.",
            interaction.interactionDescription,
        )

        assertEquals(
            "Humor remains subordinate to relationship context and constitutional boundaries.",
            interaction.appropriatenessRationale,
        )
    }

    @Test
    fun `missing Stage 247 style keeps Stage 248 deferred`() {
        val result =
            HumorSocialInteractionCoordinator()
                .establish(
                    adaptiveCommunicationStyle = null,
                    interactionDescription =
                        "Bounded social interaction.",
                    appropriatenessRationale =
                        "Bounded social appropriateness.",
                )

        assertEquals(
            HumorSocialInteractionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.interaction)
    }

    @Test
    fun `blank interaction description keeps Stage 248 deferred`() {
        val result =
            HumorSocialInteractionCoordinator()
                .establish(
                    adaptiveCommunicationStyle =
                        adaptiveCommunicationStyle(),
                    interactionDescription = "   ",
                    appropriatenessRationale =
                        "Bounded social appropriateness.",
                )

        assertEquals(
            HumorSocialInteractionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.interaction)
    }

    @Test
    fun `blank appropriateness rationale keeps Stage 248 deferred`() {
        val result =
            HumorSocialInteractionCoordinator()
                .establish(
                    adaptiveCommunicationStyle =
                        adaptiveCommunicationStyle(),
                    interactionDescription =
                        "Bounded social interaction.",
                    appropriatenessRationale = "   ",
                )

        assertEquals(
            HumorSocialInteractionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.interaction)
    }

    @Test
    fun `record normalizes supplied Stage 248 metadata`() {
        val record =
            HumorSocialInteractionRecord.create(
                adaptiveCommunicationStyle =
                    adaptiveCommunicationStyle(),
                interactionDescription =
                    "  Normalized humor and social interaction.  ",
                appropriatenessRationale =
                    "  Normalized social appropriateness rationale.  ",
            )

        assertEquals(
            "Normalized humor and social interaction.",
            record.interactionDescription,
        )

        assertEquals(
            "Normalized social appropriateness rationale.",
            record.appropriatenessRationale,
        )
    }

    @Test
    fun `record rejects blank interaction description`() {
        assertFailsWith<IllegalArgumentException> {
            HumorSocialInteractionRecord.create(
                adaptiveCommunicationStyle =
                    adaptiveCommunicationStyle(),
                interactionDescription = "   ",
                appropriatenessRationale =
                    "Bounded social appropriateness.",
            )
        }
    }

    @Test
    fun `record rejects blank appropriateness rationale`() {
        assertFailsWith<IllegalArgumentException> {
            HumorSocialInteractionRecord.create(
                adaptiveCommunicationStyle =
                    adaptiveCommunicationStyle(),
                interactionDescription =
                    "Bounded humor and social interaction.",
                appropriatenessRationale = "   ",
            )
        }
    }

    @Test
    fun `record preserves exact Stage 247 style reference`() {
        val style = adaptiveCommunicationStyle()

        val record =
            HumorSocialInteractionRecord.create(
                adaptiveCommunicationStyle = style,
                interactionDescription =
                    "Exact bounded Stage 248 interaction.",
                appropriatenessRationale =
                    "Exact bounded Stage 248 rationale.",
            )

        assertSame(
            style,
            record.adaptiveCommunicationStyle,
        )
    }

    @Test
    fun `established result preserves exact interaction reference`() {
        val interaction =
            HumorSocialInteractionRecord.create(
                adaptiveCommunicationStyle =
                    adaptiveCommunicationStyle(),
                interactionDescription =
                    "Exact Stage 248 interaction.",
                appropriatenessRationale =
                    "Exact Stage 248 rationale.",
            )

        val result =
            HumorSocialInteractionResult.create(
                status = HumorSocialInteractionStatus.ESTABLISHED,
                interaction = interaction,
            )

        assertSame(
            interaction,
            result.interaction,
        )
    }

    @Test
    fun `established result requires interaction record`() {
        assertFailsWith<IllegalArgumentException> {
            HumorSocialInteractionResult.create(
                status = HumorSocialInteractionStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain interaction record`() {
        val interaction =
            HumorSocialInteractionRecord.create(
                adaptiveCommunicationStyle =
                    adaptiveCommunicationStyle(),
                interactionDescription =
                    "Must not appear in deferred result.",
                appropriatenessRationale =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            HumorSocialInteractionResult.create(
                status = HumorSocialInteractionStatus.DEFERRED,
                interaction = interaction,
            )
        }
    }

    @Test
    fun `deferred result contains no humor social interaction`() {
        val result =
            HumorSocialInteractionResult.create(
                status = HumorSocialInteractionStatus.DEFERRED,
            )

        assertEquals(
            HumorSocialInteractionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.interaction)
    }

    private fun adaptiveCommunicationStyle():
        AdaptiveCommunicationStyleRecord {
        return AdaptiveCommunicationStyleRecord.create(
            relationshipContinuity = relationshipContinuity(),
            communicationStyleDescription =
                "Warm direct context-aware communication.",
            adaptationRationale =
                "Adapt presentation without changing constitutional authority.",
        )
    }

    private fun relationshipContinuity():
        RelationshipContinuityRecord {
        return RelationshipContinuityRecord.create(
            corePersonality = corePersonality(),
            relationship = relationship(),
            continuityDescription =
                "Bounded Stage 248 relationship continuity.",
        )
    }

    private fun corePersonality():
        DevilCorePersonalityRecord {
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
            ownerIdentityId =
                IdentityId.from("owner-stage-248"),
            subjectIdentityId =
                IdentityId.from("subject-stage-248"),
            type = OwnerRelationshipType.OTHER,
        )
    }
}
