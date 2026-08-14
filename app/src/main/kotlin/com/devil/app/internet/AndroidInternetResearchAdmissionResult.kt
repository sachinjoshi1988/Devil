package com.devil.app.internet

/**
 * Immutable Stage 75 result of one bounded Internet research-admission
 * evaluation.
 *
 * The result preserves the exact Stage 42/74 safety result that was evaluated.
 *
 * It does not copy, reinterpret, summarize, rank, trust, verify, persist, or
 * execute the external content.
 *
 * ADMITTED
 * != true
 * != trusted
 * != constitutional evidence
 * != Learning
 * != Memory
 * != authorization
 * != execution.
 */
@ConsistentCopyVisibility
data class AndroidInternetResearchAdmissionResult private constructor(
    val status: AndroidInternetResearchAdmissionStatus,
    val safety: AndroidInternetKnowledgeSafetyResult,
) {
    companion object {

        fun create(
            status: AndroidInternetResearchAdmissionStatus,
            safety: AndroidInternetKnowledgeSafetyResult,
        ): AndroidInternetResearchAdmissionResult {
            when (status) {
                AndroidInternetResearchAdmissionStatus.ADMITTED -> {
                    require(
                        safety.retrievalStatus ==
                            AndroidInternetKnowledgeStatus.AVAILABLE,
                    ) {
                        "Admitted Internet research requires an AVAILABLE retrieval."
                    }

                    require(
                        safety.disposition ==
                            AndroidInternetKnowledgeContentDisposition
                                .ELIGIBLE_FOR_LATER_ANALYSIS,
                    ) {
                        "Admitted Internet research requires explicit later-analysis eligibility."
                    }

                    require(safety.retrievedDocument != null) {
                        "Admitted Internet research requires one retrieved document."
                    }
                }

                AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY -> {
                    require(
                        safety.disposition ==
                            AndroidInternetKnowledgeContentDisposition
                                .RETRIEVAL_ONLY,
                    ) {
                        "Retrieval-only Internet research admission must preserve retrieval-only safety disposition."
                    }
                }
            }

            return AndroidInternetResearchAdmissionResult(
                status = status,
                safety = safety,
            )
        }
    }
}
