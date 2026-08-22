package com.devil.app.device

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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage223UnifiedMultiDeviceValidationTest {

    @Test
    fun `exact coherent Phase N chain validates and preserves provenance`() {
        val fixture = fixture()

        val result =
            coordinator().prepare(
                deviceProtocol = fixture.deviceProtocol,
                tabletEmbodiment = fixture.tabletEmbodiment,
                educationTabletExperience = fixture.educationTabletExperience,
                pcEmbodiment = fixture.pcEmbodiment,
                pcCapabilityAdapter = fixture.pcCapabilityAdapter,
                crossDeviceIdentity = fixture.crossDeviceIdentity,
                sessionGovernance = fixture.sessionGovernance,
                taskContinuity = fixture.taskContinuity,
                memoryContinuity = fixture.memoryContinuity,
                deviceTrustRevocation = fixture.deviceTrustRevocation,
                validationFocus = "  Phase N structural continuity  ",
                validationEvidenceDescription =
                    "  Exact Stage 213 through 222 provenance is available.  ",
            )

        assertEquals(
            AndroidUnifiedMultiDeviceValidationStatus.VALIDATED,
            result.status,
        )
        assertSame(fixture.deviceProtocol, result.deviceProtocol)
        assertSame(fixture.tabletEmbodiment, result.tabletEmbodiment)
        assertSame(
            fixture.educationTabletExperience,
            result.educationTabletExperience,
        )
        assertSame(fixture.pcEmbodiment, result.pcEmbodiment)
        assertSame(fixture.pcCapabilityAdapter, result.pcCapabilityAdapter)
        assertSame(fixture.crossDeviceIdentity, result.crossDeviceIdentity)
        assertSame(fixture.sessionGovernance, result.sessionGovernance)
        assertSame(fixture.taskContinuity, result.taskContinuity)
        assertSame(fixture.memoryContinuity, result.memoryContinuity)
        assertSame(
            fixture.deviceTrustRevocation,
            result.deviceTrustRevocation,
        )
        assertEquals(
            "Phase N structural continuity",
            result.validationFocus,
        )
        assertEquals(
            "Exact Stage 213 through 222 provenance is available.",
            result.validationEvidenceDescription,
        )
    }

    @Test
    fun `revoked Stage 222 context keeps unified validation deferred`() {
        val fixture = fixture()

        val revoked =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = fixture.memoryContinuity,
                    disposition = AndroidDeviceTrustRevocationStatus.REVOKED,
                )

        val result =
            coordinator().prepare(
                deviceProtocol = fixture.deviceProtocol,
                tabletEmbodiment = fixture.tabletEmbodiment,
                educationTabletExperience = fixture.educationTabletExperience,
                pcEmbodiment = fixture.pcEmbodiment,
                pcCapabilityAdapter = fixture.pcCapabilityAdapter,
                crossDeviceIdentity = fixture.crossDeviceIdentity,
                sessionGovernance = fixture.sessionGovernance,
                taskContinuity = fixture.taskContinuity,
                memoryContinuity = fixture.memoryContinuity,
                deviceTrustRevocation = revoked,
                validationFocus = "Phase N",
                validationEvidenceDescription = "Bounded structural evidence.",
            )

        assertEquals(
            AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.deviceTrustRevocation)
    }

    @Test
    fun `individually available but unrelated provenance remains deferred`() {
        val first = fixture()
        val second = fixture(suffix = "other")

        val result =
            coordinator().prepare(
                deviceProtocol = first.deviceProtocol,
                tabletEmbodiment = first.tabletEmbodiment,
                educationTabletExperience = first.educationTabletExperience,
                pcEmbodiment = first.pcEmbodiment,
                pcCapabilityAdapter = first.pcCapabilityAdapter,
                crossDeviceIdentity = second.crossDeviceIdentity,
                sessionGovernance = first.sessionGovernance,
                taskContinuity = first.taskContinuity,
                memoryContinuity = first.memoryContinuity,
                deviceTrustRevocation = first.deviceTrustRevocation,
                validationFocus = "Phase N",
                validationEvidenceDescription = "Bounded structural evidence.",
            )

        assertEquals(
            AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `blank validation metadata remains deferred`() {
        val fixture = fixture()

        val result =
            coordinator().prepare(
                deviceProtocol = fixture.deviceProtocol,
                tabletEmbodiment = fixture.tabletEmbodiment,
                educationTabletExperience = fixture.educationTabletExperience,
                pcEmbodiment = fixture.pcEmbodiment,
                pcCapabilityAdapter = fixture.pcCapabilityAdapter,
                crossDeviceIdentity = fixture.crossDeviceIdentity,
                sessionGovernance = fixture.sessionGovernance,
                taskContinuity = fixture.taskContinuity,
                memoryContinuity = fixture.memoryContinuity,
                deviceTrustRevocation = fixture.deviceTrustRevocation,
                validationFocus = "   ",
                validationEvidenceDescription = "Evidence.",
            )

        assertEquals(
            AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `validated result rejects broken exact provenance`() {
        val first = fixture()
        val second = fixture(suffix = "other")

        assertFailsWith<IllegalArgumentException> {
            AndroidUnifiedMultiDeviceValidationResult.create(
                status = AndroidUnifiedMultiDeviceValidationStatus.VALIDATED,
                deviceProtocol = first.deviceProtocol,
                tabletEmbodiment = first.tabletEmbodiment,
                educationTabletExperience = first.educationTabletExperience,
                pcEmbodiment = first.pcEmbodiment,
                pcCapabilityAdapter = first.pcCapabilityAdapter,
                crossDeviceIdentity = second.crossDeviceIdentity,
                sessionGovernance = first.sessionGovernance,
                taskContinuity = first.taskContinuity,
                memoryContinuity = first.memoryContinuity,
                deviceTrustRevocation = first.deviceTrustRevocation,
                validationFocus = "Phase N",
                validationEvidenceDescription = "Evidence.",
            )
        }
    }

    @Test
    fun `deferred result rejects validation metadata`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidUnifiedMultiDeviceValidationResult.create(
                status = AndroidUnifiedMultiDeviceValidationStatus.DEFERRED,
                validationFocus = "Phase N",
            )
        }
    }

    private fun coordinator():
        AndroidUnifiedMultiDeviceValidationCoordinator {
        return AndroidUnifiedMultiDeviceValidationCoordinator()
    }

    private fun fixture(
        suffix: String = "main",
    ): Fixture {
        val relationship =
            CrossDeviceRelationshipRepresentationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage223-relationship-$suffix",
                    ),
                status =
                    CrossDeviceRelationshipRepresentationStatus.REPRESENTED,
                relationship =
                    CrossDeviceRelationshipRecord.create(
                        sourceEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage223:source:$suffix",
                            ),
                        targetEmbodimentId =
                            EmbodimentId.from(
                                "embodiment:stage223:target:$suffix",
                            ),
                        description =
                            "Bounded Stage 223 cross-device relationship.",
                    ),
            )

        val deviceProtocol =
            AndroidDeviceProtocolIntegrationCoordinator()
                .integrate(
                    relationshipRepresentation = relationship,
                    protocolId = "devil.phase-n.$suffix",
                )

        val tabletEmbodiment =
            tabletEmbodiment(suffix)

        val educationTabletExperience =
            AndroidEducationTabletExperienceCoordinator()
                .integrate(
                    tabletEmbodiment = tabletEmbodiment,
                    educationSession = educationSession(suffix),
                )

        val pcEmbodiment =
            pcEmbodiment(suffix)

        val pcCapabilityAdapter =
            AndroidPcCapabilityAdapterCoordinator()
                .integrate(
                    pcEmbodiment = pcEmbodiment,
                    capability = capability(suffix),
                    adapterId = "pc.stage223.$suffix",
                )

        val identity =
            IdentityId.from(
                "identity:stage223:$suffix",
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
                        "session:stage223:$suffix",
                    ),
                subjectIdentityId = identity,
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(100L),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(200L),
            )

        val sessionTrace =
            TraceId.from(
                "trace-stage223-session-$suffix",
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
                            DevilTimestamp.fromEpochMilliseconds(150L),
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
                    task = task(suffix),
                )

        val memoryContinuity =
            AndroidCrossDeviceMemoryContinuityCoordinator()
                .integrate(
                    taskContinuity = taskContinuity,
                    memoryContinuity =
                        establishedMemoryContinuity(
                            identity = identity,
                            suffix = suffix,
                        ),
                )

        val trust =
            AndroidDeviceTrustRevocationCoordinator()
                .integrate(
                    memoryContinuity = memoryContinuity,
                    disposition = AndroidDeviceTrustRevocationStatus.TRUSTED,
                )

        return Fixture(
            deviceProtocol = deviceProtocol,
            tabletEmbodiment = tabletEmbodiment,
            educationTabletExperience = educationTabletExperience,
            pcEmbodiment = pcEmbodiment,
            pcCapabilityAdapter = pcCapabilityAdapter,
            crossDeviceIdentity = crossDeviceIdentity,
            sessionGovernance = sessionGovernance,
            taskContinuity = taskContinuity,
            memoryContinuity = memoryContinuity,
            deviceTrustRevocation = trust,
        )
    }

    private fun tabletEmbodiment(
        suffix: String,
    ): AndroidTabletEmbodimentResult {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage223:tablet:$suffix",
                    ),
                platformId = EmbodimentPlatformId.from("android"),
                description = "Stage 223 bounded tablet embodiment.",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage223-tablet-$suffix",
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

    private fun educationSession(
        suffix: String,
    ): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage223:$suffix",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "identity:stage223:learner:$suffix",
                ),
            objective =
                EducationObjective.create(
                    subject = "Unified Multi-Device Validation",
                    objective =
                        "Preserve bounded tablet education context.",
                ),
        )
    }

    private fun pcEmbodiment(
        suffix: String,
    ): AndroidPcEmbodimentResult {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage223:pc:$suffix",
                    ),
                platformId = EmbodimentPlatformId.from("pc"),
                description = "Stage 223 bounded PC embodiment.",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage223-pc-$suffix",
                    ),
                status = PcEmbodimentAssessmentStatus.PC,
                embodiment = embodiment,
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily = "Linux",
                    ),
            )

        return AndroidPcEmbodimentCoordinator()
            .integrate(assessment)
    }

    private fun capability(
        suffix: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability:stage223:$suffix",
                ),
            category = CapabilityCategory.ACTION,
            name = "Stage 223 PC Adapter",
            description =
                "Bounded existing capability for unified multi-device validation.",
        )
    }

    private fun establishedMemoryContinuity(
        identity: IdentityId,
        suffix: String,
    ): MemoryContinuityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage223-$suffix",
                    ),
                subjectIdentityId = identity,
                memoryClass = MemoryClass.WORKING,
                sensitivity = MemorySensitivity.PRIVATE,
                confidence = MemoryConfidence.from(80),
                retention = MemoryRetention.SESSION,
                source =
                    MemorySource.create(
                        sourceId = "source-stage223-$suffix",
                        sourceType = "bounded-existing-memory",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve bounded Stage 223 memory continuity.",
                    ),
                content = "Existing bounded logical-memory context.",
            )

        return MemoryContinuityResult.create(
            traceId =
                TraceId.from(
                    "trace-stage223-memory-$suffix",
                ),
            status = MemoryContinuityStatus.ESTABLISHED,
            record =
                MemoryContinuityRecord.create(
                    representation = representation,
                ),
        )
    }

    private fun task(
        suffix: String,
    ): TaskRecord {
        val decision = decision(suffix)

        return TaskRecord.create(
            taskId =
                TaskId.from(
                    "task-stage223-$suffix",
                ),
            decision = decision,
            state = TaskState.CREATED,
            summary = decision.summary,
        )
    }

    private fun decision(
        suffix: String,
    ): DecisionRecord {
        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId =
                            TraceId.from(
                                "trace-stage223-task-$suffix",
                            ),
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel = ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(150L),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Preserve bounded unified multi-device validation.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = DecisionState.SELECTED,
            summary =
                "Preserve bounded unified multi-device validation.",
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

    private data class Fixture(
        val deviceProtocol: AndroidDeviceProtocolIntegrationResult,
        val tabletEmbodiment: AndroidTabletEmbodimentResult,
        val educationTabletExperience: AndroidEducationTabletExperienceResult,
        val pcEmbodiment: AndroidPcEmbodimentResult,
        val pcCapabilityAdapter: AndroidPcCapabilityAdapterResult,
        val crossDeviceIdentity: AndroidCrossDeviceIdentityResult,
        val sessionGovernance: AndroidCrossDeviceSessionGovernanceResult,
        val taskContinuity: AndroidCrossDeviceTaskContinuityResult,
        val memoryContinuity: AndroidCrossDeviceMemoryContinuityResult,
        val deviceTrustRevocation: AndroidDeviceTrustRevocationResult,
    )
}
