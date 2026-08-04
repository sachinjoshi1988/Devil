package com.devil.core.runtime.understanding

/**
 * Describes whether a structured understanding-evaluation request is available.
 *
 * This status does not interpret language, infer intent, produce understanding,
 * create memory, select decisions, plan work, execute capabilities, or verify
 * outcomes.
 */
enum class UnderstandingEvaluationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
