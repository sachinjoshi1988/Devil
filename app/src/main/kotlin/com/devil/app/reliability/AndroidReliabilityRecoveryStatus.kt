package com.devil.app.reliability

/**
 * Stage 194 bounded Android Reliability & Recovery integration status.
 *
 * AVAILABLE means Stage 45 produced one bounded RecoveryRequest.
 * DEFERRED means Stage 45 did not produce a recovery request.
 */
enum class AndroidReliabilityRecoveryStatus {
    AVAILABLE,
    DEFERRED,
}
