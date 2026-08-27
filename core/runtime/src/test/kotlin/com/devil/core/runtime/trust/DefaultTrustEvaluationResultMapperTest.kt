package com.devil.core.runtime.trust

import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class DefaultTrustEvaluationResultMapperTest {

    @Test
    fun `map preserves unestablished subject trust without fabricating context trust`() {
        val traceId =
            TraceId.from(
                "trace-trust-result-mapper-001",
            )
        val assessment =
            createAssessment(
                SubjectTrustLevel.UNESTABLISHED,
            )

        val result =
            DefaultTrustEvaluationResultMapper()
                .map(
                    traceId = traceId,
                    assessment = assessment,
                )

        assertEquals(traceId, result.traceId)
        assertEquals(TrustStatus.EVALUATED, result.status)
        assertSame(assessment, result.assessment)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `map preserves restricted subject trust without granting authorization`() {
        val assessment =
            createAssessment(
                SubjectTrustLevel.RESTRICTED,
            )

        val result =
            DefaultTrustEvaluationResultMapper()
                .map(
                    traceId =
                        TraceId.from(
                            "trace-trust-result-mapper-002",
                        ),
                    assessment = assessment,
                )

        assertEquals(TrustStatus.EVALUATED, result.status)
        assertSame(assessment, result.assessment)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `map preserves trusted subject assessment without converting it into authorization`() {
        val assessment =
            createAssessment(
                SubjectTrustLevel.TRUSTED,
            )

        val result =
            DefaultTrustEvaluationResultMapper()
                .map(
                    traceId =
                        TraceId.from(
                            "trace-trust-result-mapper-003",
                        ),
                    assessment = assessment,
                )

        assertEquals(TrustStatus.EVALUATED, result.status)
        assertSame(assessment, result.assessment)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    private fun createAssessment(
        level: SubjectTrustLevel,
    ): TrustAssessment {
        return TrustAssessment.create(
            subjectIdentityId =
                IdentityId.from(
                    "subject-trust-result-mapper",
                ),
            level = level,
            rationale =
                "Bounded subject trust assessment.",
        )
    }
}
