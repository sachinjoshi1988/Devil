package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp

/**
 * Default Android observation-time provider.
 *
 * This implementation reads the current wall-clock time in epoch
 * milliseconds and delegates timestamp validation to DevilTimestamp.
 *
 * The clock source is injectable only so this bounded adapter can be tested
 * deterministically without changing production behavior.
 *
 * It creates no constitutional meaning beyond representing the observed
 * timestamp.
 */
class DefaultAndroidObservationTimeProvider(
    private val currentTimeMilliseconds: () -> Long = System::currentTimeMillis,
) : AndroidObservationTimeProvider {

    override fun observe(): DevilTimestamp {
        return DevilTimestamp.fromEpochMilliseconds(
            currentTimeMilliseconds(),
        )
    }
}
