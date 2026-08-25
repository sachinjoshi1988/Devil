package com.devil.app.securityhardening

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 278 Capability Authorization Hardening governance tests.
 *
 * Stage 278 strengthens architectural capability-authorization boundaries only.
 * It must not grant authorization, authorize capabilities, execute, or begin
 * Stage 279.
 */
class Stage278CapabilityAuthorizationHardeningTest {

    @Test
    fun `complete supplied capability authorization hardening evidence becomes hardened`() {
        val sessionHardening = hardenedSessionResult()

        val evidence =
            completeEvidence(
                sessionHardening = sessionHardening,
            )

        val result =
            DevilCapabilityAuthorizationHardeningCoordinator()
                .evaluate(evidence)

        assertEquals(
            DevilCapabilityAuthorizationHardeningStatus.HARDENED,
            result.status,
        )
        assertSame(evidence, result.evidence)
        assertSame(
            sessionHardening,
            result.evidence.sessionHardening,
        )
    }

    @Test
    fun `non hardened Stage 277 result prevents hardened classification`() {
        val hardenedSession = hardenedSessionResult()

        val incompleteSessionEvidence =
            hardenedSession.evidence.copy(
                sessionValiditySeparatedFromAuthorization = false,
            )

        val nonHardenedSession =
            DevilSessionHardeningResult.create(
                evidence = incompleteSessionEvidence,
            )

        val result =
            DevilCapabilityAuthorizationHardeningCoordinator()
                .evaluate(
                    completeEvidence(
                        sessionHardening = nonHardenedSession,
                    ),
                )

        assertEquals(
            DevilCapabilityAuthorizationHardeningStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `missing any required capability authorization boundary prevents hardened classification`() {
        val sessionHardening = hardenedSessionResult()
        val complete =
            completeEvidence(
                sessionHardening = sessionHardening,
            )

        val incompleteEvidence =
            listOf(
                complete.copy(
                    constitutionalAuthorizationSeparatedFromCapabilityAuthorization = false,
                ),
                complete.copy(
                    capabilitySelectionCannotGrantAuthorization = false,
                ),
                complete.copy(
                    androidPermissionCannotGrantDevilAuthorization = false,
                ),
                complete.copy(
                    capabilityAvailabilityCannotGrantAuthorization = false,
                ),
                complete.copy(
                    capabilityReadinessCannotGrantAuthorization = false,
                ),
                complete.copy(
                    executionCapabilityCannotGrantAuthorization = false,
                ),
                complete.copy(
                    deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = false,
                ),
            )

        incompleteEvidence.forEach { evidence ->
            val result =
                DevilCapabilityAuthorizationHardeningCoordinator()
                    .evaluate(evidence)

            assertEquals(
                DevilCapabilityAuthorizationHardeningStatus.NOT_HARDENED,
                result.status,
            )
            assertFalse(evidence.isComplete())
        }
    }

    @Test
    fun `Stage 278 preserves exact Stage 277 provenance`() {
        val sessionHardening = hardenedSessionResult()
        val evidence =
            completeEvidence(
                sessionHardening = sessionHardening,
            )

        val result =
            DevilCapabilityAuthorizationHardeningCoordinator()
                .evaluate(evidence)

        assertSame(sessionHardening, evidence.sessionHardening)
        assertSame(
            sessionHardening,
            result.evidence.sessionHardening,
        )
        assertSame(
            sessionHardening.evidence.authenticationHardening,
            result.evidence
                .sessionHardening
                .evidence
                .authenticationHardening,
        )
        assertSame(
            sessionHardening.evidence
                .authenticationHardening
                .evidence
                .threatModel,
            result.evidence
                .sessionHardening
                .evidence
                .authenticationHardening
                .evidence
                .threatModel,
        )
    }

    @Test
    fun `Stage 278 cannot harden when upstream threat model lacks authorization bypass privilege escalation coverage`() {
        val threatModel =
            DevilFullThreatModelCoordinator()
                .evaluate(
                    evidence =
                        DevilThreatModelEvidence(
                            identityAuthenticationThreatsCovered = true,
                            sessionThreatsCovered = true,
                            authorizationThreatsCovered = false,
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
                            threatModel = threatModel,
                            wakePhraseSeparatedFromAuthentication = true,
                            identityResolutionSeparatedFromAuthentication = true,
                            genuineAuthenticatorRequired = true,
                            unavailableAuthenticatorFailsClosed = true,
                            authenticationRequestCannotEstablishSession = true,
                        ),
                )

        val sessionHardening =
            DevilSessionHardeningResult.create(
                evidence =
                    DevilSessionHardeningEvidence(
                        authenticationHardening = authenticationHardening,
                        nonActiveSessionsRejected = true,
                        validityWindowEnforced = true,
                        authoritativeObservationTimeRequired = true,
                        revokedSessionsInvalidated = true,
                        sessionValiditySeparatedFromAuthentication = true,
                        sessionValiditySeparatedFromAuthorization = true,
                    ),
            )

        assertEquals(
            DevilSessionHardeningStatus.NOT_HARDENED,
            sessionHardening.status,
        )

        val result =
            DevilCapabilityAuthorizationHardeningCoordinator()
                .evaluate(
                    completeEvidence(
                        sessionHardening = sessionHardening,
                    ),
                )

        assertEquals(
            DevilCapabilityAuthorizationHardeningStatus.NOT_HARDENED,
            result.status,
        )
    }

    @Test
    fun `Stage 278 preserves capability authorization and constitutional boundaries`() {
        val source = stage278Source()

        val boundaries =
            listOf(
                "CAPABILITY_SELECTED != CAPABILITY_AUTHORIZED.",
                "CAPABILITY_AVAILABLE != CAPABILITY_AUTHORIZED.",
                "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
                "AUTHORIZATION != EXECUTION_APPROVAL.",
                "AUTHORIZATION != EXECUTION.",
                "SESSION_HARDENED != AUTHORIZATION.",
                "CAPABILITY_AUTHORIZATION_HARDENED != AUTHORIZATION_GRANTED.",
                "CAPABILITY_AUTHORIZATION_HARDENED != VERIFIED_OUTCOME.",
            )

        boundaries.forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 278 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 278 stops before Stage 279 Data Protection`() {
        val source = stage278Source()

        assertTrue(
            source.contains(
                "Stage 279 Data Protection",
            ),
        )
    }

    @Test
    fun `Stage 278 contains no operational authorization or execution wiring`() {
        val source =
            stage278Source()
                .lineSequence()
                .filterNot { line ->
                    line.trimStart().startsWith("*") ||
                        line.trimStart().startsWith("//")
                }
                .joinToString("\n")

        val forbidden =
            listOf(
                "AuthorizationAuthority(",
                "DefaultAuthorizationAuthority(",
                "CapabilitySelectionAuthority(",
                "DefaultCapabilitySelectionAuthority(",
                "ExecutionRequest.create(",
                "AndroidPermissionAuthorityAdapter(",
                ".authorize(",
                ".select(",
                ".execute(",
            )

        forbidden.forEach { value ->
            assertFalse(
                source.contains(value),
                "Stage 278 must not introduce operational authorization wiring: $value",
            )
        }
    }

    private fun completeEvidence(
        sessionHardening: DevilSessionHardeningResult,
    ): DevilCapabilityAuthorizationHardeningEvidence =
        DevilCapabilityAuthorizationHardeningEvidence(
            sessionHardening = sessionHardening,
            constitutionalAuthorizationSeparatedFromCapabilityAuthorization = true,
            capabilitySelectionCannotGrantAuthorization = true,
            androidPermissionCannotGrantDevilAuthorization = true,
            capabilityAvailabilityCannotGrantAuthorization = true,
            capabilityReadinessCannotGrantAuthorization = true,
            executionCapabilityCannotGrantAuthorization = true,
            deniedOrDeferredAuthorizationCannotBeUpgradedDownstream = true,
        )

    private fun hardenedSessionResult(): DevilSessionHardeningResult {
        val threatModel =
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

        return DevilSessionHardeningResult.create(
            evidence =
                DevilSessionHardeningEvidence(
                    authenticationHardening = authenticationHardening,
                    nonActiveSessionsRejected = true,
                    validityWindowEnforced = true,
                    authoritativeObservationTimeRequired = true,
                    revokedSessionsInvalidated = true,
                    sessionValiditySeparatedFromAuthentication = true,
                    sessionValiditySeparatedFromAuthorization = true,
                ),
        )
    }

    private fun stage278Source(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/securityhardening/" +
                        "DevilCapabilityAuthorizationHardening.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/securityhardening/" +
                        "DevilCapabilityAuthorizationHardening.kt",
                ),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate Stage 278 source from: " +
                    candidates.joinToString(),
            )
    }
}
