package com.devil.core.model.understanding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * Stage 337E model-contract proof.
 *
 * LANGUAGE_DETECTED != LANGUAGE_VERIFIED.
 * LANGUAGE_DECLARED != LANGUAGE_VERIFIED.
 */
class UnderstandingLanguageEvidenceTest {

    @Test
    fun `unknown evidence preserves script without inventing language`() {
        val evidence =
            UnderstandingLanguageEvidence.create(
                status = UnderstandingLanguageEvidenceStatus.UNKNOWN,
                script = UnderstandingScript.DEVANAGARI,
            )

        assertEquals(
            UnderstandingLanguageEvidenceStatus.UNKNOWN,
            evidence.status,
        )
        assertEquals(
            UnderstandingScript.DEVANAGARI,
            evidence.script,
        )
        assertNull(evidence.languageTag)
    }

    @Test
    fun `unknown evidence rejects language tag`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingLanguageEvidence.create(
                status = UnderstandingLanguageEvidenceStatus.UNKNOWN,
                script = UnderstandingScript.LATIN,
                languageTag = "en",
            )
        }
    }

    @Test
    fun `detected evidence requires nonblank language tag`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingLanguageEvidence.create(
                status = UnderstandingLanguageEvidenceStatus.DETECTED,
                script = UnderstandingScript.LATIN,
                languageTag = "   ",
            )
        }
    }

    @Test
    fun `declared evidence requires nonblank language tag`() {
        assertFailsWith<IllegalArgumentException> {
            UnderstandingLanguageEvidence.create(
                status = UnderstandingLanguageEvidenceStatus.DECLARED,
                script = UnderstandingScript.LATIN,
            )
        }
    }

    @Test
    fun `language tag is normalized without becoming verified`() {
        val evidence =
            UnderstandingLanguageEvidence.create(
                status = UnderstandingLanguageEvidenceStatus.DECLARED,
                script = UnderstandingScript.DEVANAGARI,
                languageTag = "  HI-IN  ",
            )

        assertEquals(
            UnderstandingLanguageEvidenceStatus.DECLARED,
            evidence.status,
        )
        assertEquals("hi-in", evidence.languageTag)
        assertEquals(
            UnderstandingScript.DEVANAGARI,
            evidence.script,
        )
    }
}
