package com.devil.app.diagnostic

import kotlin.test.Test
import kotlin.test.assertContains

class Stage314DebugCrashReportFormatterTest {

    @Test
    fun `formats exact uncaught exception evidence`() {
        val throwable =
            IllegalStateException(
                "stage-314-device-failure",
            )

        val report =
            Stage314DebugCrashReportFormatter.format(
                threadName = "main",
                throwable = throwable,
                recordedAtMillis = 314L,
            )

        assertContains(
            report,
            "DEVIL STAGE 314 DEBUG CRASH REPORT",
        )
        assertContains(
            report,
            "recordedAtMillis=314",
        )
        assertContains(
            report,
            "thread=main",
        )
        assertContains(
            report,
            "throwable=java.lang.IllegalStateException",
        )
        assertContains(
            report,
            "message=stage-314-device-failure",
        )
        assertContains(
            report,
            "Stage314DebugCrashReportFormatterTest",
        )
    }
}
