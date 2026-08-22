package com.devil.app.security

import com.devil.app.device.AndroidCrossDeviceIdentityCoordinator
import com.devil.app.device.AndroidCrossDeviceMemoryContinuityCoordinator
import com.devil.app.device.AndroidCrossDeviceSessionGovernanceCoordinator
import com.devil.app.device.AndroidCrossDeviceTaskContinuityCoordinator
import com.devil.app.device.AndroidDeviceProtocolIntegrationCoordinator
import com.devil.app.device.AndroidDeviceTrustRevocationCoordinator
import com.devil.app.device.AndroidDeviceTrustRevocationStatus
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationCoordinator
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationResult
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationStatus
import com.devil.app.device.pc.AndroidPcCapabilityAdapterCoordinator
import com.devil.app.device.pc.AndroidPcEmbodimentCoordinator
import com.devil.app.device.tablet.AndroidEducationTabletExperienceCoordinator
import com.devil.app.device.tablet.AndroidTabletEmbodimentCoordinator
import com.devil.app.device.tablet.AndroidTabletFormFactorAssessmentResult
import com.devil.app.device.tablet.AndroidTabletFormFactorAssessmentStatus
import com.devil.app.device.tablet.AndroidTabletFormFactorEvidence
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.embodiment.CrossDeviceRelationshipRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.model.embodiment.PcEmbodimentEvidence
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationResult
import com.devil.core.runtime.embodiment.CrossDeviceRelationshipRepresentationStatus
import com.devil.core.runtime.embodiment.PcEmbodimentAssessmentResult
import com.devil.core.runtime.embodiment.PcEmbodimentAssessmentStatus
import com.devil.core.runtime.memory.MemoryContinuityResult
import com.devil.core.runtime.memory.MemoryContinuityStatus
import com.devil.core.runtime.security.SessionValidityResult
import com.devil.core.runtime.security.SessionValidityStatus
import com.devil.core.runtime.surveillance.SecuritySurveillanceCoordinator
import com.devil.core.runtime.surveillance.SecuritySurveillancePreparationResult
import com.devil.core.runtime.surveillance.SecuritySurveillancePreparationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage224SecuritySurveillanceIntegrationTest {

    @Test
    fun `validated multi device context and prepared surveillance become available with exact provenance`() {
        val multiDevice = validatedMultiDevice()
        val surveillance = preparedSurveillance()

        val result =
            AndroidSecuritySurveillanceIntegrationCoordinator()
                .integrate(
                    multiDeviceValidation = multiDevice,
                    surveillancePreparation = surveillance,
                )

        assertEquals(
            AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE,
            result.status,
        )
        assertSame(multiDevice, result.multiDeviceValidation)
        assertSame(surveillance, result.surveillancePreparation)
        assertSame(
            surveillance.record,
            result.surveillancePreparation.record,
        )
        assertEquals(
            surveillance.traceId,
            result.surveillancePreparation.traceId,
        )
    }

    @Test
    fun `deferred Stage 223 context keeps surveillance integration deferred`() {
        val multiDevice =
            AndroidUnifiedMultiDeviceValidationResult.create(
                status = AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
            )

        val surveillance = preparedSurveillance()

        val result =
            AndroidSecuritySurveillanceIntegrationCoordinator()
                .integrate(
                    multiDeviceValidation = multiDevice,
                    surveillancePreparation = surveillance,
                )

        assertEquals(
            AndroidSecuritySurveillanceIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(multiDevice, result.multiDeviceValidation)
        assertSame(surveillance, result.surveillancePreparation)
    }

    @Test
    fun `deferred Stage 90 surveillance keeps Stage 224 deferred`() {
        val multiDevice = validatedMultiDevice()

        val surveillance =
            SecuritySurveillancePreparationResult.create(
                traceId = TraceId.from("trace-stage224-deferred"),
                status = SecuritySurveillancePreparationStatus.DEFERRED,
            )

        val result =
            AndroidSecuritySurveillanceIntegrationCoordinator()
                .integrate(
                    multiDeviceValidation = multiDevice,
                    surveillancePreparation = surveillance,
                )

        assertEquals(
            AndroidSecuritySurveillanceIntegrationStatus.DEFERRED,
            result.status,
        )
        assertSame(surveillance, result.surveillancePreparation)
    }

    @Test
    fun `available result rejects deferred Stage 223 context`() {
        val deferred =
            AndroidUnifiedMultiDeviceValidationResult.create(
                status = AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSecuritySurveillanceIntegrationResult.create(
                status = AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE,
                multiDeviceValidation = deferred,
                surveillancePreparation = preparedSurveillance(),
            )
        }
    }

    @Test
    fun `available result rejects deferred Stage 90 surveillance`() {
        val deferredSurveillance =
            SecuritySurveillancePreparationResult.create(
                traceId = TraceId.from("trace-stage224-invalid"),
                status = SecuritySurveillancePreparationStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSecuritySurveillanceIntegrationResult.create(
                status = AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE,
                multiDeviceValidation = validatedMultiDevice(),
                surveillancePreparation = deferredSurveillance,
            )
        }
    }

    @Test
    fun `prepared watchlist candidate claim remains exact Stage 90 claim only`() {
        val surveillance =
            SecuritySurveillanceCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage224-watchlist",
                        ),
                    sourceId = "camera:stage224-entry",
                    sourceType = "network-camera",
                    occurredAtEpochMilliseconds = 224L,
                    description =
                        "Externally supplied bounded surveillance signal.",
                    externalWatchlistReferenceId =
                        "watchlist-reference:stage224",
                    externalWatchlistSourceSystem =
                        "external-stage224-matcher",
                    externalWatchlistConfidencePermille = 800,
                )

        val result =
            AndroidSecuritySurveillanceIntegrationCoordinator()
                .integrate(
                    multiDeviceValidation = validatedMultiDevice(),
                    surveillancePreparation = surveillance,
                )

        assertEquals(
            AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            requireNotNull(surveillance.record).watchlistMatchClaim,
            requireNotNull(result.surveillancePreparation.record)
                .watchlistMatchClaim,
        )
    }

    private fun preparedSurveillance():
        SecuritySurveillancePreparationResult {
        return SecuritySurveillanceCoordinator()
            .prepare(
                traceId = TraceId.from("trace-stage224-surveillance"),
                sourceId = "sensor:stage224",
                sourceType = "bounded-surveillance-source",
                occurredAtEpochMilliseconds = 224L,
                description =
                    "Existing Stage 90 bounded surveillance signal.",
            )
    }

    private fun validatedMultiDevice():
        AndroidUnifiedMultiDeviceValidationResult {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId = TraceId.from("trace-stage224-relationship"),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage224:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage224:target",
                            ),
                        description =
                            "Bounded Stage 224 relationship.",
                    ),
            )

        val deviceProtocol =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = "devil.stage224",
                )

        val tabletRecord =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage224:tablet",
                    ),
                platformId = EmbodimentPlatformId.from("android"),
                description = "Stage 224 tablet embodiment.",
            )

        val tabletAssessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage224-tablet"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.TABLET,
                embodiment = tabletRecord,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 720,
                    ),
            )

        val tablet =
            AndroidTabletEmbodimentCoordinator()
                .integrate(tabletAssessment)

        val educationTablet =
            AndroidEducationTabletExperienceCoordinator()
                .integrate(
                    tabletEmbodiment = tablet,
                    educationSession =
                        EducationSessionRecord.create(
                            sessionId =
                                EducationSessionId.from(
                                    "education-session:stage224",
                                ),
                            subjectIdentityId =
                                IdentityId.from(
                                    "identity:stage224:learner",
                                ),
                            objective =
                                EducationObjective.create(
                                    subject = "Security integration",
                                    objective =
                                        "Preserve bounded tablet context.",
                                ),
                        ),
                )

        val pcRecord =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage224:pc",
                    ),
                platformId = EmbodimentPlatformId.from("pc"),
                description = "Stage 224 PC embodiment.",
            )

        val pc =
            AndroidPcEmbodimentCoordinator()
                .integrate(
                    PcEmbodimentAssessmentResult.create(
                        traceId = TraceId.from("trace-stage224-pc"),
                        status = PcEmbodimentAssessmentStatus.PC,
                        embodiment = pcRecord,
                        evidence =
                            PcEmbodimentEvidence.create(
                                operatingSystemFamily = "Linux",
                            ),
                    ),
                )

        val pcAdapter =
            AndroidPcCapabilityAdapterCoordinator()
                .integrate(
                    pcEmbodiment = pc,
                    capability =
                        CapabilityContract.create(
                            capabilityId =
                                CapabilityId.from(
                                    "capability:stage224",
                                ),
                            category = CapabilityCategory.ACTION,
                            name = "Stage 224 Existing Capability",
                            description =
                                "Existing bounded capability context.",
                        ),
                    adapterId = "pc.stage224",
                )

        val identity =
            IdentityId.from("identity:stage224:subject")

        val crossDeviceIdentity =
            AndroidCrossDeviceIdentityCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    identityId = identity,
                )

        val session =
            SessionRecord.create(
                sessionId = SessionId.from("session:stage224"),
                subjectIdentityId = identity,
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(100L),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(200L),
            )

        val sessionTrace =
            TraceId.from("trace-stage224-session")

        val sessionGovernance =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity =
                        SessionValidityResult.create(
                            traceId = sessionTrace,
                            status = SessionValidityStatus.VALID,
                            request =
                                SessionValidityRequest.create(
                                    context = context(sessionTrace),
                                    session = session,
                                    observedAt =
                                        DevilTimestamp
                                            .fromEpochMilliseconds(
                                                150L,
                                            ),
                                ),
                        ),
                )

        val taskContinuity =
            AndroidCrossDeviceTaskContinuityCoordinator()
                .integrate(
                    sessionGovernance = sessionGovernance,
                    task = task(),
                )

        val representation =
            LogicalMemoryRepresentation.create(
                memoryId = MemoryId.from("memory-stage224"),
                subjectIdentityId = identity,
                memoryClass = MemoryClass.WORKING,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(80),
                retention = MemoryRetention.SESSION,
                source =
                    MemorySource.create(
                        sourceId = "source-stage224",
                        sourceType = "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve Stage 224 upstream provenance.",
                    ),
                content =
                    "Existing bounded logical-memory context.",
            )

        val memoryContinuity =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = taskContinuity,
                    memoryContinuity =
                        MemoryContinuityResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage224-memory",
                                ),
                            status =
                                MemoryContinuityStatus.ESTABLISHED,
                            record =
                                MemoryContinuityRecord.create(
                                    representation = representation,
                                ),
                        ),
                )

        val trust =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = memoryContinuity,
                    disposition =
                        AndroidDeviceTrustRevocationStatus.TRUSTED,
                )

        return AndroidUnifiedMultiDeviceValidationCoordinator()
            .prepare(
                deviceProtocol = deviceProtocol,
                tabletEmbodiment = tablet,
                educationTabletExperience = educationTablet,
                pcEmbodiment = pc,
                pcCapabilityAdapter = pcAdapter,
                crossDeviceIdentity = crossDeviceIdentity,
                sessionGovernance = sessionGovernance,
                taskContinuity = taskContinuity,
                memoryContinuity = memoryContinuity,
                deviceTrustRevocation = trust,
                validationFocus =
                    "Stage 224 bounded surveillance integration",
                validationEvidenceDescription =
                    "Exact existing Phase N provenance.",
            )
    }

    private fun task(): TaskRecord {
        val decision =
            DecisionRecord.create(
                understanding =
                    UnderstandingRecord.create(
                        context =
                            ContextEnvelope.create(
                                traceId =
                                    TraceId.from(
                                        "trace-stage224-task",
                                    ),
                                schemaVersion =
                                    SchemaVersion.from(1),
                                source = ContextSource.TEXT,
                                trustLevel =
                                    ContextTrustLevel.VERIFIED,
                                securityLevel =
                                    ContextSecurityLevel.RESTRICTED,
                                observedAt =
                                    DevilTimestamp
                                        .fromEpochMilliseconds(
                                            150L,
                                        ),
                            ),
                        state = UnderstandingState.COMPLETE,
                        summary =
                            "Preserve bounded Stage 224 integration.",
                    ),
                state = DecisionState.SELECTED,
                summary =
                    "Preserve bounded Stage 224 integration.",
            )

        return TaskRecord.create(
            taskId = TaskId.from("task-stage224"),
            decision = decision,
            state = TaskState.CREATED,
            summary = decision.summary,
        )
    }

    private fun context(
        traceId: TraceId,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(150L),
        )
    }
}
