package com.devil.core.runtime.proactive

/**
 * Stage 80 operational status for bounded proactive-assistance governance.
 *
 * ELIGIBLE_FOR_PRESENTATION means only that:
 *
 * - Stage 79 established trigger eligibility for reconsideration;
 * - one fresh selected constitutional Decision exists;
 * - relevance was explicitly established;
 * - interruption was explicitly justified;
 * - and bounded presentation content exists.
 *
 * It does not mean Android notification delivery, speech, execution,
 * authorization, verified Outcome, or user acknowledgement occurred.
 *
 * DEFERRED means proactive presentation is not currently justified.
 */
enum class ProactiveAssistanceEvaluationStatus {
    ELIGIBLE_FOR_PRESENTATION,
    DEFERRED,
}
