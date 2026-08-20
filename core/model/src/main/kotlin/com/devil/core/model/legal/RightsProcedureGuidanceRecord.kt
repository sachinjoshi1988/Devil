package com.devil.core.model.legal

/**
 * Immutable Stage 163 representation of one bounded Rights & Procedure Guidance
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 159 Legal Intelligence Foundation context;
 * - one explicitly supplied nonblank guidance focus;
 * - one explicitly supplied nonblank rights/procedure context description;
 * - one explicitly supplied nonblank guidance objective.
 *
 * Stage 163 represents supplied rights-and-procedure guidance context only.
 *
 * It does not:
 *
 * - verify current law;
 * - infer jurisdiction;
 * - provide legal advice;
 * - determine legal rights;
 * - determine legal obligations;
 * - determine liability;
 * - establish authoritative procedure;
 * - verify deadlines, limitation periods, or filing windows;
 * - determine filing requirements;
 * - determine appeal or remedy eligibility;
 * - select a court, tribunal, registry, or forum;
 * - access courts, tribunals, registries, government portals, or legal systems;
 * - file or submit documents;
 * - authenticate legal documents;
 * - establish evidence authenticity or weight;
 * - create or verify authoritative legal citations;
 * - establish precedent;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external legal providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 164 Legal Evidence & Citation.
 *
 * RIGHTS_PROCEDURE_GUIDANCE != LEGAL_ADVICE.
 * RIGHTS_PROCEDURE_GUIDANCE != VERIFIED_CURRENT_LAW.
 * RIGHTS_PROCEDURE_GUIDANCE != JURISDICTION_DETERMINATION.
 * RIGHTS_PROCEDURE_GUIDANCE != RIGHTS_DETERMINATION.
 * RIGHTS_PROCEDURE_GUIDANCE != OBLIGATION_DETERMINATION.
 * RIGHTS_PROCEDURE_GUIDANCE != LIABILITY_DETERMINATION.
 * RIGHTS_PROCEDURE_GUIDANCE != AUTHORITATIVE_PROCEDURE.
 * RIGHTS_PROCEDURE_GUIDANCE != VERIFIED_DEADLINE.
 * RIGHTS_PROCEDURE_GUIDANCE != FILING_REQUIREMENT.
 * RIGHTS_PROCEDURE_GUIDANCE != COURT_ACCESS.
 * RIGHTS_PROCEDURE_GUIDANCE != FILING.
 * RIGHTS_PROCEDURE_GUIDANCE != EXECUTION.
 * RIGHTS_PROCEDURE_GUIDANCE != CONSTITUTIONAL_VERIFICATION.
 * SUPPLIED_RIGHTS_PROCEDURE_CONTEXT != VERIFIED_LEGAL_FACT.
 * GUIDANCE_CONTEXT != LEGAL_CONCLUSION.
 */
@ConsistentCopyVisibility
data class RightsProcedureGuidanceRecord private constructor(
    val legalFoundation: LegalIntelligenceFoundationRecord,
    val guidanceFocus: String,
    val suppliedRightsProcedureContextDescription: String,
    val guidanceObjective: String,
) {
    companion object {

        fun create(
            legalFoundation: LegalIntelligenceFoundationRecord,
            guidanceFocus: String,
            suppliedRightsProcedureContextDescription: String,
            guidanceObjective: String,
        ): RightsProcedureGuidanceRecord {
            val normalizedGuidanceFocus =
                guidanceFocus.trim()

            val normalizedSuppliedRightsProcedureContextDescription =
                suppliedRightsProcedureContextDescription.trim()

            val normalizedGuidanceObjective =
                guidanceObjective.trim()

            require(normalizedGuidanceFocus.isNotEmpty()) {
                "Rights & Procedure Guidance focus must not be blank."
            }

            require(
                normalizedSuppliedRightsProcedureContextDescription.isNotEmpty(),
            ) {
                "Rights & Procedure Guidance context description must not be blank."
            }

            require(normalizedGuidanceObjective.isNotEmpty()) {
                "Rights & Procedure Guidance objective must not be blank."
            }

            return RightsProcedureGuidanceRecord(
                legalFoundation = legalFoundation,
                guidanceFocus = normalizedGuidanceFocus,
                suppliedRightsProcedureContextDescription =
                    normalizedSuppliedRightsProcedureContextDescription,
                guidanceObjective = normalizedGuidanceObjective,
            )
        }
    }
}
