package com.devil.core.model.child

/**
 * Stage 44 bounded classification describing whether approved upstream
 * information currently classifies one subject for child-policy purposes.
 *
 * This classification is policy context only.
 *
 * CHILD
 * != identity resolution
 * != age proof
 * != authentication
 * != ownership
 * != guardian authority
 * != trust
 * != authorization
 * != Owner Mode
 * != permission to execute.
 *
 * Devil must not infer CHILD merely from appearance, voice, profile name,
 * relationship label, device ownership, camera perception, or conversational
 * behavior.
 */
enum class ChildSubjectClassification {
    CHILD,
    NOT_CHILD,
    UNKNOWN,
}
