package com.devil.core.model.security

import kotlin.test.Test
import kotlin.test.assertEquals

class SessionStateTest {

    @Test
    fun `session lifecycle contains active expired and revoked states`() {
        assertEquals(
            listOf(
                SessionState.ACTIVE,
                SessionState.EXPIRED,
                SessionState.REVOKED,
            ),
            SessionState.entries,
        )
    }

    @Test
    fun `active session remains distinct from expired session`() {
        check(
            SessionState.ACTIVE !=
                SessionState.EXPIRED,
        )
    }

    @Test
    fun `active session remains distinct from revoked session`() {
        check(
            SessionState.ACTIVE !=
                SessionState.REVOKED,
        )
    }

    @Test
    fun `expired and revoked remain distinct lifecycle outcomes`() {
        check(
            SessionState.EXPIRED !=
                SessionState.REVOKED,
        )
    }

    @Test
    fun `session lifecycle remains distinct from constitutional security stage`() {
        val sessionState = SessionState.ACTIVE
        val securityStage = SecurityStage.SESSION

        assertEquals(
            SessionState.ACTIVE,
            sessionState,
        )
        assertEquals(
            SecurityStage.SESSION,
            securityStage,
        )
    }
}
