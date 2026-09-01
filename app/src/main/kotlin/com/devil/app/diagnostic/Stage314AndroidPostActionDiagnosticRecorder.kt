package com.devil.app.diagnostic

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Process
import android.provider.MediaStore
import com.devil.app.BuildConfig
import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Stage 314 debug-runtime recorder for bounded post-action O/V/O diagnostics.
 *
 * The recorder writes only diagnostic facts supplied by the existing
 * constitutional execution path.
 *
 * It does not alter any O/V/O decision.
 *
 * DIAGNOSTIC_EVENT != CONSTITUTIONAL_EVIDENCE.
 */
class Stage314AndroidPostActionDiagnosticRecorder(
    context: Context,
) : Stage314PostActionDiagnostic {

    private val applicationContext =
        context.applicationContext

    override fun observation(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        elements: List<AndroidScreenElementRecord>,
    ) {
        record(
            stage = "OBSERVATION",
            traceId = traceId,
            capabilityId = capabilityId,
            event = event,
            expectedVisibleText = null,
            elements = elements,
        )
    }

    override fun verification(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        expectedVisibleText: String?,
    ) {
        record(
            stage = "VERIFICATION",
            traceId = traceId,
            capabilityId = capabilityId,
            event = event,
            expectedVisibleText = expectedVisibleText,
            elements = emptyList(),
        )
    }

    override fun outcome(
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
    ) {
        record(
            stage = "OUTCOME",
            traceId = traceId,
            capabilityId = capabilityId,
            event = event,
            expectedVisibleText = null,
            elements = emptyList(),
        )
    }

    private fun record(
        stage: String,
        traceId: TraceId,
        capabilityId: CapabilityId,
        event: String,
        expectedVisibleText: String?,
        elements: List<AndroidScreenElementRecord>,
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

            val normalizedStage =
                normalizeFileToken(stage)

            val normalizedEvent =
                normalizeFileToken(event)

            val values =
                ContentValues().apply {
                    put(
                        MediaStore.Downloads.DISPLAY_NAME,
                        "Devil-Stage314-ovo-" +
                            timestamp +
                            "-" +
                            normalizedStage +
                            "-" +
                            normalizedEvent +
                            ".txt",
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
                applicationContext.contentResolver

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
                        "DEVIL STAGE 314 POST-ACTION O/V/O DIAGNOSTIC",
                    )
                    writer.appendLine(
                        "recordedAtMillis=$timestampMillis",
                    )
                    writer.appendLine(
                        "stage=$normalizedStage",
                    )
                    writer.appendLine(
                        "event=$normalizedEvent",
                    )
                    writer.appendLine(
                        "traceId=${traceId.value}",
                    )
                    writer.appendLine(
                        "capabilityId=${capabilityId.value}",
                    )
                    writer.appendLine(
                        "pid=${Process.myPid()}",
                    )
                    writer.appendLine(
                        "thread=${Thread.currentThread().name}",
                    )

                    if (expectedVisibleText != null) {
                        writer.appendLine(
                            "expectedVisibleText=$expectedVisibleText",
                        )
                    }

                    writer.appendLine(
                        "screenElementCount=${elements.size}",
                    )

                    elements.forEach { element ->
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

    private fun normalizeFileToken(
        value: String,
    ): String {
        return value
            .trim()
            .uppercase(Locale.US)
            .replace(
                regex = Regex("[^A-Z0-9_-]"),
                replacement = "_",
            )
            .ifEmpty {
                "UNKNOWN"
            }
    }
}
