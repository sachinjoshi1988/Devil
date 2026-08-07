package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus
import com.devil.core.runtime.UnifiedDevilRuntime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidRuntimeGatewayTest {

    @Test
    fun `submit adapts Android text and sends it through one unified runtime`() {
        val context = createContext(
            "trace-android-runtime-gateway-001",
        )
        var receivedInput: ConversationInput? = null

        val runtime = object : UnifiedDevilRuntime {
            override fun accept(
                input: ConversationInput,
            ): RuntimeResult {
                receivedInput = input

                return RuntimeResult.create(
                    traceId = input.context.traceId,
                    status = RuntimeStatus.DEFERRED,
                )
            }
        }

        val gateway: AndroidRuntimeGateway =
            DefaultAndroidRuntimeGateway(
                runtime = runtime,
            )

        val result = gateway.submit(
            context = context,
            content = "   Hello Devil   ",
        )

        assertEquals(context, receivedInput?.context)
        assertEquals("Hello Devil", receivedInput?.content)
        assertEquals(context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    @Test
    fun `submit preserves supplied constitutional context classification`() {
        val context = createContext(
            "trace-android-runtime-gateway-002",
        )
        var receivedInput: ConversationInput? = null

        val runtime = object : UnifiedDevilRuntime {
            override fun accept(
                input: ConversationInput,
            ): RuntimeResult {
                receivedInput = input

                return RuntimeResult.create(
                    traceId = input.context.traceId,
                    status = RuntimeStatus.ACCEPTED,
                )
            }
        }

        DefaultAndroidRuntimeGateway(
            runtime = runtime,
        ).submit(
            context = context,
            content = "Show current status",
        )

        assertEquals(
            ContextSource.TEXT,
            receivedInput?.context?.source,
        )
        assertEquals(
            ContextTrustLevel.UNVERIFIED,
            receivedInput?.context?.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.RESTRICTED,
            receivedInput?.context?.securityLevel,
        )
        assertEquals(
            1_754_000_188_000L,
            receivedInput?.context?.observedAt?.epochMilliseconds,
        )
    }

    @Test
    fun `submit preserves existing conversation input validation`() {
        val gateway = DefaultAndroidRuntimeGateway(
            runtime = deferredRuntime(),
        )

        assertFailsWith<IllegalArgumentException> {
            gateway.submit(
                context = createContext(
                    "trace-android-runtime-gateway-003",
                ),
                content = "   ",
            )
        }
    }

    @Test
    fun `submit rejects runtime result from another trace`() {
        val context = createContext(
            "trace-android-runtime-gateway-004",
        )

        val runtime = object : UnifiedDevilRuntime {
            override fun accept(
                input: ConversationInput,
            ): RuntimeResult {
                return RuntimeResult.create(
                    traceId = TraceId.from(
                        "trace-android-runtime-gateway-other",
                    ),
                    status = RuntimeStatus.DEFERRED,
                )
            }
        }

        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidRuntimeGateway(
                runtime = runtime,
            ).submit(
                context = context,
                content = "Hello Devil",
            )
        }
    }

    private fun deferredRuntime(): UnifiedDevilRuntime {
        return object : UnifiedDevilRuntime {
            override fun accept(
                input: ConversationInput,
            ): RuntimeResult {
                return RuntimeResult.create(
                    traceId = input.context.traceId,
                    status = RuntimeStatus.DEFERRED,
                )
            }
        }
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_188_000L,
                ),
        )
    }
}
