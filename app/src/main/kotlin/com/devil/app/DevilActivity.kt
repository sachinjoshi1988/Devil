package com.devil.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.devil.app.accessibility.AndroidAccessibilityServiceDiagnosticStatus
import com.devil.app.authentication.Stage314AndroidOwnerAuthenticationCoordinator
import com.devil.app.education.Stage316EducationAlphaResult
import com.devil.app.education.Stage317SpokenEnglishAlphaResult
import com.devil.app.education.Stage318ForeignLanguageAlphaResult
import com.devil.app.education.Stage325ExtendedEducationTestingResult
import com.devil.app.education.Stage326LanguageCurriculumValidationResult
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionState
import com.devil.app.accessibility.DefaultAndroidAccessibilityServiceDiagnosticSource
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationScreen
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationUiState
import com.devil.app.ui.education.DevilEducationInterface
import com.devil.app.ui.education.DevilLanguageLearningInterface
import com.devil.app.ui.finance.DevilFinanceInterface
import com.devil.app.ui.security.DevilSecurityInterface
import com.devil.app.ui.settings.DevilSettingsPrivacyPermissionsInterface
import com.devil.app.ui.research.DevilResearchInterface
import com.devil.app.ui.launch.DevilAwakeningScreen
import com.devil.app.ui.memory.DevilMemoryInterface
import com.devil.app.ui.task.DevilTaskAutomationInterface
import com.devil.app.ui.theme.DevilTheme
import com.devil.app.voice.AndroidVoiceInputListener
import com.devil.app.voice.AndroidVoiceInputResult
import com.devil.app.voice.AndroidVoiceInputSource
import com.devil.app.voice.AndroidVoiceInputStatus
import com.devil.app.voice.AndroidVoiceInteractionMode
import com.devil.app.voice.AndroidVoiceOutputListener
import com.devil.app.voice.AndroidVoiceOutputResult
import com.devil.app.voice.AndroidVoiceOutputStatus
import com.devil.app.voice.DefaultAndroidVoiceInputSource
import com.devil.app.voice.AndroidVoiceLanguagePolicy
import com.devil.app.voice.AndroidVoiceLanguageSelection
import com.devil.app.voice.HandsFreeConversationState
import com.devil.app.voice.HandsFreeProductionAction
import com.devil.app.voice.HandsFreeAuthenticationHandoffStatus
import com.devil.app.voice.HandsFreeProductionResult
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Android launcher surface for Devil.
 *
 * Typed text and voice-derived text enter the same Conversation Domain and
 * Unified Devil Runtime.
 *
 * Stage 35 owns bounded Android speech-recognition presentation/lifecycle.
 *
 * Stage 36 owns bounded Android TextToSpeech presentation/lifecycle.
 *
 * Stage 37 adds explicit wake and hands-free orchestration.
 *
 * Approved wake phrases establish attention only.
 *
 * Wake != authentication.
 *
 * "Code Red" requests the authentication handoff only.
 *
 * Code Red != authentication.
 *
 * The current default Stage 37 authentication handoff is fail-closed and cannot
 * create ACTIVE_SESSION.
 *
 * Android microphone permission != Devil authorization.
 *
 * This Activity does not grant authority, authenticate a speaker, create a
 * session, enter Owner Mode, bypass the constitutional runtime, execute a
 * capability, or fabricate success.
 */
class DevilActivity : FragmentActivity() {

    private var conversationState by
        mutableStateOf(
            ConversationUiState(),
        )

    private var isVoiceListening by
        mutableStateOf(false)

    private var voiceInputMessage by
        mutableStateOf<String?>(null)

    private var isVoiceSpeaking by
        mutableStateOf(false)

    private var voiceOutputMessage by
        mutableStateOf<String?>(null)

    private var handsFreeEnabled by
        mutableStateOf(false)

    private var handsFreeMessage by
        mutableStateOf<String?>(null)

    private var accessibilityDiagnosticMessage by
        mutableStateOf<String?>(null)

    private var handsFreeState by
        mutableStateOf(
            HandsFreeConversationState.IDLE,
        )

    private var pendingVoiceInteractionMode:
        AndroidVoiceInteractionMode? = null

    private var activeVoiceInteractionMode:
        AndroidVoiceInteractionMode? = null

    private var resumeHandsFreeAfterVoiceOutput:
        Boolean = false

    private lateinit var accessibilityDiagnosticSource:
        DefaultAndroidAccessibilityServiceDiagnosticSource

    private val stage314OwnerAuthenticationCoordinator:
        Stage314AndroidOwnerAuthenticationCoordinator by lazy {
        Stage314AndroidOwnerAuthenticationCoordinator(
            activity = this,
        )
    }

    private var stage314OwnerAuthenticationInProgress = false

    private var stage315HandsFreeOwnerAuthenticationInProgress = false

    private var stage337bModelCredentialAuthenticationInProgress = false

    private var stage337bModelCredentialStatus by
        mutableStateOf<String?>(null)

    /**
     * Stage 314 real-Android submission worker.
     *
     * Only the already-recognized bounded owner-alpha real Android action path
     * uses this worker. Ordinary conversation submission remains unchanged.
     *
     * WORKER_THREAD != AUTHORIZATION.
     * BACKGROUND_SUBMISSION != EXECUTION_SUCCESS.
     */
    private val stage314RealAndroidSubmissionExecutor: ExecutorService =
        Executors.newSingleThreadExecutor()

    private var stage314RealAndroidSubmissionInProgress = false

    private lateinit var voiceInputSource:
        AndroidVoiceInputSource

    private var voiceLanguageSelection by
        mutableStateOf(
            AndroidVoiceLanguageSelection.fromDeviceLanguageTag(
                java.util.Locale
                    .getDefault()
                    .toLanguageTag(),
            ),
        )

    private val voiceLanguagePolicy =
        AndroidVoiceLanguagePolicy()

    private val recordAudioPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            val requestedMode =
                pendingVoiceInteractionMode

            pendingVoiceInteractionMode = null

            if (granted && requestedMode != null) {
                startVoiceInput(
                    mode = requestedMode,
                )
            } else {
                isVoiceListening = false
                activeVoiceInteractionMode = null

                if (
                    requestedMode ==
                    AndroidVoiceInteractionMode.HANDS_FREE
                ) {
                    handsFreeEnabled = false
                    handsFreeState =
                        HandsFreeConversationState.IDLE
                    handsFreeMessage =
                        "Microphone permission is required for hands-free voice input."
                } else {
                    voiceInputMessage =
                        "Microphone permission is required for voice input."
                }
            }
        }

    private val voiceInputListener =
        object : AndroidVoiceInputListener {

            override fun onReady() {
                isVoiceListening = true

                if (
                    activeVoiceInteractionMode ==
                    AndroidVoiceInteractionMode.HANDS_FREE
                ) {
                    handsFreeMessage =
                        when (handsFreeState) {
                            HandsFreeConversationState.IDLE ->
                                "Listening for Devil."

                            HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE ->
                                "Listening for Code Red."

                            HandsFreeConversationState.AUTHENTICATION_REQUESTED ->
                                "Authentication is required."

                            HandsFreeConversationState.ACTIVE_SESSION ->
                                "Listening."
                        }
                } else {
                    voiceInputMessage =
                        "Listening…"
                }
            }

            override fun onResult(
                result: AndroidVoiceInputResult,
            ) {
                isVoiceListening = false

                val completedMode =
                    activeVoiceInteractionMode

                activeVoiceInteractionMode = null

                when (completedMode) {
                    AndroidVoiceInteractionMode.HANDS_FREE ->
                        handleHandsFreeVoiceResult(
                            result = result,
                        )

                    AndroidVoiceInteractionMode.MANUAL ->
                        handleManualVoiceResult(
                            result = result,
                        )

                    null -> Unit
                }
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        installSplashScreen()
        super.onCreate(
            savedInstanceState,
        )

        val devilApplication =
            application as DevilApplication


        setContent {
            DevilTheme {
                /* Stage 262 presentation navigation only. */
                var showSettingsInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 261 presentation navigation only. */
                var showSecurityInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 260 presentation navigation only. */
                var showFinanceInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 259 presentation navigation only. */
                var showResearchInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 258 presentation navigation only. */
                var showLanguageLearningInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 257 presentation navigation only. */
                var showEducationInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 316 bounded Education Alpha presentation state.
                 * UI/process state only; not authentication, authorization,
                 * verified mastery, constitutional Learning, Memory, or persistence.
                 */
                var stage316EducationAlphaResult by remember {
                    mutableStateOf<Stage316EducationAlphaResult?>(null)
                }

                /* Stage 317 bounded Spoken English Alpha presentation state.
                 * Prepared Education Domain context only; not teaching, completed
                 * conversation, speech recognition, verified pronunciation,
                 * verified proficiency, constitutional Learning, or persistence.
                 */
                var stage317SpokenEnglishAlphaResult by remember {
                    mutableStateOf<Stage317SpokenEnglishAlphaResult?>(null)
                }

                /* Stage 318 bounded Foreign Language Alpha presentation state.
                 * Prepared Education Domain context only; not teaching, translation,
                 * completed conversation, verified proficiency, constitutional Learning,
                 * Memory, or persistence.
                 */
                var stage318ForeignLanguageAlphaResult by remember {
                    mutableStateOf<Stage318ForeignLanguageAlphaResult?>(null)
                }

                /* Stage 325 bounded Extended Education Testing presentation state.
                 * Preserves the existing Stage 316 result only; not education
                 * delivery, verified mastery, constitutional Learning, curriculum
                 * execution, Memory commitment, or persistence.
                 */
                var stage325ExtendedEducationTestingResult by remember {
                    mutableStateOf<Stage325ExtendedEducationTestingResult?>(null)
                }

                /* Stage 326 bounded Language Curriculum Validation presentation state.
                 * Preserves existing Stage 318, Stage 131, and Stage 142 contexts only;
                 * not curriculum execution, lesson generation, verified proficiency,
                 * verified mastery, constitutional Learning, Memory, or persistence.
                 */
                var stage326LanguageCurriculumValidationResult by remember {
                    mutableStateOf<Stage326LanguageCurriculumValidationResult?>(null)
                }

                /* Stage 256 presentation navigation only. */
                var showTaskAutomationInterface by remember {
                    mutableStateOf(false)
                }

                /* Stage 255 presentation navigation only. */
                var showMemoryInterface by remember {
                    mutableStateOf(false)
                }

                var showAwakening by remember {
                    mutableStateOf(true)
                }

                BackHandler(
                    enabled =
                        showMemoryInterface ||
                            showTaskAutomationInterface ||
                            showSettingsInterface ||
                            showSecurityInterface ||
                            showFinanceInterface ||
                            showResearchInterface ||
                            showLanguageLearningInterface ||
                            showEducationInterface,
                ) {
                    when {
                        showMemoryInterface ->
                            showMemoryInterface = false

                        showTaskAutomationInterface ->
                            showTaskAutomationInterface = false

                        showSettingsInterface ->
                            showSettingsInterface = false

                        showSecurityInterface ->
                            showSecurityInterface = false

                        showFinanceInterface ->
                            showFinanceInterface = false

                        showResearchInterface ->
                            showResearchInterface = false

                        showLanguageLearningInterface ->
                            showLanguageLearningInterface = false

                        showEducationInterface ->
                            showEducationInterface = false
                    }
                }

                if (showAwakening) {
                    DevilAwakeningScreen(
                        onComplete = {
                            showAwakening = false
                        },
                    )
                } else if (showMemoryInterface) {
                    DevilMemoryInterface(
                        memoryClass = null,
                        sensitivity = null,
                        confidence = null,
                        retention = null,
                        source = null,
                        ownerVisibleReason = null,
                        onBack = {
                            showMemoryInterface = false
                        },
                    )
                } else if (showTaskAutomationInterface) {
                    DevilTaskAutomationInterface(
                        taskId = null,
                        taskSummary = null,
                        taskState = null,
                        triggerKind = null,
                        triggerCondition = null,
                        proactiveStatus = null,
                        proactiveMessage = null,
                        controlledAutonomyStatus = null,
                        controlledAutonomyScope = null,
                        recoveryDisposition = null,
                        recoveryAttemptStatus = null,
                        onBack = {
                            showTaskAutomationInterface = false
                        },
                    )
                } else if (showSettingsInterface) {
                    DevilSettingsPrivacyPermissionsInterface(
                        settingsCommand = null,
                        settingsControlStatus = null,
                        permissionCapability = null,
                        permissionAssessmentStatus = null,
                        requiredPermissions = null,
                        privacyExposureStatus = null,
                        privacyExposureRationale = null,
                        privacyDisclosureStatus = null,
                        privacyDisclosureTreatment = null,
                        privacyDisclosureRationale = null,
                        privacyRepresentationStatus = null,
                        privacyDataClassification = null,
                        modelCredentialStatus =
                            stage337bModelCredentialStatus,
                        modelCredentialAuthenticationInProgress =
                            stage337bModelCredentialAuthenticationInProgress,
                        onModelCredentialProvisionRequested = { credential ->
                            requestStage337bModelCredentialProvisioning(
                                credential = credential,
                            )
                        },
                        onModelCredentialRemovalRequested = {
                            requestStage337bModelCredentialRemoval()
                        },
                        onBack = {
                            showSettingsInterface = false
                        },
                    )
                } else if (showSecurityInterface) {
                    DevilSecurityInterface(
                        securityStage = null,
                        securityState = null,
                        surveillanceIntegrationStatus = null,
                        cameraAdapterStatus = null,
                        eventUnderstandingStatus = null,
                        eventUnderstandingDescription = null,
                        alertingStatus = null,
                        alertDescription = null,
                        responseGovernanceStatus = null,
                        emergencyEscalationStatus = null,
                        escalationDescription = null,
                        ownerDashboardStatus = null,
                        dashboardSummary = null,
                        evidenceRetentionStatus = null,
                        retentionDescription = null,
                        privacyControlsStatus = null,
                        privacyControlsDescription = null,
                        productionValidationStatus = null,
                        validationFocus = null,
                        validationEvidenceDescription = null,
                        onBack = {
                            showSecurityInterface = false
                        },
                    )
                  } else if (showFinanceInterface) {
                      DevilFinanceInterface(
                          financialSubject = null,
                          suppliedFinancialFacts = null,
                          integrationFocus = null,
                          integrationObjective = null,
                          integrationStatus = null,
                          personalFinanceFocus = null,
                          personalFinanceObjective = null,
                          personalFinanceApproach = null,
                          personalFinanceStatus = null,
                          accountingFocus = null,
                          accountingObjective = null,
                          accountingBasis = null,
                          accountingStatus = null,
                          businessAccountingFocus = null,
                          businessAccountingObjective = null,
                          businessAccountingApproach = null,
                          businessAccountingStatus = null,
                          taxFocus = null,
                          taxObjective = null,
                          taxContext = null,
                          taxStatus = null,
                          indianTaxFocus = null,
                          indianTaxObjective = null,
                          indianTaxContext = null,
                          indianTaxStatus = null,
                          documentFocus = null,
                          suppliedDocumentDescription = null,
                          documentInterpretationObjective = null,
                          documentStatus = null,
                          safetyFocus = null,
                          verificationBasisDescription = null,
                          safetyInterpretation = null,
                          safetyStatus = null,
                          onBack = {
                              showFinanceInterface = false
                          },
                      )
                  } else if (showResearchInterface) {
                      DevilResearchInterface(
                          researchSubject = null,
                          evidenceSourceReference = null,
                          evidenceSourceKind = null,
                          evidenceDescription = null,
                          sourceAuthenticity = null,
                          sourceTrust = null,
                          sourceFreshness = null,
                          corroborationStatus = null,
                          conflictStatus = null,
                          confidenceStatus = null,
                          synthesisStatus = null,
                          synthesisDescription = null,
                          internetAdmissionStatus = null,
                          internetAnalysisStatus = null,
                          onBack = {
                              showResearchInterface = false
                          },
                      )
                  } else if (showLanguageLearningInterface) {
                      DevilLanguageLearningInterface(
                          languageSessionId =
                              stage318ForeignLanguageAlphaResult?.languageSession?.educationSession?.sessionId?.value,
                          targetLanguage =
                              stage318ForeignLanguageAlphaResult?.languageSession?.targetLanguage,
                          learningObjective =
                              stage318ForeignLanguageAlphaResult?.languageSession?.educationSession?.objective?.objective,
                          spokenEnglishStatus = null,
                          pronunciationStatus = null,
                          listeningStatus = null,
                          grammarStatus = null,
                          vocabularyStatus = null,
                          writingStatus = null,
                          confidenceStatus = null,
                          academicEnglishStatus = null,
                          professionalEnglishStatus = null,
                          curriculumStatus =
                              stage326LanguageCurriculumValidationResult
                                  ?.curriculumPreparation
                                  ?.curriculum
                                  ?.let {
                                      "Curriculum context prepared"
                                  },
                          multilingualTeachingStatus =
                              stage318ForeignLanguageAlphaResult?.multilingualTeaching?.let {
                                  "Teaching context prepared"
                              },
                          multilingualConversationStatus = null,
                          crossLanguageAssistanceStatus = null,
                          progressStatus = null,
                          assessmentStatus = null,
                          spokenEducationStatus = null,
                          onBack = {
                              showLanguageLearningInterface = false
                                showEducationInterface = true
                          },
                      )
                } else if (showEducationInterface) {
                    DevilEducationInterface(
                        educationSessionId =
                            stage325ExtendedEducationTestingResult?.educationAlphaResult?.session?.sessionId?.value,
                        subject =
                            stage325ExtendedEducationTestingResult?.educationAlphaResult?.session?.objective?.subject,
                        educationObjective =
                            stage325ExtendedEducationTestingResult?.educationAlphaResult?.session?.objective?.objective,
                        targetLanguage =
                            null,
                        studyFocus = null,
                        studyApproach = null,
                        learnerSupportObjective = null,
                        progressFocus = null,
                        learnerEvidence = null,
                        progressInterpretation = null,
                        childEducationStatus = null,
                        teachingLevel = null,
                        teachingApproach = null,
                        guardianPolicyStatus = null,
                        privacyBoundaryStatus = null,
                        spokenEducationStatus = null,
                        educationalVisionStatus = null,
                        tabletEducationStatus = null,
                          onLanguageLearningOpen = {
                              stage317SpokenEnglishAlphaResult =
                                  stage316EducationAlphaResult?.session?.let {
                                      educationSession ->
                                      devilApplication
                                          .stage317SpokenEnglishAlphaCoordinator
                                          .prepare(
                                              traceId =
                                                  TraceId.from(
                                                      "stage317-spoken-english-alpha",
                                                  ),
                                              educationSession =
                                                  educationSession,
                                              targetLanguage = "English",
                                              conversationTopic =
                                                  "Daily conversation",
                                              pronunciationTarget =
                                                  "Good morning",
                                          )
                                  }
                                stage318ForeignLanguageAlphaResult =
                                    stage316EducationAlphaResult?.session?.let {
                                        educationSession ->
                                        devilApplication
                                            .stage318ForeignLanguageAlphaCoordinator
                                            .prepare(
                                                traceId =
                                                    TraceId.from(
                                                        "stage318-foreign-language-alpha",
                                                    ),
                                                educationSession =
                                                    educationSession,
                                                targetLanguage = "French",
                                                teachingFocus = "Everyday French",
                                                teachingObjective =
                                                    "Prepare bounded French learning context.",
                                                frenchLearningFocus =
                                                    "Daily expressions",
                                                frenchLearningObjective =
                                                    "Prepare bounded French Alpha specialization.",
                                            )
                                    }

                                stage326LanguageCurriculumValidationResult =
                                    stage318ForeignLanguageAlphaResult?.let {
                                        foreignLanguageAlphaResult ->
                                        devilApplication
                                            .stage326LanguageCurriculumValidationCoordinator
                                            .validate(
                                                traceId =
                                                    TraceId.from(
                                                        "stage326-language-curriculum-validation",
                                                    ),
                                                foreignLanguageAlphaResult =
                                                    foreignLanguageAlphaResult,
                                                curriculumFocus =
                                                    "Everyday French conversation and daily expressions",
                                                adaptationRationale =
                                                    "Owner explicitly selected practical French practice.",
                                                validationFocus =
                                                    "Bounded French curriculum architecture",
                                                validationEvidenceDescription =
                                                    "Existing Stage 318 language and multilingual contexts are preserved.",
                                            )
                                    }

                              showEducationInterface = false
                              showLanguageLearningInterface = true
                          },
                        onBack = {
                            showEducationInterface = false
                        },
                    )
                } else {
                ConversationScreen(
                    onMemoryOpen = {
                        showMemoryInterface = true
                    },
                    onTaskOpen = {
                        showTaskAutomationInterface = true
                    },
                    onSettingsOpen = {
                        showSettingsInterface = true
                    },
                    onSecurityOpen = {
                        showSecurityInterface = true
                    },
                    onFinanceOpen = {
                        showFinanceInterface = true
                    },
                    onResearchOpen = {
                        showResearchInterface = true
                    },
                    onEducationOpen = {
                        stage316EducationAlphaResult =
                            devilApplication.stage316EducationAlphaCoordinator.prepare(
                                traceId = TraceId.from("stage316-education-alpha"),
                                sessionId = EducationSessionId.from("stage316-owner-alpha-session"),
                                subjectIdentityId = IdentityId.from("android-primary-local-subject"),
                                subject = "General Education",
                                objective = "Support bounded owner education alpha testing.",
                            )
                        stage325ExtendedEducationTestingResult =
                            devilApplication
                                .stage325ExtendedEducationTestingCoordinator
                                .validate(
                                    educationAlphaResult =
                                        requireNotNull(stage316EducationAlphaResult),
                                )

                        showEducationInterface = true
                    },
                    state =
                        conversationState,
                    onDraftChange = {
                        updatedDraft ->

                        conversationState =
                            devilApplication
                                .conversationInteractionCoordinator
                                .updateDraft(
                                    state =
                                        conversationState,
                                    draft =
                                        updatedDraft,
                                )

                        voiceInputMessage = null
                        voiceOutputMessage = null
                    },
                    onSubmit = {
                        submitTypedConversationWithStage314OwnerAuthentication()
                    },
                    onVoiceInput = {
                        requestVoiceInput(
                            mode =
                                AndroidVoiceInteractionMode.MANUAL,
                        )
                    },
                      voiceLanguageSelection =
                          voiceLanguageSelection,
                      onVoiceLanguageSelectionChange = {
                          selection ->

                          voiceLanguageSelection =
                              selection

                          (application as DevilApplication)
                              .voiceLanguageSelectionProvider
                              .select(selection)

                          voiceInputMessage = null
                          voiceOutputMessage = null
                      },
                    isVoiceListening =
                        isVoiceListening,
                    voiceInputEnabled =
                        !isVoiceSpeaking &&
                            !handsFreeEnabled,
                    voiceInputMessage =
                        voiceInputMessage,
                    isVoiceSpeaking =
                        isVoiceSpeaking,
                    voiceOutputMessage =
                        voiceOutputMessage,
                    onHandsFreeToggle = {
                        toggleHandsFree()
                    },
                    handsFreeEnabled =
                        handsFreeEnabled,
                    handsFreeMessage =
                        handsFreeMessage,
                      accessibilityDiagnosticMessage =
                        accessibilityDiagnosticMessage,
                )
                }
            }
        }
        voiceInputSource =
            DefaultAndroidVoiceInputSource(
                context = applicationContext,
                recognitionLanguageTagProvider = {
                    voiceLanguagePolicy.recognitionLanguageTag(
                        selection =
                            (application as DevilApplication)
                                .voiceLanguageSelectionProvider
                                .current(),
                        mode =
                            activeVoiceInteractionMode,
                        handsFreeState =
                            handsFreeState,
                    )
                },
            )

        accessibilityDiagnosticSource =
            DefaultAndroidAccessibilityServiceDiagnosticSource(
                context = applicationContext,
            )

        refreshAccessibilityDiagnostic()

    }

    override fun onResume() {
        super.onResume()

        if (::accessibilityDiagnosticSource.isInitialized) {
            refreshAccessibilityDiagnostic()
        }
    }

    private fun refreshAccessibilityDiagnostic() {
        val diagnostic =
            accessibilityDiagnosticSource.diagnose()

        accessibilityDiagnosticMessage =
            if (
                diagnostic.status ==
                AndroidAccessibilityServiceDiagnosticStatus.CONNECTED
            ) {
                null
            } else {
                diagnostic.message
            }
    }

    override fun onDestroy() {
        if (
            ::voiceInputSource
                .isInitialized
        ) {
            voiceInputSource.release()
        }

        val devilApplication =
            application as DevilApplication

        devilApplication
            .voiceConversationOutputCoordinator
            .release()

        devilApplication
            .voiceOutputSource
            .release()

        stage314RealAndroidSubmissionExecutor.shutdown()

        super.onDestroy()
    }

    private fun handleManualVoiceResult(
        result: AndroidVoiceInputResult,
    ) {
        val devilApplication =
            application as DevilApplication

        val previousEntryCount =
            conversationState.entries.size

        val handled =
            devilApplication
                .voiceConversationResultCoordinator
                .handle(
                    state =
                        conversationState,
                    result =
                        result,
                )

        conversationState =
            handled.state

        voiceInputMessage =
            handled.message

        speakNewestRuntimeEntry(
            previousEntryCount =
                previousEntryCount,
            resumeHandsFree = false,
        )
    }

    private fun toggleHandsFree() {
        if (handsFreeEnabled) {
            stopHandsFree()
        } else {
            startHandsFree()
        }
    }

    private fun startHandsFree() {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        val devilApplication =
            application as DevilApplication

        val reset =
            devilApplication
                .handsFreeProductionCoordinator
                .reset()

        handsFreeState =
            reset.state

        handsFreeEnabled = true
        handsFreeMessage =
            "Hands-Free ready."

        requestVoiceInput(
            mode =
                AndroidVoiceInteractionMode.HANDS_FREE,
        )
    }

    private fun stopHandsFree() {
        handsFreeEnabled = false
        pendingVoiceInteractionMode = null
        resumeHandsFreeAfterVoiceOutput = false

        if (
            activeVoiceInteractionMode ==
            AndroidVoiceInteractionMode.HANDS_FREE &&
            ::voiceInputSource.isInitialized
        ) {
            voiceInputSource.cancel()
        }

        activeVoiceInteractionMode = null
        isVoiceListening = false

        val devilApplication =
            application as DevilApplication

        handsFreeState =
            devilApplication
                .handsFreeProductionCoordinator
                .reset()
                .state

        handsFreeMessage =
            "Hands-Free stopped."
    }

    private fun requestVoiceInput(
        mode: AndroidVoiceInteractionMode,
    ) {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        if (
            mode ==
                AndroidVoiceInteractionMode.HANDS_FREE &&
            !handsFreeEnabled
        ) {
            return
        }

        pendingVoiceInteractionMode =
            mode

        if (
            checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            pendingVoiceInteractionMode = null

            startVoiceInput(
                mode = mode,
            )
        } else {
            recordAudioPermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO,
            )
        }
    }

    private fun startVoiceInput(
        mode: AndroidVoiceInteractionMode,
    ) {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        if (
            mode ==
                AndroidVoiceInteractionMode.HANDS_FREE &&
            !handsFreeEnabled
        ) {
            return
        }

        try {
            activeVoiceInteractionMode =
                mode

            isVoiceListening = true

            when (mode) {
                AndroidVoiceInteractionMode.MANUAL ->
                    voiceInputMessage =
                        "Starting voice input…"

                AndroidVoiceInteractionMode.HANDS_FREE ->
                    handsFreeMessage =
                        when (handsFreeState) {
                            HandsFreeConversationState.IDLE ->
                                "Listening for Devil."

                            HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE ->
                                "Listening for Code Red."

                            HandsFreeConversationState.AUTHENTICATION_REQUESTED ->
                                "Authentication is required."

                            HandsFreeConversationState.ACTIVE_SESSION ->
                                "Listening."
                        }
            }

            voiceInputSource.startListening(
                listener =
                    voiceInputListener,
            )
        } catch (
            throwable: RuntimeException,
        ) {
            activeVoiceInteractionMode = null
            isVoiceListening = false

            when (mode) {
                AndroidVoiceInteractionMode.MANUAL ->
                    voiceInputMessage =
                        "Voice input is unavailable."

                AndroidVoiceInteractionMode.HANDS_FREE -> {
                    handsFreeEnabled = false
                    handsFreeState =
                        HandsFreeConversationState.IDLE
                    handsFreeMessage =
                        "Hands-Free voice input is unavailable."
                }
            }
        }
    }

    private fun handleHandsFreeVoiceResult(
        result: AndroidVoiceInputResult,
    ) {
        if (!handsFreeEnabled) {
            return
        }

        when (result.status) {
            AndroidVoiceInputStatus.RECOGNIZED -> {
                val transcript =
                    requireNotNull(
                        result.transcript,
                    )

                val devilApplication =
                    application as DevilApplication

                val productionResult =
                    devilApplication
                        .handsFreeProductionCoordinator
                        .handleRecognizedTranscript(
                            state =
                                handsFreeState,
                            transcript =
                                transcript,
                        )

                handsFreeState =
                    productionResult.state

                handleHandsFreeProductionResult(
                    result =
                        productionResult,
                )
            }

            AndroidVoiceInputStatus.NO_MATCH -> {
                handsFreeMessage =
                    "No speech recognized."

                resumeHandsFreeListening()
            }

            AndroidVoiceInputStatus.CANCELLED -> {
                if (handsFreeEnabled) {
                    handsFreeMessage =
                        "Hands-Free listening cancelled."
                }
            }

            AndroidVoiceInputStatus.FAILED -> {
                handsFreeEnabled = false
                handsFreeState =
                    HandsFreeConversationState.IDLE
                handsFreeMessage =
                    "Hands-Free voice input failed."
            }
        }
    }

    /**
     * Stage 337B owner-authenticated local model-credential provisioning.
     *
     * Android authentication permits only this bounded local storage
     * operation. It does not establish Devil authorization or a Devil
     * session.
     */
    private fun requestStage337bModelCredentialProvisioning(
        credential: String,
    ) {
        if (stage337bModelCredentialAuthenticationInProgress) {
            return
        }

        if (credential.isBlank()) {
            stage337bModelCredentialStatus =
                "Model credential was not stored because it was blank."
            return
        }

        val devilApplication =
            application as DevilApplication

        stage337bModelCredentialAuthenticationInProgress = true
        stage337bModelCredentialStatus =
            "Android owner authentication is required to store the model credential."

        stage314OwnerAuthenticationCoordinator.authenticate(
            promptSubtitle =
                "Confirm Android owner authentication to manage the model credential.",
            onAuthenticated = {
                stage337bModelCredentialAuthenticationInProgress = false

                val stored =
                    devilApplication
                        .conversationalModelCredentialProvisioningCoordinator
                        .provision(
                            credential = credential,
                        )

                stage337bModelCredentialStatus =
                    if (stored) {
                        "Model credential stored in Android-protected local storage."
                    } else {
                        "Model credential could not be stored."
                    }
            },
            onUnavailable = { message ->
                stage337bModelCredentialAuthenticationInProgress = false
                stage337bModelCredentialStatus =
                    "Model credential unchanged: $message"
            },
            onCancelledOrFailed = { message ->
                stage337bModelCredentialAuthenticationInProgress = false
                stage337bModelCredentialStatus =
                    "Model credential unchanged: $message"
            },
        )
    }

    /**
     * Stage 337B owner-authenticated local model-credential removal.
     *
     * Local removal does not revoke a credential at the remote provider.
     */
    private fun requestStage337bModelCredentialRemoval() {
        if (stage337bModelCredentialAuthenticationInProgress) {
            return
        }

        val devilApplication =
            application as DevilApplication

        stage337bModelCredentialAuthenticationInProgress = true
        stage337bModelCredentialStatus =
            "Android owner authentication is required to remove the local model credential."

        stage314OwnerAuthenticationCoordinator.authenticate(
            promptSubtitle =
                "Confirm Android owner authentication to remove the local model credential.",
            onAuthenticated = {
                stage337bModelCredentialAuthenticationInProgress = false

                val cleared =
                    devilApplication
                        .conversationalModelCredentialProvisioningCoordinator
                        .remove()

                stage337bModelCredentialStatus =
                    if (cleared) {
                        "Local model credential storage cleared."
                    } else {
                        "Local model credential storage could not be cleared."
                    }
            },
            onUnavailable = { message ->
                stage337bModelCredentialAuthenticationInProgress = false
                stage337bModelCredentialStatus =
                    "Local model credential unchanged: $message"
            },
            onCancelledOrFailed = { message ->
                stage337bModelCredentialAuthenticationInProgress = false
                stage337bModelCredentialStatus =
                    "Local model credential unchanged: $message"
            },
        )
    }

    private fun submitTypedConversationWithStage314OwnerAuthentication() {
        val normalizedDraft =
            conversationState
                .draft
                .trim()
                .lowercase()

        if (normalizedDraft != "open settings") {
            submitTypedConversationNow()
            return
        }

        if (stage314OwnerAuthenticationInProgress) {
            return
        }

        val devilApplication =
            application as DevilApplication

        val ownerIdentityId =
            IdentityId.from(
                "android-primary-local-subject",
            )

        val currentSession =
            devilApplication
                .stage314OwnerSessionStore
                .current()

        val observedAtMilliseconds =
            System.currentTimeMillis()

        val hasCurrentlyUsableOwnerSession =
            currentSession != null &&
                currentSession.subjectIdentityId == ownerIdentityId &&
                currentSession.state == SessionState.ACTIVE &&
                observedAtMilliseconds >=
                    currentSession.establishedAt.epochMilliseconds &&
                observedAtMilliseconds <
                    currentSession.expiresAt.epochMilliseconds

        /*
         * This local check decides only whether Android authentication must be
         * requested again. It does not grant runtime authority.
         *
         * The submitted action still passes independently through Devil's
         * Session Validity Authority and Authorization Authority.
         *
         * UI_SESSION_CHECK != SESSION_AUTHORITY.
         * SESSION_VALID != AUTHORIZATION.
         */
        if (hasCurrentlyUsableOwnerSession) {
            submitStage314RealAndroidConversationNow()
            return
        }

        /*
         * A missing, expired, revoked, wrong-subject, or otherwise unusable
         * process-local record must not survive into a new authentication
         * attempt.
         */
        devilApplication
            .stage314OwnerSessionStore
            .clear()

        stage314OwnerAuthenticationInProgress = true

        stage314OwnerAuthenticationCoordinator.authenticate(
            onAuthenticated = {
                stage314OwnerAuthenticationInProgress = false

                /*
                 * Stage 314 owner-alpha policy approved for real-device
                 * testing: one successful Android authentication establishes
                 * a bounded 30-minute process-local session.
                 *
                 * No renewal, persistence, Owner Mode, or blanket capability
                 * authorization is established here.
                 */
                devilApplication
                    .stage314OwnerSessionEstablishmentCoordinator
                    .establish(
                        subjectIdentityId =
                            ownerIdentityId,
                        validityDurationMilliseconds =
                            30L * 60L * 1000L,
                    )

                submitStage314RealAndroidConversationNow()
            },
            onUnavailable = { message ->
                stage314OwnerAuthenticationInProgress = false
                voiceOutputMessage = message
            },
            onCancelledOrFailed = { message ->
                stage314OwnerAuthenticationInProgress = false
                voiceOutputMessage = message
            },
        )
    }

    /**
     * Stage 314 bounded real-Android submission boundary.
     *
     * The existing conversation flow and single Unified Devil Runtime execute
     * once on a worker so Android's main thread remains available for platform
     * navigation and accessibility callbacks.
     *
     * The genuine returned ConversationUiState is installed only on the main
     * thread. No TraceId, runtime result, Observation, Verification, or Outcome
     * is created here.
     */
    private fun submitStage314RealAndroidConversationNow() {
        if (stage314RealAndroidSubmissionInProgress) {
            return
        }

        val devilApplication =
            application as DevilApplication

        val stateAtSubmission =
            conversationState

        val previousEntryCount =
            stateAtSubmission.entries.size

        stage314RealAndroidSubmissionInProgress = true

        stage314RealAndroidSubmissionExecutor.execute {
            val submittedState =
                devilApplication
                    .conversationSubmissionFlowCoordinator
                    .submit(
                        state = stateAtSubmission,
                    )

            runOnUiThread {
                stage314RealAndroidSubmissionInProgress = false

                if (isDestroyed) {
                    return@runOnUiThread
                }

                conversationState =
                    submittedState

                voiceInputMessage = null
                voiceOutputMessage = null

                speakNewestRuntimeEntry(
                    previousEntryCount = previousEntryCount,
                    resumeHandsFree = false,
                )
            }
        }
    }

    private fun submitTypedConversationNow() {
        val devilApplication =
            application as DevilApplication

        val previousEntryCount =
            conversationState.entries.size

        conversationState =
            devilApplication
                .conversationSubmissionFlowCoordinator
                .submit(
                    state = conversationState,
                )

        voiceInputMessage = null
        voiceOutputMessage = null

        speakNewestRuntimeEntry(
            previousEntryCount = previousEntryCount,
            resumeHandsFree = false,
        )
    }

    private fun handleHandsFreeProductionResult(
        result: HandsFreeProductionResult,
    ) {
        handsFreeMessage =
            result.message

        when (result.action) {
            HandsFreeProductionAction.NONE ->
                Unit

            HandsFreeProductionAction.LISTEN ->
                resumeHandsFreeListening()

            HandsFreeProductionAction.SPEAK_AND_LISTEN -> {
                val message =
                    requireNotNull(
                        result.message,
                    )

                speakHandsFreeMessage(
                    message = message,
                    resumeListening = true,
                )
            }

            HandsFreeProductionAction.AUTHENTICATION_HANDOFF -> {
                val authenticationResult =
                    requireNotNull(
                        result.authenticationResult,
                    )

                when (authenticationResult.status) {
                    HandsFreeAuthenticationHandoffStatus.UNAVAILABLE -> {
                        handsFreeEnabled = false
                        handsFreeState =
                            HandsFreeConversationState.IDLE

                        speakHandsFreeMessage(
                            message =
                                authenticationResult.message,
                            resumeListening = false,
                        )
                    }

                    HandsFreeAuthenticationHandoffStatus.REQUIRED -> {
                        if (stage315HandsFreeOwnerAuthenticationInProgress) {
                            return
                        }

                        val devilApplication =
                            application as DevilApplication

                        val ownerIdentityId =
                            IdentityId.from(
                                "android-primary-local-subject",
                            )

                        val currentSession =
                            devilApplication
                                .stage314OwnerSessionStore
                                .current()

                        val observedAtMilliseconds =
                            System.currentTimeMillis()

                        val hasCurrentlyUsableOwnerSession =
                            currentSession != null &&
                                currentSession.subjectIdentityId ==
                                    ownerIdentityId &&
                                currentSession.state ==
                                    SessionState.ACTIVE &&
                                observedAtMilliseconds >=
                                    currentSession
                                        .establishedAt
                                        .epochMilliseconds &&
                                observedAtMilliseconds <
                                    currentSession
                                        .expiresAt
                                        .epochMilliseconds

                        /*
                         * This local check decides only whether genuine Android
                         * authentication must be requested again.
                         *
                         * It does not grant runtime authority. Ordinary Voice
                         * conversation still passes through Devil's existing
                         * Session Validity Authority and Authorization Authority.
                         *
                         * UI_SESSION_CHECK != SESSION_AUTHORITY.
                         * SESSION_VALID != AUTHORIZATION.
                         */
                        if (hasCurrentlyUsableOwnerSession) {
                            handsFreeState =
                                HandsFreeConversationState.ACTIVE_SESSION

                            speakHandsFreeMessage(
                                message =
                                    "Authenticated owner session active. Hands-Free active.",
                                resumeListening = true,
                            )

                            return
                        }

                        /*
                         * A missing, expired, wrong-subject, or otherwise
                         * unusable process-local record must not survive into a
                         * fresh authentication attempt.
                         */
                        devilApplication
                            .stage314OwnerSessionStore
                            .clear()

                        /*
                         * Stage 315 reaches this boundary only after the
                         * hands-free state machine requested genuine
                         * authentication.
                         *
                         * Code Red != authentication.
                         * AUTHENTICATION_REQUIRED != AUTHENTICATED.
                         * AUTHENTICATION_REQUESTED != ACTIVE_SESSION.
                         */
                        stage315HandsFreeOwnerAuthenticationInProgress =
                            true

                        stage314OwnerAuthenticationCoordinator.authenticate(
                            onAuthenticated = {
                                stage315HandsFreeOwnerAuthenticationInProgress =
                                    false

                                /*
                                 * Genuine Android authentication success is
                                 * evidence from the platform boundary. It may
                                 * establish the same bounded process-local owner
                                 * session already introduced in Stage 314.
                                 *
                                 * AUTHENTICATION_SUCCESS != SESSION_VALID.
                                 * SESSION_ESTABLISHED != AUTHORIZATION.
                                 */
                                devilApplication
                                    .stage314OwnerSessionEstablishmentCoordinator
                                    .establish(
                                        subjectIdentityId =
                                            ownerIdentityId,
                                        validityDurationMilliseconds =
                                            30L * 60L * 1000L,
                                    )

                                /*
                                 * ACTIVE_SESSION here represents the bounded
                                 * authenticated hands-free conversation state.
                                 * It does not itself grant runtime authority.
                                 *
                                 * ACTIVE_SESSION != AUTHORIZATION.
                                 */
                                handsFreeState =
                                    HandsFreeConversationState.ACTIVE_SESSION

                                speakHandsFreeMessage(
                                    message =
                                        "Owner authentication succeeded. Hands-Free active.",
                                    resumeListening = true,
                                )
                            },
                            onUnavailable = { message ->
                                stage315HandsFreeOwnerAuthenticationInProgress =
                                    false
                                handsFreeEnabled = false
                                handsFreeState =
                                    HandsFreeConversationState.IDLE

                                speakHandsFreeMessage(
                                    message = message,
                                    resumeListening = false,
                                )
                            },
                            onCancelledOrFailed = { message ->
                                stage315HandsFreeOwnerAuthenticationInProgress =
                                    false
                                handsFreeEnabled = false
                                handsFreeState =
                                    HandsFreeConversationState.IDLE

                                speakHandsFreeMessage(
                                    message = message,
                                    resumeListening = false,
                                )
                            },
                        )
                    }
                }
            }

            HandsFreeProductionAction.SUBMIT_CONVERSATION -> {
                val transcript =
                    requireNotNull(
                        result.runtimeTranscript,
                    )

                submitAuthenticatedHandsFreeConversation(
                    transcript =
                        transcript,
                )
            }
        }
    }

    private fun resumeHandsFreeListening() {
        if (
            !handsFreeEnabled ||
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        startVoiceInput(
            mode =
                AndroidVoiceInteractionMode.HANDS_FREE,
        )
    }

    private fun speakHandsFreeMessage(
        message: String,
        resumeListening: Boolean,
    ) {
        val normalizedMessage =
            message.trim()

        require(
            normalizedMessage.isNotEmpty(),
        ) {
            "Hands-free spoken presentation must not be blank."
        }

        if (isVoiceSpeaking) {
            return
        }

        val devilApplication =
            application as DevilApplication

        isVoiceSpeaking = true

        resumeHandsFreeAfterVoiceOutput =
            resumeListening

        voiceOutputMessage =
            "Speaking hands-free status…"

        devilApplication
            .voiceOutputSource
            .speak(
                text =
                    normalizedMessage,
                listener =
                    AndroidVoiceOutputListener {
                        result ->

                        handleVoiceOutputResult(
                            result = result,
                        )
                    },
            )
    }

    private fun submitAuthenticatedHandsFreeConversation(
        transcript: String,
    ) {
        require(
            handsFreeState ==
                HandsFreeConversationState.ACTIVE_SESSION,
        ) {
            "Hands-free conversation submission requires ACTIVE_SESSION."
        }

        val devilApplication =
            application as DevilApplication

        val previousEntryCount =
            conversationState.entries.size

        val handled =
            devilApplication
                .voiceConversationResultCoordinator
                .handle(
                    state =
                        conversationState,
                    result =
                        AndroidVoiceInputResult.recognized(
                            transcript =
                                transcript,
                        ),
                )

        conversationState =
            handled.state

        handsFreeMessage =
            handled.message

        speakNewestRuntimeEntry(
            previousEntryCount =
                previousEntryCount,
            resumeHandsFree = true,
        )
    }

    private fun speakNewestRuntimeEntry(
        previousEntryCount: Int,
        resumeHandsFree: Boolean,
    ) {
        val newEntries =
            conversationState
                .entries
                .drop(
                    previousEntryCount,
                )

        val runtimeEntry =
            newEntries.lastOrNull {
                it.role ==
                    ConversationEntryRole.RUNTIME
            }

        if (runtimeEntry != null) {
            speakRuntimeEntry(
                entry =
                    runtimeEntry,
                resumeHandsFree =
                    resumeHandsFree,
            )
        } else if (
            resumeHandsFree
        ) {
            resumeHandsFreeListening()
        }
    }

    private fun speakRuntimeEntry(
        entry: ConversationTimelineEntry,
        resumeHandsFree: Boolean,
    ) {
        if (isVoiceSpeaking) {
            return
        }

        val devilApplication =
            application as DevilApplication

        isVoiceSpeaking = true

        resumeHandsFreeAfterVoiceOutput =
            resumeHandsFree

        voiceOutputMessage =
            "Speaking runtime status…"

        devilApplication
            .voiceConversationOutputCoordinator
            .speak(
                entry =
                    entry,
                listener =
                    AndroidVoiceOutputListener {
                        result ->

                        handleVoiceOutputResult(
                            result =
                                result,
                        )
                    },
            )
    }

    private fun handleVoiceOutputResult(
        result: AndroidVoiceOutputResult,
    ) {
        isVoiceSpeaking = false

        val shouldResumeHandsFree =
            resumeHandsFreeAfterVoiceOutput

        resumeHandsFreeAfterVoiceOutput =
            false

        voiceOutputMessage =
            when (result.status) {
                AndroidVoiceOutputStatus.SPOKEN ->
                    null

                AndroidVoiceOutputStatus.UNAVAILABLE ->
                    "Voice output is unavailable."

                AndroidVoiceOutputStatus.CANCELLED ->
                    "Voice output cancelled."

                AndroidVoiceOutputStatus.FAILED ->
                    "Voice output failed."
            }

        if (
            shouldResumeHandsFree &&
            handsFreeEnabled &&
            result.status ==
                AndroidVoiceOutputStatus.SPOKEN
        ) {
            resumeHandsFreeListening()
        }
    }
}
