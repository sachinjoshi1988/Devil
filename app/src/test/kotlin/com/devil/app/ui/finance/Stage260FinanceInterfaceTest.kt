package com.devil.app.ui.finance

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 260 Finance Interface governance tests.
 *
 * These tests verify bounded financial presentation and navigation without
 * treating UI state as financial authority, verified external state,
 * transaction authority, execution, constitutional Verification, or Memory.
 */
class Stage260FinanceInterfaceTest {

    @Test
    fun `finance interface uses locked Devil identity asset`() {
        val source = financeInterfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )
        assertTrue(
            source.contains(
                "text = \"FINANCE\"",
            ),
        )
    }

    @Test
    fun `finance interface presents bounded financial integration context`() {
        val source = financeInterfaceSource()

        for (
            expected in
                listOf(
                    "\"FINANCIAL CONTEXT\"",
                    "\"SUBJECT\"",
                    "\"SUPPLIED FACTS\"",
                    "\"INTEGRATION FOCUS\"",
                    "\"INTEGRATION OBJECTIVE\"",
                    "\"STATUS\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 260 financial integration presentation: $expected",
            )
        }
    }

    @Test
    fun `finance interface presents bounded personal finance context`() {
        val source = financeInterfaceSource()

        assertTrue(
            source.contains(
                "\"PERSONAL FINANCE\"",
            ),
        )
        assertTrue(
            source.contains(
                "personalFinanceFocus",
            ),
        )
        assertTrue(
            source.contains(
                "personalFinanceObjective",
            ),
        )
        assertTrue(
            source.contains(
                "personalFinanceApproach",
            ),
        )
        assertTrue(
            source.contains(
                "personalFinanceStatus",
            ),
        )
    }

    @Test
    fun `finance interface presents bounded accounting contexts`() {
        val source = financeInterfaceSource()

        for (
            expected in
                listOf(
                    "\"ACCOUNTING\"",
                    "\"ACCOUNTING FOCUS\"",
                    "\"ACCOUNTING OBJECTIVE\"",
                    "\"ACCOUNTING BASIS\"",
                    "\"ACCOUNTING STATUS\"",
                    "\"BUSINESS FOCUS\"",
                    "\"BUSINESS OBJECTIVE\"",
                    "\"BUSINESS APPROACH\"",
                    "\"BUSINESS STATUS\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 260 accounting presentation: $expected",
            )
        }
    }

    @Test
    fun `finance interface presents bounded tax contexts`() {
        val source = financeInterfaceSource()

        for (
            expected in
                listOf(
                    "\"TAX INTELLIGENCE\"",
                    "\"TAX FOCUS\"",
                    "\"TAX OBJECTIVE\"",
                    "\"TAX CONTEXT\"",
                    "\"TAX STATUS\"",
                    "\"INDIAN TAX FOCUS\"",
                    "\"INDIAN TAX OBJECTIVE\"",
                    "\"INDIA TAX CONTEXT\"",
                    "\"INDIAN TAX STATUS\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 260 tax presentation: $expected",
            )
        }
    }

    @Test
    fun `finance interface presents bounded financial document context`() {
        val source = financeInterfaceSource()

        for (
            expected in
                listOf(
                    "\"FINANCIAL DOCUMENTS\"",
                    "\"DOCUMENT FOCUS\"",
                    "\"SUPPLIED DOCUMENT\"",
                    "\"INTERPRETATION OBJECTIVE\"",
                    "\"No financial document description supplied.\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 260 financial-document presentation: $expected",
            )
        }
    }

    @Test
    fun `finance interface presents bounded safety and verification context`() {
        val source = financeInterfaceSource()

        for (
            expected in
                listOf(
                    "\"SAFETY & VERIFICATION\"",
                    "\"SAFETY FOCUS\"",
                    "\"SUPPLIED VERIFICATION BASIS\"",
                    "\"SAFETY INTERPRETATION\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 260 financial-safety presentation: $expected",
            )
        }
    }

    @Test
    fun `finance interface preserves constitutional financial boundaries`() {
        val source = financeInterfaceSource()

        for (
            boundary in
                listOf(
                    "FINANCE_INTERFACE != FINANCIAL_AUTHORITY.",
                    "FINANCE_PRESENTATION != ACCOUNT_ACCESS.",
                    "SUPPLIED_FINANCIAL_FACT != VERIFIED_EXTERNAL_FINANCIAL_STATE.",
                    "FINANCE_INTERFACE != FINANCIAL_ADVICE.",
                    "FINANCE_INTERFACE != INVESTMENT_SUITABILITY.",
                    "FINANCE_INTERFACE != TRANSACTION.",
                    "FINANCE_INTERFACE != EXECUTION.",
                    "ACCOUNTING_PRESENTATION != BOOKKEEPING_EXECUTION.",
                    "ACCOUNTING_PRESENTATION != VERIFIED_ACCOUNTING.",
                    "TAX_PRESENTATION != TAX_AUTHORITY.",
                    "TAX_PRESENTATION != VERIFIED_CURRENT_TAX_LAW.",
                    "TAX_PRESENTATION != TAX_FILING.",
                    "FINANCIAL_DOCUMENT_PRESENTATION != OCR.",
                    "FINANCIAL_DOCUMENT_PRESENTATION != DOCUMENT_AUTHENTICITY.",
                    "FINANCIAL_SAFETY_PRESENTATION != CONSTITUTIONAL_VERIFICATION.",
                    "FINANCIAL_SAFETY_PRESENTATION != FINANCIAL_GUARANTEE.",
                    "FINANCE_INTERFACE != WORLD_MODEL_UPDATE.",
                    "FINANCE_INTERFACE != CONSTITUTIONAL_LEARNING.",
                    "FINANCE_INTERFACE != MEMORY_COMMITMENT.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 260 financial boundary: $boundary",
            )
        }
    }

    @Test
    fun `missing supplied finance information remains truthful`() {
        val source = financeInterfaceSource()

        assertTrue(
            source.contains(
                "\"Unavailable\"",
            ),
        )
        assertTrue(
            source.contains(
                "\"No financial document description supplied.\"",
            ),
        )
        assertTrue(
            source.contains(
                "?.trim()",
            ),
        )
        assertTrue(
            source.contains(
                "?.takeIf(String::isNotEmpty)",
            ),
        )
    }

    @Test
    fun `conversation exposes bounded finance navigation`() {
        val source = conversationSource()

        assertTrue(
            source.contains(
                "onFinanceOpen: () -> Unit = {}",
            ),
        )
        assertTrue(
            source.contains(
                "onClick = onFinanceOpen",
            ),
        )
        assertTrue(
            source.contains(
                "text = \"FINANCE\"",
            ),
        )
        assertTrue(
            source.contains(
                "financeNavigationEnabled",
            ),
        )

        for (
            boundary in
                listOf(
                    "FINANCE_NAVIGATION != FINANCIAL_AUTHORITY.",
                    "FINANCE_NAVIGATION != ACCOUNT_ACCESS.",
                    "FINANCE_NAVIGATION != TRANSACTION.",
                    "FINANCE_NAVIGATION != EXECUTION.",
                    "FINANCE_NAVIGATION != FINANCIAL_VERIFICATION.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 260 finance-navigation boundary: $boundary",
            )
        }
    }

    @Test
    fun `activity supplies no fabricated finance state`() {
        val source = activitySource()

        assertTrue(
            source.contains(
                "DevilFinanceInterface(",
            ),
        )

        for (
            suppliedNull in
                listOf(
                    "financialSubject = null",
                    "suppliedFinancialFacts = null",
                    "integrationFocus = null",
                    "integrationObjective = null",
                    "integrationStatus = null",
                    "personalFinanceFocus = null",
                    "personalFinanceObjective = null",
                    "personalFinanceApproach = null",
                    "personalFinanceStatus = null",
                    "accountingFocus = null",
                    "accountingObjective = null",
                    "accountingBasis = null",
                    "accountingStatus = null",
                    "businessAccountingFocus = null",
                    "businessAccountingObjective = null",
                    "businessAccountingApproach = null",
                    "businessAccountingStatus = null",
                    "taxFocus = null",
                    "taxObjective = null",
                    "taxContext = null",
                    "taxStatus = null",
                    "indianTaxFocus = null",
                    "indianTaxObjective = null",
                    "indianTaxContext = null",
                    "indianTaxStatus = null",
                    "documentFocus = null",
                    "suppliedDocumentDescription = null",
                    "documentInterpretationObjective = null",
                    "documentStatus = null",
                    "safetyFocus = null",
                    "verificationBasisDescription = null",
                    "safetyInterpretation = null",
                    "safetyStatus = null",
                )
        ) {
            assertTrue(
                source.contains(suppliedNull),
                "Activity must not fabricate Stage 260 finance state: $suppliedNull",
            )
        }
    }

    @Test
    fun `finance interface contains no operational financial wiring`() {
        val source = financeInterfaceSource()

        for (
            forbidden in
                listOf(
                    "FinancialAnalysisCoordinator",
                    "FinancialIntelligenceIntegrationCoordinator",
                    "PersonalFinanceAssistanceCoordinator",
                    "AccountingFoundationCoordinator",
                    "BusinessAccountingAssistanceCoordinator",
                    "TaxIntelligenceFoundationCoordinator",
                    "IndianTaxAssistanceCoordinator",
                    "FinancialDocumentIntelligenceCoordinator",
                    "FinancialSafetyVerificationCoordinator",
                    "WorldModelUpdateRequest",
                    "MemoryAuthority",
                    "ExecutionRequest",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 260 UI must not invoke operational financial wiring: $forbidden",
            )
        }
    }

    @Test
    fun `Stage 260 does not implement Stage 261 or later UI work`() {
        val source = financeInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 260 does not implement Stage 261 or later UI work.",
            ),
        )
    }

    private fun financeInterfaceSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/finance/DevilFinanceInterface.kt",
            "src/main/kotlin/com/devil/app/ui/finance/DevilFinanceInterface.kt",
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
                "Unable to locate Stage 260 source from: ${candidates.joinToString()}",
            )
    }
}
