package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord

/**
 * Stable Stage 84 result of bounded cross-device relationship representation.
 *
 * REPRESENTED requires one CrossDeviceRelationshipRecord.
 *
 * DEFERRED must not contain a relationship.
 *
 * This result creates no connectivity, trust, authentication, authorization,
 * session, capability, permission, execution, synchronization, replication,
 * Observation, Verification, Outcome, Learning, Memory, or persistence authority.
 */
@ConsistentCopyVisibility
data class CrossDeviceRelationshipRepresentationResult private constructor(
    val traceId: TraceId,
    val status: CrossDeviceRelationshipRepresentationStatus,
    val relationship: CrossDeviceRelationshipRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: CrossDeviceRelationshipRepresentationStatus,
            relationship: CrossDeviceRelationshipRecord? = null,
        ): CrossDeviceRelationshipRepresentationResult {
            when (status) {
                CrossDeviceRelationshipRepresentationStatus.REPRESENTED -> {
                    require(relationship != null) {
                        "Represented cross-device relationship results require one relationship."
                    }
                }

                CrossDeviceRelationshipRepresentationStatus.DEFERRED -> {
                    require(relationship == null) {
                        "Deferred cross-device relationship results must not contain a relationship."
                    }
                }
            }

            return CrossDeviceRelationshipRepresentationResult(
                traceId = traceId,
                status = status,
                relationship = relationship,
            )
        }
    }
}
