package com.devil.core.model.legal

/**
 * Immutable Stage 161 representation of one bounded Legal Document
 * Understanding context.
 *
 * This record preserves:
 *
 * - one existing Stage 159 Legal Intelligence Foundation context;
 * - one explicitly supplied nonblank legal-document focus;
 * - one explicitly supplied nonblank legal-document description;
 * - one explicitly supplied nonblank interpretation objective.
 *
 * Stage 161 represents supplied legal-document context only.
 *
 * It does not:
 *
 * - open or read files;
 * - perform OCR;
 * - inspect images or PDFs;
 * - extract clauses, fields, signatures, dates, or other document elements;
 * - parse document structure;
 * - infer missing terms or provisions;
 * - establish document authenticity;
 * - verify signatures;
 * - establish verified document content;
 * - determine legal effect;
 * - determine legal rights or obligations;
 * - determine liability;
 * - provide legal advice;
 * - establish a legal conclusion;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external legal or document providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 162.
 *
 * LEGAL_DOCUMENT_UNDERSTANDING != OCR.
 * LEGAL_DOCUMENT_UNDERSTANDING != DOCUMENT_EXTRACTION.
 * LEGAL_DOCUMENT_UNDERSTANDING != DOCUMENT_AUTHENTICITY.
 * LEGAL_DOCUMENT_UNDERSTANDING != SIGNATURE_VERIFICATION.
 * LEGAL_DOCUMENT_UNDERSTANDING != VERIFIED_DOCUMENT_CONTENT.
 * LEGAL_DOCUMENT_UNDERSTANDING != LEGAL_EFFECT_DETERMINATION.
 * LEGAL_DOCUMENT_UNDERSTANDING != RIGHTS_DETERMINATION.
 * LEGAL_DOCUMENT_UNDERSTANDING != LIABILITY_DETERMINATION.
 * LEGAL_DOCUMENT_UNDERSTANDING != LEGAL_ADVICE.
 * LEGAL_DOCUMENT_UNDERSTANDING != CONSTITUTIONAL_VERIFICATION.
 * SUPPLIED_LEGAL_DOCUMENT_DESCRIPTION != OBSERVED_DOCUMENT_CONTENT.
 * DOCUMENT_INTERPRETATION != LEGAL_CONCLUSION.
 */
@ConsistentCopyVisibility
data class LegalDocumentUnderstandingRecord private constructor(
    val legalFoundation: LegalIntelligenceFoundationRecord,
    val documentFocus: String,
    val suppliedLegalDocumentDescription: String,
    val interpretationObjective: String,
) {
    companion object {

        fun create(
            legalFoundation: LegalIntelligenceFoundationRecord,
            documentFocus: String,
            suppliedLegalDocumentDescription: String,
            interpretationObjective: String,
        ): LegalDocumentUnderstandingRecord {
            val normalizedDocumentFocus =
                documentFocus.trim()

            val normalizedSuppliedLegalDocumentDescription =
                suppliedLegalDocumentDescription.trim()

            val normalizedInterpretationObjective =
                interpretationObjective.trim()

            require(normalizedDocumentFocus.isNotEmpty()) {
                "Legal Document Understanding focus must not be blank."
            }

            require(normalizedSuppliedLegalDocumentDescription.isNotEmpty()) {
                "Legal Document Understanding supplied document description must not be blank."
            }

            require(normalizedInterpretationObjective.isNotEmpty()) {
                "Legal Document Understanding interpretation objective must not be blank."
            }

            return LegalDocumentUnderstandingRecord(
                legalFoundation = legalFoundation,
                documentFocus = normalizedDocumentFocus,
                suppliedLegalDocumentDescription =
                    normalizedSuppliedLegalDocumentDescription,
                interpretationObjective =
                    normalizedInterpretationObjective,
            )
        }
    }
}
