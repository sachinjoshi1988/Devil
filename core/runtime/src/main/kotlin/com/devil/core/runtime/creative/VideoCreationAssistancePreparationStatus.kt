package com.devil.core.runtime.creative

/**
 * Stage 173 bounded Video Creation Assistance preparation status.
 *
 * PREPARED means one structurally valid provider-neutral text-to-video or image-to-video
 * assistance context was prepared from preserved Stage 171 and Stage 172 provenance plus
 * explicitly supplied video-creation metadata.
 *
 * PREPARED does not mean:
 *
 * - image bytes were inspected;
 * - a video provider or model was selected or invoked;
 * - frames or video were generated;
 * - MP4 or another requested file exists;
 * - motion or camera direction was executed;
 * - lip synchronization occurred;
 * - video editing or rendering occurred;
 * - generated media was verified;
 * - publishing was authorized;
 * - Stage 174 behavior was implemented;
 * - or constitutional Verification occurred.
 *
 * DEFERRED means no truthful Video Creation Assistance context was produced.
 */
enum class VideoCreationAssistancePreparationStatus {
    PREPARED,
    DEFERRED,
}
