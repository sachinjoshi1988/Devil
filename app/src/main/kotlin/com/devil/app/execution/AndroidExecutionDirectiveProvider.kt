package com.devil.app.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Supplies one already-established typed Android execution directive.
 *
 * The provider is a data-boundary only.
 *
 * It must not infer Android action parameters from summaries, raw user text,
 * capability names, or unrelated runtime state.
 *
 * Returning null means the Android embodiment does not currently possess enough
 * explicit action data to make a justified platform attempt.
 */
fun interface AndroidExecutionDirectiveProvider {

    fun provide(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionDirective?
}
