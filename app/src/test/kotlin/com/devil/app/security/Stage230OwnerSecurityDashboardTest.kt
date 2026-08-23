package com.devil.app.security

import com.devil.app.device.AndroidCrossDeviceIdentityCoordinator
import com.devil.app.device.AndroidCrossDeviceMemoryContinuityCoordinator
import com.devil.app.device.AndroidCrossDeviceSessionGovernanceCoordinator
import com.devil.app.device.AndroidCrossDeviceTaskContinuityCoordinator
import com.devil.app.device.AndroidDeviceProtocolIntegrationCoordinator
import com.devil.app.device.AndroidDeviceTrustRevocationCoordinator
import com.devil.app.device.AndroidDeviceTrustRevocationStatus
import com.devil.app.device.AndroidUnifiedMultiDeviceValidationCoordinator
import com.devil.app.device.pc.AndroidPcCapabilityAdapterCoordinator
import com.devil.app.device.pc.AndroidPcEmbodimentCoordinator
import com.devil.app.device.pc.AndroidPcEmbodimentResult
import com.devil.app.device.tablet.AndroidEducationTabletExperienceCoordinator
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
import com.devil.core.runtime.surveillance.SecurityResponseCoordinator
import com.devil.core.runtime.surveillance.SecurityResponsePreparationResult
import com.devil.core.runtime.surveillance.SecuritySurveillanceCoordinator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import com.devil.core.model.owner.OwnerContext
import com.devil.core.runtime.owner.OwnerMultiUserContextRecord
import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus


class Stage230OwnerSecurityDashboardTest {

    @Test
    fun `ready Stage 229 escalation and established owner context produce available dashboard`() {
        val escalation =
            readyEscalation()

        val ownerContext =
            establishedOwnerContext()

        val result =
            AndroidOwnerSecurityDashboardCoordinator()
                .prepare(
                    emergencyEscalation = escalation,
                    ownerContext = ownerContext,
                    dashboardSummary =
                        "  Bounded owner security dashboard summary.  ",
                )

        assertEquals(
            AndroidOwnerSecurityDashboardStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            escalation,
            result.emergencyEscalation,
        )
        assertSame(
            ownerContext,
            result.ownerContext,
        )
        assertSame(
            ownerContext.record,
            result.ownerContext.record,
        )
        assertEquals(
            "Bounded owner security dashboard summary.",
            result.dashboardSummary,
        )
    }

    @Test
    fun `blank dashboard summary keeps Stage 230 deferred`() {
        val escalation =
            readyEscalation()

        val ownerContext =
            establishedOwnerContext()

        val result =
            AndroidOwnerSecurityDashboardCoordinator()
                .prepare(
                    emergencyEscalation = escalation,
                    ownerContext = ownerContext,
                    dashboardSummary = "   ",
                )

        assertEquals(
            AndroidOwnerSecurityDashboardStatus.DEFERRED,
            result.status,
        )
        assertSame(
            escalation,
            result.emergencyEscalation,
        )
        assertSame(
            ownerContext,
            result.ownerContext,
        )
        assertEquals(
            null,
            result.dashboardSummary,
        )
    }

    @Test
    fun `deferred Stage 229 escalation keeps Stage 230 deferred`() {
        val governed =
            governedResponse()

        val deferredEscalation =
            AndroidEmergencyEscalationResult.create(
                status =
                    AndroidEmergencyEscalationStatus.DEFERRED,
                responseGovernance = governed,
            )

        val ownerContext =
            establishedOwnerContext()

        val result =
            AndroidOwnerSecurityDashboardCoordinator()
                .prepare(
                    emergencyEscalation = deferredEscalation,
                    ownerContext = ownerContext,
                    dashboardSummary =
                        "Bounded dashboard summary.",
                )

        assertEquals(
            AndroidOwnerSecurityDashboardStatus.DEFERRED,
            result.status,
        )
        assertSame(
            deferredEscalation,
            result.emergencyEscalation,
        )
        assertSame(
            ownerContext,
            result.ownerContext,
        )
        assertEquals(
            null,
            result.dashboardSummary,
        )
    }

    @Test
    fun `deferred owner context keeps Stage 230 deferred`() {
        val escalation =
            readyEscalation()

        val deferredOwnerContext =
            OwnerMultiUserContextResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage230-owner-deferred",
                    ),
                status =
                    OwnerMultiUserContextStatus.DEFERRED,
            )

        val result =
            AndroidOwnerSecurityDashboardCoordinator()
                .prepare(
                    emergencyEscalation = escalation,
                    ownerContext = deferredOwnerContext,
                    dashboardSummary =
                        "Bounded dashboard summary.",
                )

        assertEquals(
            AndroidOwnerSecurityDashboardStatus.DEFERRED,
            result.status,
        )
        assertSame(
            deferredOwnerContext,
            result.ownerContext,
        )
        assertEquals(
            null,
            result.dashboardSummary,
        )
    }

    @Test
    fun `available result requires ready Stage 229 escalation`() {
        val governed =
            governedResponse()

        val deferredEscalation =
            AndroidEmergencyEscalationResult.create(
                status =
                    AndroidEmergencyEscalationStatus.DEFERRED,
                responseGovernance = governed,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidOwnerSecurityDashboardResult.create(
                status =
                    AndroidOwnerSecurityDashboardStatus.AVAILABLE,
                emergencyEscalation = deferredEscalation,
                ownerContext = establishedOwnerContext(),
                dashboardSummary =
                    "Bounded dashboard summary.",
            )
        }
    }

    @Test
    fun `available result requires established owner context`() {
        val deferredOwnerContext =
            OwnerMultiUserContextResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage230-owner-unavailable",
                    ),
                status =
                    OwnerMultiUserContextStatus.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidOwnerSecurityDashboardResult.create(
                status =
                    AndroidOwnerSecurityDashboardStatus.AVAILABLE,
                emergencyEscalation = readyEscalation(),
                ownerContext = deferredOwnerContext,
                dashboardSummary =
                    "Bounded dashboard summary.",
            )
        }
    }

    @Test
    fun `available result preserves exact Stage 229 and Stage 100 provenance`() {
        val escalation =
            readyEscalation()

        val ownerContext =
            establishedOwnerContext()

        val result =
            AndroidOwnerSecurityDashboardResult.create(
                status =
                    AndroidOwnerSecurityDashboardStatus.AVAILABLE,
                emergencyEscalation = escalation,
                ownerContext = ownerContext,
                dashboardSummary =
                    "Bounded dashboard summary.",
            )

        assertSame(
            escalation,
            result.emergencyEscalation,
        )
        assertSame(
            ownerContext,
            result.ownerContext,
        )
        assertSame(
            ownerContext.record,
            result.ownerContext.record,
        )
    }

    @Test
    fun `available result rejects blank dashboard summary`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidOwnerSecurityDashboardResult.create(
                status =
                    AndroidOwnerSecurityDashboardStatus.AVAILABLE,
                emergencyEscalation = readyEscalation(),
                ownerContext = establishedOwnerContext(),
                dashboardSummary = "   ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle dashboard summary metadata`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidOwnerSecurityDashboardResult.create(
                status =
                    AndroidOwnerSecurityDashboardStatus.DEFERRED,
                emergencyEscalation = readyEscalation(),
                ownerContext = establishedOwnerContext(),
                dashboardSummary =
                    "Must not be present.",
            )
        }
    }

    private fun readyEscalation():
        AndroidEmergencyEscalationResult {
        return AndroidEmergencyEscalationCoordinator()
            .prepare(
                responseGovernance = governedResponse(),
                escalationDescription =
                    "Bounded Stage 230 escalation context.",
            )
    }

    private fun establishedOwnerContext():
        OwnerMultiUserContextResult {
        val identity =
            IdentityId.from(
                "identity:stage230:owner",
            )

        val ownerContext =
            OwnerContext.create(
                ownerIdentityId = identity,
                subjectIdentityId = identity,
            )

        val record =
            OwnerMultiUserContextRecord.create(
                ownerContext = ownerContext,
                currentSubjectIdentityId = identity,
            )

        return OwnerMultiUserContextResult.create(
            traceId =
                TraceId.from(
                    "trace-stage230-owner-context",
                ),
            status =
                OwnerMultiUserContextStatus.ESTABLISHED,
            record = record,
        )
    }

    private fun governedResponse(): AndroidSecurityResponseGovernanceResult {
        val alerting = availableAlert()

        return AndroidSecurityResponseGovernanceCoordinator()
            .govern(
                alerting = alerting,
                responsePreparation = preparedResponse(alerting),
            )
    }

    private fun availableAlert(): AndroidSecurityAlertingResult {
        val event =
            AndroidSecurityEventUnderstandingCoordinator()
                .integrate(
                    cameraAdapter = availableCameraAdapter(),
                    understandingDescription =
                        "Observed bounded Stage 229 security event candidate.",
                )

        return AndroidSecurityAlertingCoordinator()
            .prepare(
                eventUnderstanding = event,
                alertDescription =
                    "Bounded Stage 229 security alert.",
            )
    }

    private fun preparedResponse(
        alerting: AndroidSecurityAlertingResult,
    ): SecurityResponsePreparationResult {
        val surveillance =
            requireNotNull(
                alerting
                    .eventUnderstanding
                    .cameraAdapter
                    .surveillanceIntegration
                    .surveillancePreparation
                    .record,
            )

        return SecurityResponseCoordinator()
            .prepare(
                traceId = TraceId.from("trace-stage229-response"),
                surveillance = surveillance,
                action = "Request emergency escalation",
                rationale =
                    "Preserve bounded Stage 229 escalation preparation.",
            )
    }

    private fun availableCameraAdapter(): AndroidSecurityCameraAdapterResult {
        return AndroidSecurityCameraAdapterCoordinator()
            .integrate(
                surveillanceIntegration =
                    availableSurveillanceIntegration(),
                adapterId = "security.camera.stage229",
            )
    }

    private fun availableSurveillanceIntegration():
        AndroidSecuritySurveillanceIntegrationResult {
        val surveillance =
            SecuritySurveillanceCoordinator()
                .prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage229-surveillance",
                        ),
                    sourceId = "camera:stage229-entry",
                    sourceType = "network-camera",
                    occurredAtEpochMilliseconds = 229L,
                    description =
                        "Explicit bounded Stage 229 surveillance signal.",
                )

        return AndroidSecuritySurveillanceIntegrationCoordinator()
            .integrate(
                multiDeviceValidation =
                    validatedMultiDeviceContext(),
                surveillancePreparation = surveillance,
            )
    }

    private fun validatedMultiDeviceContext() =
        run {
            val relationship =
                CrossDeviceRelationshipRepresentationResult.create(
                    traceId =
                        TraceId.from(
                            "trace-stage229-relationship",
                        ),
                    status =
                        CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                    relationship =
                        CrossDeviceRelationshipRecord.create(
                            sourceEmbodimentId =
                                EmbodimentId.from(
                                    "embodiment:stage229:source",
                                ),
                            targetEmbodimentId =
                                EmbodimentId.from(
                                    "embodiment:stage229:target",
                                ),
                            description =
                                "Bounded Stage 229 cross-device relationship.",
                        ),
                )

            val deviceProtocol =
                AndroidDeviceProtocolIntegrationCoordinator()
                    .integrate(
                        relationshipRepresentation = relationship,
                        protocolId = "devil.stage229",
                    )

            val tablet = tabletEmbodiment()

            val educationTablet =
                AndroidEducationTabletExperienceCoordinator()
                    .integrate(
                        tabletEmbodiment = tablet,
                        educationSession = educationSession(),
                    )

            val pc = pcEmbodiment()

            val pcAdapter =
                AndroidPcCapabilityAdapterCoordinator()
                    .integrate(
                        pcEmbodiment = pc,
                        capability = capability(),
                        adapterId = "pc.stage229",
                    )

            val identity =
                IdentityId.from(SUBJECT_ID)

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
                            "session:stage229",
                        ),
                    subjectIdentityId = identity,
                    state = SessionState.ACTIVE,
                    establishedAt =
                        DevilTimestamp.fromEpochMilliseconds(100L),
                    expiresAt =
                        DevilTimestamp.fromEpochMilliseconds(300L),
                )

            val sessionTrace =
                TraceId.from("trace-stage229-session")

            val sessionValidity =
                SessionValidityResult.create(
                    traceId = sessionTrace,
                    status = SessionValidityStatus.VALID,
                    request =
                        SessionValidityRequest.create(
                            context = context(sessionTrace),
                            session = session,
                            observedAt =
                                DevilTimestamp.fromEpochMilliseconds(200L),
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

            AndroidUnifiedMultiDeviceValidationCoordinator()
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
                        "Stage 229 bounded multi-device context",
                    validationEvidenceDescription =
                        "Existing Phase N structural provenance.",
                )
        }

    private fun tabletEmbodiment(): AndroidTabletEmbodimentResult {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage229:tablet",
                    ),
                platformId =
                    EmbodimentPlatformId.from("android"),
                description =
                    "Stage 229 bounded Android tablet embodiment.",
            )

        return AndroidTabletEmbodimentCoordinator()
            .integrate(
                AndroidTabletFormFactorAssessmentResult.create(
                    traceId =
                        TraceId.from(
                            "trace-stage229-tablet",
                        ),
                    status =
                        AndroidTabletFormFactorAssessmentStatus.TABLET,
                    embodiment = embodiment,
                    evidence =
                        AndroidTabletFormFactorEvidence.create(
                            smallestScreenWidthDp = 720,
                        ),
                ),
            )
    }

    private fun educationSession(): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage229",
                ),
            subjectIdentityId = IdentityId.from(SUBJECT_ID),
            objective =
                EducationObjective.create(
                    subject = "Security",
                    objective =
                        "Preserve bounded Stage 229 validation provenance.",
                ),
        )
    }

    private fun pcEmbodiment(): AndroidPcEmbodimentResult {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage229:pc",
                    ),
                platformId =
                    EmbodimentPlatformId.from("pc"),
                description =
                    "Stage 229 bounded PC embodiment.",
            )

        return AndroidPcEmbodimentCoordinator()
            .integrate(
                PcEmbodimentAssessmentResult.create(
                    traceId =
                        TraceId.from(
                            "trace-stage229-pc",
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

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability:stage229",
                ),
            category = CapabilityCategory.ACTION,
            name = "Stage 229 bounded capability",
            description =
                "Existing bounded capability for Stage 229 provenance.",
        )
    }

    private fun establishedMemoryContinuity(
        identity: IdentityId,
    ): MemoryContinuityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId = MemoryId.from("memory-stage229"),
                subjectIdentityId = identity,
                memoryClass = MemoryClass.WORKING,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(80),
                retention = MemoryRetention.SESSION,
                source =
                    MemorySource.create(
                        sourceId = "source-stage229",
                        sourceType = "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve Stage 229 upstream provenance.",
                    ),
                content =
                    "Existing bounded Stage 229 memory context.",
            )

        return MemoryContinuityResult.create(
            traceId = TraceId.from("trace-stage229-memory"),
            status = MemoryContinuityStatus.ESTABLISHED,
            record =
                MemoryContinuityRecord.create(
                    representation = representation,
                ),
        )
    }

    private fun task(): TaskRecord {
        val decision = decision()

        return TaskRecord.create(
            taskId = TaskId.from("task-stage229"),
            decision = decision,
            state = TaskState.CREATED,
            summary = decision.summary,
        )
    }

    private fun decision(): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId =
                            TraceId.from(
                                "trace-stage229-task",
                            ),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(200L),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Preserve bounded Stage 229 emergency escalation context.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Preserve bounded Stage 229 emergency escalation context.",
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
                DevilTimestamp.fromEpochMilliseconds(200L),
        )
    }

    private companion object {
        const val SUBJECT_ID =
            "identity:stage229:subject"
    }
}
