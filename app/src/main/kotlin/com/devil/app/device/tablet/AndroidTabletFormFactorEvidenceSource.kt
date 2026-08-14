package com.devil.app.device.tablet

/**
 * Supplies bounded Android screen-configuration evidence for Stage 82
 * tablet form-factor assessment.
 *
 * Returning null means genuine form-factor evidence is not currently
 * available and the caller must fail closed.
 *
 * This source observes Android configuration only.
 *
 * It must not:
 *
 * - infer Devil or subject identity;
 * - authenticate a subject;
 * - grant authorization;
 * - establish session validity;
 * - register or enable capabilities;
 * - grant Android permission;
 * - invoke UnifiedDevilRuntime;
 * - execute an action;
 * - establish Observation, Verification, or Outcome;
 * - create or commit Memory;
 * - or persist logical state.
 */
fun interface AndroidTabletFormFactorEvidenceSource {

    fun evidence(): AndroidTabletFormFactorEvidence?
}
