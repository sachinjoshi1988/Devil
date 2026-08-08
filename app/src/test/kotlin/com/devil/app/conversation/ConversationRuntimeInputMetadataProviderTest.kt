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
    fun `default production provider truthfully remains unavailable`() {
        val provider: ConversationRuntimeInputMetadataProvider =
            DefaultConversationRuntimeInputMetadataProvider()

        val result = provider.provide()

        assertEquals(
            ConversationRuntimeInputMetadataStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.metadata)
    }
}
