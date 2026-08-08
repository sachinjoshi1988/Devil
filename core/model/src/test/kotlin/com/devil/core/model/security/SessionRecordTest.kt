package com.devil.core.model.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SessionRecordTest {

    @Test
    fun `create preserves bounded active session data`() {
        val sessionId = SessionId.from(
            "session-record-001",
        )
        val subjectIdentityId = IdentityId.from(
            "subject-session-001",
        )
        val establishedAt =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_196_000L,
            )
        val expiresAt =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_003_796_000L,
            )

        val record = SessionRecord.create(
            sessionId = sessionId,
            subjectIdentityId = subjectIdentityId,
            state = SessionState.ACTIVE,
            establishedAt = establishedAt,
            expiresAt = expiresAt,
        )

        assertEquals(sessionId, record.sessionId)
        assertEquals(
            subjectIdentityId,
            record.subjectIdentityId,
        )
        assertEquals(
            SessionState.ACTIVE,
            record.state,
        )
        assertEquals(
            establishedAt,
            record.establishedAt,
        )
        assertEquals(
            expiresAt,
            record.expiresAt,
        )
    }

    @Test
    fun `expired session remains an explicit lifecycle state`() {
        val record = SessionRecord.create(
            sessionId = SessionId.from(
                "session-record-002",
            ),
            subjectIdentityId = IdentityId.from(
                "subject-session-002",
            ),
            state = SessionState.EXPIRED,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_196_000L,
                ),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_197_000L,
                ),
        )

        assertEquals(
            SessionState.EXPIRED,
            record.state,
        )
    }

    @Test
    fun `revoked session remains distinct from expiration`() {
        val record = SessionRecord.create(
            sessionId = SessionId.from(
                "session-record-003",
            ),
            subjectIdentityId = IdentityId.from(
                "subject-session-003",
            ),
            state = SessionState.REVOKED,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_196_000L,
                ),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_003_796_000L,
                ),
        )

        assertEquals(
            SessionState.REVOKED,
            record.state,
        )

        check(record.state != SessionState.EXPIRED)
    }

    @Test
    fun `session lifecycle remains represented only by session state`() {
        val record = SessionRecord.create(
            sessionId = SessionId.from(
                "session-record-004",
            ),
            subjectIdentityId = IdentityId.from(
                "subject-session-004",
            ),
            state = SessionState.ACTIVE,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_196_000L,
                ),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_003_796_000L,
                ),
        )

        assertEquals(
            SessionState.ACTIVE,
            record.state,
        )
    }

    @Test
    fun `create rejects expiration equal to establishment time`() {
        val timestamp =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_196_000L,
            )

        assertFailsWith<IllegalArgumentException> {
            SessionRecord.create(
                sessionId = SessionId.from(
                    "session-record-005",
                ),
                subjectIdentityId = IdentityId.from(
                    "subject-session-005",
                ),
                state = SessionState.ACTIVE,
                establishedAt = timestamp,
                expiresAt = timestamp,
            )
        }
    }

    @Test
    fun `create rejects expiration before establishment time`() {
        assertFailsWith<IllegalArgumentException> {
            SessionRecord.create(
                sessionId = SessionId.from(
                    "session-record-006",
                ),
                subjectIdentityId = IdentityId.from(
                    "subject-session-006",
                ),
                state = SessionState.ACTIVE,
                establishedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_196_000L,
                    ),
                expiresAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_195_999L,
                    ),
            )
        }
    }
}
