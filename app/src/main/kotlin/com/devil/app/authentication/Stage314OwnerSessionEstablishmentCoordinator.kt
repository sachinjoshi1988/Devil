package com.devil.app.authentication

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState

/**
 * Stage 314 bounded owner-alpha session-establishment mechanism.
 *
 * This coordinator may establish one ACTIVE SessionRecord only from explicitly
 * supplied genuine-authentication success handling at the Android boundary.
 *
 * It owns only structural establishment of the bounded session record and
 * process-local storage.
 *
 * It does not:
 *
 * - perform Android authentication;
 * - infer owner identity;
 * - determine session validity;
 * - grant authorization;
 * - advance SecurityStage;
 * - enter Owner Mode;
 * - renew or persist sessions;
 * - execute capabilities;
 * - or establish Outcome.
 *
 * AUTHENTICATION_SUCCESS != SESSION_VALID.
 * SESSION_ESTABLISHED != AUTHORIZATION.
 */
class Stage314OwnerSessionEstablishmentCoordinator(
    private val sessionStore: Stage314OwnerSessionStore,
    private val sessionIdProvider: () -> String,
    private val timeProvider: () -> Long,
) {

    fun establish(
        subjectIdentityId: IdentityId,
        validityDurationMilliseconds: Long,
    ): SessionRecord {
        require(validityDurationMilliseconds > 0L) {
            "Stage 314 owner session validity duration must be positive."
        }

        val establishedAtMilliseconds =
            timeProvider()

        require(establishedAtMilliseconds >= 0L) {
            "Stage 314 owner session establishment time must not precede the Unix epoch."
        }

        require(
            validityDurationMilliseconds <=
                Long.MAX_VALUE - establishedAtMilliseconds,
        ) {
            "Stage 314 owner session expiration must not overflow."
        }

        val session =
            SessionRecord.create(
                sessionId =
                    SessionId.from(
                        sessionIdProvider(),
                    ),
                subjectIdentityId =
                    subjectIdentityId,
                state =
                    SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        establishedAtMilliseconds,
                    ),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        establishedAtMilliseconds +
                            validityDurationMilliseconds,
                    ),
            )

        sessionStore.replace(
            session,
        )

        return session
    }
}
