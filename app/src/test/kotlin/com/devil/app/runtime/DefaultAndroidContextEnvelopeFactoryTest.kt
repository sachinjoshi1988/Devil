package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidContextEnvelopeFactoryTest {

    @Test
    fun `create preserves all supplied constitutional context values`() {
        val traceId = TraceId.from(
            "trace-android-context-envelope-001",
        )
        val schemaVersion = SchemaVersion.from(1)
        val observedAt =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_189_000L,
            )

        val context =
            DefaultAndroidContextEnvelopeFactory().create(
                traceId = traceId,
                schemaVersion = schemaVersion,
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.UNVERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = observedAt,
            )

        assertEquals(traceId, context.traceId)
        assertEquals(schemaVersion, context.schemaVersion)
        assertEquals(ContextSource.TEXT, context.source)
        assertEquals(
            ContextTrustLevel.UNVERIFIED,
            context.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.RESTRICTED,
            context.securityLevel,
        )
        assertEquals(observedAt, context.observedAt)
    }

    @Test
    fun `create does not replace supplied trust or security classification`() {
        val context =
            DefaultAndroidContextEnvelopeFactory().create(
                traceId = TraceId.from(
                    "trace-android-context-envelope-002",
                ),
                schemaVersion = SchemaVersion.from(3),
                source = ContextSource.SYSTEM,
                trustLevel = ContextTrustLevel.TRUSTED,
                securityLevel = ContextSecurityLevel.SENSITIVE,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_189_500L,
                    ),
            )

        assertEquals(ContextSource.SYSTEM, context.source)
        assertEquals(
            ContextTrustLevel.TRUSTED,
            context.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.SENSITIVE,
            context.securityLevel,
        )
        assertEquals(3, context.schemaVersion.value)
    }

    @Test
    fun `create preserves supplied trace and observation time exactly`() {
        val traceId = TraceId.from(
            "trace-android-context-envelope-003",
        )
        val observedAt =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_190_000L,
            )

        val context =
            DefaultAndroidContextEnvelopeFactory().create(
                traceId = traceId,
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.UNVERIFIED,
                securityLevel = ContextSecurityLevel.PUBLIC,
                observedAt = observedAt,
            )

        assertEquals(traceId, context.traceId)
        assertEquals(observedAt, context.observedAt)
        assertEquals(
            1_754_000_190_000L,
            context.observedAt.epochMilliseconds,
        )
    }
}
