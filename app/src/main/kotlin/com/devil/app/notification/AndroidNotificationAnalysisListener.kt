package com.devil.app.notification

/**
 * Receives one bounded Stage 39 notification-analysis result.
 *
 * Receipt of an analysis result grants no authority.
 *
 * Implementations must not reinterpret ANALYZED as permission to:
 *
 * - submit notification text to the Unified Devil Runtime;
 * - create conversation input;
 * - interrupt the user;
 * - speak notification content;
 * - persist notification content;
 * - create memory;
 * - execute a capability;
 * - or claim success.
 */
fun interface AndroidNotificationAnalysisListener {

    fun onAnalysis(
        result: AndroidNotificationAnalysisResult,
    )
}
