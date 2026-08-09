package com.devil.app.conversation

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConversationRuntimeInputMetadataProviderTest {

    @Test
    fun `available result preserves complete supplied metadata`() {
        val metadata =
            ConversationRuntimeInputMetadata(
                schemaVersion = SchemaVersion.from(7),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.SENSITIVE,
            )

        val result =
            ConversationRuntimeInputMetadataResult.available(
                metadata = metadata,
            )

        assertEquals(
            ConversationRuntimeInputMetadataStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            metadata,
            result.metadata,
        )
    }

    @Test
    fun `unavailable result contains no metadata`() {
        val result =
            ConversationRuntimeInputMetadataResult.unavailable()

        assertEquals(
            ConversationRuntimeInputMetadataStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.metadata)
    }

    @Test
    fun `default production provider establishes conservative typed text metadata`() {
        val provider: ConversationRuntimeInputMetadataProvider =
            DefaultConversationRuntimeInputMetadataProvider()

        val result = provider.provide()

        assertEquals(
            ConversationRuntimeInputMetadataStatus.AVAILABLE,
            result.status,
        )

        val metadata = requireNotNull(result.metadata)

        assertEquals(
            SchemaVersion.from(1),
            metadata.schemaVersion,
        )
        assertEquals(
            ContextSource.TEXT,
            metadata.source,
        )
        assertEquals(
            ContextTrustLevel.UNVERIFIED,
            metadata.trustLevel,
        )
        assertEquals(
            ContextSecurityLevel.RESTRICTED,
            metadata.securityLevel,
        )
    }

    @Test
    fun `default typed text metadata does not claim verified or trusted supplied context`() {
        val result =
            DefaultConversationRuntimeInputMetadataProvider()
                .provide()

        val metadata = requireNotNull(result.metadata)

        assertEquals(
            false,
            metadata.trustLevel == ContextTrustLevel.VERIFIED,
        )
        assertEquals(
            false,
            metadata.trustLevel == ContextTrustLevel.TRUSTED,
        )
    }
}
