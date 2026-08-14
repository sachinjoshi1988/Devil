package com.devil.app.internet

/**
 * Stage 75 boundary deciding whether one already-assessed Internet retrieval may
 * approach later bounded research analysis.
 *
 * This policy consumes only the explicit result of the existing Internet
 * structural-safety boundary.
 *
 * It does not inspect external prose to infer:
 *
 * - truth;
 * - trust;
 * - intent;
 * - authority;
 * - importance;
 * - urgency;
 * - commands;
 * - memory value;
 * - or execution eligibility.
 *
 * Only material explicitly marked ELIGIBLE_FOR_LATER_ANALYSIS by the existing
 * Internet safety policy may receive ADMITTED status.
 *
 * External Internet content remains untrusted data after admission.
 */
class AndroidInternetResearchAdmissionPolicy {

    fun evaluate(
        safety: AndroidInternetKnowledgeSafetyResult,
    ): AndroidInternetResearchAdmissionResult {
        val status =
            when (safety.disposition) {
                AndroidInternetKnowledgeContentDisposition
                    .ELIGIBLE_FOR_LATER_ANALYSIS ->
                    AndroidInternetResearchAdmissionStatus.ADMITTED

                AndroidInternetKnowledgeContentDisposition
                    .RETRIEVAL_ONLY ->
                    AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY
            }

        return AndroidInternetResearchAdmissionResult.create(
            status = status,
            safety = safety,
        )
    }
}
