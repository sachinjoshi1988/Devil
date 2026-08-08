package com.devil.app.runtime

import com.devil.core.model.common.TraceId
import java.util.UUID

/**
 * Default Android trace-identity provider.
 *
 * This implementation generates one raw UUID value and delegates validation
 * and representation to the existing TraceId core model contract.
 *
 * The raw generator is injectable only so this bounded adapter can be tested
 * deterministically without changing production behavior.
 *
 * It establishes no constitutional meaning beyond supplying a traceable
 * runtime-flow identity.
 */
class DefaultAndroidTraceIdProvider(
    private val rawTraceIdGenerator: () -> String = {
        UUID.randomUUID().toString()
    },
) : AndroidTraceIdProvider {

    override fun provide(): TraceId {
        return TraceId.from(
            rawTraceIdGenerator(),
        )
    }
}
