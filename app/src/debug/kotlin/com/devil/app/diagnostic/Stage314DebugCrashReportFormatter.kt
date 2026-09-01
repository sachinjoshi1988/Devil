package com.devil.app.diagnostic

import java.io.PrintWriter
import java.io.StringWriter

/**
 * Stage 314 debug-build-only crash-report formatter.
 *
 * DIAGNOSTIC_EVIDENCE != VERIFICATION.
 * CRASH_RECORDED != CRASH_CAUSE_PROVEN.
 */
internal object Stage314DebugCrashReportFormatter {

    fun format(
        threadName: String,
        throwable: Throwable,
        recordedAtMillis: Long,
    ): String {
        val stackTrace =
            StringWriter().also { writer ->
                throwable.printStackTrace(
                    PrintWriter(writer),
                )
            }.toString()

        return buildString {
            appendLine("DEVIL STAGE 314 DEBUG CRASH REPORT")
            appendLine("recordedAtMillis=$recordedAtMillis")
            appendLine("thread=$threadName")
            appendLine(
                "throwable=${throwable::class.java.name}",
            )
            appendLine(
                "message=${throwable.message.orEmpty()}",
            )
            appendLine()
            append(stackTrace)
        }
    }
}
