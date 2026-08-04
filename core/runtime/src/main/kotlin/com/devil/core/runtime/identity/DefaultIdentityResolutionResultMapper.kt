package com.devil.core.runtime.identity

import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityResolutionRecord
import com.devil.core.model.identity.IdentityResolutionState

/**
 * Default Stage 3 translation from identity-resolution records to the stable
 * runtime identity result contract.
 *
 * Resolved records expose their selected identity. Unresolved and ambiguous
 * records remain unresolved at the runtime boundary. This implementation does
 * not perform resolution, authentication, trust evaluation, ownership
 * determination, or authorization.
 */
class DefaultIdentityResolutionResultMapper :
    IdentityResolutionResultMapper {

    override fun map(
        traceId: TraceId,
        record: IdentityResolutionRecord,
    ): IdentityResult {
        return when (record.state) {
            IdentityResolutionState.RESOLVED -> {
                val selection = requireNotNull(record.selection)

                IdentityResult.create(
                    traceId = traceId,
                    status = IdentityStatus.RESOLVED,
                    identityId = selection.candidate.identityId,
                )
            }

            IdentityResolutionState.UNRESOLVED,
            IdentityResolutionState.AMBIGUOUS,
            -> IdentityResult.create(
                traceId = traceId,
                status = IdentityStatus.UNRESOLVED,
            )
        }
    }
}
