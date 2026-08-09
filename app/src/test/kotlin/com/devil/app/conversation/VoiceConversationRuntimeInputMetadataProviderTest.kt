package com.devil.app.conversation

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class VoiceConversationRuntimeInputMetadataProviderTest {

    @Test
    fun `voice provider establishes bounded conservative voice metadata`() {
        val result =
            VoiceConversationRuntimeInputMetadataProvider()
                .provide()

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
            ContextSource.VOICE,
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
    fun `voice provenance remains distinct from typed text provenance`() {
        val voiceMetadata =
            requireNotNull(
                VoiceConversationRuntimeInputMetadataProvider()
                    .provide()
                    .metadata,
            )

        val textMetadata =
            requireNotNull(
                DefaultConversationRuntimeInputMetadataProvider()
                    .provide()
                    .metadata,
            )

        assertEquals(
            ContextSource.VOICE,
            voiceMetadata.source,
        )
        assertEquals(
            ContextSource.TEXT,
            textMetadata.source,
        )
        assertNotEquals(
            textMetadata.source,
            voiceMetadata.source,
        )
    }

    @Test
    fun `voice metadata does not claim verified or trusted supplied context`() {
        val metadata =
            requireNotNull(
                VoiceConversationRuntimeInputMetadataProvider()
                    .provide()
                    .metadata,
            )

        assertNotEquals(
            ContextTrustLevel.VERIFIED,
            metadata.trustLevel,
        )
        assertNotEquals(
            ContextTrustLevel.TRUSTED,
            metadata.trustLevel,
        )
    }
}
