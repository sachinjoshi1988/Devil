package com.devil.core.runtime.outcome

import com.devil.core.runtime.verification.VerificationEvidenceResult
import com.devil.core.runtime.verification.VerificationResult

/**
 * Neutral outcome-embodiment port between constitutional Verification and
 * constitutional Outcome.
 *
 * The core runtime may approach this port only with the genuine
 * VerificationResult produced by the Verification Authority together with the
 * genuine VerificationEvidenceResult from which that verification was derived.
 *
 * Implementations may obtain bounded outcome evidence only through authorized
 * embodiment-specific mechanisms.
 *
 * This port grants no authority of its own.
 *
 * VerificationStatus.VERIFIED is necessary for outcome evidence but does not
 * itself establish outcome evidence or a constitutional Outcome.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Outcome Authority, or runtime.
 */
fun interface OutcomeEvidencePort {

    fun establish(
        verification: VerificationResult,
        verificationEvidence: VerificationEvidenceResult,
    ): OutcomeEvidenceResult
}
