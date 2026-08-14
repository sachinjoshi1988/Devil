package com.devil.app.internet

/**
 * Stage 75 coordinator for bounded Internet research admission.
 *
 * Flow:
 *
 * AndroidInternetKnowledgeSafetyResult
 * -> AndroidInternetResearchAdmissionPolicy
 * -> AndroidInternetResearchAdmissionResult.
 *
 * This coordinator does not retrieve Internet content, create another Brain,
 * invoke UnifiedDevilRuntime, create ConversationInput, establish truth or
 * trust, authorize actions, perform Learning, propose or persist Memory, or
 * execute anything.
 */
class AndroidInternetResearchAdmissionCoordinator(
    private val policy: AndroidInternetResearchAdmissionPolicy =
        AndroidInternetResearchAdmissionPolicy(),
) {

    fun evaluate(
        safety: AndroidInternetKnowledgeSafetyResult,
    ): AndroidInternetResearchAdmissionResult {
        return policy.evaluate(
            safety = safety,
        )
    }
}
