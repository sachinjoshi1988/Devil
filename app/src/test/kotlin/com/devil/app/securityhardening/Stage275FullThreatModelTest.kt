package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 275 Full Threat Model governance tests.
 *
 * Stage 275 models supplied threat coverage only.
 *
 * It must not become an authentication mechanism, session authority,
 * authorization authority, security executor, attack detector, or mitigation engine.
 */
class Stage275FullThreatModelTest {

    @Test
    fun `complete supplied threat coverage becomes complete`() {
        val evidence =
            completeEvidence()

        val result =
            DevilFullThreatModelCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilThreatModelStatus.COMPLETE,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertEquals(
            DevilThreatCategory.entries.toSet(),
            result.coveredCategories,
        )
    }

    @Test
    fun `missing any required threat domain keeps model incomplete`() {
        val incompleteEvidence =
            listOf(
                completeEvidence().copy(
                    identityAuthenticationThreatsCovered = false,
                ),
                completeEvidence().copy(
                    sessionThreatsCovered = false,
                ),
                completeEvidence().copy(
                    authorizationThreatsCovered = false,
                ),
                completeEvidence().copy(
                    deviceTrustThreatsCovered = false,
                ),
                completeEvidence().copy(
                    untrustedInputThreatsCovered = false,
                ),
                completeEvidence().copy(
                    dataMemoryThreatsCovered = false,
                ),
                completeEvidence().copy(
                    capabilityExecutionThreatsCovered = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilFullThreatModelCoordinator()
                    .evaluate(
                        evidence = evidence,
                    )

            assertEquals(
                DevilThreatModelStatus.INCOMPLETE,
                result.status,
            )

            assertFalse(
                result.coveredCategories.size ==
                    DevilThreatCategory.entries.size,
            )
        }
    }

    @Test
    fun `Stage 275 preserves exact supplied evidence identity`() {
        val evidence =
            completeEvidence()

        val result =
            DevilThreatModelResult.create(
                evidence = evidence,
            )

        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `Stage 275 exposes exactly the required threat categories`() {
        assertEquals(
            setOf(
                DevilThreatCategory.IDENTITY_AUTHENTICATION_SPOOFING,
                DevilThreatCategory.SESSION_COMPROMISE_REPLAY,
                DevilThreatCategory.AUTHORIZATION_BYPASS_PRIVILEGE_ESCALATION,
                DevilThreatCategory.DEVICE_TRUST_MISUSE,
                DevilThreatCategory.UNTRUSTED_EXTERNAL_MODEL_INPUT,
                DevilThreatCategory.DATA_MEMORY_EXPOSURE,
                DevilThreatCategory.CAPABILITY_EXECUTION_MISUSE,
            ),
            DevilThreatCategory.entries.toSet(),
        )
    }

    @Test
    fun `covered categories correspond exactly to supplied evidence`() {
        val evidence =
            completeEvidence().copy(
                sessionThreatsCovered = false,
                dataMemoryThreatsCovered = false,
            )

        assertEquals(
            setOf(
                DevilThreatCategory.IDENTITY_AUTHENTICATION_SPOOFING,
                DevilThreatCategory.AUTHORIZATION_BYPASS_PRIVILEGE_ESCALATION,
                DevilThreatCategory.DEVICE_TRUST_MISUSE,
                DevilThreatCategory.UNTRUSTED_EXTERNAL_MODEL_INPUT,
                DevilThreatCategory.CAPABILITY_EXECUTION_MISUSE,
            ),
            evidence.coveredCategories(),
        )
    }

    @Test
    fun `Stage 275 preserves threat model and constitutional boundaries`() {
        val source =
            stage275Source()

        listOf(
            "THREAT_IDENTIFIED != ATTACK_OCCURRED.",
            "THREAT_MODELED != THREAT_MITIGATED.",
            "THREAT_MODELED != AUTHENTICATION_HARDENED.",
            "THREAT_MODELED != SESSION_HARDENED.",
            "THREAT_MODELED != AUTHORIZATION_HARDENED.",
            "THREAT_MODEL != CONSTITUTIONAL_VERIFICATION.",
            "THREAT_MODEL != SECURITY_VALIDATION.",
            "THREAT_MODEL != AUTHORIZATION.",
            "THREAT_MODEL != EXECUTION_APPROVAL.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 275 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 275 stops before Stage 276 Authentication Hardening`() {
        val source =
            stage275Source()

        assertTrue(
            source.contains(
                "Stage 275 does not implement Stage 276 Authentication Hardening",
            ),
        )
    }

    @Test
    fun `Stage 275 contains no operational security hardening wiring`() {
        val source =
            stage275Source()
                .replace(
                    Regex("(?s)/\\*.*?\\*/"),
                    "",
                )
                .replace(
                    Regex("(?m)//.*$"),
                    "",
                )

        listOf(
            "SecurityIntegrationV2Coordinator(",
            "DefaultTrustAuthority(",
            "DefaultAuthorizationAuthority(",
            "DefaultSecurityTransitionAuthority(",
            "DefaultSessionValidityAuthority(",
            "AndroidDeviceTrustRevocationCoordinator(",
            "checkSelfPermission(",
            "requestPermissions(",
            "Process.killProcess",
            "System.exit",
            "Runtime.getRuntime().exit",
            "startActivity(",
            "startService(",
            "startForegroundService(",
            "WorkManager",
            "JobScheduler",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 275 must not introduce operational security wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence():
        DevilThreatModelEvidence =
        DevilThreatModelEvidence(
            identityAuthenticationThreatsCovered = true,
            sessionThreatsCovered = true,
            authorizationThreatsCovered = true,
            deviceTrustThreatsCovered = true,
            untrustedInputThreatsCovered = true,
            dataMemoryThreatsCovered = true,
            capabilityExecutionThreatsCovered = true,
        )

    private fun stage275Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilFullThreatModel.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilFullThreatModel.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 275 source from: ${candidates.joinToString()}",
            )
    }
}
