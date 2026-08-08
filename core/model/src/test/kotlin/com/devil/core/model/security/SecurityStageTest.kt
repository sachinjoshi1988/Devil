package com.devil.core.model.security

import kotlin.test.Test
import kotlin.test.assertEquals

class SecurityStageTest {

    @Test
    fun `security stages preserve constitutional ordering`() {
        assertEquals(
            listOf(
                SecurityStage.LOCKED,
                SecurityStage.WAKE,
                SecurityStage.AUTHENTICATION,
                SecurityStage.SESSION,
                SecurityStage.OWNER_MODE,
                SecurityStage.HIGH_SECURITY_CONFIRMATION,
            ),
            SecurityStage.entries,
        )
    }

    @Test
    fun `locked is the first constitutional security stage`() {
        assertEquals(
            SecurityStage.LOCKED,
            SecurityStage.entries.first(),
        )
    }

    @Test
    fun `wake remains distinct from authentication`() {
        val wake = SecurityStage.WAKE
        val authentication = SecurityStage.AUTHENTICATION

        check(wake != authentication)
    }

    @Test
    fun `owner mode remains distinct from high security confirmation`() {
        val ownerMode = SecurityStage.OWNER_MODE
        val highSecurityConfirmation =
            SecurityStage.HIGH_SECURITY_CONFIRMATION

        check(ownerMode != highSecurityConfirmation)
    }
}
