package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp

/**
 * Supplies one observed timestamp for bounded Android runtime input.
 *
 * Clock access belongs at the Android runtime boundary rather than inside
 * core model or constitutional runtime authorities.
 *
 * This provider does not generate trace identity, construct constitutional
 * context, choose schema version, infer provenance, assign trust or security
 * classification, resolve identity, grant authorization, interpret input,
 * make decisions, execute capabilities, or create or persist logical memory.
 *
 * It grants no authority.
 */
interface AndroidObservationTimeProvider {

    fun observe(): DevilTimestamp
}
