package com.devil.app.device

import com.devil.core.model.identity.IdentityId
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus

/**
 * Stage 218 bounded Cross-Device Identity result.
 *
 * AVAILABLE preserves one exact represented Stage 84 cross-device relationship
 * together with one exact explicitly supplied existing IdentityId.
 *
 * DEFERRED preserves the exact Stage 84 relationship representation and no
 * identity.
 *
 * CROSS_DEVICE_IDENTITY != IDENTITY_RESOLUTION.
 * CROSS_DEVICE_IDENTITY != AUTHENTICATION.
 * CROSS_DEVICE_IDENTITY != DEVICE_TRUST.
 * CROSS_DEVICE_IDENTITY != AUTHORIZATION.
 * IDENTITY_CONTINUITY != SECURITY_SESSION.
 * IDENTITY_ID != OWNERSHIP_PROOF.
 * CROSS_DEVICE_RELATIONSHIP != IDENTITY_EVIDENCE.
 * SAME_IDENTITY_CONTEXT != SAME_AUTHENTICATED_SUBJECT.
 * CROSS_DEVICE_IDENTITY != MEMORY_SYNC.
 */
@ConsistentCopyVisibility
data class AndroidCrossDeviceIdentityResult private constructor(
    val status: AndroidCrossDeviceIdentityStatus,
    val relationshipRepresentation: CrossDeviceRelationshipRepresentationResult,
    val identityId: IdentityId?,
) {
    companion object {
        fun create(
            status: AndroidCrossDeviceIdentityStatus,
            relationshipRepresentation: CrossDeviceRelationshipRepresentationResult,
            identityId: IdentityId? = null,
        ): AndroidCrossDeviceIdentityResult {
            when (status) {
                AndroidCrossDeviceIdentityStatus.AVAILABLE -> {
                    require(
                        relationshipRepresentation.status ==
                            CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                    ) {
                        "Available Stage 218 Cross-Device Identity requires represented Stage 84 cross-device relationship."
                    }

                    require(identityId != null) {
                        "Available Stage 218 Cross-Device Identity requires one existing identity."
                    }
                }

                AndroidCrossDeviceIdentityStatus.DEFERRED -> {
                    require(identityId == null) {
                        "Deferred Stage 218 Cross-Device Identity must not contain an identity."
                    }
                }
            }

            return AndroidCrossDeviceIdentityResult(
                status = status,
                relationshipRepresentation = relationshipRepresentation,
                identityId = identityId,
            )
        }
    }
}
