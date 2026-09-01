package com.devil.app.authentication

import com.devil.core.model.security.SessionRecord

/**
 * Stage 314 process-local owner-alpha security-session store.
 *
 * This store preserves at most one already-established bounded SessionRecord.
 *
 * It does not:
 *
 * - authenticate a subject;
 * - prove owner identity;
 * - create SessionRecord values;
 * - determine session validity;
 * - renew or extend sessions;
 * - grant authorization;
 * - enter Owner Mode;
 * - approve high-security confirmation;
 * - execute capabilities;
 * - or establish Outcome.
 *
 * Session validity remains owned by Devil's existing Session Validity Authority.
 *
 * STORED_SESSION != VALID_SESSION.
 * ACTIVE_SESSION_RECORD != AUTHORIZATION.
 */
class Stage314OwnerSessionStore {

    private var session: SessionRecord? = null

    @Synchronized
    fun replace(
        session: SessionRecord,
    ) {
        this.session = session
    }

    @Synchronized
    fun current(): SessionRecord? {
        return session
    }

    @Synchronized
    fun clear() {
        session = null
    }
}
