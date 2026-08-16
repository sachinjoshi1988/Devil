package com.devil.core.model.memory

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 101 representation of one bounded logical-memory item.
 *
 * This type introduces the core-domain vocabulary required to represent memory
 * without granting memory authority or claiming persistence.
 *
 * The representation preserves:
 *
 * - one explicit MemoryId;
 * - one subject identity;
 * - one constitutional MemoryClass;
 * - one memory-specific sensitivity classification;
 * - one bounded confidence value;
 * - one retention classification;
 * - one explicit provenance source;
 * - one owner-visible reason;
 * - and one nonblank bounded content value.
 *
 * The supplied subject identity is descriptive memory binding only.
 *
 * SUBJECT_BINDING != AUTHENTICATION.
 * SUBJECT_BINDING != OWNERSHIP_PROOF.
 * SUBJECT_BINDING != OWNER_MODE.
 * SUBJECT_BINDING != AUTHORIZATION.
 *
 * Creating this representation does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Memory Authority;
 * - create another logical Memory Domain;
 * - infer memory from conversation;
 * - infer the subject identity;
 * - authenticate the subject;
 * - prove ownership;
 * - establish trust;
 * - grant authorization;
 * - establish Owner Mode;
 * - perform security review;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - perform Learning;
 * - create a MemoryProposalRequest;
 * - invoke Memory Authority;
 * - approve memory;
 * - create a MemoryCommitmentRequest;
 * - commit memory;
 * - create a MemoryPersistenceRequest;
 * - persist memory;
 * - write to a database;
 * - write to a filesystem;
 * - invoke Android storage;
 * - invoke cloud storage;
 * - expose memory;
 * - recall memory;
 * - delete memory;
 * - enforce retention;
 * - select a storage destination;
 * - apply encryption;
 * - replicate data;
 * - authorize a capability;
 * - create an ExecutionRequest;
 * - execute an action;
 * - or establish verified success.
 *
 * LOGICAL_MEMORY_REPRESENTATION != MEMORY_PROPOSAL.
 * LOGICAL_MEMORY_REPRESENTATION != MEMORY_AUTHORITY_APPROVAL.
 * LOGICAL_MEMORY_REPRESENTATION != MEMORY_COMMITMENT.
 * LOGICAL_MEMORY_REPRESENTATION != MEMORY_PERSISTENCE.
 * MEMORY_CONFIDENCE != TRUTH.
 * MEMORY_SOURCE != TRUSTED_SOURCE.
 * MEMORY_SENSITIVITY != SECURITY_STAGE.
 * RETENTION_CLASSIFICATION != RETENTION_ENFORCEMENT.
 */
@ConsistentCopyVisibility
data class LogicalMemoryRepresentation private constructor(
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
            memoryId: MemoryId,
            subjectIdentityId: IdentityId,
            memoryClass: MemoryClass,
            sensitivity: MemorySensitivity,
            confidence: MemoryConfidence,
            retention: MemoryRetention,
            source: MemorySource,
            ownerVisibleReason: OwnerVisibleMemoryReason,
            content: String,
        ): LogicalMemoryRepresentation {
            val normalizedContent =
                content.trim()

            require(normalizedContent.isNotEmpty()) {
                "Logical memory content must not be blank."
            }

            return LogicalMemoryRepresentation(
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
