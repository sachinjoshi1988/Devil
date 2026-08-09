package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationResult

/**
 * Android embodiment boundary for outcome determination after Stage 32
 * verification.
 *
 * Only a genuine VERIFIED result may approach the bounded Android outcome
 * source.
 *
 * DEFERRED verification produces no outcome attempt.
 *
 * FAILED verification preserves its matching operational failure.
 *
 * Verified != Outcome Established != Completed.
 */
fun interface AndroidOutcomeAdapter {

    fun establish(
        verification: AndroidVerificationResult,
    ): AndroidOutcomeResult
}
