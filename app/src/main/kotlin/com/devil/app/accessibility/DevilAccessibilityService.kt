package com.devil.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Stage 38 Android AccessibilityService embodiment.
 *
 * The service is intentionally thin.
 *
 * It does not contain a Brain, planner, conversation engine, authorization
 * authority, execution authority, observation authority, verification
 * authority, or memory authority.
 *
 * Android connecting this service establishes platform connectivity only.
 *
 * Accessibility enabled != Devil authorization.
 *
 * Accessibility event != observation proof.
 *
 * Accessibility action != verified outcome.
 */
class DevilAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()

        DevilAccessibilityServiceRegistry.register(
            service = this,
        )
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?,
    ) {
        /*
         * Stage 38 deliberately performs no autonomous interpretation or
         * execution from accessibility events.
         *
         * Events may become bounded observation evidence only through a later
         * explicitly approved observation mechanism.
         */
    }

    override fun onInterrupt() {
        /*
         * Android interruption does not establish task failure or constitutional
         * outcome.
         */
    }

    override fun onDestroy() {
        DevilAccessibilityServiceRegistry.unregister(
            service = this,
        )

        super.onDestroy()
    }
}
