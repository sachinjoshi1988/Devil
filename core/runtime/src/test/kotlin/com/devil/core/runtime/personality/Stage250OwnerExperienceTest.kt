package com.devil.core.runtime.personality

import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerProfile
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.model.owner.OwnerRelationshipType
import com.devil.core.model.personality.AdaptiveCommunicationStyleRecord
import com.devil.core.model.personality.DevilCorePersonalityRecord
import com.devil.core.model.personality.HumorSocialInteractionRecord
import com.devil.core.model.personality.OwnerExperienceRecord
import com.devil.core.model.personality.PersonaPresentationRecord
import com.devil.core.model.personality.PersonalityFoundationRecord
import com.devil.core.model.personality.RelationshipContinuityRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage250OwnerExperienceTest {

    @Test
    fun `valid Stage 249 and owner profile establish bounded owner experience`() {
        val personaPresentation = personaPresentation()
        val ownerProfile = ownerProfile()

        val result =
            OwnerExperienceCoordinator()
                .establish(
                    personaPresentation = personaPresentation,
                    ownerProfile = ownerProfile,
                    ownerExperienceDescription =
                        "  Bounded owner-facing Devil experience preserving established personality.  ",
                    ownerExperienceBoundaryRationale =
                        "  Owner-facing presentation remains separate from authentication and authority.  ",
                )

        assertEquals(
            OwnerExperienceStatus.ESTABLISHED,
            result.status,
        )

        val experience = assertNotNull(result.experience)

        assertSame(
            personaPresentation,
            experience.personaPresentation,
        )

        assertSame(
            ownerProfile,
            experience.ownerProfile,
        )

        assertEquals(
            "Bounded owner-facing Devil experience preserving established personality.",
            experience.ownerExperienceDescription,
        )

        assertEquals(
            "Owner-facing presentation remains separate from authentication and authority.",
            experience.ownerExperienceBoundaryRationale,
        )
    }

    @Test
    fun `missing Stage 249 provenance keeps Stage 250 deferred`() {
        val result =
            OwnerExperienceCoordinator()
                .establish(
                    personaPresentation = null,
                    ownerProfile = ownerProfile(),
                    ownerExperienceDescription =
                        "Bounded owner experience.",
                    ownerExperienceBoundaryRationale =
                        "Bounded owner-experience rationale.",
                )

        assertEquals(
            OwnerExperienceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.experience)
    }

    @Test
    fun `missing owner profile keeps Stage 250 deferred`() {
        val result =
            OwnerExperienceCoordinator()
                .establish(
                    personaPresentation = personaPresentation(),
                    ownerProfile = null,
                    ownerExperienceDescription =
                        "Bounded owner experience.",
                    ownerExperienceBoundaryRationale =
                        "Bounded owner-experience rationale.",
                )

        assertEquals(
            OwnerExperienceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.experience)
    }

    @Test
    fun `blank owner experience description keeps Stage 250 deferred`() {
        val result =
            OwnerExperienceCoordinator()
                .establish(
                    personaPresentation = personaPresentation(),
                    ownerProfile = ownerProfile(),
                    ownerExperienceDescription = "   ",
                    ownerExperienceBoundaryRationale =
                        "Bounded owner-experience rationale.",
                )

        assertEquals(
            OwnerExperienceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.experience)
    }

    @Test
    fun `blank owner experience boundary rationale keeps Stage 250 deferred`() {
        val result =
            OwnerExperienceCoordinator()
                .establish(
                    personaPresentation = personaPresentation(),
                    ownerProfile = ownerProfile(),
                    ownerExperienceDescription =
                        "Bounded owner experience.",
                    ownerExperienceBoundaryRationale = "   ",
                )

        assertEquals(
            OwnerExperienceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.experience)
    }

    @Test
    fun `record normalizes Stage 250 metadata`() {
        val record =
            OwnerExperienceRecord.create(
                personaPresentation = personaPresentation(),
                ownerProfile = ownerProfile(),
                ownerExperienceDescription =
                    "  Normalized owner experience.  ",
                ownerExperienceBoundaryRationale =
                    "  Normalized owner-experience boundary.  ",
            )

        assertEquals(
            "Normalized owner experience.",
            record.ownerExperienceDescription,
        )

        assertEquals(
            "Normalized owner-experience boundary.",
            record.ownerExperienceBoundaryRationale,
        )
    }

    @Test
    fun `record rejects blank owner experience description`() {
        assertFailsWith<IllegalArgumentException> {
            OwnerExperienceRecord.create(
                personaPresentation = personaPresentation(),
                ownerProfile = ownerProfile(),
                ownerExperienceDescription = "   ",
                ownerExperienceBoundaryRationale =
                    "Bounded owner-experience rationale.",
            )
        }
    }

    @Test
    fun `record rejects blank owner experience boundary rationale`() {
        assertFailsWith<IllegalArgumentException> {
            OwnerExperienceRecord.create(
                personaPresentation = personaPresentation(),
                ownerProfile = ownerProfile(),
                ownerExperienceDescription =
                    "Bounded owner experience.",
                ownerExperienceBoundaryRationale = "   ",
            )
        }
    }

    @Test
    fun `record preserves exact Stage 249 and owner profile references`() {
        val personaPresentation = personaPresentation()
        val ownerProfile = ownerProfile()

        val record =
            OwnerExperienceRecord.create(
                personaPresentation = personaPresentation,
                ownerProfile = ownerProfile,
                ownerExperienceDescription =
                    "Exact Stage 250 owner experience.",
                ownerExperienceBoundaryRationale =
                    "Exact Stage 250 boundary rationale.",
            )

        assertSame(
            personaPresentation,
            record.personaPresentation,
        )

        assertSame(
            ownerProfile,
            record.ownerProfile,
        )
    }

    @Test
    fun `established result preserves exact owner experience reference`() {
        val experience =
            OwnerExperienceRecord.create(
                personaPresentation = personaPresentation(),
                ownerProfile = ownerProfile(),
                ownerExperienceDescription =
                    "Exact Stage 250 owner experience.",
                ownerExperienceBoundaryRationale =
                    "Exact Stage 250 boundary rationale.",
            )

        val result =
            OwnerExperienceResult.create(
                status = OwnerExperienceStatus.ESTABLISHED,
                experience = experience,
            )

        assertSame(
            experience,
            result.experience,
        )
    }

    @Test
    fun `established result requires owner experience record`() {
        assertFailsWith<IllegalArgumentException> {
            OwnerExperienceResult.create(
                status = OwnerExperienceStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain owner experience record`() {
        val experience =
            OwnerExperienceRecord.create(
                personaPresentation = personaPresentation(),
                ownerProfile = ownerProfile(),
                ownerExperienceDescription =
                    "Must not appear in deferred result.",
                ownerExperienceBoundaryRationale =
                    "Must not appear in deferred result.",
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerExperienceResult.create(
                status = OwnerExperienceStatus.DEFERRED,
                experience = experience,
            )
        }
    }

    @Test
    fun `deferred result contains no owner experience`() {
        val result =
            OwnerExperienceResult.create(
                status = OwnerExperienceStatus.DEFERRED,
            )

        assertEquals(
            OwnerExperienceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.experience)
    }

    private fun personaPresentation(): PersonaPresentationRecord {
        return PersonaPresentationRecord.create(
            humorSocialInteraction = humorSocialInteraction(),
            presentationDescription =
                "Bounded Stage 250 Devil persona presentation.",
            presentationBoundaryRationale =
                "Persona presentation remains subordinate to constitutional authority.",
        )
    }

    private fun humorSocialInteraction():
        HumorSocialInteractionRecord {
        return HumorSocialInteractionRecord.create(
            adaptiveCommunicationStyle =
                adaptiveCommunicationStyle(),
            interactionDescription =
                "Bounded Stage 250 humor and social interaction.",
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
                "Bounded Stage 250 relationship continuity.",
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
                IdentityId.from("owner-stage-250"),
            subjectIdentityId =
                IdentityId.from("subject-stage-250"),
            type = OwnerRelationshipType.OTHER,
        )
    }

    private fun ownerProfile(): OwnerProfile {
        return OwnerProfile.create(
            ownerIdentityId =
                IdentityId.from("owner-stage-250"),
            displayName = "Devil Owner",
            preferredFormOfAddress = "Boss",
        )
    }
}
