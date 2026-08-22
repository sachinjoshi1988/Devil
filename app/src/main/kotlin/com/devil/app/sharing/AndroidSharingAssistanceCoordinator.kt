package com.devil.app.sharing

/**
 * Stage 189 bounded Clipboard & Sharing coordinator.
 *
 * It accepts only explicitly supplied text.
 *
 * It does not:
 *
 * - read or write Android clipboard;
 * - call ClipboardManager or create ClipData;
 * - create or launch ACTION_SEND;
 * - select a sharing destination;
 * - grant Devil authorization;
 * - establish constitutional Execution APPROVED;
 * - establish that anything was shared;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 190 Camera Capability.
 *
 * SHARE_READY != SHARED.
 * CLIPBOARD_READY != CLIPBOARD_WRITTEN.
 * SHARED != VERIFIED_OUTCOME.
 */
class AndroidSharingAssistanceCoordinator {
    fun prepare(
        text: String?,
    ): AndroidSharingAssistanceResult {
        if (text == null) {
            return AndroidSharingAssistanceResult.create(
                status = AndroidSharingAssistanceStatus.DEFERRED,
            )
        }

        return AndroidSharingAssistanceResult.create(
            status = AndroidSharingAssistanceStatus.READY,
            payload = AndroidSharingPayload.create(text),
        )
    }
}
