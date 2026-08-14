package com.devil.core.runtime.embodiment

/**
 * Stage 81 operational status for bounded embodiment representation.
 *
 * REPRESENTED means one valid EmbodimentRecord was structurally represented.
 *
 * It does not mean the embodiment is:
 *
 * - connected;
 * - running;
 * - trusted;
 * - authenticated;
 * - authorized;
 * - session-valid;
 * - capability-registered;
 * - available;
 * - healthy;
 * - ready;
 * - permitted by its operating system;
 * - or allowed to execute.
 *
 * DEFERRED means no valid embodiment representation was produced.
 */
enum class EmbodimentRepresentationStatus {
    REPRESENTED,
    DEFERRED,
}
