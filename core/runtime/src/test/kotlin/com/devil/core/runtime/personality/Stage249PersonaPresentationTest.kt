package com.devil.core.runtime.personality

import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.model.owner.OwnerRelationshipType
import com.devil.core.model.personality.AdaptiveCommunicationStyleRecord
import com.devil.core.model.personality.DevilCorePersonalityRecord
import com.devil.core.model.personality.HumorSocialInteractionRecord
import com.devil.core.model.personality.PersonaPresentationRecord
import com.devil.core.model.personality.PersonalityFoundationRecord
import com.devil.core.model.personality.RelationshipContinuityRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage249PersonaPresentationTest {

    @Test
    fun `valid Stage 248 context establishes bounded persona presentation`() {
        val humorSocialInteraction = humorSocialInteraction()

        val result =
            PersonaPresentationCoordinator()
                .establish(
                    humorSocialInteraction = humorSocialInteraction,
                    presentationDescription =
                        "  Distinctive Devil persona presentation that remains constitutionally subordinate.  ",
                    presentationBoundaryRationale =
                        "  Presentation changes expression only and never creates authority.  ",
                )

        assertEquals(
            PersonaPresentationStatus.ESTABLISHED,
            result.status,
        )

        val presentation = assertNotNull(result.presentation)

        assertSame(
            humorSocialInteraction,
            presentation.humorSocialInteraction,
        )

        assertSame(
            humorSocialInteraction.adaptiveCommunicationStyle,
            presentation
                .humorSocialInteraction
                .adaptiveCommunicationStyle,
        )

        assertEquals(
            "Distinctive Devil persona presentation that remains constitutionally subordinate.",
            presentation.presentationDescription,
        )

        assertEquals(
            "Presentation changes expression only and never creates authority.",
            presentation.presentationBoundaryRationale,
        )
    }

    @Test
    fun `missing Stage 248 provenance keeps Stage 249 deferred`() {
        val result =
            PersonaPresentationCoordinator()
                .establish(
                    humorSocialInteraction = null,
                    presentationDescription =
                        "Bounded persona presentation.",
                    presentationBoundaryRationale =
                        "Bounded presentation rationale.",
                )

        assertEquals(
            PersonaPresentationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.presentation)
    }

    @Test
    fun `blank presentation description keeps Stage 249 deferred`() {
        val result =
            PersonaPresentationCoordinator()
                .establish(
                    humorSocialInteraction =
                        humorSocialInteraction(),
                    presentationDescription = "   ",
                    presentationBoundaryRationale =
                        "Bounded presentation rationale.",
                )

        assertEquals(
            PersonaPresentationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.presentation)
    }

    @Test
    fun `blank presentation boundary rationale keeps Stage 249 deferred`() {
        val result =
            PersonaPresentationCoordinator()
                .establish(
                    humorSocialInteraction =
                        humorSocialInteraction(),
                    presentationDescription =
                        "Bounded persona presentation.",
                    presentationBoundaryRationale = "   ",
                )

        assertEquals(
            PersonaPresentationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.presentation)
    }

    @Test
    fun `record normalizes Stage 249 metadata`() {
        val record =
            PersonaPresentationRecord.create(
                humorSocialInteraction =
                    humorSocialInteraction(),
                presentationDescription =
                    "  Normalized persona presentation.  ",
                presentationBoundaryRationale =
                    "  Normalized presentation boundary.  ",
            )

        assertEquals(
            "Normalized persona presentation.",
            record.presentationDescription,
        )

        assertEquals(
            "Normalized presentation boundary.",
            record.presentationBoundaryRationale,
        )
    }

    @Test
    fun `record rejects blank presentation description`() {
        assertFailsWith<IllegalArgumentException> {
            PersonaPresentationRecord.create(
                humorSocialInteraction =
                    humorSocialInteraction(),
                presentationDescription = "   ",
                presentationBoundaryRationale =
                    "Bounded presentation rationale.",
            )
        }
    }

    @Test
    fun `record rejects blank presentation boundary rationale`() {
        assertFailsWith<IllegalArgumentException> {
            PersonaPresentationRecord.create(
                humorSocialInteraction =
                    humorSocialInteraction(),
                presentationDescription =
                    "Bounded persona presentation.",
                presentationBoundaryRationale = "   ",
            )
        }
    }

    @Test
    fun `record preserves exact Stage 248 reference`() {
        val humorSocialInteraction = humorSocialInteraction()

        val record =
            PersonaPresentationRecord.create(
                humorSocialInteraction =
                    humorSocialInteraction,
                presentationDescription =
                    "Exact Stage 249 presentation.",
                presentationBoundaryRationale =
                    "Exact Stage 249 presentation rationale.",
            )

        assertSame(
            humorSocialInteraction,
            record.humorSocialInteraction,
        )
    }

    @Test
    fun `established result preserves exact presentation reference`() {
        val presentation =
            PersonaPresentationRecord.create(
                humorSocialInteraction =
                    humorSocialInteraction(),
                presentationDescription =
                    "Exact Stage 249 presentation.",
                presentationBoundaryRationale =
                    "Exact Stage 249 rationale.",
            )

        val result =
            PersonaPresentationResult.create(
                status = PersonaPresentationStatus.ESTABLISHED,
                presentation = presentation,
            )

        assertSame(
            presentation,
            result.presentation,
        )
    }

    @Test
    fun `established result requires presentation record`() {
        assertFailsWith<IllegalArgumentException> {
            PersonaPresentationResult.create(
                status = PersonaPresentationStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain presentation record`() {
        val presentation =
            PersonaPresentationRecord.create(
                humorSocialInteraction =
                    humorSocialInteraction(),
                presentationDescription =
                    "Must not appear in deferred result.",
                presentationBoundaryRationale =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            PersonaPresentationResult.create(
                status = PersonaPresentationStatus.DEFERRED,
                presentation = presentation,
            )
        }
    }

    @Test
    fun `deferred result contains no persona presentation`() {
        val result =
            PersonaPresentationResult.create(
                status = PersonaPresentationStatus.DEFERRED,
            )

        assertEquals(
            PersonaPresentationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.presentation)
    }

    private fun humorSocialInteraction():
        HumorSocialInteractionRecord {
        return HumorSocialInteractionRecord.create(
            adaptiveCommunicationStyle =
                adaptiveCommunicationStyle(),
            interactionDescription =
                "Bounded Stage 249 humor and social interaction.",
            appropriatenessRationale =
                "Humor remains context appropriate and constitutionally subordinate.",
        )
    }

    private fun adaptiveCommunicationStyle():
        AdaptiveCommunicationStyleRecord {
        return AdaptiveCommunicationStyleRecord.create(
            relationshipContinuity =
                relationshipContinuity(),
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
                "Bounded Stage 249 relationship continuity.",
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
                IdentityId.from("owner-stage-249"),
            subjectIdentityId =
                IdentityId.from("subject-stage-249"),
            type = OwnerRelationshipType.OTHER,
        )
    }
}
