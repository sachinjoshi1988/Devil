package com.devil.core.runtime.embodiment

/**
 * Stage 83 bounded PC-embodiment assessment status.
 *
 * PC means supplied architectural evidence identifies the represented embodiment
 * as belonging to the PC platform family.
 *
 * NON_PC means supplied architectural evidence was available but the represented
 * embodiment does not belong to that platform family.
 *
 * DEFERRED means a truthful assessment could not be made.
 *
 * PC does not mean:
 *
 * - another Devil intelligence exists;
 * - another Brain exists;
 * - another Unified Devil Runtime exists;
 * - a desktop application exists;
 * - Windows, Linux, or macOS execution exists;
 * - the embodiment is reachable;
 * - authentication succeeded;
 * - authorization exists;
 * - capabilities are available;
 * - execution is permitted;
 * - or an Outcome has occurred.
 *
 * PC_PLATFORM != INTELLIGENCE.
 * PC_PLATFORM != AUTHORITY.
 * PC_PLATFORM != EXECUTION.
 */
enum class PcEmbodimentAssessmentStatus {
    PC,
    NON_PC,
    DEFERRED,
}
