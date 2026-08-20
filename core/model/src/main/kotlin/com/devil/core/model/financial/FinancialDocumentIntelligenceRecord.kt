package com.devil.core.model.financial

/**
 * Immutable Stage 157 representation of one bounded Financial Document
 * Intelligence context.
 *
 * This record preserves:
 *
 * - one existing Stage 151 Financial Intelligence Integration context;
 * - one explicitly supplied nonblank financial-document focus;
 * - one explicitly supplied nonblank supplied-document description;
 * - one explicitly supplied nonblank interpretation objective.
 *
 * Stage 157 represents supplied financial-document context only.
 *
 * It does not:
 *
 * - open or read files;
 * - perform OCR;
 * - inspect images or PDFs;
 * - extract document fields;
 * - parse document structure;
 * - infer missing financial facts;
 * - establish document authenticity;
 * - verify amounts, balances, transactions, or totals;
 * - establish that supplied document content is current or complete;
 * - establish account ownership or access;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external document, vision, tax, or financial providers;
 * - communicate with Android or platform APIs;
 * - establish financial safety;
 * - or implement Stage 158 Financial Safety & Verification.
 *
 * FINANCIAL_DOCUMENT_INTELLIGENCE != OCR.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != DOCUMENT_EXTRACTION.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != VERIFIED_DOCUMENT.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != VERIFIED_FINANCIAL_FACT.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != DOCUMENT_AUTHENTICITY.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != ACCOUNT_ACCESS.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != TRANSACTION.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != EXECUTION.
 * FINANCIAL_DOCUMENT_INTELLIGENCE != FINANCIAL_SAFETY_VERIFICATION.
 * SUPPLIED_DOCUMENT_DESCRIPTION != OBSERVED_DOCUMENT_CONTENT.
 * DOCUMENT_INTERPRETATION != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class FinancialDocumentIntelligenceRecord private constructor(
    val financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
    val documentFocus: String,
    val suppliedDocumentDescription: String,
    val interpretationObjective: String,
) {
    companion object {

        fun create(
            financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
            documentFocus: String,
            suppliedDocumentDescription: String,
            interpretationObjective: String,
        ): FinancialDocumentIntelligenceRecord {
            val normalizedDocumentFocus =
                documentFocus.trim()

            val normalizedSuppliedDocumentDescription =
                suppliedDocumentDescription.trim()

            val normalizedInterpretationObjective =
                interpretationObjective.trim()

            require(normalizedDocumentFocus.isNotEmpty()) {
                "Financial Document Intelligence focus must not be blank."
            }

            require(normalizedSuppliedDocumentDescription.isNotEmpty()) {
                "Financial Document Intelligence supplied document description must not be blank."
            }

            require(normalizedInterpretationObjective.isNotEmpty()) {
                "Financial Document Intelligence interpretation objective must not be blank."
            }

            return FinancialDocumentIntelligenceRecord(
                financialIntelligenceIntegration =
                    financialIntelligenceIntegration,
                documentFocus = normalizedDocumentFocus,
                suppliedDocumentDescription =
                    normalizedSuppliedDocumentDescription,
                interpretationObjective =
                    normalizedInterpretationObjective,
            )
        }
    }
}
