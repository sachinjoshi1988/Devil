package com.devil.core.model.legal

/**
 * Immutable Stage 165 representation of one bounded High-Stakes Legal Safety
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 159 Legal Intelligence Foundation context;
 * - one explicitly supplied nonblank high-stakes safety focus;
 * - one explicitly supplied nonblank legal-risk context description;
 * - one explicitly supplied nonblank safety interpretation.
 *
 * Stage 165 represents supplied high-stakes legal safety context only.
 *
 * It does not:
 *
 * - verify that a situation is legally high-stakes;
 * - verify legal risk or severity;
 * - diagnose urgency or emergency;
 * - verify current law;
 * - infer jurisdiction;
 * - provide legal advice;
 * - determine legal rights, obligations, liability, remedies, or procedure;
 * - verify deadlines or filing requirements;
 * - determine evidence authenticity, admissibility, or weight;
 * - establish authoritative citations, precedent, or controlling authority;
 * - select or contact lawyers, courts, tribunals, authorities, providers,
 *   or emergency services;
 * - file, submit, communicate, escalate, or execute anything;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - duplicate Stage 158 Financial Safety & Verification;
 * - broaden security or emergency-escalation authority;
 * - or implement Stage 166 Creative Media Integration.
 *
 * SUPPLIED_HIGH_STAKES_LEGAL_CONTEXT != VERIFIED_LEGAL_RISK.
 * SAFETY_INTERPRETATION != LEGAL_ADVICE.
 * SAFETY_INTERPRETATION != VERIFIED_OUTCOME.
 * HIGH_STAKES_LEGAL_SAFETY != EMERGENCY_ESCALATION.
 * HIGH_STAKES_LEGAL_SAFETY != EXECUTION_AUTHORIZATION.
 */
@ConsistentCopyVisibility
data class HighStakesLegalSafetyRecord private constructor(
    val legalFoundation: LegalIntelligenceFoundationRecord,
    val highStakesSafetyFocus: String,
    val suppliedLegalRiskContextDescription: String,
    val safetyInterpretation: String,
) {
    companion object {

        fun create(
            legalFoundation: LegalIntelligenceFoundationRecord,
            highStakesSafetyFocus: String,
            suppliedLegalRiskContextDescription: String,
            safetyInterpretation: String,
        ): HighStakesLegalSafetyRecord {
            val normalizedHighStakesSafetyFocus =
                highStakesSafetyFocus.trim()

            val normalizedSuppliedLegalRiskContextDescription =
                suppliedLegalRiskContextDescription.trim()

            val normalizedSafetyInterpretation =
                safetyInterpretation.trim()

            require(normalizedHighStakesSafetyFocus.isNotEmpty()) {
                "High-Stakes Legal Safety focus must not be blank."
            }

            require(normalizedSuppliedLegalRiskContextDescription.isNotEmpty()) {
                "High-Stakes Legal Safety risk context description must not be blank."
            }

            require(normalizedSafetyInterpretation.isNotEmpty()) {
                "High-Stakes Legal Safety interpretation must not be blank."
            }

            return HighStakesLegalSafetyRecord(
                legalFoundation = legalFoundation,
                highStakesSafetyFocus = normalizedHighStakesSafetyFocus,
                suppliedLegalRiskContextDescription =
                    normalizedSuppliedLegalRiskContextDescription,
                safetyInterpretation = normalizedSafetyInterpretation,
            )
        }
    }
}
