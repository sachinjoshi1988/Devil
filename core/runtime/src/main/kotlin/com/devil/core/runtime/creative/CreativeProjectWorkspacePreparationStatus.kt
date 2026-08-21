package com.devil.core.runtime.creative

/**
 * Stage 174 bounded Creative Project Workspace preparation status.
 *
 * PREPARED means one structurally valid workspace representation was prepared
 * from one exact existing Stage 173 Video Creation Assistance context and
 * explicitly supplied creative continuity metadata.
 *
 * PREPARED does not mean:
 *
 * - workspace state was persisted;
 * - files or generated assets exist;
 * - character, location, shot, or episode continuity was verified;
 * - constitutional Memory was created or persisted;
 * - media generation occurred;
 * - execution occurred;
 * - or constitutional Verification occurred.
 *
 * DEFERRED means no truthful Creative Project Workspace representation was produced.
 *
 * WORKSPACE_PREPARED != WORKSPACE_PERSISTED.
 * CREATIVE_WORKSPACE != MEMORY.
 * PREPARED != EXECUTION.
 */
enum class CreativeProjectWorkspacePreparationStatus {
    PREPARED,
    DEFERRED,
}
