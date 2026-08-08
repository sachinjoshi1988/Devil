package com.devil.app.conversation

import com.devil.app.runtime.AndroidRuntimeInputCoordinator
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultConversationRuntimeSubmissionCoordinatorTest {

    @Test
    fun `unavailable metadata prevents runtime submission`() {
        var runtimeCalls = 0

        val runtimeCoordinator =
            object : AndroidRuntimeInputCoordinator {
                override fun submit(
                    schemaVersion: SchemaVersion,
                    source: ContextSource,
                    trustLevel: ContextTrustLevel,
                    securityLevel: ContextSecurityLevel,
                    content: String,
                ): RuntimeResult {
                    runtimeCalls += 1

                    error("Runtime must not be called.")
                }
            }

        val coordinator =
            DefaultConversationRuntimeSubmissionCoordinator(
                metadataProvider =
                    DefaultConversationRuntimeInputMetadataProvider(),
                runtimeInputCoordinator = runtimeCoordinator,
            )

        val result =
            coordinator.submit(
                content = "Hello Devil",
            )

        assertEquals(
            ConversationRuntimeSubmissionStatus.METADATA_UNAVAILABLE,
            result.status,
        )
        assertNull(result.presentation)
        assertEquals(0, runtimeCalls)
    }

    @Test
    fun `available metadata is preserved into exactly one runtime submission`() {
        val metadata =
            ConversationRuntimeInputMetadata(
                schemaVersion = SchemaVersion.from(3),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
            )

        var runtimeCalls = 0
        var receivedSchemaVersion: SchemaVersion? = null
        var receivedSource: ContextSource? = null
        var receivedTrustLevel: ContextTrustLevel? = null
        var receivedSecurityLevel: ContextSecurityLevel? = null
        var receivedContent: String? = null

        val traceId =
            TraceId.from(
                "trace-conversation-runtime-submission-001",
            )

        val coordinator =
            DefaultConversationRuntimeSubmissionCoordinator(
                metadataProvider =
                    fixedMetadataProvider(metadata),
                runtimeInputCoordinator =
                    object : AndroidRuntimeInputCoordinator {
                        override fun submit(
                            schemaVersion: SchemaVersion,
                            source: ContextSource,
                            trustLevel: ContextTrustLevel,
                            securityLevel: ContextSecurityLevel,
                            content: String,
                        ): RuntimeResult {
                            runtimeCalls += 1
                            receivedSchemaVersion = schemaVersion
                            receivedSource = source
                            receivedTrustLevel = trustLevel
                            receivedSecurityLevel = securityLevel
                            receivedContent = content

                            return RuntimeResult.create(
                                traceId = traceId,
                                status = RuntimeStatus.ACCEPTED,
                            )
                        }
                    },
            )

        val result =
            coordinator.submit(
                content = "  Hello Devil  ",
            )

        assertEquals(1, runtimeCalls)
        assertEquals(metadata.schemaVersion, receivedSchemaVersion)
        assertEquals(metadata.source, receivedSource)
        assertEquals(metadata.trustLevel, receivedTrustLevel)
        assertEquals(metadata.securityLevel, receivedSecurityLevel)
        assertEquals("Hello Devil", receivedContent)

        assertEquals(
            ConversationRuntimeSubmissionStatus.SUBMITTED,
            result.status,
        )

        val presentation = requireNotNull(result.presentation)

        assertEquals(traceId, presentation.traceId)
        assertEquals(
            ConversationRuntimePresentationStatus.ACCEPTED,
            presentation.status,
        )
        assertEquals(
            "Accepted for constitutional processing.",
            presentation.message,
        )
    }

    @Test
    fun `runtime deferred result remains deferred presentation`() {
        val traceId =
            TraceId.from(
                "trace-conversation-runtime-submission-002",
            )

        val coordinator =
            DefaultConversationRuntimeSubmissionCoordinator(
                metadataProvider =
                    fixedMetadataProvider(
                        standardMetadata(),
                    ),
                runtimeInputCoordinator =
                    fixedRuntimeCoordinator(
                        RuntimeResult.create(
                            traceId = traceId,
                            status = RuntimeStatus.DEFERRED,
                        ),
                    ),
            )

        val result =
            coordinator.submit(
                content = "Hello Devil",
            )

        assertEquals(
            ConversationRuntimeSubmissionStatus.SUBMITTED,
            result.status,
        )

        val presentation = requireNotNull(result.presentation)

        assertEquals(traceId, presentation.traceId)
        assertEquals(
            ConversationRuntimePresentationStatus.DEFERRED,
            presentation.status,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            presentation.message,
        )
    }

    @Test
    fun `blank prepared content is rejected before metadata or runtime access`() {
        var metadataCalls = 0
        var runtimeCalls = 0

        val metadataProvider =
            object : ConversationRuntimeInputMetadataProvider {
                override fun provide(): ConversationRuntimeInputMetadataResult {
                    metadataCalls += 1

                    return ConversationRuntimeInputMetadataResult.available(
                        standardMetadata(),
                    )
                }
            }

        val runtimeCoordinator =
            object : AndroidRuntimeInputCoordinator {
                override fun submit(
                    schemaVersion: SchemaVersion,
                    source: ContextSource,
                    trustLevel: ContextTrustLevel,
                    securityLevel: ContextSecurityLevel,
                    content: String,
                ): RuntimeResult {
                    runtimeCalls += 1

                    return RuntimeResult.create(
                        traceId =
                            TraceId.from(
                                "trace-conversation-runtime-submission-003",
                            ),
                        status = RuntimeStatus.DEFERRED,
                    )
                }
            }

        val coordinator =
            DefaultConversationRuntimeSubmissionCoordinator(
                metadataProvider = metadataProvider,
                runtimeInputCoordinator = runtimeCoordinator,
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.submit("   ")
        }

        assertEquals(0, metadataCalls)
        assertEquals(0, runtimeCalls)
    }

    private fun fixedMetadataProvider(
        metadata: ConversationRuntimeInputMetadata,
    ): ConversationRuntimeInputMetadataProvider {
        return object : ConversationRuntimeInputMetadataProvider {
            override fun provide(): ConversationRuntimeInputMetadataResult {
                return ConversationRuntimeInputMetadataResult.available(
                    metadata,
                )
            }
        }
    }

    private fun fixedRuntimeCoordinator(
        result: RuntimeResult,
    ): AndroidRuntimeInputCoordinator {
        return object : AndroidRuntimeInputCoordinator {
            override fun submit(
                schemaVersion: SchemaVersion,
                source: ContextSource,
                trustLevel: ContextTrustLevel,
                securityLevel: ContextSecurityLevel,
                content: String,
            ): RuntimeResult {
                return result
            }
        }
    }

    private fun standardMetadata(): ConversationRuntimeInputMetadata {
        return ConversationRuntimeInputMetadata(
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEXT,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
        )
    }
}
