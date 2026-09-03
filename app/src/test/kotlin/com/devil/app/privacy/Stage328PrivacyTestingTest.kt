package com.devil.app.privacy

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyDisclosureDecision
import com.devil.core.model.privacy.PrivacyDisclosureRequest
import com.devil.core.model.privacy.PrivacyDisclosureStatus
import com.devil.core.model.privacy.PrivacyDisclosureTreatment
import com.devil.core.model.privacy.PrivacyExposureAssessment
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureStatus
import com.devil.core.model.privacy.PrivacyExposureTarget
import com.devil.core.model.privacy.PrivacyProtectedContextStatus
import com.devil.core.model.privacy.PrivacyRepresentationReducer
import com.devil.core.model.privacy.PrivacyRepresentationStatus
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.runtime.privacy.PrivacyProtectedContextResolver
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 328 — Privacy Testing.
 *
 * Beta-level regression validation of the existing Stage 46 privacy architecture.
 *
 * Stage 328 validates existing privacy behavior. It creates no new Privacy Authority,
 * Security Authority, Authorization Authority, runtime path, disclosure mechanism,
 * transport mechanism, persistence mechanism, or execution mechanism.
 *
 * PRIVACY_ALLOWED != DEVIL_AUTHORIZATION.
 * DISCLOSURE_TREATMENT != DISCLOSURE_PERFORMED.
 * REPRESENTATION_REDUCTION != TRANSMISSION.
 * REPRESENTATION_REDUCTION != PERSISTENCE.
 * PROTECTED_CONTEXT_ESTABLISHED != AUTHENTICATION.
 * PROTECTED_CONTEXT_ESTABLISHED != OWNER_MODE.
 * STAGE_328 != STAGE_329_SECURITY_PENETRATION_TESTING.
 */
class Stage328PrivacyTestingTest {

    private val exposureCoordinator =
        PrivacyExposureCoordinator()

    private val disclosureCoordinator =
        PrivacyDisclosureCoordinator()

    private val representationReducer =
        PrivacyRepresentationReducer()

    private fun source(path: String): String {
        val workingDirectory =
            File(
                requireNotNull(System.getProperty("user.dir")) {
                    "Stage 328 requires a JVM user.dir for source validation."
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
                    "Unable to resolve Stage 328 source file: $path " +
                        "from ${workingDirectory.absolutePath}",
                )

        return resolved.readText()
    }

    @Test
    fun `privacy exposure matrix preserves bounded fail closed behavior`() {
        assertExposure(
            classification = PrivacyDataClassification.PUBLIC,
            target = PrivacyExposureTarget.EXTERNAL_SYSTEM,
            protectedContextEstablished = false,
            expected = PrivacyExposureStatus.ALLOWED,
        )

        assertExposure(
            classification = PrivacyDataClassification.PRIVATE,
            target = PrivacyExposureTarget.OWNER_PRESENTATION,
            protectedContextEstablished = false,
            expected = PrivacyExposureStatus.RESTRICTED,
        )

        assertExposure(
            classification = PrivacyDataClassification.PRIVATE,
            target = PrivacyExposureTarget.EXTERNAL_SYSTEM,
            protectedContextEstablished = true,
            expected = PrivacyExposureStatus.BLOCKED,
        )

        assertExposure(
            classification = PrivacyDataClassification.SENSITIVE,
            target = PrivacyExposureTarget.OWNER_PRESENTATION,
            protectedContextEstablished = false,
            expected = PrivacyExposureStatus.RESTRICTED,
        )

        assertExposure(
            classification = PrivacyDataClassification.SENSITIVE,
            target = PrivacyExposureTarget.EXTERNAL_SYSTEM,
            protectedContextEstablished = true,
            expected = PrivacyExposureStatus.BLOCKED,
        )

        assertExposure(
            classification = PrivacyDataClassification.HIGHLY_SENSITIVE,
            target = PrivacyExposureTarget.OWNER_PRESENTATION,
            protectedContextEstablished = true,
            expected = PrivacyExposureStatus.BLOCKED,
        )

        assertExposure(
            classification = PrivacyDataClassification.HIGHLY_SENSITIVE,
            target = PrivacyExposureTarget.INTERNAL_PROCESSING,
            protectedContextEstablished = true,
            expected = PrivacyExposureStatus.ALLOWED,
        )
    }

    @Test
    fun `protected owner privacy context cannot be fabricated from session or represented security stage`() {
        val resolver =
            PrivacyProtectedContextResolver()

        val invalid =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    determinedSessionValidity(
                        SessionValidityStatus.INVALID,
                    ),
                securityState =
                    securityState(SecurityStage.OWNER_MODE),
            )

        assertEquals(
            PrivacyProtectedContextStatus.NOT_ESTABLISHED,
            invalid.status,
        )

        val deferred =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId =
                            TraceId.from(
                                "stage-328-deferred-validity",
                            ),
                        status =
                            SessionValidityStatus.DEFERRED,
                    ),
                securityState =
                    securityState(SecurityStage.OWNER_MODE),
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            deferred.status,
        )

        val failedTraceId =
            TraceId.from(
                "stage-328-failed-validity",
            )

        val failed =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    SessionValidityResult.create(
                        traceId = failedTraceId,
                        status =
                            SessionValidityStatus.FAILED,
                        error =
                            UniversalErrorRecord.create(
                                errorCode =
                                    ErrorCode.from(
                                        "stage-328-session-validity-failure",
                                    ),
                                traceId = failedTraceId,
                                occurredAt =
                                    DevilTimestamp
                                        .fromEpochMilliseconds(
                                            4_000L,
                                        ),
                                summary =
                                    "Stage 328 session-validity evaluation failed.",
                            ),
                    ),
                securityState =
                    securityState(SecurityStage.OWNER_MODE),
            )

        assertEquals(
            PrivacyProtectedContextStatus.UNAVAILABLE,
            failed.status,
        )

        val validSessionOnly =
            resolver.resolveOwnerProtectedContext(
                sessionValidityResult =
                    determinedSessionValidity(
                        SessionValidityStatus.VALID,
                    ),
                securityState =
                    securityState(SecurityStage.SESSION),
            )

        assertEquals(
            PrivacyProtectedContextStatus.NOT_ESTABLISHED,
            validSessionOnly.status,
        )

        listOf(
            SecurityStage.OWNER_MODE,
            SecurityStage.HIGH_SECURITY_CONFIRMATION,
        ).forEach { stage ->
            val result =
                resolver.resolveOwnerProtectedContext(
                    sessionValidityResult =
                        determinedSessionValidity(
                            SessionValidityStatus.VALID,
                        ),
                    securityState =
                        securityState(stage),
                )

            assertEquals(
                PrivacyProtectedContextStatus.UNAVAILABLE,
                result.status,
            )
        }
    }

    @Test
    fun `exposure provenance governs disclosure treatment without performing disclosure`() {
        val restricted =
            exposure(
                classification =
                    PrivacyDataClassification.PRIVATE,
                target =
                    PrivacyExposureTarget.OWNER_PRESENTATION,
                protectedContextEstablished = false,
            )

        val restrictedDisclosure =
            disclosureCoordinator.evaluate(restricted)

        assertSame(
            restricted,
            restrictedDisclosure.request.exposureAssessment,
        )
        assertEquals(
            PrivacyDisclosureStatus.AVAILABLE,
            restrictedDisclosure.status,
        )
        assertEquals(
            PrivacyDisclosureTreatment.METADATA_ONLY,
            restrictedDisclosure.treatment,
        )

        val blocked =
            exposure(
                classification =
                    PrivacyDataClassification.SENSITIVE,
                target =
                    PrivacyExposureTarget.EXTERNAL_SYSTEM,
                protectedContextEstablished = true,
            )

        val blockedDisclosure =
            disclosureCoordinator.evaluate(blocked)

        assertSame(
            blocked,
            blockedDisclosure.request.exposureAssessment,
        )
        assertEquals(
            PrivacyDisclosureStatus.BLOCKED,
            blockedDisclosure.status,
        )
        assertNull(blockedDisclosure.treatment)
    }

    @Test
    fun `representation reduction removes protected content according to disclosure treatment`() {
        val secret =
            "stage-328-protected-secret"

        val redactedDisclosure =
            disclosureCoordinator.evaluate(
                exposure(
                    classification =
                        PrivacyDataClassification.SENSITIVE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyDisclosureTreatment.REDACTED,
            redactedDisclosure.treatment,
        )

        val redacted =
            representationReducer.reduce(
                decision = redactedDisclosure,
                representation = secret,
            )

        assertEquals(
            PrivacyRepresentationStatus.REDACTED,
            redacted.status,
        )
        assertEquals(
            "[REDACTED]",
            redacted.representation,
        )
        assertTrue(
            redacted.representation != secret,
        )

        val metadataOnlyDisclosure =
            disclosureCoordinator.evaluate(
                exposure(
                    classification =
                        PrivacyDataClassification.PRIVATE,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                    protectedContextEstablished = false,
                ),
            )

        val metadataOnly =
            representationReducer.reduce(
                decision = metadataOnlyDisclosure,
                representation = secret,
            )

        assertEquals(
            PrivacyRepresentationStatus.METADATA_ONLY,
            metadataOnly.status,
        )
        assertNull(metadataOnly.representation)

        /*
         * SUPPRESSED is tested directly at the representation boundary.
         *
         * The current Stage 46 exposure policy fails closed before several
         * suppression combinations can become an AVAILABLE end-to-end
         * disclosure decision. Stage 328 therefore must not fabricate an
         * exposure-coordinator success merely to reach this reducer branch.
         */
        val suppressedDecision =
            disclosureDecision(
                classification =
                    PrivacyDataClassification.PUBLIC,
                target =
                    PrivacyExposureTarget.INTERNAL_PROCESSING,
                treatment =
                    PrivacyDisclosureTreatment.SUPPRESSED,
            )

        val suppressed =
            representationReducer.reduce(
                decision = suppressedDecision,
                representation = secret,
            )

        assertEquals(
            PrivacyRepresentationStatus.SUPPRESSED,
            suppressed.status,
        )
        assertNull(suppressed.representation)
    }

    @Test
    fun `blocked disclosure cannot enter representation reduction`() {
        val blockedDisclosure =
            disclosureCoordinator.evaluate(
                exposure(
                    classification =
                        PrivacyDataClassification.HIGHLY_SENSITIVE,
                    target =
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                    protectedContextEstablished = true,
                ),
            )

        assertEquals(
            PrivacyDisclosureStatus.BLOCKED,
            blockedDisclosure.status,
        )
        assertNull(blockedDisclosure.treatment)

        assertFailsWith<IllegalArgumentException> {
            representationReducer.reduce(
                decision = blockedDisclosure,
                representation = "must-not-pass",
            )
        }
    }

    @Test
    fun `stage 328 preserves privacy non authority boundaries in production composition`() {
        val application =
            source(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            )

        assertTrue(
            application.contains(
                "Privacy ALLOWED does not grant constitutional authorization.",
            ),
        )
        assertTrue(
            application.contains(
                "Privacy disclosure treatment does not perform disclosure.",
            ),
        )
        assertTrue(
            application.contains(
                "Representation reduction does not transmit or persist content.",
            ),
        )
        assertTrue(
            application.contains(
                "Production deliberately does not fabricate protected owner context.",
            ),
        )

        assertFalse(
            application.contains(
                "Privacy ALLOWED grants constitutional authorization.",
            ),
        )
    }

    @Test
    fun `stage 328 begins from stage 327 and does not implement stage 329 penetration testing`() {
        val stage327 =
            source(
                "src/test/kotlin/com/devil/app/ui/accessibility/Stage327AccessibilityTestingTest.kt",
            )

        assertTrue(
            stage327.contains(
                "STAGE_327 != STAGE_328_PRIVACY_TESTING",
            ),
            "Stage 328 must begin from the explicit frozen Stage 327 boundary.",
        )

        /*
         * Stage 329 Security Penetration Testing remains a later roadmap target.
         *
         * Stage 328 creates no attack tooling, penetration mechanism,
         * Security Authority, Authorization Authority, transport,
         * persistence, execution, or production security acceptance.
         */
    }

    private fun exposure(
        classification: PrivacyDataClassification,
        target: PrivacyExposureTarget,
        protectedContextEstablished: Boolean,
    ): PrivacyExposureAssessment =
        exposureCoordinator.assess(
            PrivacyExposureRequest.create(
                classification = classification,
                target = target,
                protectedContextEstablished =
                    protectedContextEstablished,
            ),
        )

    private fun assertExposure(
        classification: PrivacyDataClassification,
        target: PrivacyExposureTarget,
        protectedContextEstablished: Boolean,
        expected: PrivacyExposureStatus,
    ) {
        assertEquals(
            expected,
            exposure(
                classification = classification,
                target = target,
                protectedContextEstablished =
                    protectedContextEstablished,
            ).status,
        )
    }

    private fun determinedSessionValidity(
        status: SessionValidityStatus,
    ): SessionValidityResult {
        require(
            status == SessionValidityStatus.VALID ||
                status == SessionValidityStatus.INVALID,
        ) {
            "Stage 328 determined-session fixture requires VALID or INVALID."
        }

        val request =
            validityRequest()

        return SessionValidityResult.create(
            traceId = request.context.traceId,
            status = status,
            request = request,
        )
    }

    private fun validityRequest(): SessionValidityRequest {
        val observedAt =
            DevilTimestamp.fromEpochMilliseconds(
                2_000L,
            )

        return SessionValidityRequest.create(
            context =
                ContextEnvelope.create(
                    traceId =
                        TraceId.from(
                            "stage-328-privacy-trace",
                        ),
                    schemaVersion =
                        SchemaVersion.from(1),
                    source =
                        ContextSource.SYSTEM,
                    trustLevel =
                        ContextTrustLevel.UNVERIFIED,
                    securityLevel =
                        ContextSecurityLevel.PUBLIC,
                    observedAt = observedAt,
                ),
            session =
                SessionRecord.create(
                    sessionId =
                        SessionId.from(
                            "stage-328-privacy-session",
                        ),
                    subjectIdentityId =
                        IdentityId.from(
                            "stage-328-privacy-subject",
                        ),
                    state =
                        SessionState.ACTIVE,
                    establishedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_000L,
                        ),
                    expiresAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            3_000L,
                        ),
                ),
            observedAt = observedAt,
        )
    }

    private fun securityState(
        stage: SecurityStage,
    ): SecurityStateRecord =
        SecurityStateRecord.create(
            stage = stage,
            rationale =
                "Stage 328 bounded represented security state.",
        )

    private fun disclosureDecision(
        classification: PrivacyDataClassification,
        target: PrivacyExposureTarget,
        treatment: PrivacyDisclosureTreatment,
    ): PrivacyDisclosureDecision {
        val request =
            PrivacyExposureRequest.create(
                classification = classification,
                target = target,
                protectedContextEstablished = true,
            )

        val assessment =
            PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.ALLOWED,
                request = request,
                rationale =
                    "Stage 328 bounded reducer fixture supplies an already-available disclosure context.",
            )

        return PrivacyDisclosureDecision.create(
            status = PrivacyDisclosureStatus.AVAILABLE,
            treatment = treatment,
            request =
                PrivacyDisclosureRequest.create(
                    exposureAssessment = assessment,
                ),
            rationale =
                "Stage 328 bounded representation-reduction fixture.",
        )
    }
}
