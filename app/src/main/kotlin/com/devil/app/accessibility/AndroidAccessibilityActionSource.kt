package com.devil.app.accessibility

/**
 * Android platform boundary for one explicitly supplied accessibility action.
 *
 * Production callers must reach this source only after the proper
 * constitutional execution gates have already approved the action.
 *
 * This source must never:
 *
 * - infer an action from conversation text;
 * - authenticate a subject;
 * - grant authorization;
 * - select a capability;
 * - approve execution;
 * - reinterpret Android accessibility enablement as Devil authority;
 * - claim observation;
 * - claim verification;
 * - or establish final Outcome.
 *
 * Accessibility enabled != Devil authorization.
 */
fun interface AndroidAccessibilityActionSource {

    fun perform(
        request: AndroidAccessibilityActionRequest,
    ): AndroidAccessibilityActionResult
}
