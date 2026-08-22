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
import com.devil.core.model.surveillance.SecuritySurveillanceRecord
import com.devil.core.model.surveillance.SecuritySurveillanceSignal
import com.devil.core.model.surveillance.SecuritySurveillanceSource
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
import com.devil.core.runtime.surveillance.SecuritySurveillancePreparationResult
import com.devil.core.runtime.surveillance.SecuritySurveillancePreparationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage225SecurityCameraAdapterTest {

    @Test
    fun `available Stage 224 context and adapter id produce available adapter with exact source provenance`() {
        val integration = availableSurveillanceIntegration()
        val source =
            requireNotNull(
                integration.surveillancePreparation.record,
            ).source

        val result =
            AndroidSecurityCameraAdapterCoordinator()
                .integrate(
                    surveillanceIntegration = integration,
                    adapterId = "  security.camera.stage225  ",
                )

        assertEquals(
            AndroidSecurityCameraAdapterStatus.AVAILABLE,
            result.status,
        )
        assertSame(integration, result.surveillanceIntegration)
        assertSame(source, result.source)
        assertEquals(
            "security.camera.stage225",
            result.adapterId,
        )
    }

    @Test
    fun `blank adapter identifier keeps Stage 225 deferred`() {
        val integration = availableSurveillanceIntegration()

        val result =
            AndroidSecurityCameraAdapterCoordinator()
                .integrate(
                    surveillanceIntegration = integration,
                    adapterId = "   ",
                )

        assertEquals(
            AndroidSecurityCameraAdapterStatus.DEFERRED,
            result.status,
        )
        assertSame(integration, result.surveillanceIntegration)
        assertNull(result.source)
        assertNull(result.adapterId)
    }

    @Test
    fun `deferred Stage 224 integration keeps Stage 225 deferred`() {
        val integration =
            AndroidSecuritySurveillanceIntegrationResult.create(
                status =
                    AndroidSecuritySurveillanceIntegrationStatus.DEFERRED,
                multiDeviceValidation =
                    AndroidUnifiedMultiDeviceValidationResult.create(
                        status =
                            AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
                    ),
                surveillancePreparation =
                    SecuritySurveillancePreparationResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage225-deferred",
                            ),
                        status =
                            SecuritySurveillancePreparationStatus.DEFERRED,
                    ),
            )

        val result =
            AndroidSecurityCameraAdapterCoordinator()
                .integrate(
                    surveillanceIntegration = integration,
                    adapterId = "security.camera.stage225",
                )

        assertEquals(
            AndroidSecurityCameraAdapterStatus.DEFERRED,
            result.status,
        )
        assertSame(integration, result.surveillanceIntegration)
        assertNull(result.source)
        assertNull(result.adapterId)
    }

    @Test
    fun `available result requires available Stage 224 integration`() {
        val surveillance = preparedSurveillance()

        val deferredIntegration =
            AndroidSecuritySurveillanceIntegrationResult.create(
                status =
                    AndroidSecuritySurveillanceIntegrationStatus.DEFERRED,
                multiDeviceValidation =
                    AndroidUnifiedMultiDeviceValidationResult.create(
                        status =
                            AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
                    ),
                surveillancePreparation = surveillance,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityCameraAdapterResult.create(
                status = AndroidSecurityCameraAdapterStatus.AVAILABLE,
                surveillanceIntegration = deferredIntegration,
                source =
                    requireNotNull(
                        surveillance.record,
                    ).source,
                adapterId = "security.camera.stage225",
            )
        }
    }

    @Test
    fun `available result rejects reconstructed surveillance source provenance`() {
        val integration = availableSurveillanceIntegration()

        val originalSource =
            requireNotNull(
                integration.surveillancePreparation.record,
            ).source

        val reconstructed =
            SecuritySurveillanceSource.create(
                sourceId = originalSource.sourceId,
                sourceType = originalSource.sourceType,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityCameraAdapterResult.create(
                status = AndroidSecurityCameraAdapterStatus.AVAILABLE,
                surveillanceIntegration = integration,
                source = reconstructed,
                adapterId = "security.camera.stage225",
            )
        }
    }

    @Test
    fun `available result rejects blank adapter identifier`() {
        val integration = availableSurveillanceIntegration()

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityCameraAdapterResult.create(
                status = AndroidSecurityCameraAdapterStatus.AVAILABLE,
                surveillanceIntegration = integration,
                source =
                    requireNotNull(
                        integration.surveillancePreparation.record,
                    ).source,
                adapterId = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle source or adapter identifier`() {
        val integration = availableSurveillanceIntegration()
        val source =
            requireNotNull(
                integration.surveillancePreparation.record,
            ).source

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityCameraAdapterResult.create(
                status = AndroidSecurityCameraAdapterStatus.DEFERRED,
                surveillanceIntegration = integration,
                source = source,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityCameraAdapterResult.create(
                status = AndroidSecurityCameraAdapterStatus.DEFERRED,
                surveillanceIntegration = integration,
                adapterId = "security.camera.stage225",
            )
        }
    }

    @Test
    fun `source type remains descriptive and does not need camera classification`() {
        val integration =
            availableSurveillanceIntegration(
                sourceType = "future-surveillance-source",
            )

        val result =
            AndroidSecurityCameraAdapterCoordinator()
                .integrate(
                    surveillanceIntegration = integration,
                    adapterId = "future.adapter.stage225",
                )

        assertEquals(
            AndroidSecurityCameraAdapterStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "future-surveillance-source",
            result.source?.sourceType,
        )
        assertEquals(
            "future.adapter.stage225",
            result.adapterId,
        )
    }

    private fun availableSurveillanceIntegration(
        sourceType: String = "network-camera",
    ): AndroidSecuritySurveillanceIntegrationResult {
        return AndroidSecuritySurveillanceIntegrationResult.create(
            status =
                AndroidSecuritySurveillanceIntegrationStatus.AVAILABLE,
            multiDeviceValidation = validatedMultiDevice(),
            surveillancePreparation =
                preparedSurveillance(
                    sourceType = sourceType,
                ),
        )
    }

    private fun preparedSurveillance(
        sourceType: String = "network-camera",
    ): SecuritySurveillancePreparationResult {
        val source =
            SecuritySurveillanceSource.create(
                sourceId = "camera:stage225",
                sourceType = sourceType,
            )

        val signal =
            SecuritySurveillanceSignal.create(
                source = source,
                occurredAtEpochMilliseconds = 225L,
                description =
                    "Existing bounded Stage 225 surveillance signal.",
            )

        return SecuritySurveillancePreparationResult.create(
            traceId =
                TraceId.from(
                    "trace-stage225-surveillance",
                ),
            status =
                SecuritySurveillancePreparationStatus.PREPARED,
            record =
                SecuritySurveillanceRecord.create(
                    source = source,
                    signal = signal,
                ),
        )
    }

    private fun validatedMultiDevice():
        AndroidUnifiedMultiDeviceValidationResult {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage225-relationship",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage225:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage225:target",
                            ),
                        description =
                            "Bounded Stage 225 relationship.",
                    ),
            )

        val deviceProtocol =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = "devil.stage225",
                )

        val tabletRecord =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage225:tablet",
                    ),
                platformId =
                    EmbodimentPlatformId.from("android"),
                description =
                    "Stage 225 tablet embodiment.",
            )

        val tablet =
            AndroidTabletEmbodimentCoordinator()
                .integrate(
                    AndroidTabletFormFactorAssessmentResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage225-tablet",
                            ),
                        status =
                            AndroidTabletFormFactorAssessmentStatus.TABLET,
                        embodiment = tabletRecord,
                        evidence =
                            AndroidTabletFormFactorEvidence.create(
                                smallestScreenWidthDp = 720,
                            ),
                    ),
                )

        val educationTablet =
            AndroidEducationTabletExperienceCoordinator()
                .integrate(
                    tabletEmbodiment = tablet,
                    educationSession =
                        EducationSessionRecord.create(
                            sessionId =
                                EducationSessionId.from(
                                    "education-session:stage225",
                                ),
                            subjectIdentityId =
                                IdentityId.from(
                                    "identity:stage225:learner",
                                ),
                            objective =
                                EducationObjective.create(
                                    subject =
                                        "Security camera adapter",
                                    objective =
                                        "Preserve bounded tablet context.",
                                ),
                        ),
                )

        val pcRecord =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage225:pc",
                    ),
                platformId = EmbodimentPlatformId.from("pc"),
                description =
                    "Stage 225 PC embodiment.",
            )

        val pc =
            AndroidPcEmbodimentCoordinator()
                .integrate(
                    PcEmbodimentAssessmentResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage225-pc",
                            ),
                        status =
                            PcEmbodimentAssessmentStatus.PC,
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
                                    "capability:stage225",
                                ),
                            category = CapabilityCategory.ACTION,
                            name =
                                "Stage 225 Existing Capability",
                            description =
                                "Existing bounded capability context.",
                        ),
                    adapterId = "pc.stage225",
                )

        val identity =
            IdentityId.from(
                "identity:stage225:subject",
            )

        val crossDeviceIdentity =
            AndroidCrossDeviceIdentityCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    identityId = identity,
                )

        val session =
            SessionRecord.create(
                sessionId =
                    SessionId.from(
                        "session:stage225",
                    ),
                subjectIdentityId = identity,
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp
                        .fromEpochMilliseconds(100L),
                expiresAt =
                    DevilTimestamp
                        .fromEpochMilliseconds(200L),
            )

        val sessionTrace =
            TraceId.from(
                "trace-stage225-session",
            )

        val sessionGovernance =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity =
                        SessionValidityResult.create(
                            traceId = sessionTrace,
                            status =
                                SessionValidityStatus.VALID,
                            request =
                                SessionValidityRequest.create(
                                    context =
                                        context(sessionTrace),
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
                memoryId =
                    MemoryId.from(
                        "memory-stage225",
                    ),
                subjectIdentityId = identity,
                memoryClass = MemoryClass.WORKING,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(80),
                retention =
                    MemoryRetention.SESSION,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage225",
                        sourceType =
                            "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve Stage 225 upstream provenance.",
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
                                    "trace-stage225-memory",
                                ),
                            status =
                                MemoryContinuityStatus.ESTABLISHED,
                            record =
                                MemoryContinuityRecord.create(
                                    representation =
                                        representation,
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
                educationTabletExperience =
                    educationTablet,
                pcEmbodiment = pc,
                pcCapabilityAdapter = pcAdapter,
                crossDeviceIdentity = crossDeviceIdentity,
                sessionGovernance = sessionGovernance,
                taskContinuity = taskContinuity,
                memoryContinuity = memoryContinuity,
                deviceTrustRevocation = trust,
                validationFocus =
                    "Stage 225 bounded camera adapter",
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
                                        "trace-stage225-task",
                                    ),
                                schemaVersion =
                                    SchemaVersion.from(1),
                                source =
                                    ContextSource.TEXT,
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
                        state =
                            UnderstandingState.COMPLETE,
                        summary =
                            "Preserve bounded Stage 225 camera adapter context.",
                    ),
                state = DecisionState.SELECTED,
                summary =
                    "Preserve bounded Stage 225 camera adapter context.",
            )

        return TaskRecord.create(
            taskId =
                TaskId.from(
                    "task-stage225",
                ),
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
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp
                    .fromEpochMilliseconds(150L),
        )
    }
}
