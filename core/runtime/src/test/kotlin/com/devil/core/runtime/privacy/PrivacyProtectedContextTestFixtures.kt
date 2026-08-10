package com.devil.core.runtime.privacy

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest

internal object PrivacyProtectedContextTestFixtures {

    fun validityRequest(): SessionValidityRequest {
        val observedAt =
            DevilTimestamp.fromEpochMilliseconds(
                2_000L,
            )

        return SessionValidityRequest.create(
            context =
                ContextEnvelope.create(
                    traceId =
                        TraceId.from(
                            "stage-46-privacy-trace",
                        ),
                    schemaVersion =
                        SchemaVersion.from(1),
                    source =
                        ContextSource.SYSTEM,
                    trustLevel =
                        ContextTrustLevel.UNVERIFIED,
                    securityLevel =
                        ContextSecurityLevel.PUBLIC,
                    observedAt =
                        observedAt,
                ),
            session =
                SessionRecord.create(
                    sessionId =
                        SessionId.from(
                            "stage-46-privacy-session",
                        ),
                    subjectIdentityId =
                        IdentityId.from(
                            "stage-46-privacy-subject",
                        ),
                    state =
                        SessionState.ACTIVE,
                    establishedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_000L,
                        ),
                    expiresAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            3_000L,
                        ),
                ),
            observedAt =
                observedAt,
        )
    }
}
