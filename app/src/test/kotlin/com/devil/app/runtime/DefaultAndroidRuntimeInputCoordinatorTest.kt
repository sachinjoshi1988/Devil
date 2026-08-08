package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidRuntimeInputCoordinatorTest {

    @Test
    fun `submit preserves constitutional classifications through context provider`() {
        var receivedSchemaVersion: SchemaVersion? = null
        var receivedSource: ContextSource? = null
        var receivedTrustLevel: ContextTrustLevel? = null
        var receivedSecurityLevel: ContextSecurityLevel? = null

        val context =
            createContext(
                traceValue =
                    "trace-android-runtime-input-coordinator-001",
            )

        val contextProvider =
            object : AndroidContextEnvelopeProvider {
                override fun provide(
                    schemaVersion: SchemaVersion,
                    source: ContextSource,
                    trustLevel: ContextTrustLevel,
                    securityLevel: ContextSecurityLevel,
                ): ContextEnvelope {
                    receivedSchemaVersion = schemaVersion
                    receivedSource = source
                    receivedTrustLevel = trustLevel
                    receivedSecurityLevel = securityLevel

                    return context
                }
            }

        val coordinator =
            DefaultAndroidRuntimeInputCoordinator(
                contextEnvelopeProvider = contextProvider,
                runtimeGateway =
                    fixedGateway(
                        context = context,
                        status = RuntimeStatus.DEFERRED,
                    ),
            )

        val result = coordinator.submit(
            schemaVersion = SchemaVersion.from(5),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.SENSITIVE,
            content = "Show current status",
        )

        assertEquals(
            5,
            receivedSchemaVersion?.value,
        )
        assertEquals(
            ContextSource.SYSTEM,
            receivedSource,
        )
        assertEquals(
            ContextTrustLevel.VERIFIED,
            receivedTrustLevel,
        )
        assertEquals(
            ContextSecurityLevel.SENSITIVE,
            receivedSecurityLevel,
        )
        assertEquals(
            context.traceId,
            result.traceId,
        )
        assertEquals(
            RuntimeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `submit passes provider context and textual content to one runtime gateway`() {
        val context =
            createContext(
                traceValue =
                    "trace-android-runtime-input-coordinator-002",
            )

        var receivedContext: ContextEnvelope? = null
        var receivedContent: String? = null
        var gatewayCalls = 0

        val gateway =
            object : AndroidRuntimeGateway {
                override fun submit(
                    context: ContextEnvelope,
                    content: String,
                ): RuntimeResult {
                    gatewayCalls += 1
                    receivedContext = context
                    receivedContent = content

                    return RuntimeResult.create(
                        traceId = context.traceId,
                        status = RuntimeStatus.ACCEPTED,
                    )
                }
            }

        val coordinator =
            DefaultAndroidRuntimeInputCoordinator(
                contextEnvelopeProvider =
                    fixedContextProvider(context),
                runtimeGateway = gateway,
            )

        val result = coordinator.submit(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            content = "   Hello Devil   ",
        )

        assertEquals(1, gatewayCalls)
        assertEquals(context, receivedContext)
        assertEquals(
            "   Hello Devil   ",
            receivedContent,
        )
        assertEquals(
            context.traceId,
            result.traceId,
        )
        assertEquals(
            RuntimeStatus.ACCEPTED,
            result.status,
        )
    }

    @Test
    fun `submit requests exactly one context per invocation`() {
        var providerCalls = 0

        val provider =
            object : AndroidContextEnvelopeProvider {
                override fun provide(
                    schemaVersion: SchemaVersion,
                    source: ContextSource,
                    trustLevel: ContextTrustLevel,
                    securityLevel: ContextSecurityLevel,
                ): ContextEnvelope {
                    providerCalls += 1

                    return createContext(
                        traceValue =
                            "trace-android-runtime-input-coordinator-$providerCalls",
                    )
                }
            }

        val gateway =
            object : AndroidRuntimeGateway {
                override fun submit(
                    context: ContextEnvelope,
                    content: String,
                ): RuntimeResult {
                    return RuntimeResult.create(
                        traceId = context.traceId,
                        status = RuntimeStatus.DEFERRED,
                    )
                }
            }

        val coordinator =
            DefaultAndroidRuntimeInputCoordinator(
                contextEnvelopeProvider = provider,
                runtimeGateway = gateway,
            )

        coordinator.submit(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            content = "First input",
        )

        coordinator.submit(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            content = "Second input",
        )

        assertEquals(2, providerCalls)
    }

    @Test
    fun `submit rejects runtime result from another trace`() {
        val context =
            createContext(
                traceValue =
                    "trace-android-runtime-input-coordinator-003",
            )

        val gateway =
            object : AndroidRuntimeGateway {
                override fun submit(
                    context: ContextEnvelope,
                    content: String,
                ): RuntimeResult {
                    return RuntimeResult.create(
                        traceId = TraceId.from(
                            "trace-android-runtime-input-coordinator-other",
                        ),
                        status = RuntimeStatus.DEFERRED,
                    )
                }
            }

        val coordinator =
            DefaultAndroidRuntimeInputCoordinator(
                contextEnvelopeProvider =
                    fixedContextProvider(context),
                runtimeGateway = gateway,
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.submit(
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.UNVERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                content = "Hello Devil",
            )
        }
    }

    private fun fixedContextProvider(
        context: ContextEnvelope,
    ): AndroidContextEnvelopeProvider {
        return object : AndroidContextEnvelopeProvider {
            override fun provide(
                schemaVersion: SchemaVersion,
                source: ContextSource,
                trustLevel: ContextTrustLevel,
                securityLevel: ContextSecurityLevel,
            ): ContextEnvelope {
                return context
            }
        }
    }

    private fun fixedGateway(
        context: ContextEnvelope,
        status: RuntimeStatus,
    ): AndroidRuntimeGateway {
        return object : AndroidRuntimeGateway {
            override fun submit(
                context: ContextEnvelope,
                content: String,
            ): RuntimeResult {
                return RuntimeResult.create(
                    traceId = context.traceId,
                    status = status,
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
                    1_754_000_190_000L,
                ),
        )
    }
}
