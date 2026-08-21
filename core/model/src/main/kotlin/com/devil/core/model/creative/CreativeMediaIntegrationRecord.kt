package com.devil.core.model.creative

/**
 * Immutable Stage 166 representation of one bounded Creative Media Integration
 * context.
 *
 * This record preserves:
 *
 * - one exact existing Stage 87 CreativeMediaProjectRecord;
 * - one explicitly supplied nonblank integration focus;
 * - one explicitly supplied nonblank Creative Media context description;
 * - one explicitly supplied nonblank integration objective.
 *
 * Stage 166 integrates the existing Creative Media domain foundation into the
 * current roadmap without replacing or redesigning Stage 87 or Stage 88.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, or Security Authority;
 * - replace or reconstruct the preserved CreativeMediaProjectRecord;
 * - create provider-specific Creative Media architecture;
 * - select or invoke image, audio, video, text, or multimodal providers;
 * - register, authorize, prepare, activate, or execute capabilities;
 * - generate images;
 * - understand or edit images;
 * - create stories;
 * - create storyboards;
 * - implement the production Story-to-Animation pipeline;
 * - create audio or music;
 * - generate, edit, render, or export video;
 * - establish Creative Project Workspace persistence;
 * - create files or generated assets;
 * - publish, upload, distribute, or transmit media;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or implement Stages 167 through 174.
 *
 * CREATIVE_MEDIA = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * CREATIVE_MEDIA_INTEGRATION != ANOTHER_INTELLIGENCE.
 * CAPABILITY != PROVIDER.
 * INTEGRATION_PREPARED != MEDIA_GENERATED.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * INTEGRATION_PREPARED != EXECUTION.
 */
@ConsistentCopyVisibility
data class CreativeMediaIntegrationRecord private constructor(
    val creativeProject: CreativeMediaProjectRecord,
    val integrationFocus: String,
    val suppliedCreativeMediaContextDescription: String,
    val integrationObjective: String,
) {
    companion object {

        fun create(
            creativeProject: CreativeMediaProjectRecord,
            integrationFocus: String,
            suppliedCreativeMediaContextDescription: String,
            integrationObjective: String,
        ): CreativeMediaIntegrationRecord {
            val normalizedIntegrationFocus =
                integrationFocus.trim()

            val normalizedSuppliedCreativeMediaContextDescription =
                suppliedCreativeMediaContextDescription.trim()

            val normalizedIntegrationObjective =
                integrationObjective.trim()

            require(normalizedIntegrationFocus.isNotEmpty()) {
                "Creative Media Integration focus must not be blank."
            }

            require(normalizedSuppliedCreativeMediaContextDescription.isNotEmpty()) {
                "Creative Media Integration context description must not be blank."
            }

            require(normalizedIntegrationObjective.isNotEmpty()) {
                "Creative Media Integration objective must not be blank."
            }

            return CreativeMediaIntegrationRecord(
                creativeProject = creativeProject,
                integrationFocus = normalizedIntegrationFocus,
                suppliedCreativeMediaContextDescription =
                    normalizedSuppliedCreativeMediaContextDescription,
                integrationObjective = normalizedIntegrationObjective,
            )
        }
    }
}
