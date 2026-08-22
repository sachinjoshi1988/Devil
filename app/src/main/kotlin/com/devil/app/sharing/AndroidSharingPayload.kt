package com.devil.app.sharing

/**
 * Stage 189 bounded Clipboard & Sharing payload.
 *
 * It preserves only explicitly supplied text.
 *
 * It does not write to Android clipboard, create an ACTION_SEND Intent,
 * launch an activity, share externally, or establish execution/outcome.
 *
 * SHARE_READY != SHARED.
 * CLIPBOARD_READY != CLIPBOARD_WRITTEN.
 */
@ConsistentCopyVisibility
data class AndroidSharingPayload private constructor(
    val text: String,
) {
    companion object {
        fun create(
            text: String,
        ): AndroidSharingPayload {
            val normalizedText = text.trim()

            require(normalizedText.isNotEmpty()) {
                "Android sharing payload text must not be blank."
            }

            return AndroidSharingPayload(
                text = normalizedText,
            )
        }
    }
}
