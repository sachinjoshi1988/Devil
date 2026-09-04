package com.devil.app.modelprovider.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DefaultConversationalModelCredentialProvisioningCoordinatorTest {

    @Test
    fun provisionPreservesExactNonBlankCredential() {
        val store = RecordingCredentialStore(
            replaceResult = true,
        )
        val coordinator =
            DefaultConversationalModelCredentialProvisioningCoordinator(
                credentialStore = store,
            )

        val credential = "  inert-stage337b-fixture  "

        val result =
            coordinator.provision(
                credential = credential,
            )

        assertTrue(result)
        assertEquals(
            credential,
            store.replacedCredential,
        )
        assertEquals(
            1,
            store.replaceCount,
        )
        assertEquals(
            0,
            store.clearCount,
        )
    }

    @Test
    fun blankCredentialFailsClosedWithoutStoreMutation() {
        val store = RecordingCredentialStore(
            replaceResult = true,
        )
        val coordinator =
            DefaultConversationalModelCredentialProvisioningCoordinator(
                credentialStore = store,
            )

        val result =
            coordinator.provision(
                credential = "   ",
            )

        assertFalse(result)
        assertNull(store.replacedCredential)
        assertEquals(
            0,
            store.replaceCount,
        )
        assertEquals(
            0,
            store.clearCount,
        )
    }

    @Test
    fun provisionPropagatesStoreFailure() {
        val store = RecordingCredentialStore(
            replaceResult = false,
        )
        val coordinator =
            DefaultConversationalModelCredentialProvisioningCoordinator(
                credentialStore = store,
            )

        val result =
            coordinator.provision(
                credential = "inert-stage337b-fixture",
            )

        assertFalse(result)
        assertEquals(
            1,
            store.replaceCount,
        )
        assertEquals(
            0,
            store.clearCount,
        )
    }

    @Test
    fun removeDelegatesOnlyToLocalCredentialStore() {
        val store = RecordingCredentialStore(
            clearResult = true,
        )
        val coordinator =
            DefaultConversationalModelCredentialProvisioningCoordinator(
                credentialStore = store,
            )

        val result =
            coordinator.remove()

        assertTrue(result)
        assertEquals(
            0,
            store.replaceCount,
        )
        assertEquals(
            1,
            store.clearCount,
        )
    }

    private class RecordingCredentialStore(
        private val replaceResult: Boolean = false,
        private val clearResult: Boolean = false,
    ) : ConversationalModelCredentialStore {

        var replacedCredential: String? = null
            private set

        var replaceCount: Int = 0
            private set

        var clearCount: Int = 0
            private set

        override fun read(): ConversationalModelCredentialStoreResult {
            error(
                "Provisioning coordinator must not read the stored credential.",
            )
        }

        override fun replace(
            credential: String,
        ): Boolean {
            replaceCount += 1
            replacedCredential = credential
            return replaceResult
        }

        override fun clear(): Boolean {
            clearCount += 1
            return clearResult
        }
    }
}
