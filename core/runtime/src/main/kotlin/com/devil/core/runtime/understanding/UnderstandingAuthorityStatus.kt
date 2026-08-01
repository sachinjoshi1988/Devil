package com.devil.core.runtime.understanding

/**
 * Describes the operational result of the Understanding Authority.
 *
 * This status reports whether an UnderstandingRecord was produced. The quality
 * of that understanding belongs to UnderstandingState inside the record.
 */
enum class UnderstandingAuthorityStatus {
    PRODUCED,
    DEFERRED,
    FAILED,
}
