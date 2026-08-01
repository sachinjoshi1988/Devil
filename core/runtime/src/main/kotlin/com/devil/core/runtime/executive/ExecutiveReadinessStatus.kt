package com.devil.core.runtime.executive

/**
 * Describes the operational result of Executive readiness evaluation.
 *
 * A ready result permits the runtime to approach the execution boundary. It
 * does not itself execute a capability or claim execution success.
 */
enum class ExecutiveReadinessStatus {
    READY,
    DEFERRED,
    FAILED,
}
