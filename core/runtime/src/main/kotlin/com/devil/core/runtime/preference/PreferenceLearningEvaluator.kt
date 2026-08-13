package com.devil.core.runtime.preference

import com.devil.core.model.preference.PreferenceEvidenceSet

/**
 * Pure bounded evaluator for accumulated preference evidence.
 *
 * This evaluator grants no constitutional authority and performs no Learning,
 * Memory Proposal, Memory Authority, Memory Commitment, Memory Persistence,
 * execution, communication, or world-state mutation.
 */
fun interface PreferenceLearningEvaluator {

    fun evaluate(
        evidenceSet: PreferenceEvidenceSet,
        criteria: PreferenceLearningCriteria,
    ): PreferenceLearningResult
}
