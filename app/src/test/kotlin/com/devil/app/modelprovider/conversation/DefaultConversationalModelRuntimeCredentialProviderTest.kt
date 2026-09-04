package com.devil.app.modelprovider.conversation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultConversationalModelRuntimeCredentialProviderTest {

    @Test
    fun availableCredentialIsPreservedExactly() {
        val credential =
            " exact-owner-provisioned-test-credential "

        val provider =
            DefaultConversationalModelRuntimeCredentialProvider(
                credentialStore =
                    FakeCredentialStore(
                        result =
                            ConversationalModelCredentialStoreResult.available(
                                credential = credential,
                            ),
                    ),
            )

        assertEquals(
            credential,
            provider.credential(),
        )
    }

    @Test
    fun unavailableStoreFailsClosedWithoutCredential() {
        val provider =
            DefaultConversationalModelRuntimeCredentialProvider(
                credentialStore =
                    FakeCredentialStore(
                        result =
                            ConversationalModelCredentialStoreResult.unavailable(),
                    ),
            )

        assertNull(
            provider.credential(),
        )
    }

    private class FakeCredentialStore(
        private val result: ConversationalModelCredentialStoreResult,
    ) : ConversationalModelCredentialStore {

        override fun read(): ConversationalModelCredentialStoreResult {
            return result
        }

        override fun replace(
            credential: String,
        ): Boolean {
            error("replace() is outside this read-only test boundary.")
        }

        override fun clear(): Boolean {
            error("clear() is outside this read-only test boundary.")
        }
    }
}
