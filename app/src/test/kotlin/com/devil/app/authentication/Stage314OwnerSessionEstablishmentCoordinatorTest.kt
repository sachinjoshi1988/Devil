package com.devil.app.authentication

import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertFailsWith

class Stage314OwnerSessionEstablishmentCoordinatorTest {

    @Test
    fun `establishment creates one bounded active session for supplied identity`() {
        val store =
            Stage314OwnerSessionStore()

        val subjectIdentityId =
            IdentityId.from(
                "android-primary-local-subject",
            )

        val coordinator =
            Stage314OwnerSessionEstablishmentCoordinator(
                sessionStore = store,
                sessionIdProvider = {
                    "stage314-owner-session-test"
                },
                timeProvider = {
                    1_000L
                },
            )

        val result =
            coordinator.establish(
                subjectIdentityId = subjectIdentityId,
                validityDurationMilliseconds = 5_000L,
            )

        assertEquals(
            "stage314-owner-session-test",
            result.sessionId.value,
        )
        assertSame(
            subjectIdentityId,
            result.subjectIdentityId,
        )
        assertEquals(
            SessionState.ACTIVE,
            result.state,
        )
        assertEquals(
            1_000L,
            result.establishedAt.epochMilliseconds,
        )
        assertEquals(
            6_000L,
            result.expiresAt.epochMilliseconds,
        )
        assertSame(
            result,
            store.current(),
        )
    }

    @Test
    fun `store contains no session before genuine establishment`() {
        val store =
            Stage314OwnerSessionStore()

        assertNull(
            store.current(),
        )
    }

    @Test
    fun `clear removes process-local session`() {
        val store =
            Stage314OwnerSessionStore()

        val coordinator =
            Stage314OwnerSessionEstablishmentCoordinator(
                sessionStore = store,
                sessionIdProvider = {
                    "stage314-owner-session-clear-test"
                },
                timeProvider = {
                    10_000L
                },
            )

        coordinator.establish(
            subjectIdentityId =
                IdentityId.from(
                    "android-primary-local-subject",
                ),
            validityDurationMilliseconds =
                10_000L,
        )

        store.clear()

        assertNull(
            store.current(),
        )
    }

    @Test
    fun `non positive validity duration is rejected`() {
        val coordinator =
            Stage314OwnerSessionEstablishmentCoordinator(
                sessionStore =
                    Stage314OwnerSessionStore(),
                sessionIdProvider = {
                    "stage314-owner-session-invalid-duration"
                },
                timeProvider = {
                    1_000L
                },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.establish(
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                validityDurationMilliseconds =
                    0L,
            )
        }
    }

    @Test
    fun `expiration overflow is rejected without storing session`() {
        val store =
            Stage314OwnerSessionStore()

        val coordinator =
            Stage314OwnerSessionEstablishmentCoordinator(
                sessionStore = store,
                sessionIdProvider = {
                    "stage314-owner-session-overflow"
                },
                timeProvider = {
                    Long.MAX_VALUE - 5L
                },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.establish(
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                validityDurationMilliseconds =
                    10L,
            )
        }

        assertNull(
            store.current(),
        )
    }
}
