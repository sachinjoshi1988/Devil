package com.devil.core.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultUnifiedDevilRuntimeTest {

    @Test
    fun `accept returns a structured deferred result with the same trace`() {
        val context = createContext()
        val runtime: UnifiedDevilRuntime = DefaultUnifiedDevilRuntime()

        val result = runtime.accept(context)

        assertEquals(context.traceId, result.traceId)
        assertEquals(RuntimeStatus.DEFERRED, result.status)
        assertNull(result.error)
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from("trace-runtime-skeleton-001"),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_004_000L),
        )
    }
}
