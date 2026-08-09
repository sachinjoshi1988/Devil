package com.devil.app.capability

import com.devil.core.runtime.capability.CapabilityRegistry

/**
 * Android embodiment of the constitutional CapabilityRegistry boundary.
 *
 * This registry exposes only explicitly registered CapabilityContract values.
 *
 * It does not establish capability availability, health, readiness, Android
 * permission, Devil authorization, execution permission, execution success,
 * observation, verification, or outcome.
 *
 * Registered != Available != Authorized != Ready != Executed.
 */
interface AndroidCapabilityRegistry : CapabilityRegistry
