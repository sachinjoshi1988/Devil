package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.LanguageEducationSessionRecord

/**
 * Stable Stage 120 result of bounded Language Education foundation preparation.
 *
 * PREPARED requires one LanguageEducationSessionRecord.
 * DEFERRED must not contain one.
 *
 * This result creates no Brain, Decision, Task, Plan, capability authority,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, persistence authority, curriculum, lesson, assessment,
 * or verified learner progress.
 */
@ConsistentCopyVisibility
data class LanguageEducationFoundationResult private constructor(
    val traceId: TraceId,
    val status: LanguageEducationFoundationStatus,
    val languageSession: LanguageEducationSessionRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: LanguageEducationFoundationStatus,
            languageSession: LanguageEducationSessionRecord? = null,
        ): LanguageEducationFoundationResult {
            when (status) {
                LanguageEducationFoundationStatus.PREPARED -> {
                    require(languageSession != null) {
                        "Prepared Language Education results require one language session."
                    }
                }

                LanguageEducationFoundationStatus.DEFERRED -> {
                    require(languageSession == null) {
                        "Deferred Language Education results must not contain a language session."
                    }
                }
            }

            return LanguageEducationFoundationResult(
                traceId = traceId,
                status = status,
                languageSession = languageSession,
            )
        }
    }
}
