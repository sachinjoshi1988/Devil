package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidContextEnvelopeProviderTest {

    @Test
    fun `provide composes fresh trace and observation time with supplied constitutional classification`() {
        val provider: AndroidContextEnvelopeProvider =
            DefaultAndroidContextEnvelopeProvider(
                traceIdProvider =
                    fixedTraceIdProvider(
                        "trace-android-context-provider-001",
                    ),
                observationTimeProvider =
                    fixedObservationTimeProvider(
                        1_754_000_189_000L,
                    ),
            )

        val context = provider.provide(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
        )

        assertEquals(
            "trace-android-context-provider-001",
            context.traceId.value,
        )
        assertEquals(
            1,
            context.schemaVersion.value,
        )
        assertEquals(
            ContextSource.TEXT,
            context.source,
        )
        assertEquals(
            ContextTrustLevel.UNVERIFIED,
            context.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.RESTRICTED,
            context.securityLevel,
        )
        assertEquals(
            1_754_000_189_000L,
            context.observedAt.epochMilliseconds,
        )
    }

    @Test
    fun `provide does not replace supplied constitutional classification`() {
        val provider =
            DefaultAndroidContextEnvelopeProvider(
                traceIdProvider =
                    fixedTraceIdProvider(
                        "trace-android-context-provider-002",
                    ),
                observationTimeProvider =
                    fixedObservationTimeProvider(
                        1_754_000_189_100L,
                    ),
            )

        val context = provider.provide(
            schemaVersion = SchemaVersion.from(7),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.TRUSTED,
            securityLevel = ContextSecurityLevel.SENSITIVE,
        )

        assertEquals(
            7,
            context.schemaVersion.value,
        )
        assertEquals(
            ContextSource.SYSTEM,
            context.source,
        )
        assertEquals(
            ContextTrustLevel.TRUSTED,
            context.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.SENSITIVE,
            context.securityLevel,
        )
    }

    @Test
    fun `provide requests one trace and one observation timestamp per context`() {
        var traceCalls = 0
        var clockCalls = 0

        val traceProvider =
            object : AndroidTraceIdProvider {
                override fun provide(): TraceId {
                    traceCalls += 1

                    return TraceId.from(
                        "trace-android-context-provider-$traceCalls",
                    )
                }
            }

        val timeProvider =
            object : AndroidObservationTimeProvider {
                override fun observe(): DevilTimestamp {
                    clockCalls += 1

                    return DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_189_000L + clockCalls,
                    )
                }
            }

        val provider =
            DefaultAndroidContextEnvelopeProvider(
                traceIdProvider = traceProvider,
                observationTimeProvider = timeProvider,
            )

        val first = provider.provide(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
        )

        val second = provider.provide(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
        )

        assertEquals(2, traceCalls)
        assertEquals(2, clockCalls)

        assertEquals(
            "trace-android-context-provider-1",
            first.traceId.value,
        )
        assertEquals(
            "trace-android-context-provider-2",
            second.traceId.value,
        )

        assertEquals(
            1_754_000_189_001L,
            first.observedAt.epochMilliseconds,
        )
        assertEquals(
            1_754_000_189_002L,
            second.observedAt.epochMilliseconds,
        )
    }

    @Test
    fun `provide delegates final envelope construction to supplied factory`() {
        val expectedTraceId =
            TraceId.from(
                "trace-android-context-provider-003",
            )
        val expectedTime =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_189_200L,
            )
        val expectedSchema =
            SchemaVersion.from(3)

        var receivedTraceId: TraceId? = null
        var receivedSchemaVersion: SchemaVersion? = null
        var receivedSource: ContextSource? = null
        var receivedTrustLevel: ContextTrustLevel? = null
        var receivedSecurityLevel: ContextSecurityLevel? = null
        var receivedObservedAt: DevilTimestamp? = null

        val factory =
            object : AndroidContextEnvelopeFactory {
                override fun create(
                    traceId: TraceId,
                    schemaVersion: SchemaVersion,
                    source: ContextSource,
                    trustLevel: ContextTrustLevel,
                    securityLevel: ContextSecurityLevel,
                    observedAt: DevilTimestamp,
                ): ContextEnvelope {
                    receivedTraceId = traceId
                    receivedSchemaVersion = schemaVersion
                    receivedSource = source
                    receivedTrustLevel = trustLevel
                    receivedSecurityLevel = securityLevel
                    receivedObservedAt = observedAt

                    return ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = schemaVersion,
                        source = source,
                        trustLevel = trustLevel,
                        securityLevel = securityLevel,
                        observedAt = observedAt,
                    )
                }
            }

        DefaultAndroidContextEnvelopeProvider(
            traceIdProvider =
                object : AndroidTraceIdProvider {
                    override fun provide(): TraceId =
                        expectedTraceId
                },
            observationTimeProvider =
                object : AndroidObservationTimeProvider {
                    override fun observe(): DevilTimestamp =
                        expectedTime
                },
            contextEnvelopeFactory = factory,
        ).provide(
            schemaVersion = expectedSchema,
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.PUBLIC,
        )

        assertEquals(expectedTraceId, receivedTraceId)
        assertEquals(expectedSchema, receivedSchemaVersion)
        assertEquals(ContextSource.TEXT, receivedSource)
        assertEquals(
            ContextTrustLevel.VERIFIED,
            receivedTrustLevel,
        )
        assertEquals(
            ContextSecurityLevel.PUBLIC,
            receivedSecurityLevel,
        )
        assertEquals(expectedTime, receivedObservedAt)
    }

    private fun fixedTraceIdProvider(
        value: String,
    ): AndroidTraceIdProvider {
        return object : AndroidTraceIdProvider {
            override fun provide(): TraceId {
                return TraceId.from(value)
            }
        }
    }

    private fun fixedObservationTimeProvider(
        value: Long,
    ): AndroidObservationTimeProvider {
        return object : AndroidObservationTimeProvider {
            override fun observe(): DevilTimestamp {
                return DevilTimestamp.fromEpochMilliseconds(value)
            }
        }
    }
}
