package com.devil.app.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class DefaultConversationEntryIdProviderTest {

    @Test
    fun `provider creates conversation entry identity from supplied raw value`() {
        val provider: ConversationEntryIdProvider =
            DefaultConversationEntryIdProvider(
                rawIdProvider = {
                    "entry-provider-001"
                },
            )

        val id = provider.provide()

        assertEquals(
            "entry-provider-001",
            id.value,
        )
    }

    @Test
    fun `provider preserves conversation entry normalization`() {
        val provider =
            DefaultConversationEntryIdProvider(
                rawIdProvider = {
                    "  entry-provider-002  "
                },
            )

        val id = provider.provide()

        assertEquals(
            "entry-provider-002",
            id.value,
        )
    }

    @Test
    fun `provider rejects blank generated identity`() {
        val provider =
            DefaultConversationEntryIdProvider(
                rawIdProvider = {
                    "   "
                },
            )

        assertFailsWith<IllegalArgumentException> {
            provider.provide()
        }
    }

    @Test
    fun `separate generated values remain separate presentation identities`() {
        val generatedValues =
            ArrayDeque(
                listOf(
                    "entry-provider-003",
                    "entry-provider-004",
                ),
            )

        val provider =
            DefaultConversationEntryIdProvider(
                rawIdProvider = {
                    generatedValues.removeFirst()
                },
            )

        val first = provider.provide()
        val second = provider.provide()

        assertNotEquals(
            first,
            second,
        )
        assertEquals(
            "entry-provider-003",
            first.value,
        )
        assertEquals(
            "entry-provider-004",
            second.value,
        )
    }

    @Test
    fun `default provider creates non blank presentation identity`() {
        val provider =
            DefaultConversationEntryIdProvider()

        val id = provider.provide()

        check(id.value.isNotBlank())
    }

    @Test
    fun `default provider creates distinct presentation identities`() {
        val provider =
            DefaultConversationEntryIdProvider()

        val first = provider.provide()
        val second = provider.provide()

        assertNotEquals(
            first,
            second,
        )
    }
}
