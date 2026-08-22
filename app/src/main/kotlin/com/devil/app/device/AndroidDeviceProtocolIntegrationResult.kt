package com.devil.app.device

import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus

/**
 * Stage 213 bounded Device Protocol Integration result.
 *
 * AVAILABLE preserves the exact represented Stage 84 cross-device relationship
 * together with one normalized explicitly supplied protocol identifier.
 *
 * DEFERRED preserves the exact Stage 84 representation and no protocol identifier.
 *
 * DEVICE_PROTOCOL_INTEGRATED != CONNECTION.
 * DEVICE_PROTOCOL != DEVICE_DISCOVERY.
 * DEVICE_PROTOCOL != PAIRING.
 * DEVICE_PROTOCOL != TRUST.
 * DEVICE_PROTOCOL != AUTHENTICATION.
 * DEVICE_PROTOCOL != AUTHORIZATION.
 * DEVICE_PROTOCOL != SESSION_CONTINUITY.
 * DEVICE_PROTOCOL != DATA_TRANSFER.
 * DEVICE_PROTOCOL != REMOTE_EXECUTION.
 * DEVICE_PROTOCOL != MEMORY_SYNC.
 * REMOTE_EMBODIMENT != ANOTHER_DEVIL.
 */
@ConsistentCopyVisibility
data class AndroidDeviceProtocolIntegrationResult private constructor(
    val status: AndroidDeviceProtocolIntegrationStatus,
    val relationshipRepresentation: CrossDeviceRelationshipRepresentationResult,
    val protocolId: String?,
) {
    companion object {
        fun create(
            status: AndroidDeviceProtocolIntegrationStatus,
            relationshipRepresentation: CrossDeviceRelationshipRepresentationResult,
            protocolId: String? = null,
        ): AndroidDeviceProtocolIntegrationResult {
            return when (status) {
                AndroidDeviceProtocolIntegrationStatus.AVAILABLE -> {
                    require(
                        relationshipRepresentation.status ==
                            CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                    ) {
                        "Available Stage 213 Device Protocol Integration requires represented Stage 84 cross-device relationship."
                    }

                    val normalizedProtocolId =
                        requireNotNull(protocolId)
                            .trim()

                    require(normalizedProtocolId.isNotEmpty()) {
                        "Stage 213 device protocol identifier must not be blank."
                    }

                    AndroidDeviceProtocolIntegrationResult(
                        status = status,
                        relationshipRepresentation = relationshipRepresentation,
                        protocolId = normalizedProtocolId,
                    )
                }

                AndroidDeviceProtocolIntegrationStatus.DEFERRED -> {
                    require(protocolId == null) {
                        "Deferred Stage 213 Device Protocol Integration must not contain a protocol identifier."
                    }

                    AndroidDeviceProtocolIntegrationResult(
                        status = status,
                        relationshipRepresentation = relationshipRepresentation,
                        protocolId = null,
                    )
                }
            }
        }
    }
}
