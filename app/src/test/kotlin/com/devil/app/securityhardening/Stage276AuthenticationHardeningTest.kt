package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 276 Authentication Hardening governance tests.
 *
 * Stage 276 strengthens architectural authentication boundaries only.
 * It must not fabricate successful authentication or begin Stage 277.
 */
class Stage276AuthenticationHardeningTest {

    @Test
    fun `complete supplied authentication hardening evidence becomes hardened`() {
        val threatModel = completeThreatModel()

        val evidence =
            completeEvidence(
                threatModel = threatModel,
            )

        val result =
            DevilAuthenticationHardeningCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilAuthenticationHardeningStatus.HARDENED,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            threatModel,
            result.evidence.threatModel,
        )
    }

    @Test
    fun `incomplete Stage 275 threat model prevents hardened classification`() {
        val threatModel =
            DevilFullThreatModelCoordinator()
                .evaluate(
                    evidence =
                        DevilThreatModelEvidence(
                            identityAuthenticationThreatsCovered = false,
                            sessionThreatsCovered = true,
                            authorizationThreatsCovered = true,
                            deviceTrustThreatsCovered = true,
                            untrustedInputThreatsCovered = true,
                            dataMemoryThreatsCovered = true,
                            capabilityExecutionThreatsCovered = true,
                        ),
                )

        val result =
            DevilAuthenticationHardeningCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            threatModel = threatModel,
                        ),
                )

        assertEquals(
            DevilAuthenticationHardeningStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `missing any required authentication boundary prevents hardened classification`() {
        val threatModel = completeThreatModel()

        listOf(
            completeEvidence(
                threatModel = threatModel,
                wakePhraseSeparatedFromAuthentication = false,
            ),
            completeEvidence(
                threatModel = threatModel,
                identityResolutionSeparatedFromAuthentication = false,
            ),
            completeEvidence(
                threatModel = threatModel,
                genuineAuthenticatorRequired = false,
            ),
            completeEvidence(
                threatModel = threatModel,
                unavailableAuthenticatorFailsClosed = false,
            ),
            completeEvidence(
                threatModel = threatModel,
                authenticationRequestCannotEstablishSession = false,
            ),
        ).forEach { evidence ->
            val result =
                DevilAuthenticationHardeningCoordinator()
                    .evaluate(
                        evidence = evidence,
                    )

            assertEquals(
                DevilAuthenticationHardeningStatus.NOT_HARDENED,
                result.status,
            )
        }
    }

    @Test
    fun `Stage 276 preserves exact Stage 275 provenance`() {
        val threatModel = completeThreatModel()

        val evidence =
            completeEvidence(
                threatModel = threatModel,
            )

        val result =
            DevilAuthenticationHardeningResult.create(
                evidence = evidence,
            )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            threatModel,
            result.evidence.threatModel,
        )
    }

    @Test
    fun `Stage 276 requires identity authentication spoofing threat coverage`() {
        val threatModel = completeThreatModel()

        assertTrue(
            threatModel.coveredCategories.contains(
                DevilThreatCategory.IDENTITY_AUTHENTICATION_SPOOFING,
            ),
        )

        assertTrue(
            completeEvidence(
                threatModel = threatModel,
            ).isComplete(),
        )
    }

    @Test
    fun `Stage 276 preserves authentication and constitutional boundaries`() {
        val source = stage276Source()

        listOf(
            "AUTHENTICATION_HARDENED != AUTHENTICATED.",
            "AUTHENTICATION_HARDENED != OWNER_AUTHENTICATED.",
            "IDENTITY_RESOLVED != AUTHENTICATED.",
            "WAKE_MATCHED != AUTHENTICATED.",
            "CODE_RED_RECOGNIZED != AUTHENTICATED.",
            "AUTHENTICATION_REQUESTED != AUTHENTICATED.",
            "AUTHENTICATION_HARDENED != SESSION_ESTABLISHED.",
            "AUTHENTICATION_HARDENED != AUTHORIZATION.",
            "AUTHENTICATION_HARDENED != EXECUTION_APPROVAL.",
            "AUTHENTICATION_HARDENED != VERIFIED_OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 276 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 276 stops before Stage 277 Session Hardening`() {
        assertTrue(
            stage276Source().contains(
                "Stage 277 Session Hardening",
            ),
        )
    }

    @Test
    fun `Stage 276 contains no operational authentication or session wiring`() {
        val source =
            stage276Source()
                .replace(Regex("(?s)/\\*.*?\\*/"), "")
                .replace(Regex("(?m)//.*$"), "")

        listOf(
            "BiometricPrompt(",
            "BiometricManager(",
            "CredentialManager(",
            "createCredential(",
            "getCredential(",
            "KeyguardManager(",
            "FingerprintManager(",
            "SessionRecord.create(",
            "SecurityTransitionRequest.create(",
            "AuthorizationAuthority(",
            "DefaultAuthorizationAuthority(",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 276 must not introduce operational authentication wiring: $forbidden",
            )
        }
    }

    private fun completeThreatModel(): DevilThreatModelResult =
        DevilFullThreatModelCoordinator()
            .evaluate(
                evidence =
                    DevilThreatModelEvidence(
                        identityAuthenticationThreatsCovered = true,
                        sessionThreatsCovered = true,
                        authorizationThreatsCovered = true,
                        deviceTrustThreatsCovered = true,
                        untrustedInputThreatsCovered = true,
                        dataMemoryThreatsCovered = true,
                        capabilityExecutionThreatsCovered = true,
                    ),
            )
            .also { result ->
                assertEquals(
                    DevilThreatModelStatus.COMPLETE,
                    result.status,
                )
            }

    private fun completeEvidence(
        threatModel: DevilThreatModelResult,
        wakePhraseSeparatedFromAuthentication: Boolean = true,
        identityResolutionSeparatedFromAuthentication: Boolean = true,
        genuineAuthenticatorRequired: Boolean = true,
        unavailableAuthenticatorFailsClosed: Boolean = true,
        authenticationRequestCannotEstablishSession: Boolean = true,
    ): DevilAuthenticationHardeningEvidence =
        DevilAuthenticationHardeningEvidence(
            threatModel = threatModel,
            wakePhraseSeparatedFromAuthentication =
                wakePhraseSeparatedFromAuthentication,
            identityResolutionSeparatedFromAuthentication =
                identityResolutionSeparatedFromAuthentication,
            genuineAuthenticatorRequired =
                genuineAuthenticatorRequired,
            unavailableAuthenticatorFailsClosed =
                unavailableAuthenticatorFailsClosed,
            authenticationRequestCannotEstablishSession =
                authenticationRequestCannotEstablishSession,
        )

    private fun stage276Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilAuthenticationHardening.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilAuthenticationHardening.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 276 source from: ${candidates.joinToString()}",
            )
    }
}
