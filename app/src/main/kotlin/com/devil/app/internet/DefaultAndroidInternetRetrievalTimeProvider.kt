package com.devil.app.internet

import com.devil.core.model.common.DevilTimestamp

/**
 * Default Android Stage 74 retrieval-time embodiment.
 *
 * Reads the platform wall clock only when the bounded Internet source has
 * genuinely obtained a usable external representation.
 */
class DefaultAndroidInternetRetrievalTimeProvider :
    AndroidInternetRetrievalTimeProvider {

    override fun observedAt(): DevilTimestamp {
        return DevilTimestamp.fromEpochMilliseconds(
            System.currentTimeMillis(),
        )
    }
}
