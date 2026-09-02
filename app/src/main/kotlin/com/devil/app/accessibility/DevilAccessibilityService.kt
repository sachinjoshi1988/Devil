package com.devil.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.devil.app.DevilApplication

/**
 * Stage 38 Android AccessibilityService embodiment.
 *
 * Stage 324 keeps accessibility-derived screen traversal bounded to an armed
 * Stage 314 post-action snapshot-capture window. Ordinary Devil navigation does
 * not repeatedly traverse the Android accessibility tree when no bounded
 * snapshot capture is pending.
 *
 * The readiness store remains responsible for rejecting snapshots that arrive
 * before the genuine execution attempt and for deciding when stable snapshot
 * readiness has been established.
 *
 * Android service connection remains availability evidence only. It does not
 * create Devil authorization, execution approval, Observation, Verification,
 * or Outcome.
 *
 * ACCESSIBILITY_CONNECTED != DEVIL_AUTHORIZATION.
 * PENDING != AUTHORIZED.
 * ACCESSIBILITY_EVENT != OBSERVATION.
 * SNAPSHOT_CAPTURED != OBSERVED.
 * SNAPSHOT_STABLE != VERIFIED.
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
        val eventType =
            event?.eventType
                ?: return

        if (
            eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) {
            return
        }

        val devilApplication =
            application as? DevilApplication
                ?: return

        val readinessStore =
            devilApplication
                .stage314AccessibilityChangeReadinessStore

        if (!readinessStore.isAccessibilitySnapshotCapturePending()) {
            return
        }

        val eventScreen =
            if (event.packageName?.toString() == packageName) {
                DefaultAndroidScreenUnderstandingSource(
                    serviceProvider = {
                        this
                    },
                ).inspect()
            } else {
                null
            }

        val eventElements =
            eventScreen
                ?.takeIf {
                    it.status ==
                        AndroidScreenUnderstandingStatus.AVAILABLE
                }
                ?.elements

        eventElements?.let {
            readinessStore.signalAccessibilitySnapshot(it)
        }
    }

    override fun onInterrupt() {
        // Android lifecycle callback only.
        // Interruption does not establish Devil authority or execution evidence.
    }

    override fun onUnbind(
        intent: Intent?,
    ): Boolean {
        return super.onUnbind(
            intent,
        )
    }

    override fun onDestroy() {
        DevilAccessibilityServiceRegistry.unregister(
            service = this,
        )

        super.onDestroy()
    }
}
