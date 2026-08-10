package com.devil.app.device

/**
 * Stage 40 bounded Device Knowledge query types.
 *
 * These query types select only already-approved fields from one genuine
 * AndroidDeviceKnowledgeSnapshot.
 *
 * They do not interpret conversation text, infer user intent, authenticate a
 * subject, grant authorization, perform an Android action, create memory, or
 * establish an Outcome.
 */
enum class AndroidDeviceKnowledgeQueryType {
    DEVICE_SUMMARY,
    ANDROID_VERSION,
    DEVICE_MODEL,
}
