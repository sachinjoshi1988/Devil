package com.devil.app.security

import com.devil.app.device.AndroidCrossDeviceIdentityCoordinator
import com.devil.app.device.AndroidCrossDeviceIdentityResult
import com.devil.app.device.AndroidCrossDeviceMemoryContinuityCoordinator
import com.devil.app.device.AndroidCrossDeviceMemoryContinuityResult
import com.devil.app.device.AndroidCrossDeviceSessionGovernanceCoordinator
import com.devil.app.device.AndroidCrossDeviceSessionGovernanceResult
import com.devil.app.device.AndroidCrossDeviceTaskContinuityCoordinator
import com.devil.app.device.AndroidCrossDeviceTaskContinuityResult
import com.devil.app.device.AndroidDeviceProtocolIntegrationCoordinator
import com.devil.app.device.AndroidDeviceProtocolIntegrationResult
import com.devil.app.device.AndroidDeviceTrustRevocationCoordinator
import com.devil.app.device.AndroidDeviceTrustRevocationResult
import com.devil.app.device.AndroidDeviceTrustRevocationStatus
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationCoordinator
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationResult
import com.devil.app.device.pc.AndroidPcCapabilityAdapterCoordinator
import com.devil.app.device.pc.AndroidPcCapabilityAdapterResult
import com.devil.app.device.pc.AndroidPcEmbodimentCoordinator
import com.devil.app.device.pc.AndroidPcEmbodimentResult
import com.devil.app.device.tablet.AndroidEducationTabletExperienceCoordinator
import com.devil.app.device.tablet.AndroidEducationTabletExperienceResult
import com.devil.app.device.tablet.AndroidTabletEmbodimentCoordinator
import com.devil.app.device.tablet.AndroidTabletEmbodimentResult
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
import com.devil.core.runtime.surveillance.SecuritySurveillanceCoordinator
import com.devil.core.model.surveillance.SecuritySurveillanceRecord
import com.devil.core.runtime.surveillance.SecurityResponseCoordinator
import com.devil.core.runtime.surveillance.SecurityResponsePreparationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage228SecurityResponseGovernanceTest {

    @Test
    fun `available Stage 227 alert and matching prepared Stage 91 response become governed`() {
        val alerting =
            availableAlert()

        val responsePreparation =
            preparedResponse(
                alerting = alerting,
            )

        val result =
            AndroidSecurityResponseGovernanceCoordinator()
                .govern(
                    alerting = alerting,
                    responsePreparation = responsePreparation,
                )

        assertEquals(
            AndroidSecurityResponseGovernanceStatus.GOVERNED,
            result.status,
        )
        assertSame(
            alerting,
            result.alerting,
        )
        assertSame(
            responsePreparation,
            result.responsePreparation,
        )

        val surveillanceRecord =
            requireNotNull(
                alerting
                    .eventUnderstanding
                    .cameraAdapter
                    .surveillanceIntegration
                    .surveillancePreparation
                    .record,
            )

        assertSame(
            surveillanceRecord,
            requireNotNull(result.responsePreparation.record)
                .surveillance,
        )
    }

    @Test
    fun `deferred Stage 227 alert keeps Stage 228 deferred`() {
        val understood =
            understoodEvent()

        val deferredAlert =
            AndroidSecurityAlertingResult.create(
                status =
                    AndroidSecurityAlertingStatus.DEFERRED,
                eventUnderstanding = understood,
            )

        val responsePreparation =
            preparedResponse(
                alerting =
                    AndroidSecurityAlertingResult.create(
                        status =
                            AndroidSecurityAlertingStatus.AVAILABLE,
                        eventUnderstanding = understood,
                        alertDescription =
                            "Bounded security alert.",
                    ),
            )

        val result =
            AndroidSecurityResponseGovernanceCoordinator()
                .govern(
                    alerting = deferredAlert,
                    responsePreparation = responsePreparation,
                )

        assertEquals(
            AndroidSecurityResponseGovernanceStatus.DEFERRED,
            result.status,
        )
        assertSame(
            deferredAlert,
            result.alerting,
        )
        assertSame(
            responsePreparation,
            result.responsePreparation,
        )
    }

    @Test
    fun `deferred Stage 91 response keeps Stage 228 deferred`() {
        val alerting =
            availableAlert()

        val surveillanceRecord =
            requireNotNull(
                alerting
                    .eventUnderstanding
                    .cameraAdapter
                    .surveillanceIntegration
                    .surveillancePreparation
                    .record,
            )

        val responsePreparation =
            SecurityResponseCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage228-response-deferred",
                        ),
                    surveillance = surveillanceRecord,
                    action = "   ",
                    rationale =
                        "Bounded Stage 228 response rationale.",
                )

        assertEquals(
            SecurityResponsePreparationStatus.DEFERRED,
            responsePreparation.status,
        )

        val result =
            AndroidSecurityResponseGovernanceCoordinator()
                .govern(
                    alerting = alerting,
                    responsePreparation = responsePreparation,
                )

        assertEquals(
            AndroidSecurityResponseGovernanceStatus.DEFERRED,
            result.status,
        )
        assertSame(
            alerting,
            result.alerting,
        )
        assertSame(
            responsePreparation,
            result.responsePreparation,
        )
    }

    @Test
    fun `response prepared from reconstructed surveillance record remains deferred`() {
        val alerting =
            availableAlert()

        val original =
            requireNotNull(
                alerting
                    .eventUnderstanding
                    .cameraAdapter
                    .surveillanceIntegration
                    .surveillancePreparation
                    .record,
            )

        val reconstructedSignal =
            SecuritySurveillanceSignal.create(
                source = original.source,
                occurredAtEpochMilliseconds =
                    original.signal.occurredAtEpochMilliseconds,
                description =
                    original.signal.description,
            )

        val reconstructedRecord =
            SecuritySurveillanceRecord.create(
                source = original.source,
                signal = reconstructedSignal,
                watchlistMatchClaim =
                    original.watchlistMatchClaim,
            )

        val responsePreparation =
            SecurityResponseCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage228-response-reconstructed",
                        ),
                    surveillance = reconstructedRecord,
                    action =
                        "Request bounded owner review",
                    rationale =
                        "Preserve response preparation without fabricating provenance.",
                )

        assertEquals(
            SecurityResponsePreparationStatus.PREPARED,
            responsePreparation.status,
        )

        val result =
            AndroidSecurityResponseGovernanceCoordinator()
                .govern(
                    alerting = alerting,
                    responsePreparation = responsePreparation,
                )

        assertEquals(
            AndroidSecurityResponseGovernanceStatus.DEFERRED,
            result.status,
        )
        assertSame(
            reconstructedRecord,
            requireNotNull(responsePreparation.record)
                .surveillance,
        )
    }

    @Test
    fun `governed result requires available Stage 227 alert`() {
        val understood =
            understoodEvent()

        val availableAlert =
            AndroidSecurityAlertingResult.create(
                status =
                    AndroidSecurityAlertingStatus.AVAILABLE,
                eventUnderstanding = understood,
                alertDescription =
                    "Bounded security alert.",
            )

        val responsePreparation =
            preparedResponse(
                alerting = availableAlert,
            )

        val deferredAlert =
            AndroidSecurityAlertingResult.create(
                status =
                    AndroidSecurityAlertingStatus.DEFERRED,
                eventUnderstanding = understood,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityResponseGovernanceResult.create(
                status =
                    AndroidSecurityResponseGovernanceStatus.GOVERNED,
                alerting = deferredAlert,
                responsePreparation = responsePreparation,
            )
        }
    }

    @Test
    fun `governed result rejects reconstructed Stage 90 surveillance provenance`() {
        val alerting =
            availableAlert()

        val original =
            requireNotNull(
                alerting
                    .eventUnderstanding
                    .cameraAdapter
                    .surveillanceIntegration
                    .surveillancePreparation
                    .record,
            )

        val reconstructedRecord =
            SecuritySurveillanceRecord.create(
                source = original.source,
                signal =
                    SecuritySurveillanceSignal.create(
                        source = original.source,
                        occurredAtEpochMilliseconds =
                            original.signal.occurredAtEpochMilliseconds,
                        description =
                            original.signal.description,
                    ),
                watchlistMatchClaim =
                    original.watchlistMatchClaim,
            )

        val responsePreparation =
            SecurityResponseCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage228-result-reconstructed",
                        ),
                    surveillance = reconstructedRecord,
                    action =
                        "Request bounded owner review",
                    rationale =
                        "Bounded response rationale.",
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidSecurityResponseGovernanceResult.create(
                status =
                    AndroidSecurityResponseGovernanceStatus.GOVERNED,
                alerting = alerting,
                responsePreparation = responsePreparation,
            )
        }
    }

    @Test
    fun `deferred governance preserves exact upstream objects`() {
        val alerting =
            availableAlert()

        val surveillanceRecord =
            requireNotNull(
                alerting
                    .eventUnderstanding
                    .cameraAdapter
                    .surveillanceIntegration
                    .surveillancePreparation
                    .record,
            )

        val deferredResponse =
            SecurityResponseCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage228-deferred-preservation",
                        ),
                    surveillance = surveillanceRecord,
                    action =
                        "Request bounded owner review",
                    rationale = "   ",
                )

        val result =
            AndroidSecurityResponseGovernanceResult.create(
                status =
                    AndroidSecurityResponseGovernanceStatus.DEFERRED,
                alerting = alerting,
                responsePreparation = deferredResponse,
            )

        assertSame(
            alerting,
            result.alerting,
        )
        assertSame(
            deferredResponse,
            result.responsePreparation,
        )
    }

    private fun availableAlert():
        AndroidSecurityAlertingResult {
        return AndroidSecurityAlertingCoordinator()
            .prepare(
                eventUnderstanding =
                    understoodEvent(),
                alertDescription =
                    "Bounded Stage 228 security alert.",
            )
    }

    private fun preparedResponse(
        alerting: AndroidSecurityAlertingResult,
    ) = SecurityResponseCoordinator()
        .prepare(
            traceId =
                TraceId.from(
                    "trace-stage228-security-response",
                ),
            surveillance =
                requireNotNull(
                    alerting
                        .eventUnderstanding
                        .cameraAdapter
                        .surveillanceIntegration
                        .surveillancePreparation
                        .record,
                ),
            action =
                "Request bounded owner security review",
            rationale =
                "Govern the exact prepared response beneath the existing security alert.",
        )

    private fun understoodEvent():
        AndroidSecurityEventUnderstandingResult {
        return AndroidSecurityEventUnderstandingCoordinator()
            .integrate(
                cameraAdapter = availableCameraAdapter(),
                understandingDescription =
                    "Observed bounded motion candidate.",
            )
    }

    private fun deferredEvent():
        AndroidSecurityEventUnderstandingResult {
        val available =
            availableCameraAdapter()

        val deferredAdapter =
            AndroidSecurityCameraAdapterResult.create(
                status =
                    AndroidSecurityCameraAdapterStatus.DEFERRED,
                surveillanceIntegration =
                    available.surveillanceIntegration,
            )

        return AndroidSecurityEventUnderstandingCoordinator()
            .integrate(
                cameraAdapter = deferredAdapter,
                understandingDescription =
                    "Observed bounded motion candidate.",
            )
    }

    private fun availableCameraAdapter():
        AndroidSecurityCameraAdapterResult {
        val integration =
            availableSurveillanceIntegration()

        return AndroidSecurityCameraAdapterCoordinator()
            .integrate(
                surveillanceIntegration = integration,
                adapterId = "security.camera.stage226",
            )
    }

    private fun availableSurveillanceIntegration():
        AndroidSecuritySurveillanceIntegrationResult {
        val surveillance =
            SecuritySurveillanceCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage226-surveillance",
                        ),
                    sourceId = "camera:stage226-entry",
                    sourceType = "network-camera",
                    occurredAtEpochMilliseconds = 226L,
                    description =
                        "Explicit bounded Stage 226 surveillance signal.",
                )

        return AndroidSecuritySurveillanceIntegrationCoordinator()
            .integrate(
                multiDeviceValidation =
                    validatedMultiDeviceContext(),
                surveillancePreparation = surveillance,
            )
    }

    private fun validatedMultiDeviceContext():
        AndroidUnifiedMultiDeviceValidationResult {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage226-relationship",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage226:source",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage226:target",
                            ),
                        description =
                            "Bounded Stage 226 cross-device relationship.",
                    ),
            )

        val deviceProtocol =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = "devil.stage226",
                )

        val tabletEmbodiment =
            tabletEmbodiment()

        val educationTablet =
            AndroidEducationTabletExperienceCoordinator()
                .integrate(
                    tabletEmbodiment = tabletEmbodiment,
                    educationSession = educationSession(),
                )

        val pcEmbodiment =
            pcEmbodiment()

        val pcAdapter =
            AndroidPcCapabilityAdapterCoordinator()
                .integrate(
                    pcEmbodiment = pcEmbodiment,
                    capability = capability(),
                    adapterId = "pc.stage226",
                )

        val identity =
            IdentityId.from(
                SUBJECT_ID,
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
                        "session:stage226",
                    ),
                subjectIdentityId = identity,
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        100L,
                    ),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        300L,
                    ),
            )

        val sessionTrace =
            TraceId.from(
                "trace-stage226-session",
            )

        val sessionValidity =
            SessionValidityResult.create(
                traceId = sessionTrace,
                status = SessionValidityStatus.VALID,
                request =
                    SessionValidityRequest.create(
                        context = context(sessionTrace),
                        session = session,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                200L,
                            ),
                    ),
            )

        val sessionGovernance =
            AndroidCrossDeviceSessionGovernanceCoordinator()
                .govern(
                    crossDeviceIdentity = crossDeviceIdentity,
                    session = session,
                    sessionValidity = sessionValidity,
                )

        val taskContinuity =
            AndroidCrossDeviceTaskContinuityCoordinator()
                .integrate(
                    sessionGovernance = sessionGovernance,
                    task = task(),
                )

        val memoryContinuity =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = taskContinuity,
                    memoryContinuity =
                        establishedMemoryContinuity(identity),
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
                tabletEmbodiment = tabletEmbodiment,
                educationTabletExperience = educationTablet,
                pcEmbodiment = pcEmbodiment,
                pcCapabilityAdapter = pcAdapter,
                crossDeviceIdentity = crossDeviceIdentity,
                sessionGovernance = sessionGovernance,
                taskContinuity = taskContinuity,
                memoryContinuity = memoryContinuity,
                deviceTrustRevocation = trust,
                validationFocus =
                    "Stage 226 bounded multi-device context",
                validationEvidenceDescription =
                    "Existing Phase N structural provenance.",
            )
    }

    private fun tabletEmbodiment():
        AndroidTabletEmbodimentResult {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage226:tablet",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "android",
                    ),
                description =
                    "Stage 226 bounded Android tablet embodiment.",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage226-tablet",
                    ),
                status =
                    AndroidTabletFormFactorAssessmentStatus.TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 720,
                    ),
            )

        return AndroidTabletEmbodimentCoordinator()
            .integrate(assessment)
    }

    private fun educationSession():
        EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage226",
                ),
            subjectIdentityId =
                IdentityId.from(
                    SUBJECT_ID,
                ),
            objective =
                EducationObjective.create(
                    subject = "Security",
                    objective =
                        "Preserve bounded Stage 226 validation provenance.",
                ),
        )
    }

    private fun pcEmbodiment():
        AndroidPcEmbodimentResult {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage226:pc",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "pc",
                    ),
                description =
                    "Stage 226 bounded PC embodiment.",
            )

        return AndroidPcEmbodimentCoordinator()
            .integrate(
                PcEmbodimentAssessmentResult.create(
                    traceId =
                        TraceId.from(
                            "trace-stage226-pc",
                        ),
                    status =
                        PcEmbodimentAssessmentStatus.PC,
                    embodiment = embodiment,
                    evidence =
                        PcEmbodimentEvidence.create(
                            operatingSystemFamily = "Linux",
                        ),
                ),
            )
    }

    private fun capability():
        CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability:stage226",
                ),
            category =
                CapabilityCategory.ACTION,
            name =
                "Stage 226 bounded capability",
            description =
                "Existing bounded capability for Stage 226 provenance.",
        )
    }

    private fun establishedMemoryContinuity(
        identity: IdentityId,
    ): MemoryContinuityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage226",
                    ),
                subjectIdentityId = identity,
                memoryClass = MemoryClass.WORKING,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(80),
                retention = MemoryRetention.SESSION,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage226",
                        sourceType =
                            "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve Stage 226 upstream provenance.",
                    ),
                content =
                    "Existing bounded Stage 226 memory context.",
            )

        return MemoryContinuityResult.create(
            traceId =
                TraceId.from(
                    "trace-stage226-memory",
                ),
            status =
                MemoryContinuityStatus.ESTABLISHED,
            record =
                MemoryContinuityRecord.create(
                    representation = representation,
                ),
        )
    }

    private fun task():
        TaskRecord {
        val decision =
            decision()

        return TaskRecord.create(
            taskId =
                TaskId.from(
                    "task-stage226",
                ),
            decision = decision,
            state = TaskState.CREATED,
            summary = decision.summary,
        )
    }

    private fun decision():
        DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId =
                            TraceId.from(
                                "trace-stage226-task",
                            ),
                        schemaVersion =
                            SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                200L,
                            ),
                    ),
                state =
                    UnderstandingState.COMPLETE,
                summary =
                    "Preserve bounded Stage 226 security event context.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Preserve bounded Stage 226 security event context.",
        )
    }

    private fun context(
        traceId: TraceId,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = traceId,
            schemaVersion =
                SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel =
                ContextTrustLevel.UNVERIFIED,
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    200L,
                ),
        )
    }

    private companion object {
        const val SUBJECT_ID: String =
            "identity:stage226:subject"
    }
}
