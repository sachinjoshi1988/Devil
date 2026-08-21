package com.devil.app.communication

/**
 * Stage 184 bounded communication recipient.
 *
 * This record preserves only recipient metadata explicitly supplied by an
 * upstream caller.
 *
 * It does not:
 *
 * - query or represent an Android Contacts Provider record;
 * - establish that the recipient exists;
 * - authenticate or verify a human identity;
 * - infer a recipient from conversation text;
 * - grant communication authorization;
 * - send a message or place a call.
 *
 * SUPPLIED_CONTACT != ANDROID_CONTACT_RECORD.
 * RECIPIENT_AVAILABLE != IDENTITY_VERIFIED.
 * RECIPIENT_AVAILABLE != COMMUNICATION_AUTHORIZED.
 */
@ConsistentCopyVisibility
data class AndroidCommunicationRecipient private constructor(
    val displayName: String,
    val address: String,
) {
    companion object {
        fun create(
            displayName: String,
            address: String,
        ): AndroidCommunicationRecipient {
            val normalizedDisplayName =
                displayName.trim()

            val normalizedAddress =
                address.trim()

            require(normalizedDisplayName.isNotEmpty()) {
                "Android communication recipient display name must not be blank."
            }

            require(normalizedAddress.isNotEmpty()) {
                "Android communication recipient address must not be blank."
            }

            return AndroidCommunicationRecipient(
                displayName = normalizedDisplayName,
                address = normalizedAddress,
            )
        }
    }
}
