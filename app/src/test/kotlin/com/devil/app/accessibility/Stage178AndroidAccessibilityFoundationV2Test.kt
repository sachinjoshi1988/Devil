package com.devil.app.accessibility

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Stage178AndroidAccessibilityFoundationV2Test {

    @Test
    fun `connected diagnostic establishes available foundation`() {
        val diagnostic =
            diagnostic(
                status = AndroidAccessibilityServiceDiagnosticStatus.CONNECTED,
            )

        val result =
            AndroidAccessibilityFoundationV2Coordinator()
                .assess(diagnostic)

        assertEquals(
            AndroidAccessibilityFoundationV2Status.AVAILABLE,
            result.status,
        )
        assertEquals(diagnostic, result.diagnostic)
    }

    @Test
    fun `enabled but disconnected diagnostic establishes degraded foundation`() {
        val result =
            AndroidAccessibilityFoundationV2Coordinator()
                .assess(
                    diagnostic(
                        AndroidAccessibilityServiceDiagnosticStatus
                            .ENABLED_BUT_DISCONNECTED,
                    ),
                )

        assertEquals(
            AndroidAccessibilityFoundationV2Status.DEGRADED,
            result.status,
        )
    }

    @Test
    fun `disabled diagnostic establishes unavailable foundation`() {
        val result =
            AndroidAccessibilityFoundationV2Coordinator()
                .assess(
                    diagnostic(
                        AndroidAccessibilityServiceDiagnosticStatus.DISABLED,
                    ),
                )

        assertEquals(
            AndroidAccessibilityFoundationV2Status.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `unknown diagnostic remains unknown`() {
        val result =
            AndroidAccessibilityFoundationV2Coordinator()
                .assess(
                    diagnostic(
                        AndroidAccessibilityServiceDiagnosticStatus.UNKNOWN,
                    ),
                )

        assertEquals(
            AndroidAccessibilityFoundationV2Status.UNKNOWN,
            result.status,
        )
    }

    @Test
    fun `result rejects status that contradicts diagnostic evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidAccessibilityFoundationV2Result.create(
                status = AndroidAccessibilityFoundationV2Status.AVAILABLE,
                diagnostic =
                    diagnostic(
                        AndroidAccessibilityServiceDiagnosticStatus.DISABLED,
                    ),
            )
        }
    }

    private fun diagnostic(
        status: AndroidAccessibilityServiceDiagnosticStatus,
    ): AndroidAccessibilityServiceDiagnostic {
        return AndroidAccessibilityServiceDiagnostic(
            status = status,
            message = "Stage 178 bounded accessibility diagnostic.",
        )
    }
}
