package com.devil.app.internet

/**
 * Stage 76 bounded descriptive Internet research-analysis policy.
 *
 * This policy may analyze only one explicit Stage 75
 * AndroidInternetResearchAdmissionResult.
 *
 * ADMITTED material may produce one bounded description of the already
 * retrieved external document.
 *
 * RETRIEVAL_ONLY material must remain NOT_ANALYZED.
 *
 * This policy does not:
 *
 * - establish factual truth;
 * - establish source authenticity;
 * - establish factual freshness;
 * - assign source trust;
 * - reinterpret external prose as Devil instructions;
 * - create constitutional evidence;
 * - create or mutate World Model state;
 * - perform Learning;
 * - propose, commit, or persist Memory;
 * - grant authorization;
 * - select or execute capabilities;
 * - or establish verified Outcome.
 *
 * External Internet content remains untrusted data after analysis.
 */
class AndroidInternetResearchAnalysisPolicy {

    fun analyze(
        admission: AndroidInternetResearchAdmissionResult,
    ): AndroidInternetResearchAnalysisResult {
        return when (admission.status) {
            AndroidInternetResearchAdmissionStatus.ADMITTED -> {
                val document =
                    requireNotNull(
                        admission.safety.retrievedDocument,
                    ) {
                        "Admitted Internet research must preserve one retrieved document before analysis."
                    }

                AndroidInternetResearchAnalysisResult.create(
                    status =
                        AndroidInternetResearchAnalysisStatus.ANALYZED,
                    admission = admission,
                    description = document.content,
                )
            }

            AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY ->
                AndroidInternetResearchAnalysisResult.create(
                    status =
                        AndroidInternetResearchAnalysisStatus.NOT_ANALYZED,
                    admission = admission,
                )
        }
    }
}
