package com.devil.app.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Default Stage 38 Android execution-directive provider.
 *
 * The current constitutional runtime does not yet carry a structured
 * accessibility target through Understanding -> Decision -> Task -> Plan ->
 * Capability -> Execution.
 *
 * Therefore the production default returns null rather than:
 *
 * - parsing a target from conversation text;
 * - parsing a target from summaries;
 * - inventing an accessibility target;
 * - selecting an Android action;
 * - or bypassing constitutional planning.
 *
 * Missing directive data therefore remains DEFERRED at execution time.
 */
class DefaultAndroidExecutionDirectiveProvider :
    AndroidExecutionDirectiveProvider {

    override fun provide(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionDirective? {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Android execution directive request and constitutional execution request must use the same trace identity."
        }

        return null
    }
}
