package com.devil.core.model.understanding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class UnderstandingSemanticsTest {

    @Test
    fun `semantic argument preserves normalized unresolved value without resolving it`() {
        val argument =
            UnderstandingSemanticArgument.create(
                name = "  recipient_reference  ",
                value = "  him  ",
            )

        assertEquals(
            "recipient_reference",
            argument.name,
        )
        assertEquals(
            "him",
            argument.value,
        )
    }

    @Test
    fun `semantic argument rejects blank name or value`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemanticArgument.create(
                name = "   ",
                value = "him",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemanticArgument.create(
                name = "recipient_reference",
                value = "   ",
            )
        }
    }

    @Test
    fun `action request preserves structured target predicate and arguments`() {
        val semantics =
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "  set volume  ",
                target = "  volume  ",
                predicate = "  set  ",
                arguments =
                    listOf(
                        UnderstandingSemanticArgument.create(
                            name = "value",
                            value = "30",
                        ),
                        UnderstandingSemanticArgument.create(
                            name = "unit",
                            value = "percent",
                        ),
                    ),
            )

        assertEquals(
            UnderstandingIntent.ACTION_REQUEST,
            semantics.intent,
        )
        assertEquals(
            UnderstandingActionability.ACTIONABLE,
            semantics.actionability,
        )
        assertEquals(
            "set volume",
            semantics.meaning,
        )
        assertEquals(
            "volume",
            semantics.target,
        )
        assertEquals(
            "set",
            semantics.predicate,
        )
        assertEquals(
            listOf("value", "unit"),
            semantics.arguments.map { argument -> argument.name },
        )
        assertEquals(
            listOf("30", "percent"),
            semantics.arguments.map { argument -> argument.value },
        )
    }

    @Test
    fun `information query is actionable meaning without granting execution`() {
        val semantics =
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "query battery level",
                target = "battery level",
                predicate = "query",
            )

        assertEquals(
            UnderstandingIntent.INFORMATION_QUERY,
            semantics.intent,
        )
        assertEquals(
            UnderstandingActionability.ACTIONABLE,
            semantics.actionability,
        )
        assertEquals(
            "battery level",
            semantics.target,
        )
        assertEquals(
            "query",
            semantics.predicate,
        )
        assertTrue(semantics.arguments.isEmpty())
    }

    @Test
    fun `open target rejects predicate and semantic arguments`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.OPEN_TARGET,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "open target",
                target = "settings",
                predicate = "open",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.OPEN_TARGET,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "open target",
                target = "settings",
                arguments =
                    listOf(
                        UnderstandingSemanticArgument.create(
                            name = "unexpected",
                            value = "value",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `new actionable intents require target and predicate`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "lower volume",
                predicate = "decrease",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "query battery level",
                target = "battery level",
            )
        }
    }

    @Test
    fun `new actionable intents reject non actionable classification`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability = UnderstandingActionability.NON_ACTIONABLE,
                meaning = "lower volume",
                target = "volume",
                predicate = "decrease",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATION_QUERY,
                actionability = UnderstandingActionability.NON_ACTIONABLE,
                meaning = "query battery level",
                target = "battery level",
                predicate = "query",
            )
        }
    }

    @Test
    fun `duplicate semantic argument names fail closed`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability = UnderstandingActionability.ACTIONABLE,
                meaning = "reply to unresolved recipient",
                target = "message",
                predicate = "reply",
                arguments =
                    listOf(
                        UnderstandingSemanticArgument.create(
                            name = "recipient_reference",
                            value = "him",
                        ),
                        UnderstandingSemanticArgument.create(
                            name = "recipient_reference",
                            value = "them",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `greeting and informational semantics remain structurally non actionable`() {
        val greeting =
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.GREETING,
                actionability = UnderstandingActionability.NON_ACTIONABLE,
                meaning = "greeting",
            )

        val informational =
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.INFORMATIONAL,
                actionability = UnderstandingActionability.NON_ACTIONABLE,
                meaning = "informational statement",
            )

        assertEquals(null, greeting.target)
        assertEquals(null, greeting.predicate)
        assertTrue(greeting.arguments.isEmpty())

        assertEquals(null, informational.target)
        assertEquals(null, informational.predicate)
        assertTrue(informational.arguments.isEmpty())
    }
}
