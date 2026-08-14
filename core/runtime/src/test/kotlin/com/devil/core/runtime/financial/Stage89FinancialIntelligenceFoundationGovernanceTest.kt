package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialAnalysisRecord
import com.devil.core.model.financial.FinancialAnalysisSubject
import com.devil.core.model.financial.FinancialFact
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage89FinancialIntelligenceFoundationGovernanceTest {

    @Test
    fun `bounded supplied financial context may be prepared without financial execution`() {
        val traceId =
            TraceId.from(
                "trace-stage89-financial-001",
            )

        val result =
            FinancialAnalysisCoordinator().prepare(
                traceId = traceId,
                subject = "Monthly household budget",
                facts =
                    listOf(
                        "monthly income" to "80000 INR",
                        "monthly expenses" to "50000 INR",
                    ),
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            FinancialAnalysisPreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertEquals(
            "Monthly household budget",
            record.subject.value,
        )

        assertEquals(
            listOf(
                "monthly income",
                "monthly expenses",
            ),
            record.facts.map { it.label },
        )

        assertEquals(
            listOf(
                "80000 INR",
                "50000 INR",
            ),
            record.facts.map { it.value },
        )
    }

    @Test
    fun `financial subject is normalized and required`() {
        assertEquals(
            "Household savings",
            FinancialAnalysisSubject.from(
                "  Household savings  ",
            ).value,
        )

        assertFailsWith<IllegalArgumentException> {
            FinancialAnalysisSubject.from("   ")
        }
    }

    @Test
    fun `financial fact normalizes explicitly supplied values`() {
        val fact =
            FinancialFact.create(
                label = "  Monthly expense  ",
                value = "  45000 INR  ",
            )

        assertEquals(
            "Monthly expense",
            fact.label,
        )

        assertEquals(
            "45000 INR",
            fact.value,
        )
    }

    @Test
    fun `financial fact requires nonblank label`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialFact.create(
                label = "   ",
                value = "100 INR",
            )
        }
    }

    @Test
    fun `financial fact requires nonblank value`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialFact.create(
                label = "Expense",
                value = "   ",
            )
        }
    }

    @Test
    fun `financial analysis requires at least one supplied fact`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialAnalysisRecord.create(
                subject =
                    FinancialAnalysisSubject.from(
                        "Budget",
                    ),
                facts = emptyList(),
            )
        }
    }

    @Test
    fun `financial analysis rejects duplicate normalized fact labels`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialAnalysisRecord.create(
                subject =
                    FinancialAnalysisSubject.from(
                        "Budget",
                    ),
                facts =
                    listOf(
                        FinancialFact.create(
                            label = "Income",
                            value = "100",
                        ),
                        FinancialFact.create(
                            label = "income",
                            value = "200",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `financial analysis record preserves supplied subject and facts`() {
        val subject =
            FinancialAnalysisSubject.from(
                "Investment snapshot",
            )

        val fact =
            FinancialFact.create(
                label = "Supplied portfolio value",
                value = "500000 INR",
            )

        val record =
            FinancialAnalysisRecord.create(
                subject = subject,
                facts = listOf(fact),
            )

        assertSame(
            subject,
            record.subject,
        )

        assertSame(
            fact,
            record.facts.single(),
        )
    }

    @Test
    fun `blank financial subject remains deferred`() {
        val result =
            FinancialAnalysisCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage89-financial-002",
                    ),
                subject = "   ",
                facts =
                    listOf(
                        "income" to "100",
                    ),
            )

        assertEquals(
            FinancialAnalysisPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `empty financial fact collection remains deferred`() {
        val result =
            FinancialAnalysisCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage89-financial-003",
                    ),
                subject = "Budget",
                facts = emptyList(),
            )

        assertEquals(
            FinancialAnalysisPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank supplied financial fact remains deferred rather than being invented`() {
        val result =
            FinancialAnalysisCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage89-financial-004",
                    ),
                subject = "Budget",
                facts =
                    listOf(
                        "income" to "100",
                        "expense" to "   ",
                    ),
            )

        assertEquals(
            FinancialAnalysisPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `duplicate supplied financial labels remain deferred`() {
        val result =
            FinancialAnalysisCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage89-financial-005",
                    ),
                subject = "Budget",
                facts =
                    listOf(
                        "Income" to "100",
                        "income" to "200",
                    ),
            )

        assertEquals(
            FinancialAnalysisPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one financial analysis record`() {
        assertFailsWith<IllegalArgumentException> {
            FinancialAnalysisPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage89-result-001",
                    ),
                status =
                    FinancialAnalysisPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle financial analysis record`() {
        val record =
            FinancialAnalysisRecord.create(
                subject =
                    FinancialAnalysisSubject.from(
                        "Budget",
                    ),
                facts =
                    listOf(
                        FinancialFact.create(
                            label = "Income",
                            value = "100",
                        ),
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            FinancialAnalysisPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage89-result-002",
                    ),
                status =
                    FinancialAnalysisPreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no financial record`() {
        val result =
            FinancialAnalysisPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage89-result-003",
                    ),
                status =
                    FinancialAnalysisPreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }
}
