package com.devil.app

import android.app.Application
import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
import com.devil.app.capability.AndroidCapabilityRegistry
import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.capability.DefaultAndroidCapabilityRegistry
import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
import com.devil.app.capability.DefaultAndroidCapabilityStateProvider
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.ConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.DefaultConversationEntryIdProvider
import com.devil.app.conversation.DefaultConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
import com.devil.app.conversation.VoiceConversationRuntimeInputMetadataProvider
import com.devil.app.education.Stage316EducationAlphaCoordinator
import com.devil.app.execution.AndroidExecutionAdapter
import com.devil.app.execution.DefaultAndroidExecutionAttemptPort
import com.devil.app.execution.DefaultAndroidExecutionAdapter
import com.devil.app.execution.AndroidRealExecutionDirectiveStore
import com.devil.app.execution.DefaultAndroidExecutionPerformer
import com.devil.app.execution.Stage314RealAndroidSubmissionFlowCoordinator
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.app.internet.AndroidInternetKnowledgeCoordinator
import com.devil.app.internet.AndroidInternetKnowledgeSafetyCoordinator
import com.devil.app.internet.DefaultAndroidInternetKnowledgeSource
import com.devil.app.modelprovider.conversation.AndroidConversationIntakeEvidenceStore
import com.devil.app.modelprovider.conversation.AndroidConversationalResponseCompositionCoordinator
import com.devil.app.modelprovider.conversation.ConversationalResponseSubmissionFlowCoordinator
import com.devil.app.modelprovider.conversation.DefaultAndroidConversationalModelInferencePort
import com.devil.app.modelprovider.conversation.DefaultConversationalModelConfigurationSource
import com.devil.app.modelprovider.conversation.DefaultHttpsConversationalModelTransport
import com.devil.app.diagnostic.Stage314AndroidPostActionDiagnosticRecorder
import com.devil.app.device.AndroidDeviceKnowledgeCoordinator
import com.devil.app.device.AndroidDeviceKnowledgeQueryCoordinator
import com.devil.app.vision.DefaultAndroidCameraInventorySource
import com.devil.app.vision.DefaultAndroidVisionFrameSource
import com.devil.app.vision.AndroidVisionFramePerceptionCoordinator
import com.devil.app.observation.AndroidObservationAdapter
import com.devil.app.observation.DefaultAndroidObservationAdapter
import com.devil.app.observation.DefaultAndroidObservationEvidencePort
import com.devil.app.observation.Stage314AndroidPostActionObservationSource
import com.devil.app.observation.Stage314AndroidPostActionObservationStore
import com.devil.app.permission.AndroidPermissionAuthorityAdapter
import com.devil.app.permission.DefaultAndroidPermissionAuthorityAdapter
import com.devil.app.permission.DefaultAndroidPermissionGrantChecker
import com.devil.app.notification.AndroidNotificationAnalysisCoordinator
import com.devil.app.notification.AndroidNotificationPerceptionCoordinator
import com.devil.app.outcome.AndroidOutcomeAdapter
import com.devil.app.outcome.DefaultAndroidOutcomeAdapter
import com.devil.app.outcome.DefaultAndroidOutcomeEvidencePort
import com.devil.app.outcome.Stage314AndroidPostActionOutcomeSource
import com.devil.app.outcome.Stage314VerifiedAndroidOutcomePresentationStore
import com.devil.app.runtime.AndroidRuntimeInputCoordinator
import com.devil.app.runtime.DefaultAndroidContextEnvelopeProvider
import com.devil.app.runtime.DefaultAndroidRuntimeGateway
import com.devil.app.runtime.DefaultAndroidRuntimeInputCoordinator
import com.devil.app.verification.AndroidVerificationAdapter
import com.devil.app.verification.DefaultAndroidVerificationAdapter
import com.devil.app.verification.DefaultAndroidVerificationEvidencePort
import com.devil.app.verification.Stage314AndroidPostActionVerificationSource
import com.devil.app.voice.AndroidVoiceOutputSource
import com.devil.app.voice.DefaultAndroidVoiceOutputSource
import com.devil.app.voice.DevilVoiceCoordinator
import com.devil.app.voice.HandsFreeProductionCoordinator
import com.devil.app.voice.HandsFreeAuthenticationCoordinator
import com.devil.app.voice.Stage315AndroidHandsFreeAuthenticationHandoff
import com.devil.app.voice.VoiceConversationOutputCoordinator
import com.devil.app.voice.VoiceConversationResultCoordinator
import com.devil.core.model.owner.OwnerProfileUpdateCoordinator
import com.devil.core.model.child.ChildPolicyCoordinator
import com.devil.core.model.child.ChildPolicySatisfactionCoordinator
import com.devil.core.model.reliability.ReliabilityCoordinator
import com.devil.core.model.reliability.RecoveryRequestCoordinator
import com.devil.core.model.reliability.RecoveryAttemptCoordinator
import com.devil.core.model.reliability.RecoveryVerificationCoordinator
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyRepresentationReducer
import com.devil.core.runtime.privacy.PrivacyProtectedContextResolver
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import com.devil.core.runtime.modelprovider.conversation.ConversationalResponseCoordinator
import com.devil.core.runtime.UnifiedDevilRuntime
import com.devil.core.runtime.execution.ExecutionAttemptPort
import com.devil.core.runtime.observation.ObservationEvidencePort
import com.devil.core.runtime.outcome.OutcomeEvidencePort
import com.devil.core.runtime.verification.VerificationEvidencePort
import java.util.Locale

/**
 * Android process bootstrap for Devil.
 *
 * The Android application owns one process-scoped reference to the single
 * UnifiedDevilRuntime and bounded Android embodiment adapters around it.
 *
 * Stage 24 established Android conversation presentation.
 * Stage 27 established Android Capability Registry.
 * Stage 28 established capability availability and health.
 * Stage 29 established Android permission assessment.
 * Stage 30 established bounded Android execution.
 * Stage 31 established Android observation.
 * Stage 32 established Android verification.
 * Stage 33 established Android Outcome embodiment.
 * Stage 34 established typed-text runtime entry.
 * Stage 35 established bounded Android voice input.
 * Stage 36 established bounded Android voice output.
 *
 * Stage 37 adds wake and hands-free Android orchestration around those existing
 * input and output boundaries.
 *
 * Stage 37 does not create another runtime, another Brain, another Conversation
 * Domain, or another Security Authority.
 *
 * Approved wake phrases establish attention only.
 *
 * Wake != authentication.
 *
 * Recognition of "Code Red" requests the real authentication boundary only.
 *
 * Code Red != authentication.
 *
 * ContextSource.VOICE does not authenticate the speaker.
 *
 * Android RECORD_AUDIO permission is Android operating-system permission only.
 *
 * Android permission != Devil authorization.
 *
 * No Stage 37 composition may create ACTIVE_SESSION without genuine
 * authentication/session evidence.
 *
 * No authority is granted and no runtime work is performed merely because the
 * Android process was created.
 */
class DevilApplication : Application() {


    /**
     * Stage 313 process-scoped transient correlation store for the exact
     * ConversationIntakeAuthorityResult produced by the single Unified Devil Runtime.
     *
     * This store is bounded, process-local, trace-correlated, and one-shot.
     * It is not Devil Memory, persistence, authorization state, model state,
     * Verification, or Outcome state.
     *
     * OBSERVED_INTAKE != AUTHORITY.
     * GENERATED != VERIFIED.
     */
    private val conversationIntakeEvidenceStore:
        AndroidConversationIntakeEvidenceStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidConversationIntakeEvidenceStore()
    }

    /**
     * Stage 314 process-local owner-alpha authenticated-session state.
     *
     * The store itself does not authenticate, validate a session, grant
     * authorization, enter Owner Mode, or permit execution.
     *
     * STORED_SESSION != SESSION_VALID.
     * SESSION_VALID != AUTHORIZATION.
     */
    val stage314OwnerSessionStore:
        com.devil.app.authentication.Stage314OwnerSessionStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        com.devil.app.authentication.Stage314OwnerSessionStore()
    }

    /**
     * Stage 314 bounded owner-alpha session establishment composition.
     *
     * Only the Android authentication-success callback may invoke this
     * coordinator. Session validity and authorization remain owned by their
     * existing constitutional authorities.
     *
     * AUTHENTICATION_SUCCESS != SESSION_VALID.
     * SESSION_ESTABLISHED != AUTHORIZATION.
     */
    val stage314OwnerSessionEstablishmentCoordinator:
        com.devil.app.authentication.Stage314OwnerSessionEstablishmentCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        com.devil.app.authentication.Stage314OwnerSessionEstablishmentCoordinator(
            sessionStore =
                stage314OwnerSessionStore,
            sessionIdProvider = {
                "stage314-owner-alpha-" +
                    java.util.UUID.randomUUID().toString()
            },
            timeProvider = {
                System.currentTimeMillis()
            },
        )
    }

    val runtime: UnifiedDevilRuntime by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultUnifiedDevilRuntime(
            authorizationAuthority =
                com.devil.core.runtime.authorization.DefaultAuthorizationAuthority(
                    resolver =
                        com.devil.app.authorization.Stage314OwnerAuthorizationEvaluationResolver(
                            sessionStore =
                                stage314OwnerSessionStore,
                        ),
                ),
            identityAuthority =
                com.devil.core.runtime.identity.DefaultIdentityAuthority(
                    requestProvider =
                        com.devil.core.runtime.identity.DefaultIdentityResolutionRequestProvider(
                            configuredSubjectIdentityId =
                                com.devil.core.model.identity.IdentityId.from(
                                    "android-primary-local-subject",
                                ),
                        ),
                ),
            capabilitySelectionAuthority =
                com.devil.core.runtime.capability.DefaultCapabilitySelectionAuthority(
                    registry =
                        capabilityRegistry,
                    resolver =
                        com.devil.app.capability.DefaultAndroidCapabilitySelectionResolver(),
                ),
            executionAttemptPort = executionAttemptPort,
            observationEvidencePort = observationEvidencePort,
            verificationEvidencePort = verificationEvidencePort,
            outcomeEvidencePort = outcomeEvidencePort,
            conversationIntakeEvidencePort =
                conversationIntakeEvidenceStore,
        )
    }


    /**
     * Stage 42 process-scoped bounded HTTPS Internet Knowledge source.
     *
     * This source performs retrieval only. External content remains untrusted
     * data and gains no constitutional authority from successful transport.
     */
    val internetKnowledgeSource: DefaultAndroidInternetKnowledgeSource by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidInternetKnowledgeSource()
    }

    val capabilityRegistry: AndroidCapabilityRegistry by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidCapabilityRegistry()
    }
    val capabilityStateProvider: AndroidCapabilityStateProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        val cameraInventorySource =
            DefaultAndroidCameraInventorySource(
                context = applicationContext,
            )

        DefaultAndroidCapabilityStateProvider(
            availabilitySource =
                DefaultAndroidCapabilityAvailabilitySource(
                    visionCameraInventorySource =
                        cameraInventorySource,
                      internetKnowledgeSource =
                          internetKnowledgeSource,
                ),
            healthSource =
                DefaultAndroidCapabilityHealthSource(
                    visionCameraInventorySource =
                        cameraInventorySource,
                      internetKnowledgeSource =
                          internetKnowledgeSource,
                ),
        )
    }
    val permissionAuthorityAdapter: AndroidPermissionAuthorityAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidPermissionAuthorityAdapter(
            grantChecker =
                DefaultAndroidPermissionGrantChecker(
                    context = applicationContext,
                ),
        )
    }



    /**
     * Stage 314 process-local expected post-action condition store.
     *
     * EXPECTATION_STORED != OBSERVED.
     */
    private val stage314PostActionExpectationStore:
        Stage314AndroidPostActionExpectationStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage314AndroidPostActionExpectationStore()
    }

    /**
     * Stage 314 process-local genuine accessibility-derived observation store.
     *
     * OBSERVED_SCREEN_METADATA != VERIFIED.
     */
    /**
     * Stage 314 debug-runtime post-action diagnostic recorder.
     *
     * DIAGNOSTIC_RECORDED != OBSERVED.
     * DIAGNOSTIC_RECORDED != VERIFIED.
     * DIAGNOSTIC_RECORDED != OUTCOME_ESTABLISHED.
     */
    private val stage314PostActionDiagnosticRecorder:
        Stage314AndroidPostActionDiagnosticRecorder by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage314AndroidPostActionDiagnosticRecorder(
            context = applicationContext,
        )
    }

    /**
     * Stage 314 process-local Android accessibility-change readiness seam.
     *
     * Android platform change readiness is not Observation, Verification,
     * Outcome, authorization, or proof that an intended effect succeeded.
     */
    val stage314AccessibilityChangeReadinessStore:
        Stage314AndroidAccessibilityChangeReadinessStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage314AndroidAccessibilityChangeReadinessStore()
    }

    private val stage314PostActionObservationStore:
        Stage314AndroidPostActionObservationStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage314AndroidPostActionObservationStore()
    }

    /**
     * Stage 314 process-scoped one-shot real-device execution directive store.
     *
     * This store supplies only explicitly armed Android embodiment data to the
     * already-established constitutional execution path.
     *
     * It does not grant authorization, approve execution, select a capability,
     * infer a target, grant Android permission, perform an action, establish
     * Verification or Outcome, or create another execution path.
     *
     * Empty store therefore preserves the existing fail-closed DEFERRED behavior.
     *
     * ARMED != AUTHORIZED.
     * DIRECTIVE_AVAILABLE != EXECUTION_APPROVED.
     * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
     * ATTEMPTED != VERIFIED.
     */
    val realExecutionDirectiveStore:
        AndroidRealExecutionDirectiveStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidRealExecutionDirectiveStore(
            postActionExpectationStore =
                stage314PostActionExpectationStore,
            accessibilityChangeReadinessStore =
                stage314AccessibilityChangeReadinessStore,
        )
    }

    private val androidExecutionPerformer:
        DefaultAndroidExecutionPerformer by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidExecutionPerformer(
            directiveProvider =
                realExecutionDirectiveStore,
            accessibilityChangeReadinessStore =
                stage314AccessibilityChangeReadinessStore,
        )
    }

    val executionAdapter: AndroidExecutionAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidExecutionAdapter(
            performer =
                androidExecutionPerformer,
        )
    }

    private val executionAttemptPort: ExecutionAttemptPort by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidExecutionAttemptPort(
            capabilityStateProvider =
                capabilityStateProvider,
            permissionAuthorityAdapter =
                permissionAuthorityAdapter,
            executionAdapter =
                executionAdapter,
        )
    }

    val observationAdapter: AndroidObservationAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidObservationAdapter(
            observationSource =
                Stage314AndroidPostActionObservationSource(
                    expectationStore =
                        stage314PostActionExpectationStore,
                    observationStore =
                        stage314PostActionObservationStore,
                    accessibilityChangeReadinessStore =
                        stage314AccessibilityChangeReadinessStore,
                    diagnostic =
                        stage314PostActionDiagnosticRecorder,
                ),
        )
    }

    private val observationEvidencePort: ObservationEvidencePort by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidObservationEvidencePort(
            observationAdapter = observationAdapter,
        )
    }


    val verificationAdapter: AndroidVerificationAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidVerificationAdapter(
            verificationSource =
                Stage314AndroidPostActionVerificationSource(
                    expectationStore =
                        stage314PostActionExpectationStore,
                    observationStore =
                        stage314PostActionObservationStore,
                    diagnostic =
                        stage314PostActionDiagnosticRecorder,
                ),
        )
    }

    private val verificationEvidencePort: VerificationEvidencePort by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidVerificationEvidencePort(
            verificationAdapter = verificationAdapter,
        )
    }

    /**
     * Stage 314 process-local presentation handoff for an already-established
     * trace/capability-bound Android Outcome.
     *
     * PRESENTATION_HANDOFF != OUTCOME_AUTHORITY.
     * PRESENTATION_HANDOFF != RUNTIME_ACCEPTED.
     */
    private val stage314VerifiedAndroidOutcomePresentationStore:
        Stage314VerifiedAndroidOutcomePresentationStore by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage314VerifiedAndroidOutcomePresentationStore()
    }

    private val outcomeEvidencePort: OutcomeEvidencePort by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidOutcomeEvidencePort(
            outcomeAdapter = outcomeAdapter,
        )
    }

    val outcomeAdapter: AndroidOutcomeAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidOutcomeAdapter(
            outcomeSource =
                Stage314AndroidPostActionOutcomeSource(
                    expectationStore =
                        stage314PostActionExpectationStore,
                    observationStore =
                        stage314PostActionObservationStore,
                    presentationStore =
                        stage314VerifiedAndroidOutcomePresentationStore,
                    diagnostic =
                        stage314PostActionDiagnosticRecorder,
                ),
        )
    }

    val runtimeInputCoordinator: AndroidRuntimeInputCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidRuntimeInputCoordinator(
            contextEnvelopeProvider =
                DefaultAndroidContextEnvelopeProvider(),
            runtimeGateway =
                DefaultAndroidRuntimeGateway(
                    runtime = runtime,
                ),
        )
    }

    val conversationInteractionCoordinator:
        ConversationInteractionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ConversationInteractionCoordinator()
    }

    private val conversationEntryIdProvider:
        ConversationEntryIdProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationEntryIdProvider()
    }

    private val conversationRuntimeInputMetadataProvider:
        ConversationRuntimeInputMetadataProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationRuntimeInputMetadataProvider()
    }

    private val conversationRuntimeSubmissionCoordinator:
        ConversationRuntimeSubmissionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationRuntimeSubmissionCoordinator(
            metadataProvider =
                conversationRuntimeInputMetadataProvider,
            runtimeInputCoordinator =
                runtimeInputCoordinator,
        )
    }

    private val baseConversationSubmissionFlowCoordinator:
        ConversationSubmissionFlowCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationSubmissionFlowCoordinator(
            interactionCoordinator =
                conversationInteractionCoordinator,
            entryIdProvider =
                conversationEntryIdProvider,
            runtimeSubmissionCoordinator =
                conversationRuntimeSubmissionCoordinator,
        )
    }


    private val stage314RealAndroidSubmissionFlowCoordinator:
        ConversationSubmissionFlowCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage314RealAndroidSubmissionFlowCoordinator(
            submissionCoordinator =
                baseConversationSubmissionFlowCoordinator,
            directiveStore =
                realExecutionDirectiveStore,
            presentationStore =
                stage314VerifiedAndroidOutcomePresentationStore,
            interactionCoordinator =
                conversationInteractionCoordinator,
            entryIdProvider =
                conversationEntryIdProvider,
        )
    }

    /**
     * Stage 313 bounded conversational-model configuration.
     *
     * Endpoint and model identifier may be supplied through generated build
     * configuration. The credential deliberately remains unavailable because
     * Devil currently has no approved secure runtime credential mechanism for
     * conversational-model access.
     *
     * Missing credential therefore fails closed rather than embedding a secret
     * in the APK.
     *
     * CONFIGURATION_AVAILABLE != AUTHORIZATION.
     * GENERATED != VERIFIED.
     * MODEL != DEVIL.
     */
    private val conversationalModelConfigurationSource:
        DefaultConversationalModelConfigurationSource by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationalModelConfigurationSource(
            endpointProvider = {
                BuildConfig
                    .DEVIL_CONVERSATIONAL_MODEL_ENDPOINT
                    .takeIf { value ->
                        value.isNotBlank()
                    }
            },
            modelIdProvider = {
                BuildConfig
                    .DEVIL_CONVERSATIONAL_MODEL_ID
                    .takeIf { value ->
                        value.isNotBlank()
                    }
            },
            credentialProvider = {
                null
            },
        )
    }

    private val conversationalModelInferencePort:
        DefaultAndroidConversationalModelInferencePort by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidConversationalModelInferencePort(
            configurationSource =
                conversationalModelConfigurationSource,
            transport =
                DefaultHttpsConversationalModelTransport(),
        )
    }

    private val conversationalResponseCompositionCoordinator:
        AndroidConversationalResponseCompositionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidConversationalResponseCompositionCoordinator(
            intakeEvidenceStore =
                conversationIntakeEvidenceStore,
            responseCoordinator =
                ConversationalResponseCoordinator(
                    inferencePort =
                        conversationalModelInferencePort,
                ),
            interactionCoordinator =
                conversationInteractionCoordinator,
            entryIdProvider =
                conversationEntryIdProvider,
        )
    }

    val conversationSubmissionFlowCoordinator:
        ConversationSubmissionFlowCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ConversationalResponseSubmissionFlowCoordinator(
            submissionCoordinator =
                stage314RealAndroidSubmissionFlowCoordinator,
            responseCompositionCoordinator =
                conversationalResponseCompositionCoordinator,
        )
    }

    private val voiceConversationRuntimeInputMetadataProvider:
        ConversationRuntimeInputMetadataProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        VoiceConversationRuntimeInputMetadataProvider()
    }

    private val voiceConversationRuntimeSubmissionCoordinator:
        ConversationRuntimeSubmissionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationRuntimeSubmissionCoordinator(
            metadataProvider =
                voiceConversationRuntimeInputMetadataProvider,
            runtimeInputCoordinator =
                runtimeInputCoordinator,
        )
    }

    val voiceConversationSubmissionFlowCoordinator:
        ConversationSubmissionFlowCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationSubmissionFlowCoordinator(
            interactionCoordinator =
                conversationInteractionCoordinator,
            entryIdProvider =
                conversationEntryIdProvider,
            runtimeSubmissionCoordinator =
                voiceConversationRuntimeSubmissionCoordinator,
        )
    }

    val voiceConversationResultCoordinator:
        VoiceConversationResultCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        VoiceConversationResultCoordinator(
            interactionCoordinator =
                conversationInteractionCoordinator,
            submissionFlowCoordinator =
                voiceConversationSubmissionFlowCoordinator,
        )
    }

    val voiceOutputSource: AndroidVoiceOutputSource by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        val devilVoiceProfile =
            requireNotNull(
                DevilVoiceCoordinator()
                    .prepare(
                        Locale.getDefault().toLanguageTag(),
                    ),
            ) {
                "Default Devil Voice profile must be available for the current language."
            }

        DefaultAndroidVoiceOutputSource(
            context = applicationContext,
            voiceProfile = devilVoiceProfile,
        )
    }

    val voiceConversationOutputCoordinator:
        VoiceConversationOutputCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        VoiceConversationOutputCoordinator(
            outputSource =
                voiceOutputSource,
        )
    }


    /**
     * Stage 37 process-scoped wake/hands-free orchestration.
     *
     * Stage 315 composes the approved Android authentication-required handoff. This
     * coordinator still cannot itself authenticate or create ACTIVE_SESSION.
     */

    /**
     * Stage 39 process-scoped Android notification perception and analysis path.
     *
     * Notification listener connectivity and notification content grant no
     * constitutional authority.
     *
     * This coordinator does not create ConversationInput, invoke a separate
     * runtime, speak notification content, persist notification content, or
     * execute an action.
     */

    /**
     * Stage 40 process-scoped Android Device Knowledge embodiment.
     *
     * Device knowledge is bounded to directly observed, non-sensitive Android
     * platform facts approved by Stage 40.
     *
     * Reading device knowledge does not authenticate a subject, grant authority,
     * create logical memory, perform an Android action, or establish an Outcome.
     *
     * Device knowledge != device control.
     */
    val deviceKnowledgeCoordinator:
        AndroidDeviceKnowledgeCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidDeviceKnowledgeCoordinator()
    }


    /**
     * Stage 40 process-scoped bounded Device Knowledge query boundary.
     *
     * Queries are explicit typed requests only. This coordinator does not parse
     * conversation text, infer intent, invoke a separate runtime, or execute an
     * Android action.
     */
    val deviceKnowledgeQueryCoordinator:
        AndroidDeviceKnowledgeQueryCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidDeviceKnowledgeQueryCoordinator()
    }


    /**
     * Stage 41 process-scoped bounded real Camera2 frame-perception boundary.
     *
     * Camera capture is an Android input embodiment only.
     *
     * Captured frame != semantic understanding.
     * Captured frame != identity.
     * Captured frame != authentication.
     * Captured frame != authorization.
     * Captured frame != memory.
     * Captured frame != verified Outcome.
     *
     * The source must be invoked away from the Android main thread.
     */
    val visionFramePerceptionCoordinator:
        AndroidVisionFramePerceptionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidVisionFramePerceptionCoordinator(
            frameSource =
                DefaultAndroidVisionFrameSource(
                    context = applicationContext,
                ),
        )
    }


    /**
     * Stage 42 process-scoped bounded Internet Knowledge retrieval boundary.
     *
     * HTTPS retrieval remains external untrusted knowledge. Retrieval does not
     * create ConversationInput, trust, authorization, memory, execution, or
     * verified Outcome.
     */
    val internetKnowledgeCoordinator:
        AndroidInternetKnowledgeCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidInternetKnowledgeCoordinator(
            source = internetKnowledgeSource,
        )
    }


    /**
     * Stage 42 structural external-content safety boundary.
     *
     * This may establish only eligibility for later bounded analysis.
     * Eligibility is not trust or authority.
     */
    val internetKnowledgeSafetyCoordinator:
        AndroidInternetKnowledgeSafetyCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidInternetKnowledgeSafetyCoordinator(
            knowledgeCoordinator =
                internetKnowledgeCoordinator,
        )
    }


    /**
     * Stage 43 process-scoped bounded owner-profile structural update boundary.
     *
     * This coordinator may derive a new transient OwnerProfileSnapshot from
     * explicitly supplied descriptive owner-domain data.
     *
     * It does not authenticate the owner, prove relationships, establish trust,
     * grant guardian authority, enter Owner Mode, grant authorization, persist
     * logical memory, or execute an action.
     *
     * No default OwnerProfileSource is composed in Stage 43 because Devil must
     * not fabricate owner-profile information merely to satisfy production
     * composition.
     */
    val ownerProfileUpdateCoordinator:
        OwnerProfileUpdateCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        OwnerProfileUpdateCoordinator()
    }


    /**
     * Stage 44 process-scoped bounded Child and Guardian Policy evaluation.
     *
     * These coordinators evaluate only explicitly supplied Stage 44 contracts.
     *
     * Production deliberately does not fabricate a ChildGuardianContextSource,
     * GuardianApprovalSource, child classification, guardian identity, guardian
     * authority, or guardian approval.
     *
     * Child policy allowed != Devil authorization.
     * Guardian approval != Devil authorization.
     * Child policy satisfied != Execution APPROVED.
     */
    val childPolicyCoordinator:
        ChildPolicyCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ChildPolicyCoordinator()
    }

    val childPolicySatisfactionCoordinator:
        ChildPolicySatisfactionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ChildPolicySatisfactionCoordinator()
    }


    /**
     * Stage 45 process-scoped bounded Reliability and Recovery governance.
     *
     * These coordinators assess explicit reliability evidence, derive bounded
     * recovery requests, account for finite recovery attempts, and evaluate
     * explicit post-attempt recovery evidence.
     *
     * They do not automatically retry, restart, reconnect, schedule recovery,
     * invoke UnifiedDevilRuntime, execute Android actions, mutate capability
     * health, erase failure evidence, persist logical memory, or establish a
     * constitutional Verification or Outcome.
     *
     * RECOVERY_ELIGIBLE != retry authorized.
     * RecoveryRequest != execution request.
     * RECORDED != recovery executed.
     * VERIFIED_RECOVERED != constitutional Verification or Outcome success.
     */
    val reliabilityCoordinator:
        ReliabilityCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ReliabilityCoordinator()
    }

    val recoveryRequestCoordinator:
        RecoveryRequestCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        RecoveryRequestCoordinator()
    }

    val recoveryAttemptCoordinator:
        RecoveryAttemptCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        RecoveryAttemptCoordinator()
    }

    val recoveryVerificationCoordinator:
        RecoveryVerificationCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        RecoveryVerificationCoordinator()
    }


    /**
     * Stage 46 process-scoped bounded Privacy and Security Hardening governance.
     *
     * These components assess explicit privacy exposure requests, derive bounded
     * disclosure treatment, reduce an explicitly supplied representation only
     * after an AVAILABLE disclosure decision, and translate already-established
     * constitutional security evidence into privacy protected-context evidence.
     *
     * Production deliberately does not fabricate protected owner context.
     *
     * A valid session does not prove owner identity.
     * SecurityStage.OWNER_MODE does not independently prove owner identity.
     * Privacy ALLOWED does not grant constitutional authorization.
     * Privacy disclosure treatment does not perform disclosure.
     * Representation reduction does not transmit or persist content.
     *
     * These components are intentionally not connected here to conversation,
     * voice, notifications, Internet retrieval, Android execution, logical
     * memory persistence, or UnifiedDevilRuntime entry.
     */
    val privacyExposureCoordinator:
        PrivacyExposureCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        PrivacyExposureCoordinator()
    }

    val privacyDisclosureCoordinator:
        PrivacyDisclosureCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        PrivacyDisclosureCoordinator()
    }

    val privacyRepresentationReducer:
        PrivacyRepresentationReducer by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        PrivacyRepresentationReducer()
    }

    val privacyProtectedContextResolver:
        PrivacyProtectedContextResolver by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        PrivacyProtectedContextResolver()
    }

    val notificationAnalysisCoordinator:
        AndroidNotificationAnalysisCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidNotificationAnalysisCoordinator()
    }

    val notificationPerceptionCoordinator:
        AndroidNotificationPerceptionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidNotificationPerceptionCoordinator(
            analysisCoordinator =
                notificationAnalysisCoordinator,
        )
    }

    /**
     * Stage 316 process-scoped bounded Education Alpha composition.
     *
     * Delegates education-session preparation to the existing Stage 85
     * education domain. This is not another runtime or Education Authority.
     */
    val stage316EducationAlphaCoordinator:
        Stage316EducationAlphaCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        Stage316EducationAlphaCoordinator()
    }

    val handsFreeProductionCoordinator:
        HandsFreeProductionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        HandsFreeProductionCoordinator(
            authenticationCoordinator =
                HandsFreeAuthenticationCoordinator(
                    authenticationHandoff =
                        Stage315AndroidHandsFreeAuthenticationHandoff(),
                ),
        )
    }
}
