package com.devil.core.runtime.security

import com.devil.core.model.security.SecurityTransitionRequest

/**
 * Coordinates one bounded constitutional security-transition evaluation.
 *
 * The Security Transition Authority delegates transition evaluation to one
 * SecurityTransitionEvaluator and maps that bounded evaluation through one
 * SecurityTransitionResultMapper.
 *
 * It does not advance or mutate security state, authenticate a subject, prove
 * owner identity, establish trust, grant authorization, create or validate a
 * session, enter Owner Mode, approve high-security confirmation, grant Android
 * permission, or permit execution.
 *
 * An APPROVED SecurityTransitionResult means only that genuine constitutional
 * transition eligibility was established for later controlled security-state
 * handling.
 */
interface SecurityTransitionAuthority {

    fun evaluateTransition(
        request: SecurityTransitionRequest,
    ): SecurityTransitionResult
}
