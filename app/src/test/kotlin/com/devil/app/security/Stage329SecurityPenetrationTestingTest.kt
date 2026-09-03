package com.devil.app.security

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyDisclosureStatus
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureStatus
import com.devil.core.model.privacy.PrivacyExposureTarget
import com.devil.core.model.privacy.PrivacyProtectedContextStatus
import com.devil.core.model.privacy.PrivacyRepresentationReducer
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.runtime.authorization.AuthorizationEvaluationRequestStatus
import com.devil.core.runtime.authorization.DefaultAuthorizationEvaluationRequestProvider
import com.devil.core.runtime.capability.CapabilityGovernanceV2Coordinator
import com.devil.core.runtime.capability.CapabilityGovernanceV2Status
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.privacy.PrivacyProtectedContextResolver
import com.devil.core.runtime.security.DefaultSecurityTransitionAuthority
import com.devil.core.runtime.security.SecurityTransitionEvaluationResult
import com.devil.core.runtime.security.SecurityTransitionEvaluationStatus
import com.devil.core.runtime.security.SecurityTransitionEvaluator
import com.devil.core.runtime.security.SecurityTransitionResult
import com.devil.core.runtime.security.SecurityTransitionResultMapper
import com.devil.core.runtime.security.SecurityTransitionStatus
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Stage 329 — Security Penetration Testing.
 *
 * Beta-level bounded adversarial validation of already-existing Devil security
 * boundaries.
 *
 * This suite supplies hostile, mismatched, insufficient, or authority-confusing
 * inputs to existing public contracts. It introduces no attack engine, runtime
 * security authority, authentication mechanism, authorization authority,
 * exploit mechanism, credential attack, network scanner, persistence mechanism,
 * malware behavior, or production execution path.
 *
 * PENETRATION_TEST_PASSED != ATTACK_PREVENTION.
 * PENETRATION_TEST_PASSED != SECURITY_INCIDENT_ABSENT.
 * PENETRATION_TEST_PASSED != AUTHENTICATION.
 * PENETRATION_TEST_PASSED != AUTHORIZATION.
 * PENETRATION_TEST_PASSED != OWNER_MODE.
 * PENETRATION_TEST_PASSED != EXECUTION_APPROVAL.
 * PENETRATION_TEST_PASSED != CONSTITUTIONAL_VERIFICATION.
 * PENETRATION_TEST_PASSED != PRODUCTION_SECURITY_ACCEPTANCE.
 * STAGE_329 != STAGE_330_PERFORMANCE_BETA.
 */
class Stage329SecurityPenetrationTestingTest {

    private fun source(path: String): String {
        val workingDirectory =
            File(
                requireNotNull(System.getProperty("user.dir")) {
                    "Stage 329 requires a JVM user.dir for source validation."
                },
            )

        val candidates =
            listOf(
                File(workingDirectory, path),
                File(workingDirectory, "app/$path"),
                File(
                    workingDirectory.parentFile ?: workingDirectory,
                    "app/$path",
                ),
            )

        val resolved =
            candidates.firstOrNull { it.isFile }
                ?: error(
                    "Unable to resolve Stage 329 source file: $path " +
                        "from ${workingDirectory.absolutePath}",
                )

        return resolved.readText()
    }

    @Test
    fun `cross trace security transition tampering is rejected`() {
        val request =
            com.devil.core.model.security.SecurityTransitionRequest.create(
                context = context("stage-329-security-request"),
                currentState =
                    SecurityStateRecord.create(
                        stage = SecurityStage.LOCKED,
                        rationale = "Locked security stage is established.",
                    ),
                requestedStage = SecurityStage.WAKE,
                rationale = "Attempt bounded transition evaluation.",
            )

        val hostileEvaluator =
            object : SecurityTransitionEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: com.devil.core.model.security.SecurityTransitionRequest,
                ): SecurityTransitionEvaluationResult {
                    return SecurityTransitionEvaluationResult.create(
                        traceId = TraceId.from("stage-329-attacker-trace"),
                        status = SecurityTransitionEvaluationStatus.UNAVAILABLE,
                    )
                }
            }

        assertFailsWith<IllegalArgumentException> {
            DefaultSecurityTransitionAuthority(
                evaluator = hostileEvaluator,
            ).evaluateTransition(request)
        }

        val hostileMapper =
            object : SecurityTransitionResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: SecurityTransitionEvaluationResult,
                ): SecurityTransitionResult {
                    return SecurityTransitionResult.create(
                        traceId = TraceId.from("stage-329-mapped-attacker-trace"),
                        status = SecurityTransitionStatus.DEFERRED,
                    )
                }
            }

        assertFailsWith<IllegalArgumentException> {
            DefaultSecurityTransitionAuthority(
                resultMapper = hostileMapper,
            ).evaluateTransition(request)
        }
    }

    @Test
    fun `verified context and trust classification cannot be promoted into authorization`() {
        val context = context("stage-329-trust-escalation")

        val result =
            DefaultAuthorizationEvaluationRequestProvider()
                .provide(
                    context = context,
                    identity =
                        IdentityResult.create(
                            traceId = context.traceId,
                            status = IdentityStatus.RESOLVED,
                            identityId =
                                IdentityId.from(
                                    "stage-329-subject",
                                ),
                        ),
                    trust =
                        TrustResult.create(
                            traceId = context.traceId,
                            status = TrustStatus.EVALUATED,
                            trustLevel = ContextTrustLevel.VERIFIED,
                        ),
                )

        assertEquals(
            AuthorizationEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `cross subject authorization evidence is rejected`() {
        val context = context("stage-329-cross-subject")

        assertFailsWith<IllegalArgumentException> {
            DefaultAuthorizationEvaluationRequestProvider()
                .provide(
                    context = context,
                    identity =
                        IdentityResult.create(
                            traceId = context.traceId,
                            status = IdentityStatus.RESOLVED,
                            identityId =
                                IdentityId.from(
                                    "stage-329-identity-subject",
                                ),
                        ),
                    trust =
                        TrustResult.create(
                            traceId = context.traceId,
                            status = TrustStatus.EVALUATED,
                            assessment =
                                com.devil.core.model.trust.TrustAssessment.create(
                                    subjectIdentityId =
                                        IdentityId.from(
                                            "stage-329-different-trust-subject",
                                        ),
                                    level =
                                        com.devil.core.model.trust.SubjectTrustLevel
                                            .UNESTABLISHED,
                                    rationale =
                                        "Hostile cross-subject trust assertion.",
                                ),
                        ),
                )
        }
    }

    @Test
    fun `capability availability and readiness cannot become authorization or execution approval`() {
        val traceId = TraceId.from("stage-329-capability-escalation")
        val capability =
            CapabilityContract.create(
                capabilityId =
                    CapabilityId.from(
                        "stage-329-capability",
                    ),
                category = CapabilityCategory.ACTION,
                name = "Stage 329 bounded capability",
                description =
                    "Synthetic capability for adversarial governance validation.",
            )

        val result =
            CapabilityGovernanceV2Coordinator()
                .assess(
                    traceId = traceId,
                    capabilitySelection =
                        CapabilitySelectionResult.create(
                            traceId = traceId,
                            status = CapabilitySelectionStatus.SELECTED,
                            capability = capability,
                        ),
                    availability = CapabilityAvailabilityState.AVAILABLE,
                    health = CapabilityHealthState.READY,
                )

        assertEquals(
            CapabilityGovernanceV2Status.SATISFIED,
            result.status,
        )

        val governanceSource =
            source(
                "src/main/kotlin/com/devil/app/securityhardening/" +
                    "DevilCapabilityAuthorizationHardening.kt",
            )

        assertTrue(
            governanceSource.contains(
                "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
            ),
        )
        assertTrue(
            governanceSource.contains(
                "AUTHORIZATION != EXECUTION_APPROVAL.",
            ),
        )
    }

    @Test
    fun `represented owner mode cannot fabricate protected owner privacy context`() {
        val result =
            PrivacyProtectedContextResolver()
                .resolveOwnerProtectedContext(
                    sessionValidityResult =
                        SessionValidityResult.create(
                            traceId =
                                TraceId.from(
                                    "stage-329-session-deferred",
                                ),
                            status = SessionValidityStatus.DEFERRED,
                        ),
                    securityState =
                        SecurityStateRecord.create(
                            stage = SecurityStage.OWNER_MODE,
                            rationale =
                                "Hostile attempt to use represented Owner Mode.",
                        ),
                )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `protected external representation cannot escape blocked privacy disclosure`() {
        val exposure =
            PrivacyExposureCoordinator()
                .assess(
                    PrivacyExposureRequest.create(
                        classification =
                            PrivacyDataClassification.HIGHLY_SENSITIVE,
                        target =
                            PrivacyExposureTarget.EXTERNAL_SYSTEM,
                        protectedContextEstablished = true,
                    ),
                )

        assertEquals(
            PrivacyExposureStatus.BLOCKED,
            exposure.status,
        )

        val disclosure =
            PrivacyDisclosureCoordinator()
                .evaluate(exposure)

        assertEquals(
            PrivacyDisclosureStatus.BLOCKED,
            disclosure.status,
        )
        assertNull(disclosure.treatment)

        assertFailsWith<IllegalArgumentException> {
            PrivacyRepresentationReducer()
                .reduce(
                    decision = disclosure,
                    representation =
                        "stage-329-protected-adversarial-payload",
                )
        }
    }

    @Test
    fun `stage 329 preserves penetration testing non authority boundaries and stops before stage 330`() {
        val source =
            source(
                "src/test/kotlin/com/devil/app/security/" +
                    "Stage329SecurityPenetrationTestingTest.kt",
            )

        listOf(
            "PENETRATION_TEST_PASSED != ATTACK_PREVENTION.",
            "PENETRATION_TEST_PASSED != SECURITY_INCIDENT_ABSENT.",
            "PENETRATION_TEST_PASSED != AUTHENTICATION.",
            "PENETRATION_TEST_PASSED != AUTHORIZATION.",
            "PENETRATION_TEST_PASSED != OWNER_MODE.",
            "PENETRATION_TEST_PASSED != EXECUTION_APPROVAL.",
            "PENETRATION_TEST_PASSED != CONSTITUTIONAL_VERIFICATION.",
            "PENETRATION_TEST_PASSED != PRODUCTION_SECURITY_ACCEPTANCE.",
            "STAGE_329 != STAGE_330_PERFORMANCE_BETA.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 329 boundary: $boundary",
            )
        }

        assertTrue(
            source.contains(
                "introduces no attack engine",
            ),
        )
    }

    private fun context(traceValue: String): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_329_000L,
                ),
        )
    }
}
