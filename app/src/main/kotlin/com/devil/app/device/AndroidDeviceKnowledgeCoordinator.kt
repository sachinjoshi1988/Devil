package com.devil.app.device

/**
 * Bounded Stage 40 coordinator for Android Device Knowledge.
 *
 * The coordinator requests one snapshot from the supplied Android source and
 * returns that snapshot unchanged.
 *
 * It is not another Brain, runtime, Conversation Domain, Security Authority,
 * Memory Authority, or execution path.
 *
 * Reading device knowledge does not authorize changing device state.
 */
class AndroidDeviceKnowledgeCoordinator(
    private val source: AndroidDeviceKnowledgeSource =
        DefaultAndroidDeviceKnowledgeSource(),
) {

    fun snapshot(): AndroidDeviceKnowledgeSnapshot {
        return source.snapshot()
    }
}
