package com.devil.core.model.memory

/**
 * Constitutional classification of one logical-memory representation.
 *
 * The classification describes memory-domain meaning only.
 *
 * A MemoryClass does not:
 *
 * - approve memory;
 * - establish truth;
 * - establish authentication;
 * - establish authorization;
 * - grant access;
 * - establish persistence;
 * - select storage;
 * - establish retention enforcement;
 * - establish privacy-disclosure permission;
 * - or authorize execution.
 *
 * MEMORY_CLASS != MEMORY_AUTHORITY_APPROVAL.
 * MEMORY_CLASS != AUTHORIZATION.
 * MEMORY_CLASS != PERSISTENCE.
 */
enum class MemoryClass {
    WORKING,
    CONVERSATION,
    PERSONAL,
    RELATIONSHIP,
    PREFERENCE,
    EPISODIC,
    SEMANTIC,
    PROCEDURAL,
    DEVICE,
    SECURITY,
}
