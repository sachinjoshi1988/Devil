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
 * Stage 314 debug-only-at-runtime accessibility execution diagnostic recorder.
 *
 * Records bounded evidence from the existing Android accessibility action source.
 *
 * When supplied, one existing Stage 179 Screen Understanding result may also be
 * preserved for diagnosis. That snapshot contains only the accessibility-derived
 * text/content-description metadata already allowed by Stage 179.
 *
 * This recorder does not:
 *
 * - authenticate a subject;
 * - grant authorization;
 * - approve execution;
 * - select a capability;
 * - arm an execution directive;
 * - perform an accessibility action;
 * - alter accessibility-service state;
 * - infer screen meaning;
 * - resolve an execution target;
 * - establish Observation;
 * - establish Verification;
 * - or establish Outcome.
 *
 * DIAGNOSTIC_EVENT != EXECUTION_APPROVAL.
 * SCREEN_METADATA != USER_INTENT.
 * SCREEN_ELEMENT != EXECUTION_TARGET.
 * CLICK_ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 */
object Stage314AccessibilityExecutionDiagnosticRecorder {

    fun record(
        context: Context,
        event: String,
        target: AndroidAccessibilityTarget,
        screenUnderstanding: AndroidScreenUnderstandingResult? = null,
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
                "Devil-Stage314-execution-" +
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
                        "DEVIL STAGE 314 ACCESSIBILITY EXECUTION",
                    )
                    writer.appendLine(
                        "recordedAtMillis=$timestampMillis",
                    )
                    writer.appendLine(
                        "event=$normalizedEvent",
                    )
                    writer.appendLine(
                        "target=${target.text}",
                    )
                    writer.appendLine(
                        "normalizedTarget=${target.normalizedText}",
                    )
                    writer.appendLine(
                        "pid=${Process.myPid()}",
                    )
                    writer.appendLine(
                        "thread=${Thread.currentThread().name}",
                    )

                    if (screenUnderstanding != null) {
                        writer.appendLine(
                            "screenStatus=${screenUnderstanding.status}",
                        )
                        writer.appendLine(
                            "screenElementCount=${screenUnderstanding.elements.size}",
                        )

                        screenUnderstanding.elements.forEach { element ->
                            writer.appendLine(
                                "screenElement[" +
                                    element.position +
                                    "].text=" +
                                    element.text,
                            )
                            writer.appendLine(
                                "screenElement[" +
                                    element.position +
                                    "].contentDescription=" +
                                    element.contentDescription,
                            )
                        }
                    }
                }
        }
    }
}
