package com.devil.core.runtime.financial

/**
 * Stage 157 bounded Financial Document Intelligence preparation status.
 *
 * PREPARED means one structurally valid supplied financial-document context was
 * prepared from an existing Stage 151 Financial Intelligence Integration
 * context and explicitly supplied document metadata.
 *
 * PREPARED does not mean:
 *
 * - a file was opened or inspected;
 * - OCR occurred;
 * - document fields were extracted;
 * - document authenticity was established;
 * - financial facts were verified;
 * - constitutional Observation or Verification occurred;
 * - financial safety was established;
 * - or Stage 158 Financial Safety & Verification was implemented.
 *
 * DEFERRED means no truthful Financial Document Intelligence context was
 * produced.
 */
enum class FinancialDocumentIntelligencePreparationStatus {
    PREPARED,
    DEFERRED,
}
