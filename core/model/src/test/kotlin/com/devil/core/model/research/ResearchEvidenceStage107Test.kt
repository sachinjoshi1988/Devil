package com.devil.core.model.research

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResearchEvidenceStage107Test {

    @Test
    fun `research evidence preserves bounded provenance and description`() {
        val traceId =
            TraceId.from(
                "trace-stage-107-evidence",
            )

        val evidence =
            ResearchEvidence.create(
                traceId = traceId,
                sourceReference =
                    " https://example.test/research-source ",
                sourceKind =
                    " analyzed-external-material ",
                description =
                    " Source reports one bounded research claim. ",
            )

        assertEquals(
            traceId,
            evidence.traceId,
        )

        assertEquals(
            "https://example.test/research-source",
            evidence.sourceReference,
        )

        assertEquals(
            "analyzed-external-material",
            evidence.sourceKind,
        )

        assertEquals(
            "Source reports one bounded research claim.",
            evidence.description,
        )
    }

    @Test
    fun `research evidence rejects blank provenance or description`() {
        val traceId =
            TraceId.from(
                "trace-stage-107-invalid",
            )

        assertFailsWith<IllegalArgumentException> {
            ResearchEvidence.create(
                traceId = traceId,
                sourceReference = " ",
                sourceKind = "external",
                description = "bounded description",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            ResearchEvidence.create(
                traceId = traceId,
                sourceReference = "source-a",
                sourceKind = " ",
                description = "bounded description",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            ResearchEvidence.create(
                traceId = traceId,
                sourceReference = "source-a",
                sourceKind = "external",
                description = " ",
            )
        }
    }
}
