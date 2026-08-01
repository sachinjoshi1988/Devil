package com.devil.core.runtime

/**
 * Describes the constitutional result of accepting work into the runtime.
 *
 * This status does not describe execution progress or verified task outcomes.
 */
enum class RuntimeStatus {
    ACCEPTED,
    REJECTED,
    DEFERRED,
}
