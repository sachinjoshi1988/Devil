package com.devil.core.runtime.owner

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.owner.OwnerContext
import com.devil.core.model.owner.OwnerProfile
import com.devil.core.model.owner.OwnerProfileSnapshot
import com.devil.core.model.owner.OwnerRelationship
import com.devil.core.model.owner.OwnerRelationshipType
import com.devil.core.model.security.SecurityStage
import com.devil.core.model.security.SecurityStateRecord
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.runtime.security.SecurityIntegrationV2Record
import com.devil.core.runtime.security.SecurityIntegrationV2Result
import com.devil.core.runtime.security.SecurityIntegrationV2Status
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage100OwnerMultiUserContextFoundationTest {

    @Test
    fun `matching owner subject with satisfied security establishes bounded self context`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-001",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-self",
            )

        val ownerContext =
            OwnerContext.create(
                ownerIdentityId = ownerIdentityId,
                subjectIdentityId = ownerIdentityId,
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = traceId,
                        currentSubjectIdentityId = ownerIdentityId,
                    ),
                ownerContext = ownerContext,
                ownerProfileSnapshot =
                    snapshot(
                        ownerIdentityId = ownerIdentityId,
                        relationship =
                            OwnerRelationship.create(
                                ownerIdentityId = ownerIdentityId,
                                subjectIdentityId = ownerIdentityId,
                                type = OwnerRelationshipType.SELF,
                            ),
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.ESTABLISHED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            ownerContext,
            record.ownerContext,
        )

        assertEquals(
            ownerIdentityId,
            record.currentSubjectIdentityId,
        )

        assertEquals(
            OwnerRelationshipType.SELF,
            requireNotNull(record.relationship).type,
        )

        assertNull(result.error)
    }

    @Test
    fun `distinct current subject may establish bounded multi user context`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-002",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-multi-user",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage100-family",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = traceId,
                        currentSubjectIdentityId = subjectIdentityId,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = subjectIdentityId,
                    ),
                ownerProfileSnapshot =
                    snapshot(
                        ownerIdentityId = ownerIdentityId,
                        relationship =
                            OwnerRelationship.create(
                                ownerIdentityId = ownerIdentityId,
                                subjectIdentityId = subjectIdentityId,
                                type = OwnerRelationshipType.FAMILY,
                                label = "Family member",
                            ),
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.ESTABLISHED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertEquals(
            subjectIdentityId,
            record.currentSubjectIdentityId,
        )

        assertEquals(
            OwnerRelationshipType.FAMILY,
            requireNotNull(record.relationship).type,
        )

        assertNull(result.error)
    }

    @Test
    fun `matching subject without descriptive relationship remains established without invention`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-003",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-no-relationship",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage100-no-relationship",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = traceId,
                        currentSubjectIdentityId = subjectIdentityId,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = subjectIdentityId,
                    ),
                ownerProfileSnapshot =
                    snapshot(
                        ownerIdentityId = ownerIdentityId,
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.ESTABLISHED,
            result.status,
        )

        assertNull(
            requireNotNull(result.record).relationship,
        )

        assertNull(result.error)
    }

    @Test
    fun `matching subject may establish bounded context without owner profile snapshot`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-004",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-no-snapshot",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage100-no-snapshot",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = traceId,
                        currentSubjectIdentityId = subjectIdentityId,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = subjectIdentityId,
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.ESTABLISHED,
            result.status,
        )

        assertNull(
            requireNotNull(result.record).relationship,
        )

        assertNull(result.error)
    }

    @Test
    fun `security current subject mismatch remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-005",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-subject-mismatch",
            )

        val configuredSubjectIdentityId =
            IdentityId.from(
                "subject-stage100-configured",
            )

        val currentSubjectIdentityId =
            IdentityId.from(
                "subject-stage100-current-other",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = traceId,
                        currentSubjectIdentityId = currentSubjectIdentityId,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = configuredSubjectIdentityId,
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `owner profile snapshot belonging to another owner remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-006",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-expected",
            )

        val otherOwnerIdentityId =
            IdentityId.from(
                "owner-stage100-other",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage100-owner-mismatch",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = traceId,
                        currentSubjectIdentityId = subjectIdentityId,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = subjectIdentityId,
                    ),
                ownerProfileSnapshot =
                    snapshot(
                        ownerIdentityId = otherOwnerIdentityId,
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `deferred Stage 99 security remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-007",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-security-deferred",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    SecurityIntegrationV2Result.create(
                        traceId = traceId,
                        status =
                            SecurityIntegrationV2Status.DEFERRED,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = ownerIdentityId,
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `failed Stage 99 security preserves exact upstream failure`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-008",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-security-failure",
            )

        val upstreamError =
            error(
                traceId = traceId,
                code = "STAGE_100_UPSTREAM_SECURITY_FAILURE",
                summary =
                    "Synthetic Stage 100 upstream security failure.",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    SecurityIntegrationV2Result.create(
                        traceId = traceId,
                        status =
                            SecurityIntegrationV2Status.FAILED,
                        error = upstreamError,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = ownerIdentityId,
                    ),
            )

        assertEquals(
            OwnerMultiUserContextStatus.FAILED,
            result.status,
        )

        assertSame(
            upstreamError,
            result.error,
        )

        assertNull(result.record)
    }

    @Test
    fun `cross trace security integration result is rejected`() {
        val traceId =
            TraceId.from(
                "trace-stage100-owner-context-009",
            )

        val otherTraceId =
            TraceId.from(
                "trace-stage100-owner-context-other",
            )

        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-cross-trace",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator().assess(
                traceId = traceId,
                securityIntegration =
                    satisfiedSecurity(
                        traceId = otherTraceId,
                        currentSubjectIdentityId =
                            ownerIdentityId,
                    ),
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = ownerIdentityId,
                    ),
            )
        }
    }

    @Test
    fun `record rejects owner context subject different from current subject`() {
        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-record-subject",
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerMultiUserContextRecord.create(
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId =
                            IdentityId.from(
                                "subject-stage100-record-configured",
                            ),
                    ),
                currentSubjectIdentityId =
                    IdentityId.from(
                        "subject-stage100-record-current",
                    ),
            )
        }
    }

    @Test
    fun `record rejects relationship belonging to another owner`() {
        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-record-owner",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage100-record-owner",
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerMultiUserContextRecord.create(
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId = subjectIdentityId,
                    ),
                currentSubjectIdentityId =
                    subjectIdentityId,
                relationship =
                    OwnerRelationship.create(
                        ownerIdentityId =
                            IdentityId.from(
                                "owner-stage100-record-other",
                            ),
                        subjectIdentityId =
                            subjectIdentityId,
                        type =
                            OwnerRelationshipType.FAMILY,
                    ),
            )
        }
    }

    @Test
    fun `record rejects relationship belonging to another subject`() {
        val ownerIdentityId =
            IdentityId.from(
                "owner-stage100-record-relationship-subject",
            )

        val currentSubjectIdentityId =
            IdentityId.from(
                "subject-stage100-record-current",
            )

        assertFailsWith<IllegalArgumentException> {
            OwnerMultiUserContextRecord.create(
                ownerContext =
                    OwnerContext.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId =
                            currentSubjectIdentityId,
                    ),
                currentSubjectIdentityId =
                    currentSubjectIdentityId,
                relationship =
                    OwnerRelationship.create(
                        ownerIdentityId = ownerIdentityId,
                        subjectIdentityId =
                            IdentityId.from(
                                "subject-stage100-record-other",
                            ),
                        type =
                            OwnerRelationshipType.FRIEND,
                    ),
            )
        }
    }

    private fun coordinator():
        OwnerMultiUserContextCoordinator {
        return OwnerMultiUserContextCoordinator()
    }

    private fun satisfiedSecurity(
        traceId: TraceId,
        currentSubjectIdentityId: IdentityId,
    ): SecurityIntegrationV2Result {
        return SecurityIntegrationV2Result.create(
            traceId = traceId,
            status =
                SecurityIntegrationV2Status.SATISFIED,
            record =
                SecurityIntegrationV2Record.create(
                    identityId =
                        currentSubjectIdentityId,
                    session =
                        SessionRecord.create(
                            sessionId =
                                SessionId.from(
                                    "session-stage100-owner-context",
                                ),
                            subjectIdentityId =
                                currentSubjectIdentityId,
                            state =
                                SessionState.ACTIVE,
                            establishedAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    ESTABLISHED_AT,
                                ),
                            expiresAt =
                                DevilTimestamp.fromEpochMilliseconds(
                                    EXPIRES_AT,
                                ),
                        ),
                    securityState =
                        SecurityStateRecord.create(
                            stage =
                                SecurityStage.SESSION,
                            rationale =
                                "Bounded Stage 100 security fixture.",
                        ),
                ),
        )
    }

    private fun snapshot(
        ownerIdentityId: IdentityId,
        relationship: OwnerRelationship? = null,
    ): OwnerProfileSnapshot {
        return OwnerProfileSnapshot.create(
            profile =
                OwnerProfile.create(
                    ownerIdentityId =
                        ownerIdentityId,
                    displayName =
                        "Stage 100 Owner",
                    preferredFormOfAddress =
                        "Owner",
                ),
            relationships =
                if (relationship == null) {
                    emptyList()
                } else {
                    listOf(relationship)
                },
        )
    }

    private fun error(
        traceId: TraceId,
        code: String,
        summary: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    code,
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    ERROR_AT,
                ),
            summary = summary,
        )
    }

    companion object {
        private const val ESTABLISHED_AT =
            1_754_000_196_000L

        private const val EXPIRES_AT =
            1_754_003_796_000L

        private const val ERROR_AT =
            1_754_000_300_500L
    }
}
