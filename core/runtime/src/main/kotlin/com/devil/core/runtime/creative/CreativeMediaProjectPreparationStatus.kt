package com.devil.core.runtime.creative

/**
 * Stage 87 bounded Creative Media project-preparation status.
 *
 * PREPARED means one structurally valid CreativeMediaProjectRecord was prepared
 * from explicitly supplied Creative Media inputs.
 *
 * PREPARED does not mean:
 *
 * - creative intent was inferred from raw conversation;
 * - a constitutional Decision exists;
 * - authorization exists;
 * - a Task or Plan exists;
 * - a capability was registered or selected;
 * - a model or generator was selected;
 * - capability availability, health, or readiness was established;
 * - platform permission exists;
 * - generation was requested;
 * - execution is approved;
 * - media was generated;
 * - an asset or file exists;
 * - Observation occurred;
 * - Verification occurred;
 * - an Outcome occurred;
 * - World Model state changed;
 * - constitutional Learning occurred;
 * - Memory was committed;
 * - creative state was persisted;
 * - or Story-to-Animation occurred.
 *
 * DEFERRED means no truthful bounded Creative Media project was produced.
 *
 * PREPARED != AUTHORIZED.
 * PREPARED != CAPABILITY_READY.
 * PREPARED != GENERATED.
 * PREPARED != EXECUTED.
 */
enum class CreativeMediaProjectPreparationStatus {
    PREPARED,
    DEFERRED,
}
