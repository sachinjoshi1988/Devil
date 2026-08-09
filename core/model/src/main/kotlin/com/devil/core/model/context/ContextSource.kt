package com.devil.core.model.context

/**
 * Identifies the bounded origin through which context entered Devil.
 *
 * TEXT represents directly supplied textual input.
 *
 * VOICE represents textual content produced through an approved bounded voice
 * input mechanism. VOICE identifies input provenance only. It does not prove
 * speaker identity, authenticate an owner, establish subject trust, grant
 * authorization, or imply that speech recognition was correct.
 *
 * SYSTEM represents bounded system-originated context.
 *
 * TEST exists only for controlled test construction.
 */
enum class ContextSource {
    TEXT,
    VOICE,
    SYSTEM,
    TEST,
}
