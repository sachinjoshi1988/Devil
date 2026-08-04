package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultTrustEvaluationResultMapperTest {

    @Test
    fun `map defers unestablished subject trust without fabricating context trust`() {
        val traceId = TraceId.from(
            "trace-trust-result-mapper-001",
        )
        val mapper: TrustEvaluationResultMapper =
            DefaultTrustEvaluationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            assessment = createAssessment(
                SubjectTrustLevel.UNESTABLISHED,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `map defers restricted subject trust without granting runtime trust`() {
        val result = DefaultTrustEvaluationResultMapper().map(
            traceId = TraceId.from(
                "trace-trust-result-mapper-002",
            ),
            assessment = createAssessment(
                SubjectTrustLevel.RESTRICTED,
            ),
        )

        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `map defers trusted subject assessment until runtime contract is migrated`() {
        val result = DefaultTrustEvaluationResultMapper().map(
            traceId = TraceId.from(
                "trace-trust-result-mapper-003",
            ),
            assessment = createAssessment(
                SubjectTrustLevel.TRUSTED,
            ),
        )

        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    private fun createAssessment(
        level: SubjectTrustLevel,
    ): TrustAssessment {
        return TrustAssessment.create(
            subjectIdentityId = IdentityId.from(
                "subject-trust-result-mapper",
            ),
            level = level,
            rationale = "Bounded subject trust assessment.",
        )
    }
}
