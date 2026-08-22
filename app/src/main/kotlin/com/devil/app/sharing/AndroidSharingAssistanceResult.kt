package com.devil.app.sharing

/**
 * Stage 189 bounded Clipboard & Sharing assistance result.
 *
 * READY contains exactly one prepared payload.
 * DEFERRED contains no payload.
 *
 * READY does not establish clipboard mutation or external sharing.
 */
@ConsistentCopyVisibility
data class AndroidSharingAssistanceResult private constructor(
    val status: AndroidSharingAssistanceStatus,
    val payload: AndroidSharingPayload?,
) {
    companion object {
        fun create(
            status: AndroidSharingAssistanceStatus,
            payload: AndroidSharingPayload? = null,
        ): AndroidSharingAssistanceResult {
            when (status) {
                AndroidSharingAssistanceStatus.READY ->
                    require(payload != null) {
                        "Ready Android sharing assistance requires one payload."
                    }

                AndroidSharingAssistanceStatus.DEFERRED ->
                    require(payload == null) {
                        "Deferred Android sharing assistance must not contain a payload."
                    }
            }

            return AndroidSharingAssistanceResult(
                status = status,
                payload = payload,
            )
        }
    }
}
