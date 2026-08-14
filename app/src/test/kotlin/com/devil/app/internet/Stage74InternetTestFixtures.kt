package com.devil.app.internet

import com.devil.core.model.common.DevilTimestamp

/**
 * Deterministic retrieval observation time for Stage 74 Internet unit tests.
 *
 * Test fixture only. This does not read the platform clock and does not
 * represent publication time, factual freshness, truth, or trust.
 */
internal fun stage74TestRetrievedAt(): DevilTimestamp {
    return DevilTimestamp.fromEpochMilliseconds(
        1_754_000_200_000L,
    )
}
