package com.devil.app.internet

/**
 * Immutable Stage 76 result of one bounded Internet research-analysis step.
 *
 * admission preserves the exact Stage 75 research-admission result that was
 * evaluated.
 *
 * description contains bounded descriptive material only when analysis was
 * constitutionally admitted.
 *
 * The description represents what the admitted external material contains. It
 * does not establish that those contents are true, current, trustworthy, or
 * constitutionally evidenced.
 *
 * ANALYZED
 * != true
 * != trusted
 * != constitutional evidence
 * != World Model representation
 * != Learning
 * != Memory
 * != authorization
 * != execution.
 */
@ConsistentCopyVisibility
data class AndroidInternetResearchAnalysisResult private constructor(
    val status: AndroidInternetResearchAnalysisStatus,
    val admission: AndroidInternetResearchAdmissionResult,
    val description: String?,
) {
    companion object {

        fun create(
            status: AndroidInternetResearchAnalysisStatus,
            admission: AndroidInternetResearchAdmissionResult,
            description: String? = null,
        ): AndroidInternetResearchAnalysisResult {
            val normalizedDescription =
                description
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            when (status) {
                AndroidInternetResearchAnalysisStatus.ANALYZED -> {
                    require(
                        admission.status ==
                            AndroidInternetResearchAdmissionStatus.ADMITTED,
                    ) {
                        "Analyzed Internet research requires Stage 75 admission."
                    }

                    require(normalizedDescription != null) {
                        "Analyzed Internet research requires a nonblank bounded description."
                    }

                    require(
                        admission.safety.retrievedDocument != null,
                    ) {
                        "Analyzed Internet research requires one admitted retrieved document."
                    }
                }

                AndroidInternetResearchAnalysisStatus.NOT_ANALYZED -> {
                    require(
                        admission.status ==
                            AndroidInternetResearchAdmissionStatus.RETRIEVAL_ONLY,
                    ) {
                        "Non-analyzed Internet research must preserve retrieval-only admission."
                    }

                    require(normalizedDescription == null) {
                        "Non-analyzed Internet research must not contain an analysis description."
                    }
                }
            }

            return AndroidInternetResearchAnalysisResult(
                status = status,
                admission = admission,
                description = normalizedDescription,
            )
        }
    }
}
