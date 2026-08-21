package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord
import com.devil.core.model.creative.CreativeMediaProjectRecord

/**
 * Stage 166 bounded Creative Media Integration coordinator.
 *
 * This coordinator preserves one exact existing Stage 87 Creative Media project
 * and prepares explicitly supplied Stage 166 integration metadata around it.
 *
 * Stage 87 remains authoritative for Creative Media project identity and
 * objective provenance.
 *
 * This coordinator does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, or Security Authority;
 * - replace or reconstruct the preserved Stage 87 project;
 * - create provider-specific architecture;
 * - select or invoke Creative Media providers;
 * - register, authorize, prepare, activate, or execute capabilities;
 * - generate or render media;
 * - verify generated media;
 * - authorize publishing;
 * - create files or assets;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - or implement Stages 167 through 174.
 *
 * CREATIVE_MEDIA = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * CAPABILITY != PROVIDER.
 * INTEGRATION_PREPARED != MEDIA_GENERATED.
 * GENERATED != VERIFIED.
 * GENERATION != PUBLISHING_AUTHORIZATION.
 * INTEGRATION_PREPARED != EXECUTION.
 */
class CreativeMediaIntegrationCoordinator {

    fun prepare(
        traceId: TraceId,
        creativeProject: CreativeMediaProjectRecord,
        integrationFocus: String,
        suppliedCreativeMediaContextDescription: String,
        integrationObjective: String,
    ): CreativeMediaIntegrationPreparationResult {
        if (
            integrationFocus.isBlank() ||
            suppliedCreativeMediaContextDescription.isBlank() ||
            integrationObjective.isBlank()
        ) {
            return deferred(traceId)
        }

        val integration =
            CreativeMediaIntegrationRecord.create(
                creativeProject = creativeProject,
                integrationFocus = integrationFocus,
                suppliedCreativeMediaContextDescription =
                    suppliedCreativeMediaContextDescription,
                integrationObjective = integrationObjective,
            )

        return CreativeMediaIntegrationPreparationResult.create(
            traceId = traceId,
            status = CreativeMediaIntegrationPreparationStatus.PREPARED,
            integration = integration,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): CreativeMediaIntegrationPreparationResult {
        return CreativeMediaIntegrationPreparationResult.create(
            traceId = traceId,
            status = CreativeMediaIntegrationPreparationStatus.DEFERRED,
        )
    }
}
