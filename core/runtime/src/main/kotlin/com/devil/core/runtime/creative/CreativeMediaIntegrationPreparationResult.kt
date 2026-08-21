package com.devil.core.runtime.creative

import com.devil.core.model.common.TraceId
import com.devil.core.model.creative.CreativeMediaIntegrationRecord

/**
 * Stable Stage 166 result of bounded Creative Media Integration preparation.
 *
 * PREPARED requires exactly one CreativeMediaIntegrationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no provider selection, capability authorization,
 * generation, publishing authorization, execution, constitutional Verification,
 * Stage 167–174 behavior, World Model mutation, or Memory persistence.
 */
@ConsistentCopyVisibility
data class CreativeMediaIntegrationPreparationResult private constructor(
    val traceId: TraceId,
    val status: CreativeMediaIntegrationPreparationStatus,
    val integration: CreativeMediaIntegrationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CreativeMediaIntegrationPreparationStatus,
            integration: CreativeMediaIntegrationRecord? = null,
        ): CreativeMediaIntegrationPreparationResult {
            when (status) {
                CreativeMediaIntegrationPreparationStatus.PREPARED -> {
                    require(integration != null) {
                        "Prepared Creative Media Integration results require one integration context."
                    }
                }

                CreativeMediaIntegrationPreparationStatus.DEFERRED -> {
                    require(integration == null) {
                        "Deferred Creative Media Integration results must not contain an integration context."
                    }
                }
            }

            return CreativeMediaIntegrationPreparationResult(
                traceId = traceId,
                status = status,
                integration = integration,
            )
        }
    }
}
