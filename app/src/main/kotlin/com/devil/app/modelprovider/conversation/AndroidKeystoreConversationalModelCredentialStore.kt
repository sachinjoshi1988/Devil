package com.devil.app.modelprovider.conversation

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Stage 337B Android-private conversational-model credential store.
 *
 * The bearer credential is encrypted with AES-GCM before it is written to
 * app-private SharedPreferences. The AES key is generated inside Android
 * Keystore and is not written into the APK, BuildConfig, resources, files,
 * preferences, logs, or model configuration.
 *
 * The store preserves the exact explicitly supplied non-blank credential.
 * Credential interpretation and provider validation remain outside this
 * storage boundary.
 *
 * This class deliberately does not:
 *
 * - authenticate an owner;
 * - grant Devil authorization;
 * - select a provider;
 * - validate provider credentials;
 * - invoke a conversational model;
 * - execute Devil capabilities;
 * - establish Observation, Verification, or Outcome;
 * - perform Learning;
 * - create or persist Devil Memory.
 *
 * KEY_AVAILABLE != OWNER_AUTHENTICATED.
 * CREDENTIAL_STORED != CREDENTIAL_VALID.
 * CREDENTIAL_AVAILABLE != PROVIDER_AVAILABLE.
 * CREDENTIAL_AVAILABLE != DEVIL_AUTHORIZATION.
 * GENERATED != VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
class AndroidKeystoreConversationalModelCredentialStore(
    context: Context,
) : ConversationalModelCredentialStore {

    private val preferences =
        context.applicationContext.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE,
        )

    override fun read(): ConversationalModelCredentialStoreResult {
        return runCatching {
            val encodedIv =
                preferences.getString(
                    KEY_ENCRYPTED_IV,
                    null,
                )
                    ?: return ConversationalModelCredentialStoreResult.unavailable()

            val encodedCiphertext =
                preferences.getString(
                    KEY_ENCRYPTED_CIPHERTEXT,
                    null,
                )
                    ?: return ConversationalModelCredentialStoreResult.unavailable()

            val iv =
                Base64.getDecoder().decode(
                    encodedIv,
                )

            val ciphertext =
                Base64.getDecoder().decode(
                    encodedCiphertext,
                )

            if (iv.isEmpty() || ciphertext.isEmpty()) {
                return ConversationalModelCredentialStoreResult.unavailable()
            }

            val cipher =
                Cipher.getInstance(
                    TRANSFORMATION,
                )

            cipher.init(
                Cipher.DECRYPT_MODE,
                existingKey()
                    ?: return ConversationalModelCredentialStoreResult.unavailable(),
                GCMParameterSpec(
                    GCM_TAG_LENGTH_BITS,
                    iv,
                ),
            )

            val credential =
                cipher
                    .doFinal(
                        ciphertext,
                    )
                    .toString(
                        StandardCharsets.UTF_8,
                    )

            if (credential.isBlank()) {
                ConversationalModelCredentialStoreResult.unavailable()
            } else {
                ConversationalModelCredentialStoreResult.available(
                    credential = credential,
                )
            }
        }.getOrElse {
            ConversationalModelCredentialStoreResult.unavailable()
        }
    }

    override fun replace(
        credential: String,
    ): Boolean {
        if (credential.isBlank()) {
            return false
        }

        return runCatching {
            val cipher =
                Cipher.getInstance(
                    TRANSFORMATION,
                )

            cipher.init(
                Cipher.ENCRYPT_MODE,
                existingKey() ?: createKey(),
            )

            val ciphertext =
                cipher.doFinal(
                    credential.toByteArray(
                        StandardCharsets.UTF_8,
                    ),
                )

            val encodedIv =
                Base64.getEncoder().encodeToString(
                    cipher.iv,
                )

            val encodedCiphertext =
                Base64.getEncoder().encodeToString(
                    ciphertext,
                )

            preferences
                .edit()
                .putString(
                    KEY_ENCRYPTED_IV,
                    encodedIv,
                )
                .putString(
                    KEY_ENCRYPTED_CIPHERTEXT,
                    encodedCiphertext,
                )
                .commit()
        }.getOrDefault(
            false,
        )
    }

    override fun clear(): Boolean {
        return runCatching {
            preferences
                .edit()
                .remove(
                    KEY_ENCRYPTED_IV,
                )
                .remove(
                    KEY_ENCRYPTED_CIPHERTEXT,
                )
                .commit()
        }.getOrDefault(
            false,
        )
    }

    private fun existingKey(): SecretKey? {
        val keyStore =
            KeyStore.getInstance(
                ANDROID_KEYSTORE,
            )
                .apply {
                    load(
                        null,
                    )
                }

        return keyStore.getKey(
            KEY_ALIAS,
            null,
        ) as? SecretKey
    }

    private fun createKey(): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE,
            )

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(
                    KeyProperties.BLOCK_MODE_GCM,
                )
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE,
                )
                .setKeySize(
                    AES_KEY_SIZE_BITS,
                )
                .build(),
        )

        return keyGenerator.generateKey()
    }

    companion object {
        private const val ANDROID_KEYSTORE =
            "AndroidKeyStore"

        private const val KEY_ALIAS =
            "devil_conversational_model_credential_key_v1"

        private const val PREFERENCES_NAME =
            "devil_conversational_model_credential_v1"

        private const val KEY_ENCRYPTED_IV =
            "encrypted_iv"

        private const val KEY_ENCRYPTED_CIPHERTEXT =
            "encrypted_ciphertext"

        private const val TRANSFORMATION =
            "AES/GCM/NoPadding"

        private const val AES_KEY_SIZE_BITS =
            256

        private const val GCM_TAG_LENGTH_BITS =
            128
    }
}
