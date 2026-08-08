package com.devil.app.runtime

import com.devil.core.model.common.TraceId

/**
 * Supplies one fresh trace identity for bounded Android runtime input.
 *
 * Trace generation belongs at the runtime boundary rather than inside immutable
 * core model contracts or constitutional authorities.
 *
 * This provider does not construct constitutional context, access a clock,
 * choose schema version, infer provenance, assign trust or security
 * classification, resolve identity, grant authorization, interpret input,
 * make decisions, plan work, select or execute capabilities, create memory,
 * persist memory, or invoke the UnifiedDevilRuntime.
 *
 * It grants no authority.
 */
interface AndroidTraceIdProvider {

    fun provide(): TraceId
}
