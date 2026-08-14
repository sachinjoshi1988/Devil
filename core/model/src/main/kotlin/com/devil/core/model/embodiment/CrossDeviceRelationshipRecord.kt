package com.devil.core.model.embodiment

/**
 * Immutable Stage 84 representation of one bounded relationship between two
 * already-represented Devil embodiments.
 *
 * A CrossDeviceRelationshipRecord preserves only architectural relationship
 * metadata:
 *
 * - one source embodiment identity;
 * - one target embodiment identity;
 * - and one nonblank description of the bounded relationship.
 *
 * Source and target must identify different embodiments.
 *
 * The relationship does not establish that either embodiment is currently
 * reachable, connected, paired, trusted, authenticated, authorized, session-valid,
 * capability-ready, permitted to execute, synchronized, or replicated.
 *
 * It does not create:
 *
 * - another Devil intelligence;
 * - another Brain;
 * - another Constitution;
 * - another Executive;
 * - another Planner;
 * - another Memory Authority;
 * - another Security Authority;
 * - another Unified Devil Runtime;
 * - transport connectivity;
 * - device discovery;
 * - authentication;
 * - trust;
 * - authorization;
 * - session continuity;
 * - capability availability;
 * - remote execution;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - Memory synchronization;
 * - conversation synchronization;
 * - World Model synchronization;
 * - or state replication.
 *
 * CROSS_DEVICE_RELATIONSHIP != CONNECTION.
 * CROSS_DEVICE_RELATIONSHIP != TRUST.
 * CROSS_DEVICE_RELATIONSHIP != AUTHORIZATION.
 * CROSS_DEVICE_RELATIONSHIP != EXECUTION.
 * CROSS_DEVICE_RELATIONSHIP != MEMORY_SYNC.
 * REMOTE_EMBODIMENT != ANOTHER_DEVIL.
 */
@ConsistentCopyVisibility
data class CrossDeviceRelationshipRecord private constructor(
    val sourceEmbodimentId: EmbodimentId,
    val targetEmbodimentId: EmbodimentId,
    val description: String,
) {
    companion object {

        fun create(
            sourceEmbodimentId: EmbodimentId,
            targetEmbodimentId: EmbodimentId,
            description: String,
        ): CrossDeviceRelationshipRecord {
            require(sourceEmbodimentId != targetEmbodimentId) {
                "Cross-device relationship requires two distinct embodiment identities."
            }

            val normalizedDescription =
                description.trim()

            require(normalizedDescription.isNotEmpty()) {
                "Cross-device relationship description must not be blank."
            }

            return CrossDeviceRelationshipRecord(
                sourceEmbodimentId = sourceEmbodimentId,
                targetEmbodimentId = targetEmbodimentId,
                description = normalizedDescription,
            )
        }
    }
}
