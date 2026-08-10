package com.devil.core.model.owner

/**
 * Stage 43 bounded owner-profile update operations.
 *
 * These operations describe changes to one transient OwnerProfileSnapshot only.
 *
 * They do not:
 *
 * - authenticate an owner;
 * - prove a relationship;
 * - establish trust;
 * - grant guardian authority;
 * - grant authorization;
 * - enter Owner Mode;
 * - commit logical memory;
 * - persist data;
 * - or execute an action.
 */
enum class OwnerProfileUpdateType {
    REPLACE_PROFILE,
    UPSERT_RELATIONSHIP,
    REMOVE_RELATIONSHIP,
}
