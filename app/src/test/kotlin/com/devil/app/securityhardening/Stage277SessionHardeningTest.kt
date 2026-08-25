package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 277 Session Hardening governance tests.
 *
 * Stage 277 strengthens architectural session boundaries only.
 * It must not create or mutate sessions or begin Stage 278.
 */
class Stage277SessionHardeningTest {

    @Test
    fun `complete supplied session hardening evidence becomes hardened`() {
        val authenticationHardening =
            hardenedAuthenticationResult()

        val evidence =
            completeEvidence(
                authenticationHardening = authenticationHardening,
            )

        val result =
            DevilSessionHardeningCoordinator()
                .evaluate(
                    evidence = evidence,
                )

        assertEquals(
            DevilSessionHardeningStatus.HARDENED,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertSame(
            authenticationHardening,
            result.evidence.authenticationHardening,
        )
    }

    @Test
    fun `non hardened Stage 276 result prevents hardened classification`() {
        val threatModel =
            completeThreatModel()

        val authenticationHardening =
            DevilAuthenticationHardeningCoordinator()
                .evaluate(
                    evidence =
                        DevilAuthenticationHardeningEvidence(
                            threatModel = threatModel,
                            wakePhraseSeparatedFromAuthentication = true,
                            identityResolutionSeparatedFromAuthentication = true,
                            genuineAuthenticatorRequired = true,
                            unavailableAuthenticatorFailsClosed = false,
                            authenticationRequestCannotEstablishSession = true,
                        ),
                )

        val result =
            DevilSessionHardeningCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            authenticationHardening =
                                authenticationHardening,
                        ),
                )

        assertEquals(
            DevilSessionHardeningStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `missing any required session boundary prevents hardened classification`() {
        val authenticationHardening =
            hardenedAuthenticationResult()

        val variants =
            listOf(
                completeEvidence(authenticationHardening)
                    .copy(nonActiveSessionsRejected = false),
                completeEvidence(authenticationHardening)
                    .copy(validityWindowEnforced = false),
                completeEvidence(authenticationHardening)
                    .copy(authoritativeObservationTimeRequired = false),
                completeEvidence(authenticationHardening)
                    .copy(revokedSessionsInvalidated = false),
                completeEvidence(authenticationHardening)
                    .copy(sessionValiditySeparatedFromAuthentication = false),
                completeEvidence(authenticationHardening)
                    .copy(sessionValiditySeparatedFromAuthorization = false),
            )

        variants.forEach { evidence ->
            assertEquals(
                DevilSessionHardeningStatus.NOT_HARDENED,
                DevilSessionHardeningCoordinator()
                    .evaluate(evidence)
                    .status,
            )
        }
    }

    @Test
    fun `Stage 277 preserves exact Stage 276 provenance`() {
        val threatModel =
            completeThreatModel()

        val authenticationHardening =
            DevilAuthenticationHardeningCoordinator()
                .evaluate(
                    evidence =
                        DevilAuthenticationHardeningEvidence(
                            threatModel = threatModel,
                            wakePhraseSeparatedFromAuthentication = true,
                            identityResolutionSeparatedFromAuthentication = true,
                            genuineAuthenticatorRequired = true,
                            unavailableAuthenticatorFailsClosed = true,
                            authenticationRequestCannotEstablishSession = true,
                        ),
                )

        val result =
            DevilSessionHardeningCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            authenticationHardening =
                                authenticationHardening,
                        ),
                )

        assertSame(
            authenticationHardening,
            result.evidence.authenticationHardening,
        )

        assertSame(
            threatModel,
            result.evidence.authenticationHardening
                .evidence
                .threatModel,
        )
    }

    @Test
    fun `Stage 277 requires session compromise replay threat coverage`() {
        val incompleteThreatModel =
            DevilFullThreatModelCoordinator()
                .evaluate(
                    evidence =
                        DevilThreatModelEvidence(
                            identityAuthenticationThreatsCovered = true,
                            sessionThreatsCovered = false,
                            authorizationThreatsCovered = true,
                            deviceTrustThreatsCovered = true,
                            untrustedInputThreatsCovered = true,
                            dataMemoryThreatsCovered = true,
                            capabilityExecutionThreatsCovered = true,
                        ),
                )

        val authenticationHardening =
            DevilAuthenticationHardeningCoordinator()
                .evaluate(
                    evidence =
                        DevilAuthenticationHardeningEvidence(
                            threatModel = incompleteThreatModel,
                            wakePhraseSeparatedFromAuthentication = true,
                            identityResolutionSeparatedFromAuthentication = true,
                            genuineAuthenticatorRequired = true,
                            unavailableAuthenticatorFailsClosed = true,
                            authenticationRequestCannotEstablishSession = true,
                        ),
                )

        val result =
            DevilSessionHardeningCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence(
                            authenticationHardening =
                                authenticationHardening,
                        ),
                )

        assertFalse(
            incompleteThreatModel.coveredCategories.contains(
                DevilThreatCategory.SESSION_COMPROMISE_REPLAY,
            ),
        )

        assertEquals(
            DevilSessionHardeningStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `Stage 277 preserves session and constitutional boundaries`() {
        val source = stage277Source()

        listOf(
            "SESSION_HARDENED != SESSION_CREATED.",
            "SESSION_HARDENED != SESSION_RENEWED.",
            "SESSION_HARDENED != SESSION_REVOKED.",
            "SESSION_HARDENED != AUTHENTICATED.",
            "SESSION_VALID != AUTHENTICATED.",
            "SESSION_VALID != AUTHORIZATION.",
            "SESSION_HARDENED != OWNER_MODE.",
            "SESSION_HARDENED != AUTHORIZATION.",
            "SESSION_HARDENED != EXECUTION_APPROVAL.",
            "SESSION_HARDENED != VERIFIED_OUTCOME.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 277 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 277 stops before Stage 278 Capability Authorization Hardening`() {
        assertTrue(
            stage277Source().contains(
                "Stage 278 Capability Authorization Hardening",
            ),
        )
    }

    @Test
    fun `Stage 277 contains no operational session or authorization wiring`() {
        val source =
            stage277Source()
                .replace(Regex("(?s)/\\*.*?\\*/"), "")
                .replace(Regex("(?m)//.*$"), "")

        listOf(
            "SessionRecord.create(",
            "SessionValidityRequest.create(",
            "DefaultSessionValidityAuthority(",
            "DefaultSessionValidityEvaluator(",
            "SecurityTransitionRequest.create(",
            "AuthorizationEvaluationRequest.create(",
            "ExecutionRequest.create(",
            "startActivity(",
            "startService(",
            "startForegroundService(",
            "WorkManager",
            "JobScheduler",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 277 must not introduce operational session hardening wiring: $forbidden",
            )
        }
    }

    private fun completeEvidence(
        authenticationHardening: DevilAuthenticationHardeningResult,
    ): DevilSessionHardeningEvidence =
        DevilSessionHardeningEvidence(
            authenticationHardening = authenticationHardening,
            nonActiveSessionsRejected = true,
            validityWindowEnforced = true,
            authoritativeObservationTimeRequired = true,
            revokedSessionsInvalidated = true,
            sessionValiditySeparatedFromAuthentication = true,
            sessionValiditySeparatedFromAuthorization = true,
        )

    private fun hardenedAuthenticationResult():
        DevilAuthenticationHardeningResult =
        DevilAuthenticationHardeningCoordinator()
            .evaluate(
                evidence =
                    DevilAuthenticationHardeningEvidence(
                        threatModel = completeThreatModel(),
                        wakePhraseSeparatedFromAuthentication = true,
                        identityResolutionSeparatedFromAuthentication = true,
                        genuineAuthenticatorRequired = true,
                        unavailableAuthenticatorFailsClosed = true,
                        authenticationRequestCannotEstablishSession = true,
                    ),
            )

    private fun completeThreatModel():
        DevilThreatModelResult =
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

    private fun stage277Source(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/DevilSessionHardening.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/DevilSessionHardening.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 277 source from: ${candidates.joinToString()}",
            )
    }
}
