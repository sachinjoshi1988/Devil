package com.devil.app.device

/**
 * Supplies one bounded snapshot of genuine Android device/platform facts.
 *
 * Implementations may observe Android platform state only.
 *
 * They must not infer owner identity, authenticate a subject, grant authority,
 * perform an Android action, persist logical memory, or claim a verified
 * Outcome.
 */
fun interface AndroidDeviceKnowledgeSource {

    fun snapshot(): AndroidDeviceKnowledgeSnapshot
}
