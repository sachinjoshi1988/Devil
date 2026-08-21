package com.devil.core.runtime.creative

/**
 * Stage 170 bounded Story-to-Storyboard preparation status.
 *
 * PREPARED means one structurally valid Story-to-Storyboard context was prepared
 * from one exact Stage 169 Story Creation context, one explicitly supplied story,
 * one explicitly supplied ordered storyboard-scene sequence, and one objective.
 *
 * PREPARED does not mean:
 *
 * - Stage 169 generated the supplied story;
 * - storyboard panels or images were generated;
 * - detailed shot planning or animation timing occurred;
 * - providers or models were selected or invoked;
 * - capabilities were authorized or executed;
 * - generated content was verified;
 * - publishing was authorized;
 * - Stage 171–174 behavior was implemented;
 * - or constitutional Verification occurred.
 *
 * DEFERRED means no truthful Story-to-Storyboard context was produced.
 */
enum class StoryToStoryboardPreparationStatus {
    PREPARED,
    DEFERRED,
}
