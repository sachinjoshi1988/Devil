package com.devil.app.ui.research

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 259 Research Interface governance tests.
 */
class Stage259ResearchInterfaceTest {

    @Test
    fun `research interface uses locked Devil identity asset`() {
        val source = researchInterfaceSource()

        assertTrue(source.contains("R.drawable.devil_primary_logo"))
        assertTrue(source.contains("\"RESEARCH\""))
    }

    @Test
    fun `research interface presents bounded research evidence and source assessment`() {
        val source = researchInterfaceSource()

        for (
            expected in
                listOf(
                    "\"RESEARCH SUBJECT\"",
                    "\"RESEARCH EVIDENCE\"",
                    "\"SOURCE REFERENCE\"",
                    "\"SOURCE KIND\"",
                    "\"EVIDENCE DESCRIPTION\"",
                    "\"SOURCE ASSESSMENT\"",
                    "\"AUTHENTICITY\"",
                    "\"TRUST\"",
                    "\"FRESHNESS\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 259 research presentation: $expected",
            )
        }
    }

    @Test
    fun `research interface presents corroboration conflict confidence and synthesis`() {
        val source = researchInterfaceSource()

        for (
            expected in
                listOf(
                    "\"RESEARCH EVALUATION\"",
                    "\"CORROBORATION\"",
                    "\"CONFLICT\"",
                    "\"CONFIDENCE\"",
                    "\"RESEARCH SYNTHESIS\"",
                    "\"SYNTHESIS STATUS\"",
                    "\"SYNTHESIS\"",
                    "\"INTERNET RESEARCH\"",
                    "\"ADMISSION\"",
                    "\"ANALYSIS\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 259 research state: $expected",
            )
        }
    }

    @Test
    fun `research interface preserves constitutional boundaries`() {
        val source = researchInterfaceSource()

        for (
            boundary in
                listOf(
                    "RESEARCH_INTERFACE != RESEARCH_AUTHORITY.",
                    "RESEARCH_EVIDENCE_PRESENTATION != TRUE.",
                    "RESEARCH_EVIDENCE_PRESENTATION != VERIFIED.",
                    "SOURCE_ASSESSMENT_PRESENTATION != FACT_VERIFICATION.",
                    "CORROBORATION_PRESENTATION != CONSENSUS.",
                    "CONFLICT_PRESENTATION != CONFLICT_RESOLUTION.",
                    "RESEARCH_CONFIDENCE_PRESENTATION != TRUTH.",
                    "RESEARCH_CONFIDENCE_PRESENTATION != VERIFICATION.",
                    "RESEARCH_SYNTHESIS_PRESENTATION != TRUTH.",
                    "RESEARCH_SYNTHESIS_PRESENTATION != WORLD_MODEL.",
                    "RESEARCH_INTERFACE != LEARNING.",
                    "RESEARCH_INTERFACE != MEMORY.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 259 boundary: $boundary",
            )
        }
    }

    @Test
    fun `missing supplied research information remains truthful`() {
        val source = researchInterfaceSource()

        assertTrue(source.contains("\"Unavailable\""))
        assertTrue(source.contains("\"No research synthesis supplied.\""))
        assertTrue(source.contains("?.trim()"))
        assertTrue(source.contains("?.takeIf(String::isNotEmpty)"))
    }

    @Test
    fun `conversation exposes bounded research navigation`() {
        val source = conversationSource()

        assertTrue(source.contains("onResearchOpen: () -> Unit = {}"))
        assertTrue(source.contains("onClick = onResearchOpen"))
        assertTrue(source.contains("text = \"RESEARCH\""))
        assertTrue(source.contains("researchNavigationEnabled"))
        assertTrue(source.contains("RESEARCH_NAVIGATION != RESEARCH_EXECUTION."))
        assertTrue(source.contains("RESEARCH_NAVIGATION != RESEARCH_VERIFICATION."))
        assertTrue(source.contains("RESEARCH_NAVIGATION != WORLD_MODEL_UPDATE."))
        assertTrue(source.contains("RESEARCH_NAVIGATION != MEMORY."))
    }

    @Test
    fun `activity supplies no fabricated research state`() {
        val source = activitySource()

        assertTrue(source.contains("DevilResearchInterface("))

        for (
            suppliedNull in
                listOf(
                    "researchSubject = null",
                    "evidenceSourceReference = null",
                    "evidenceSourceKind = null",
                    "evidenceDescription = null",
                    "sourceAuthenticity = null",
                    "sourceTrust = null",
                    "sourceFreshness = null",
                    "corroborationStatus = null",
                    "conflictStatus = null",
                    "confidenceStatus = null",
                    "synthesisStatus = null",
                    "synthesisDescription = null",
                    "internetAdmissionStatus = null",
                    "internetAnalysisStatus = null",
                )
        ) {
            assertTrue(
                source.contains(suppliedNull),
                "Activity must not fabricate Stage 259 research state: $suppliedNull",
            )
        }
    }

    @Test
    fun `research interface contains no operational research wiring`() {
        val source = researchInterfaceSource()

        for (
            forbidden in
                listOf(
                    "AndroidInternetResearchAdmissionCoordinator",
                    "AndroidInternetResearchAnalysisCoordinator",
                    "ResearchEvidence.create",
                    "ResearchSourceAssessment.create",
                    "ResearchCorroborationAssessment.create",
                    "ResearchConfidenceAssessment.create",
                    "ResearchSynthesisRecord.create",
                    "WorldModelUpdateRequest",
                    "MemoryAuthority",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 259 UI must not invoke operational research wiring: $forbidden",
            )
        }
    }

    @Test
    fun `Stage 259 does not implement Stage 260 or later UI work`() {
        val source = researchInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 259 does not implement Stage 260 or later UI work.",
            ),
        )
    }

    private fun researchInterfaceSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/research/DevilResearchInterface.kt",
            "src/main/kotlin/com/devil/app/ui/research/DevilResearchInterface.kt",
        )

    private fun conversationSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
            "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
        )

    private fun activitySource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/DevilActivity.kt",
            "src/main/kotlin/com/devil/app/DevilActivity.kt",
        )

    private fun readSource(
        vararg candidates: String,
    ): String {
        return candidates
            .asSequence()
            .map(::File)
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate Stage 259 source from: ${candidates.joinToString()}",
            )
    }
}
