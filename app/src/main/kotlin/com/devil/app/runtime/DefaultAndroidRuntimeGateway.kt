package com.devil.app.runtime

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.UnifiedDevilRuntime

/**
 * Default bounded Android gateway into the single Unified Devil Runtime.
 *
 * Input adaptation remains separate from runtime coordination. This class
 * therefore delegates ConversationInput construction to
 * AndroidConversationInputAdapter and delegates all constitutional runtime
 * processing to the supplied UnifiedDevilRuntime.
 *
 * It creates no independent brain, planner, memory authority, security
 * authority, execution path, or Android capability path.
 */
class DefaultAndroidRuntimeGateway(
    private val runtime: UnifiedDevilRuntime,
    private val inputAdapter: AndroidConversationInputAdapter =
        DefaultAndroidConversationInputAdapter(),
) : AndroidRuntimeGateway {

    override fun submit(
        context: ContextEnvelope,
        content: String,
    ): RuntimeResult {
        val input = inputAdapter.adapt(
            context = context,
            content = content,
        )

        require(input.context.traceId == context.traceId) {
            "Android runtime gateway context and adapted input must use the same trace identity."
        }

        val result = runtime.accept(input)

        require(result.traceId == context.traceId) {
            "Android runtime gateway context and runtime result must use the same trace identity."
        }

        return result
    }
}
