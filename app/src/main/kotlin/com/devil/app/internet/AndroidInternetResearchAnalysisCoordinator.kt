package com.devil.app.internet

/**
 * Stage 76 coordinator for bounded descriptive Internet research analysis.
 *
 * Flow:
 *
 * AndroidInternetResearchAdmissionResult
 * -> AndroidInternetResearchAnalysisPolicy
 * -> AndroidInternetResearchAnalysisResult.
 *
 * The coordinator does not retrieve Internet content, create another Brain,
 * invoke UnifiedDevilRuntime, create ConversationInput, establish truth or
 * trust, create constitutional evidence, mutate World Model state, perform
 * Learning, propose or persist Memory, authorize actions, or execute anything.
 */
class AndroidInternetResearchAnalysisCoordinator(
    private val policy: AndroidInternetResearchAnalysisPolicy =
        AndroidInternetResearchAnalysisPolicy(),
) {

    fun analyze(
        admission: AndroidInternetResearchAdmissionResult,
    ): AndroidInternetResearchAnalysisResult {
        return policy.analyze(
            admission = admission,
        )
    }
}
