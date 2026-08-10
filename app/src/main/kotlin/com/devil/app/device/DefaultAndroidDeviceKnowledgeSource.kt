package com.devil.app.device

import android.os.Build

/**
 * Default Stage 40 Android platform source for bounded device knowledge.
 *
 * Only directly exposed, non-sensitive Build facts approved by Stage 40 are
 * collected here.
 *
 * No persistent hardware or user identifier is collected.
 */
class DefaultAndroidDeviceKnowledgeSource :
    AndroidDeviceKnowledgeSource {

    override fun snapshot(): AndroidDeviceKnowledgeSnapshot {
        return AndroidDeviceKnowledgeSnapshot.create(
            sdkInt = Build.VERSION.SDK_INT,
            androidRelease = Build.VERSION.RELEASE,
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            device = Build.DEVICE,
            product = Build.PRODUCT,
        )
    }
}
