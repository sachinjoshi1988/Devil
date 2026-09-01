package com.devil.app.accessibility

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Process
import android.provider.MediaStore
import com.devil.app.BuildConfig
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Stage 314 debug-only-at-runtime AccessibilityService lifecycle recorder.
 *
 * The recorder exists only to capture real-device diagnostic evidence while
 * Stage 314 investigates Android AccessibilityService behavior.
 *
 * It does not:
 *
 * - keep the service alive;
 * - restart or rebind the service;
 * - change Android accessibility state;
 * - grant Devil authorization;
 * - perform an accessibility action;
 * - establish observation or verification;
 * - or establish successful Outcome.
 *
 * ACCESSIBILITY_LIFECYCLE_EVENT != EXECUTION_EVIDENCE.
 * ACCESSIBILITY_EVENT_RECORDED != OBSERVATION.
 * ACCESSIBILITY_EVENT_RECORDED != VERIFICATION.
 * DIAGNOSTIC_TREE_SAMPLE != OBSERVATION.
 * DIAGNOSTIC_MARKER_PRESENT != VERIFICATION.
 */
object Stage314AccessibilityLifecycleDiagnosticRecorder {

    fun record(
        context: Context,
        event: String,
        serviceIdentity: Int,
        packageName: CharSequence? = null,
        className: CharSequence? = null,
        windowId: Int? = null,
        accessibilityEventTime: Long? = null,
        screenStatus: AndroidScreenUnderstandingStatus? = null,
        screenElementCount: Int? = null,
        mainConversationPresent: Boolean? = null,
        settingsDestinationPresent: Boolean? = null,
    ) {
        if (!BuildConfig.DEBUG) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }

        runCatching {
            val timestampMillis =
                System.currentTimeMillis()

            val timestamp =
                SimpleDateFormat(
                    "yyyyMMdd-HHmmss-SSS",
                    Locale.US,
                ).format(
                    Date(timestampMillis),
                )

            val normalizedEvent =
                event
                    .trim()
                    .uppercase(Locale.US)
                    .replace(
                        regex = Regex("[^A-Z0-9_-]"),
                        replacement = "_",
                    )
                    .ifEmpty {
                        "UNKNOWN"
                    }

            val fileName =
                "Devil-Stage314-accessibility-" +
                    timestamp +
                    "-" +
                    normalizedEvent +
                    ".txt"

            val values =
                ContentValues().apply {
                    put(
                        MediaStore.Downloads.DISPLAY_NAME,
                        fileName,
                    )
                    put(
                        MediaStore.Downloads.MIME_TYPE,
                        "text/plain",
                    )
                    put(
                        MediaStore.Downloads.RELATIVE_PATH,
                        "Download/DevilDiagnostics",
                    )
                }

            val resolver =
                context
                    .applicationContext
                    .contentResolver

            val uri =
                resolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values,
                )
                    ?: return@runCatching

            resolver
                .openOutputStream(
                    uri,
                    "w",
                )
                ?.bufferedWriter()
                ?.use { writer ->
                    writer.appendLine(
                        "DEVIL STAGE 314 ACCESSIBILITY LIFECYCLE",
                    )
                    writer.appendLine(
                        "recordedAtMillis=$timestampMillis",
                    )
                    writer.appendLine(
                        "event=$normalizedEvent",
                    )
                    writer.appendLine(
                        "pid=${Process.myPid()}",
                    )
                    writer.appendLine(
                        "serviceIdentity=$serviceIdentity",
                    )
                    writer.appendLine(
                        "thread=${Thread.currentThread().name}",
                    )

                    if (packageName != null) {
                        writer.appendLine(
                            "packageName=$packageName",
                        )
                    }

                    if (className != null) {
                        writer.appendLine(
                            "className=$className",
                        )
                    }

                    if (windowId != null) {
                        writer.appendLine(
                            "windowId=$windowId",
                        )
                    }

                    if (accessibilityEventTime != null) {
                        writer.appendLine(
                            "accessibilityEventTime=$accessibilityEventTime",
                        )
                    }

                    if (screenStatus != null) {
                        writer.appendLine(
                            "screenStatus=$screenStatus",
                        )
                    }

                    if (screenElementCount != null) {
                        writer.appendLine(
                            "screenElementCount=$screenElementCount",
                        )
                    }

                    if (mainConversationPresent != null) {
                        writer.appendLine(
                            "mainConversationPresent=$mainConversationPresent",
                        )
                    }

                    if (settingsDestinationPresent != null) {
                        writer.appendLine(
                            "settingsDestinationPresent=$settingsDestinationPresent",
                        )
                    }
                }
        }
    }
}
