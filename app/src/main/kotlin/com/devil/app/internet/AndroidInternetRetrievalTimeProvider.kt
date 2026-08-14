package com.devil.app.internet

import com.devil.core.model.common.DevilTimestamp

/**
 * Stage 74 boundary for observing when one bounded Internet representation was
 * actually retrieved by the Android Internet embodiment.
 *
 * This timestamp describes Devil's retrieval observation time only.
 *
 * It does not establish:
 *
 * - source publication time;
 * - source freshness;
 * - factual freshness;
 * - factual truth;
 * - source trust;
 * - constitutional authority;
 * - Learning;
 * - Memory eligibility;
 * - or verified Outcome.
 *
 * Clock access remains outside immutable model types.
 */
fun interface AndroidInternetRetrievalTimeProvider {

    fun observedAt(): DevilTimestamp
}
