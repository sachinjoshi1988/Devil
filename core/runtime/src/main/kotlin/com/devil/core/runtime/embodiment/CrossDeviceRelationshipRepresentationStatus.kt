package com.devil.core.runtime.embodiment

/**
 * Stage 84 status for bounded cross-device relationship representation.
 *
 * REPRESENTED means one structurally valid relationship between two distinct
 * already-represented embodiments was produced.
 *
 * REPRESENTED does not mean:
 *
 * - the devices are connected;
 * - either embodiment is reachable;
 * - either embodiment is trusted;
 * - authentication succeeded;
 * - authorization exists;
 * - a shared session exists;
 * - capabilities are available;
 * - remote execution is permitted;
 * - data is synchronized;
 * - Memory is synchronized;
 * - state is replicated;
 * - or an Outcome has occurred.
 *
 * DEFERRED means no truthful bounded cross-device relationship representation
 * was produced.
 */
enum class CrossDeviceRelationshipRepresentationStatus {
    REPRESENTED,
    DEFERRED,
}
