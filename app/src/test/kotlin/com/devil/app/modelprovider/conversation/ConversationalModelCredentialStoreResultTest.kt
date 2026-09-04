package com.devil.app.modelprovider.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ConversationalModelCredentialStoreResultTest {

    @Test
    fun available_preservesExactlyOneExplicitCredential() {
        val credential =
            " owner-provisioned-test-credential "

        val result =
            ConversationalModelCredentialStoreResult.available(
                credential = credential,
            )

        assertEquals(
            ConversationalModelCredentialStoreStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            credential,
            result.credentialOrNull(),
        )
    }

    @Test
    fun unavailable_containsNoCredential() {
        val result =
            ConversationalModelCredentialStoreResult.unavailable()

        assertEquals(
            ConversationalModelCredentialStoreStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(
            result.credentialOrNull(),
        )
    }

    @Test
    fun available_rejectsBlankCredential() {
        assertFailsWith<IllegalArgumentException> {
            ConversationalModelCredentialStoreResult.available(
                credential = "   ",
            )
        }
    }

    @Test
    fun stringRepresentationDoesNotExposeCredential() {
        val credential =
            "secret-test-credential-never-log-this"

        val result =
            ConversationalModelCredentialStoreResult.available(
                credential = credential,
            )

        assertFalse(
            result.toString().contains(credential),
        )

        assertEquals(
            "ConversationalModelCredentialStoreResult(status=AVAILABLE)",
            result.toString(),
        )
    }
}
