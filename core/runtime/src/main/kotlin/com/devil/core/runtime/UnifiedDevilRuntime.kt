package com.devil.core.runtime

import com.devil.core.model.context.ContextEnvelope

/**
 * Constitutional entry point into the Devil runtime.
 *
 * The runtime accepts validated context and coordinates future execution.
 * This interface performs no execution itself.
 */
interface UnifiedDevilRuntime {

    fun accept(
        context: ContextEnvelope,
    )
}
