package com.devil.core.model.research

import com.devil.core.model.common.TraceId

/**
 * Immutable Stage 107 representation of one bounded piece of research evidence.
 *
 * Research evidence may represent externally obtained material only after an
 * upstream governed mechanism has already made that material available to this
 * boundary.
 *
 * This type preserves:
 *
 * - one constitutional trace identity;
 * - one explicit source reference;
 * - one explicit source kind;
 * - and one bounded description of what the supplied source material contains.
 *
 * sourceReference is descriptive provenance only.
 *
 * sourceKind is descriptive provenance only and does not establish source trust,
 * authenticity, authority, independence, quality, or factual reliability.
 *
 * description preserves supplied research material only.
 *
 * Creating ResearchEvidence does not:
 *
 * - retrieve Internet content;
 * - browse autonomously;
 * - access Android;
 * - access a network;
 * - establish source authenticity;
 * - establish source trust;
 * - establish factual freshness;
 * - establish factual truth;
 * - establish corroboration;
 * - resolve conflicting sources;
 * - rank sources;
 * - synthesize conclusions;
 * - create constitutional Observation evidence;
 * - create Verification evidence;
 * - establish an Outcome;
 * - mutate the World Model;
 * - perform Learning;
 * - propose, approve, commit, persist, or recall Memory;
 * - grant authorization;
 * - select capabilities;
 * - execute actions;
 * - or establish verified success.
 *
 * RETRIEVED != ANALYZED.
 * ANALYZED != RESEARCH_EVIDENCE.
 * RESEARCH_EVIDENCE != TRUE.
 * RESEARCH_EVIDENCE != VERIFIED.
 * RESEARCH_EVIDENCE != WORLD_MODEL.
 * RESEARCH_EVIDENCE != LEARNING.
 * RESEARCH_EVIDENCE != MEMORY.
 */
@ConsistentCopyVisibility
data class ResearchEvidence private constructor(
    val traceId: TraceId,
    val sourceReference: String,
    val sourceKind: String,
    val description: String,
) {
    companion object {

        fun create(
            traceId: TraceId,
            sourceReference: String,
            sourceKind: String,
            description: String,
        ): ResearchEvidence {
            val normalizedSourceReference =
                sourceReference.trim()

            val normalizedSourceKind =
                sourceKind.trim()

            val normalizedDescription =
                description.trim()

            require(normalizedSourceReference.isNotEmpty()) {
                "Research evidence source reference must not be blank."
            }

            require(normalizedSourceKind.isNotEmpty()) {
                "Research evidence source kind must not be blank."
            }

            require(normalizedDescription.isNotEmpty()) {
                "Research evidence description must not be blank."
            }

            return ResearchEvidence(
                traceId = traceId,
                sourceReference = normalizedSourceReference,
                sourceKind = normalizedSourceKind,
                description = normalizedDescription,
            )
        }
    }
}
