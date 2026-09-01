package com.devil.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import com.devil.app.DevilApplication
import com.devil.app.BuildConfig

/**
 * Stage 38 Android AccessibilityService embodiment.
 *
 * Stage 314 temporarily adds debug-only lifecycle heartbeat evidence so real
 * Android service/process survival can be established on-device.
 *
 * The heartbeat does not:
 *
 * - keep the service alive;
 * - restart or rebind the service;
 * - create a foreground service;
 * - change Android accessibility state;
 * - authorize Devil;
 * - execute a capability;
 * - establish Observation, Verification, or Outcome.
 *
 * ACCESSIBILITY_CONNECTED != DEVIL_AUTHORIZATION.
 * HEARTBEAT != EXECUTION_EVIDENCE.
 */
class DevilAccessibilityService : AccessibilityService() {

    private val stage314HeartbeatHandler =
        Handler(
            Looper.getMainLooper(),
        )

    private val stage314HeartbeatRunnable =
        object : Runnable {

            override fun run() {
                if (!BuildConfig.DEBUG) {
                    return
                }

                recordStage314Lifecycle(
                    event = "HEARTBEAT",
                )

                stage314HeartbeatHandler.postDelayed(
                    this,
                    STAGE314_HEARTBEAT_INTERVAL_MILLIS,
                )
            }
        }

    override fun onServiceConnected() {
        super.onServiceConnected()

        DevilAccessibilityServiceRegistry.register(
            service = this,
        )

        recordStage314Lifecycle(
            event = "CONNECTED",
        )

        startStage314Heartbeat()
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?,
    ) {
        /*
         * Stage 314 uses only genuine Android window/content changes as a
         * readiness signal for a separately governed post-action inspection.
         *
         * The event itself does not create Observation, Verification, Outcome,
         * authorization, or proof that the requested Android effect succeeded.
         *
         * ACCESSIBILITY_EVENT != OBSERVATION.
         * READINESS_SIGNAL != VERIFICATION.
         */
        val eventType =
            event?.eventType
                ?: return

        if (
            eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        ) {
            return
        }

        val eventScreen =
            if (
                true &&
                event.packageName?.toString() == packageName
            ) {
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

        val diagnosticMainConversationPresent =
            eventElements?.any { element ->
                listOfNotNull(
                    element.text,
                    element.contentDescription,
                ).any { value ->
                    AndroidAccessibilityTarget.normalize(
                        value,
                    ) == "main conversation"
                }
            }

        val diagnosticSettingsDestinationPresent =
            eventElements?.any { element ->
                listOfNotNull(
                    element.text,
                    element.contentDescription,
                ).any { value ->
                    AndroidAccessibilityTarget.normalize(
                        value,
                    ) ==
                        AndroidAccessibilityTarget.normalize(
                            STAGE314_SETTINGS_DESTINATION_MARKER,
                        )
                }
            }

        recordStage314Lifecycle(
            event =
                when (eventType) {
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ->
                        "WINDOW_STATE_CHANGED"
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ->
                        "WINDOW_CONTENT_CHANGED"
                    else ->
                        "UNEXPECTED_ACCESSIBILITY_EVENT"
                },
            packageName = event.packageName,
            className = event.className,
            windowId = event.windowId,
            accessibilityEventTime = event.eventTime,
            screenStatus =
                eventScreen?.status,
            screenElementCount =
                eventElements?.size,
            mainConversationPresent =
                diagnosticMainConversationPresent,
            settingsDestinationPresent =
                diagnosticSettingsDestinationPresent,
        )

        val devilApplication =
            application as? DevilApplication
                ?: return

        devilApplication
            .stage314AccessibilityChangeReadinessStore
            .let { store -> eventElements?.let { store.signalAccessibilitySnapshot(it) } }
    }

    override fun onInterrupt() {
        recordStage314Lifecycle(
            event = "INTERRUPTED",
        )
    }

    override fun onUnbind(
        intent: Intent?,
    ): Boolean {
        stopStage314Heartbeat()

        recordStage314Lifecycle(
            event = "UNBOUND",
        )

        return super.onUnbind(
            intent,
        )
    }

    override fun onDestroy() {
        stopStage314Heartbeat()

        recordStage314Lifecycle(
            event = "DESTROYED",
        )

        DevilAccessibilityServiceRegistry.unregister(
            service = this,
        )

        super.onDestroy()
    }

    private fun startStage314Heartbeat() {
        if (!BuildConfig.DEBUG) {
            return
        }

        stage314HeartbeatHandler.removeCallbacks(
            stage314HeartbeatRunnable,
        )

        stage314HeartbeatHandler.postDelayed(
            stage314HeartbeatRunnable,
            STAGE314_HEARTBEAT_INTERVAL_MILLIS,
        )
    }

    private fun stopStage314Heartbeat() {
        stage314HeartbeatHandler.removeCallbacks(
            stage314HeartbeatRunnable,
        )
    }

    private fun recordStage314Lifecycle(
        event: String,
        packageName: CharSequence? = null,
        className: CharSequence? = null,
        windowId: Int? = null,
        accessibilityEventTime: Long? = null,
        screenStatus: AndroidScreenUnderstandingStatus? = null,
        screenElementCount: Int? = null,
        mainConversationPresent: Boolean? = null,
        settingsDestinationPresent: Boolean? = null,
    ) {
        Stage314AccessibilityLifecycleDiagnosticRecorder.record(
            context = this,
            event = event,
            serviceIdentity =
                System.identityHashCode(
                    this,
                ),
            packageName = packageName,
            className = className,
            windowId = windowId,
            accessibilityEventTime = accessibilityEventTime,
            screenStatus = screenStatus,
            screenElementCount = screenElementCount,
            mainConversationPresent = mainConversationPresent,
            settingsDestinationPresent = settingsDestinationPresent,
        )
    }

    private companion object {

        const val STAGE314_SETTINGS_DESTINATION_MARKER:
            String =
            "Settings, privacy, and permissions presentation"

        const val STAGE314_HEARTBEAT_INTERVAL_MILLIS:
            Long = 15_000L
    }
}
