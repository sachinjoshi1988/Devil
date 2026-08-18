package com.devil.core.runtime.research

import com.devil.core.model.common.TraceId
import com.devil.core.model.research.ResearchEvidence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage107ResearchEvidenceGovernanceTest {

    private val coordinator =
        ResearchEvidenceCoordinator()

    @Test
    fun `coordinator preserves exact supplied research evidence objects`() {
        val first =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-107-source-a",
                    ),
                sourceReference = "source-a",
                sourceKind = "external-analysis",
                description =
                    "Source A reports the bounded proposition.",
            )

        val second =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-107-source-b",
                    ),
                sourceReference = "source-b",
                sourceKind = "external-analysis",
                description =
                    "Source B disputes the bounded proposition.",
            )

        val result =
            coordinator.establish(
                evidence =
                    listOf(
                        first,
                        second,
                    ),
            )

        assertEquals(
            2,
            result.evidence.size,
        )

        assertSame(
            first,
            result.evidence[0],
        )

        assertSame(
            second,
            result.evidence[1],
        )
    }

    @Test
    fun `conflicting research evidence remains conflicting rather than becoming synthesized truth`() {
        val supports =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-107-support",
                    ),
                sourceReference = "supporting-source",
                sourceKind = "external-analysis",
                description =
                    "The supplied source supports claim X.",
            )

        val disputes =
            ResearchEvidence.create(
                traceId =
                    TraceId.from(
                        "trace-stage-107-dispute",
                    ),
                sourceReference = "disputing-source",
                sourceKind = "external-analysis",
                description =
                    "The supplied source disputes claim X.",
            )

        val result =
            coordinator.establish(
                evidence =
                    listOf(
                        supports,
                        disputes,
                    ),
            )

        assertSame(
            supports,
            result.evidence[0],
        )

        assertSame(
            disputes,
            result.evidence[1],
        )
    }

    @Test
    fun `research evidence set requires at least one supplied evidence item`() {
        assertFailsWith<IllegalArgumentException> {
            coordinator.establish(
                evidence = emptyList(),
            )
        }
    }
}
