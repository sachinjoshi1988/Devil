package com.devil.app.capability

import com.devil.core.model.capability.CapabilityContract

/**
 * Default Stage 27 Android capability-registration source.
 *
 * No production Android capability implementation has yet reached the point at
 * which its capability contract may be truthfully registered.
 *
 * Therefore the default source currently returns an empty collection rather than
 * fabricating registrations.
 *
 * Later stages may supply genuine CapabilityContract values only when their
 * bounded capability implementations and constitutional boundaries exist.
 *
 * An Android permission, framework API, hardware feature, application component,
 * or planned future capability is not by itself a capability registration.
 */
class DefaultAndroidCapabilityRegistrationSource :
    AndroidCapabilityRegistrationSource {

    override fun registrations(): List<CapabilityContract> {
        return emptyList()
    }
}
