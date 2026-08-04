package com.devil.core.runtime.identity

import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityResolutionRecord

/**
 * Translates an established identity-resolution record into the stable runtime
 * identity result contract.
 *
 * This mapper does not collect evidence, resolve identity, authenticate a
 * subject, evaluate trust, establish ownership, or grant authorization.
 */
interface IdentityResolutionResultMapper {

    fun map(
        traceId: TraceId,
        record: IdentityResolutionRecord,
    ): IdentityResult
}
