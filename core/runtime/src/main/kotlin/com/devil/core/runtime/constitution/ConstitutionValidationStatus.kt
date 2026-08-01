package com.devil.core.runtime.constitution

/**
 * Describes whether supplied context satisfies constitutional invariants.
 *
 * This status does not establish identity, trust, authorization, or any later
 * runtime-stage result.
 */
enum class ConstitutionValidationStatus {
    VALID,
    INVALID,
}
