package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.owner.OwnerMultiUserContextRecord
import com.devil.core.runtime.owner.OwnerMultiUserContextResult
import com.devil.core.runtime.owner.OwnerMultiUserContextStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage104MemoryRecallEligibilityGovernanceTest {

    private val coordinator =
        MemoryRecallEligibilityCoordinator()

    @Test
    fun `established continuity authorized state and matching subject establishes recall eligibility`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-eligible",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-eligible",
            )

        val continuity =
            establishedContinuity(
                traceId = traceId,
                subjectIdentityId = subjectIdentityId,
            )

        val continuityRecord =
            requireNotNull(
                continuity.record,
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity = continuity,
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.ELIGIBLE,
            result.status,
        )

        val eligibilityRecord =
            requireNotNull(
                result.record,
            )

        assertSame(
            continuityRecord,
            eligibilityRecord.continuity,
        )

        assertSame(
            continuityRecord.representation,
            eligibilityRecord
                .continuity
                .representation,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `non established continuity defers recall eligibility`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-continuity-deferred",
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity =
                    MemoryContinuityResult.create(
                        traceId = traceId,
                        status =
                            MemoryContinuityStatus.DEFERRED,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = traceId,
                        subjectIdentityId =
                            IdentityId.from(
                                "subject-stage-104-continuity-deferred",
                            ),
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `non authorized state defers recall eligibility`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-authorization-deferred",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-authorization-deferred",
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity =
                    establishedContinuity(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    AuthorizationResult.create(
                        traceId = traceId,
                        status =
                            AuthorizationStatus.DEFERRED,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `non established owner context defers recall eligibility`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-owner-deferred",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-owner-deferred",
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity =
                    establishedContinuity(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                ownerContext =
                    OwnerMultiUserContextResult.create(
                        traceId = traceId,
                        status =
                            OwnerMultiUserContextStatus.DEFERRED,
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `subject mismatch defers without transforming memory identity`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-subject-mismatch",
            )

        val memorySubject =
            IdentityId.from(
                "subject-stage-104-memory",
            )

        val currentSubject =
            IdentityId.from(
                "subject-stage-104-current",
            )

        val continuity =
            establishedContinuity(
                traceId = traceId,
                subjectIdentityId =
                    memorySubject,
            )

        val originalRepresentation =
            requireNotNull(
                continuity.record,
            ).representation

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity = continuity,
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = traceId,
                        subjectIdentityId =
                            currentSubject,
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertSame(
            originalRepresentation,
            requireNotNull(
                continuity.record,
            ).representation,
        )

        assertEquals(
            memorySubject,
            originalRepresentation.subjectIdentityId,
        )
    }

    @Test
    fun `continuity failure propagates matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-continuity-failure",
            )

        val error =
            createError(
                traceId = traceId,
                code =
                    "STAGE_104_CONTINUITY_FAILURE",
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity =
                    MemoryContinuityResult.create(
                        traceId = traceId,
                        status =
                            MemoryContinuityStatus.FAILED,
                        error = error,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = traceId,
                        subjectIdentityId =
                            IdentityId.from(
                                "subject-stage-104-continuity-failure",
                            ),
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.FAILED,
            result.status,
        )

        assertSame(
            error,
            result.error,
        )

        assertNull(
            result.record,
        )
    }

    @Test
    fun `authorization failure propagates matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-authorization-failure",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-authorization-failure",
            )

        val error =
            createError(
                traceId = traceId,
                code =
                    "STAGE_104_AUTHORIZATION_FAILURE",
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity =
                    establishedContinuity(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    AuthorizationResult.create(
                        traceId = traceId,
                        status =
                            AuthorizationStatus.FAILED,
                        error = error,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.FAILED,
            result.status,
        )

        assertSame(
            error,
            result.error,
        )

        assertNull(
            result.record,
        )
    }

    @Test
    fun `owner context failure propagates matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-owner-failure",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-owner-failure",
            )

        val error =
            createError(
                traceId = traceId,
                code =
                    "STAGE_104_OWNER_FAILURE",
            )

        val result =
            coordinator.evaluate(
                traceId = traceId,
                continuity =
                    establishedContinuity(
                        traceId = traceId,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    authorized(
                        traceId = traceId,
                    ),
                ownerContext =
                    OwnerMultiUserContextResult.create(
                        traceId = traceId,
                        status =
                            OwnerMultiUserContextStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            MemoryRecallEligibilityStatus.FAILED,
            result.status,
        )

        assertSame(
            error,
            result.error,
        )

        assertNull(
            result.record,
        )
    }

    @Test
    fun `continuity trace mismatch is rejected`() {
        val expectedTrace =
            TraceId.from(
                "trace-stage-104-continuity-expected",
            )

        val otherTrace =
            TraceId.from(
                "trace-stage-104-continuity-other",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-continuity-trace",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.evaluate(
                traceId = expectedTrace,
                continuity =
                    establishedContinuity(
                        traceId = otherTrace,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    authorized(
                        traceId = expectedTrace,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = expectedTrace,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
            )
        }
    }

    @Test
    fun `authorization trace mismatch is rejected`() {
        val expectedTrace =
            TraceId.from(
                "trace-stage-104-auth-expected",
            )

        val otherTrace =
            TraceId.from(
                "trace-stage-104-auth-other",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-auth-trace",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.evaluate(
                traceId = expectedTrace,
                continuity =
                    establishedContinuity(
                        traceId = expectedTrace,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    authorized(
                        traceId = otherTrace,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = expectedTrace,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
            )
        }
    }

    @Test
    fun `owner context trace mismatch is rejected`() {
        val expectedTrace =
            TraceId.from(
                "trace-stage-104-owner-expected",
            )

        val otherTrace =
            TraceId.from(
                "trace-stage-104-owner-other",
            )

        val subjectIdentityId =
            IdentityId.from(
                "subject-stage-104-owner-trace",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.evaluate(
                traceId = expectedTrace,
                continuity =
                    establishedContinuity(
                        traceId = expectedTrace,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
                authorization =
                    authorized(
                        traceId = expectedTrace,
                    ),
                ownerContext =
                    establishedOwnerContext(
                        traceId = otherTrace,
                        subjectIdentityId =
                            subjectIdentityId,
                    ),
            )
        }
    }

    private fun establishedContinuity(
        traceId: TraceId,
        subjectIdentityId: IdentityId,
    ): MemoryContinuityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-${traceId.value}",
                    ),
                subjectIdentityId =
                    subjectIdentityId,
                memoryClass =
                    MemoryClass.SEMANTIC,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(
                        91,
                    ),
                retention =
                    MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-${traceId.value}",
                        sourceType =
                            "stage-104-governance-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 104 governed recall eligibility test.",
                    ),
                content =
                    "Stage 104 governed logical-memory content.",
            )

        return MemoryContinuityResult.create(
            traceId = traceId,
            status =
                MemoryContinuityStatus.ESTABLISHED,
            record =
                MemoryContinuityRecord.create(
                    representation =
                        representation,
                ),
        )
    }

    private fun authorized(
        traceId: TraceId,
    ): AuthorizationResult {
        return AuthorizationResult.create(
            traceId = traceId,
            status =
                AuthorizationStatus.AUTHORIZED,
        )
    }

    private fun establishedOwnerContext(
        traceId: TraceId,
        subjectIdentityId: IdentityId,
    ): OwnerMultiUserContextResult {
        return OwnerMultiUserContextResult.create(
            traceId = traceId,
            status =
                OwnerMultiUserContextStatus.ESTABLISHED,
            record =
                OwnerMultiUserContextRecord.create(
                    ownerContext =
                        com.devil.core.model.owner.OwnerContext.create(
                            ownerIdentityId =
                                subjectIdentityId,
                            subjectIdentityId =
                                subjectIdentityId,
                        ),
                    currentSubjectIdentityId =
                        subjectIdentityId,
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = com.devil.core.model.error.ErrorCode.from(code),
            traceId = traceId,
            occurredAt = com.devil.core.model.common.DevilTimestamp.fromEpochMilliseconds(1_754_104_002_000L),
            summary =
                "Stage 104 synthetic governance failure.",
        )
    }
}
