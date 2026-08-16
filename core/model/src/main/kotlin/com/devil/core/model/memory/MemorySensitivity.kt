package com.devil.core.model.memory

/**
 * Memory-specific sensitivity classification.
 *
 * This classification belongs only to logical-memory governance.
 *
 * It must not be substituted for:
 *
 * - ContextSecurityLevel;
 * - PrivacyDataClassification;
 * - SecurityStage;
 * - authentication;
 * - trust;
 * - authorization;
 * - capability permission;
 * - or persistence approval.
 *
 * MEMORY_SENSITIVITY != CONTEXT_SECURITY_LEVEL.
 * MEMORY_SENSITIVITY != PRIVACY_DISCLOSURE_POLICY.
 * MEMORY_SENSITIVITY != SECURITY_STAGE.
 */
enum class MemorySensitivity {
    PUBLIC,
    PRIVATE,
    SENSITIVE,
    HIGHLY_SENSITIVE,
}
