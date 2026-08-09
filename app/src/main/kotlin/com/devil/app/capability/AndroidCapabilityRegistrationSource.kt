package com.devil.app.capability

import com.devil.core.model.capability.CapabilityContract

/**
 * Supplies capability contracts explicitly registered by the Android embodiment.
 *
 * Registration declares only capability identity and constitutional purpose.
 *
 * Registration does not establish availability, health, readiness, Android
 * permission, Devil authorization, execution permission, execution success,
 * observation, verification, or outcome.
 *
 * Implementations must not fabricate capability registrations merely because an
 * Android API, service, permission, or device feature exists.
 */
fun interface AndroidCapabilityRegistrationSource {

    fun registrations(): List<CapabilityContract>
}
