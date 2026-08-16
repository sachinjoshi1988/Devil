package com.devil.core.model.memory

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 102 request for bounded logical-memory representation
 * preparation.
 *
 * The request preserves:
 *
 * - one existing MemoryAuthorityRequest;
 * - one already-created MemoryId;
 * - one explicitly supplied subject identity;
 * - one explicitly supplied MemoryClass;
 * - one explicitly supplied MemorySensitivity;
 * - one explicitly supplied MemoryConfidence;
 * - one explicitly supplied MemoryRetention;
 * - one explicitly supplied MemorySource;
 * - one explicitly supplied OwnerVisibleMemoryReason;
 * - and one nonblank bounded content value.
 *
 * Preserving a MemoryAuthorityRequest does not prove that the single Memory
 * Authority approved that request. Approval state remains represented only by
 * the corresponding runtime MemoryAuthorityResult.
 *
 * This request does not infer, classify, calculate, or invent any logical-memory
 * metadata.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Memory Authority;
 * - create another logical Memory Domain;
 * - authenticate a subject;
 * - prove ownership;
 * - establish trust;
 * - grant authorization;
 * - establish Owner Mode;
 * - establish High-Security Confirmation;
 * - establish Memory Authority approval;
 * - commit logical memory;
 * - persist logical memory;
 * - expose or recall logical memory;
 * - delete logical memory;
 * - enforce retention;
 * - select storage;
 * - apply encryption;
 * - replicate data;
 * - mutate World Model state;
 * - perform Learning;
 * - execute an action;
 * - or establish verified success.
 *
 * MEMORY_AUTHORITY_REQUEST != MEMORY_AUTHORITY_APPROVAL.
 * REPRESENTATION_PREPARATION_REQUEST != MEMORY_COMMITMENT.
 * REPRESENTATION_PREPARATION_REQUEST != MEMORY_PERSISTENCE.
 * SUBJECT_BINDING != AUTHENTICATION.
 * EXPLICIT_METADATA != INFERRED_METADATA.
 */
@ConsistentCopyVisibility
data class MemoryRepresentationPreparationRequest private constructor(
    val authorityRequest: MemoryAuthorityRequest,
    val memoryId: MemoryId,
    val subjectIdentityId: IdentityId,
    val memoryClass: MemoryClass,
    val sensitivity: MemorySensitivity,
    val confidence: MemoryConfidence,
    val retention: MemoryRetention,
    val source: MemorySource,
    val ownerVisibleReason: OwnerVisibleMemoryReason,
    val content: String,
) {
    companion object {

        fun create(
            authorityRequest: MemoryAuthorityRequest,
            memoryId: MemoryId,
            subjectIdentityId: IdentityId,
            memoryClass: MemoryClass,
            sensitivity: MemorySensitivity,
            confidence: MemoryConfidence,
            retention: MemoryRetention,
            source: MemorySource,
            ownerVisibleReason: OwnerVisibleMemoryReason,
            content: String,
        ): MemoryRepresentationPreparationRequest {
            val normalizedContent =
                content.trim()

            require(normalizedContent.isNotEmpty()) {
                "Memory representation preparation content must not be blank."
            }

            return MemoryRepresentationPreparationRequest(
                authorityRequest = authorityRequest,
                memoryId = memoryId,
                subjectIdentityId = subjectIdentityId,
                memoryClass = memoryClass,
                sensitivity = sensitivity,
                confidence = confidence,
                retention = retention,
                source = source,
                ownerVisibleReason = ownerVisibleReason,
                content = normalizedContent,
            )
        }
    }
}
