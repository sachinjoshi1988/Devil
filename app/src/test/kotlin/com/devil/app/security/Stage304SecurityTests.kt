package com.devil.app.security

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 304 Security Tests completion coverage for the established bounded
 * Devil security, trust, authorization, session, device-trust, and security-hardening architecture.
 *
 * This test surface validates existing security behavior only.
 *
 * TRUST != AUTHENTICATION.
 * TRUST != AUTHORIZATION.
 * ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.
 * SESSION_VALID != AUTHENTICATED.
 * SESSION_VALID != AUTHORIZATION.
 * DEVICE_TRUST != AUTHENTICATION.
 * DEVICE_TRUST != AUTHORIZATION.
 * REVOCATION_STATE != REVOCATION_EXECUTION.
 * FINAL_SECURITY_REVIEW != CONSTITUTIONAL_VERIFICATION.
 *
 * Stage 304 does not create another runtime, Brain, Planner, Executive,
 * constitutional authority, authentication mechanism, authorization mechanism,
 * session authority, revocation executor, Security Authority, Memory Authority,
 * or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 305 Memory & Continuity Tests.
 */
class Stage304SecurityTests {

    @Test
    fun `trust authorization and session authorities preserve constitutional separation`() {
        val trust =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/trust/DefaultTrustAuthority.kt",
            )
        val authorization =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/authorization/DefaultAuthorizationAuthority.kt",
            )
        val session =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/security/DefaultSessionValidityAuthority.kt",
            )
        val transition =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/security/DefaultSecurityTransitionAuthority.kt",
            )

        assertTrue(
            trust.contains(
                "It does not resolve identity, authenticate a subject, prove ownership, grant",
            ),
        )
        assertTrue(
            authorization.contains(
                "grant operating-system permission",
            ),
        )
        assertTrue(
            authorization.contains(
                "enter Owner Mode, execute",
            ),
        )

        listOf(session, transition).forEachIndexed { index, text ->
            listOf(
                "authenticate a subject",
                "prove owner identity",
                "grant authorization",
                "enter Owner Mode",
                "approve high-security confirmation",
                "grant Android permission",
                "permit capability execution",
            ).forEach { marker ->
                assertTrue(
                    text.contains(marker),
                    "Stage 304 security authority surface $index lacks boundary marker: $marker",
                )
            }
        }
    }

    @Test
    fun `device trust and revocation remain descriptive governance rather than security execution`() {
        val coordinator =
            source(
                "app/src/main/kotlin/com/devil/app/device/AndroidDeviceTrustRevocationCoordinator.kt",
            )
        val result =
            source(
                "app/src/main/kotlin/com/devil/app/device/AndroidDeviceTrustRevocationResult.kt",
            )
        val status =
            source(
                "app/src/main/kotlin/com/devil/app/device/AndroidDeviceTrustRevocationStatus.kt",
            )

        listOf(
            "DEVICE_TRUST != AUTHENTICATION.",
            "DEVICE_TRUST != AUTHORIZATION.",
            "TRUSTED != EXECUTION_AUTHORITY.",
            "REVOKED != SESSION_TERMINATION.",
            "REVOKED != CREDENTIAL_REVOCATION.",
            "REVOKED != MEMORY_DELETION.",
            "REVOCATION_STATE != REVOCATION_EXECUTION.",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 304 device-trust boundary: $marker",
            )
            assertTrue(result.contains(marker))
            assertTrue(status.contains(marker))
        }

        assertTrue(
            coordinator.contains(
                "AndroidDeviceTrustRevocationStatus.DEFERRED",
            ),
        )
        assertTrue(
            result.contains(
                "Stage 222 trusted or revoked state requires available Stage 221 Cross-Device Memory Continuity.",
            ),
        )
    }

    @Test
    fun `security hardening preserves authentication session and authorization boundaries`() {
        val authentication =
            source(
                "app/src/main/kotlin/com/devil/app/securityhardening/DevilAuthenticationHardening.kt",
            )
        val session =
            source(
                "app/src/main/kotlin/com/devil/app/securityhardening/DevilSessionHardening.kt",
            )
        val authorization =
            source(
                "app/src/main/kotlin/com/devil/app/securityhardening/DevilCapabilityAuthorizationHardening.kt",
            )

        listOf(
            "AUTHENTICATION_HARDENED != AUTHENTICATED.",
            "WAKE_MATCHED != AUTHENTICATED.",
            "CODE_RED_RECOGNIZED != AUTHENTICATED.",
            "AUTHENTICATION_HARDENED != AUTHORIZATION.",
            "AUTHENTICATION_HARDENED != EXECUTION_APPROVAL.",
        ).forEach { marker ->
            assertTrue(
                authentication.contains(marker),
                "Missing Stage 304 authentication-hardening boundary: $marker",
            )
        }

        listOf(
            "SESSION_HARDENED != SESSION_CREATED.",
            "SESSION_HARDENED != SESSION_RENEWED.",
            "SESSION_HARDENED != SESSION_REVOKED.",
            "SESSION_VALID != AUTHENTICATED.",
            "SESSION_VALID != AUTHORIZATION.",
            "SESSION_HARDENED != EXECUTION_APPROVAL.",
        ).forEach { marker ->
            assertTrue(
                session.contains(marker),
                "Missing Stage 304 session-hardening boundary: $marker",
            )
        }

        listOf(
            "CAPABILITY_SELECTED != CAPABILITY_AUTHORIZED.",
            "CAPABILITY_AVAILABLE != CAPABILITY_AUTHORIZED.",
            "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
            "AUTHORIZATION != EXECUTION_APPROVAL.",
            "AUTHORIZATION != EXECUTION.",
            "CAPABILITY_AUTHORIZATION_HARDENED != AUTHORIZATION_GRANTED.",
        ).forEach { marker ->
            assertTrue(
                authorization.contains(marker),
                "Missing Stage 304 capability-authorization boundary: $marker",
            )
        }
    }

    @Test
    fun `final security review remains review evidence rather than constitutional authorization or verification`() {
        val review =
            source(
                "app/src/main/kotlin/com/devil/app/securityhardening/DevilFinalSecurityReview.kt",
            )

        listOf(
            "FINAL_SECURITY_REVIEW != CONSTITUTIONAL_SECURITY_REVIEW.",
            "FINAL_SECURITY_REVIEW != CONSTITUTIONAL_VERIFICATION.",
            "FINAL_SECURITY_REVIEW != SECURITY_AUTHORIZATION.",
            "FINAL_SECURITY_REVIEW != EXECUTION_AUTHORIZATION.",
            "FINAL_SECURITY_REVIEW != ATTACK_PREVENTION.",
            "FINAL_SECURITY_REVIEW != VERIFIED_OUTCOME.",
            "FINAL_SECURITY_REVIEW != PRODUCTION_SECURITY_ACCEPTANCE.",
        ).forEach { marker ->
            assertTrue(
                review.contains(marker),
                "Missing Stage 304 final-security-review boundary: $marker",
            )
        }
    }

    @Test
    fun `existing security tests preserve deferred failed invalid and invariant coverage`() {
        val representativeTests =
            listOf(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/trust/DefaultTrustAuthorityTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/authorization/DefaultAuthorizationAuthorityTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/security/DefaultSessionValidityAuthorityTest.kt",
                "app/src/test/kotlin/com/devil/app/device/Stage222DeviceTrustRevocationTest.kt",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage276AuthenticationHardeningTest.kt",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage277SessionHardeningTest.kt",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage278CapabilityAuthorizationHardeningTest.kt",
                "app/src/test/kotlin/com/devil/app/securityhardening/Stage285FinalSecurityReviewTest.kt",
            )

        representativeTests.forEachIndexed { index, path ->
            val test = source(path)

            assertTrue(
                test.contains("DEFERRED") ||
                    test.contains("FAILED") ||
                    test.contains("INVALID") ||
                    test.contains("assertFalse"),
                "Stage 304 representative security test $index lacks non-success coverage.",
            )

            assertTrue(
                test.contains("assertEquals") ||
                    test.contains("assertFailsWith"),
                "Stage 304 representative security test $index lacks meaningful invariant assertions.",
            )
        }
    }

    @Test
    fun `Stage 304 stops before memory and continuity test completion`() {
        val stage304 =
            source(
                "app/src/test/kotlin/com/devil/app/security/Stage304SecurityTests.kt",
            )

        assertTrue(stage304.contains("does not implement"))
        assertTrue(
            stage304.contains("Stage 305 Memory & Continuity Tests"),
        )
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("..", path),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate repository source for Stage 304: $path",
            )
    }
}
