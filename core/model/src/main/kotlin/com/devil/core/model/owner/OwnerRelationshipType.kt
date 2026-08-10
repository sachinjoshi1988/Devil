package com.devil.core.model.owner

/**
 * Stage 43 descriptive relationship classification between the Devil owner and
 * another subject identity.
 *
 * These values describe relationship context only.
 *
 * They do not establish:
 *
 * - ownership;
 * - identity;
 * - authentication;
 * - subject trust;
 * - guardian authority;
 * - child policy;
 * - authorization;
 * - Owner Mode;
 * - execution permission;
 * - or memory eligibility.
 *
 * Stage 44 remains responsible for any future child / guardian policy.
 */
enum class OwnerRelationshipType {
    SELF,
    FAMILY,
    FRIEND,
    PROFESSIONAL,
    OTHER,
    UNSPECIFIED,
}
