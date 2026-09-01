package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Stage 315 focused evidence for the Android hands-free authentication handoff.
 *
 * REQUIRED means that the genuine Android authentication boundary must run.
 *
 * REQUIRED != AUTHENTICATED.
 * REQUIRED != ACTIVE_SESSION.
 * REQUIRED != AUTHORIZATION.
 *
 * This test does not perform Android authentication and does not establish
 * a Devil security session.
 */
class Stage315AndroidHandsFreeAuthenticationHandoffTest {

    @Test
    fun `hands free authentication handoff requires genuine Android authentication`() {
        val result =
            Stage315AndroidHandsFreeAuthenticationHandoff()
                .requestAuthentication()

        assertEquals(
            HandsFreeAuthenticationHandoffStatus.REQUIRED,
            result.status,
        )

        assertEquals(
            "Android owner authentication is required for hands-free continuation.",
            result.message,
        )
    }
}
