package com.devil.core.model.embodiment

/**
 * Immutable Stage 83 evidence that one already-represented Devil embodiment
 * belongs to a bounded PC platform family.
 *
 * This evidence contains descriptive platform metadata only.
 *
 * operatingSystemFamily may describe a supplied PC operating-system family such
 * as Windows, Linux, macOS, or another future desktop-class platform family.
 *
 * The value is not discovered here and does not prove that:
 *
 * - the operating system currently exists;
 * - a process is running;
 * - the embodiment is reachable;
 * - the host is trusted;
 * - a subject is authenticated;
 * - Devil authorization exists;
 * - a session is valid;
 * - capabilities are registered, available, healthy, or ready;
 * - platform permission exists;
 * - execution is approved;
 * - Observation, Verification, or Outcome exists;
 * - Memory is eligible;
 * - or another Devil intelligence or Unified Devil Runtime exists.
 *
 * PC_PLATFORM_EVIDENCE != DEVIL_IDENTITY.
 * PC_PLATFORM_EVIDENCE != AUTHORITY.
 * PC_PLATFORM_EVIDENCE != EXECUTION.
 */
@ConsistentCopyVisibility
data class PcEmbodimentEvidence private constructor(
    val operatingSystemFamily: String,
) {
    companion object {

        fun create(
            operatingSystemFamily: String,
        ): PcEmbodimentEvidence {
            val normalizedOperatingSystemFamily =
                operatingSystemFamily.trim()

            require(normalizedOperatingSystemFamily.isNotEmpty()) {
                "PC embodiment evidence requires a nonblank operating-system family."
            }

            return PcEmbodimentEvidence(
                operatingSystemFamily =
                    normalizedOperatingSystemFamily,
            )
        }
    }
}
