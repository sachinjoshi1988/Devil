package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecurityResponseAction
import com.devil.core.model.surveillance.SecurityResponseRecord
import com.devil.core.model.surveillance.SecuritySurveillanceRecord
import com.devil.core.model.surveillance.SecuritySurveillanceSignal
import com.devil.core.model.surveillance.SecuritySurveillanceSource
import com.devil.core.model.surveillance.SecurityWatchlistMatchClaim
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage91SecurityResponseFoundationGovernanceTest {

    @Test
    fun `bounded security response may be prepared from existing surveillance record without execution`() {
        val traceId =
            TraceId.from(
                "trace-stage91-security-response-001",
            )

        val surveillance =
            surveillanceRecord()

        val result =
            SecurityResponseCoordinator().prepare(
                traceId = traceId,
                surveillance = surveillance,
                action =
                    "Notify authorized security operator",
                rationale =
                    "Request human review of the supplied surveillance event.",
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            SecurityResponsePreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            surveillance,
            record.surveillance,
        )

        assertEquals(
            "Notify authorized security operator",
            record.action.value,
        )

        assertEquals(
            "Request human review of the supplied surveillance event.",
            record.rationale,
        )
    }

    @Test
    fun `security response action is normalized and required`() {
        assertEquals(
            "Raise bounded alert",
            SecurityResponseAction.from(
                "  Raise bounded alert  ",
            ).value,
        )

        assertFailsWith<IllegalArgumentException> {
            SecurityResponseAction.from("   ")
        }
    }

    @Test
    fun `security response rationale is normalized and required`() {
        val surveillance =
            surveillanceRecord()

        val response =
            SecurityResponseRecord.create(
                surveillance = surveillance,
                action =
                    SecurityResponseAction.from(
                        "Request additional observation",
                    ),
                rationale =
                    "  Additional evidence is required.  ",
            )

        assertEquals(
            "Additional evidence is required.",
            response.rationale,
        )

        assertFailsWith<IllegalArgumentException> {
            SecurityResponseRecord.create(
                surveillance = surveillance,
                action =
                    SecurityResponseAction.from(
                        "Request additional observation",
                    ),
                rationale = "   ",
            )
        }
    }

    @Test
    fun `security response preserves exact stage90 surveillance provenance`() {
        val surveillance =
            surveillanceRecord()

        val response =
            SecurityResponseRecord.create(
                surveillance = surveillance,
                action =
                    SecurityResponseAction.from(
                        "Notify owner",
                    ),
                rationale =
                    "Present bounded surveillance information for owner review.",
            )

        assertSame(
            surveillance,
            response.surveillance,
        )

        assertSame(
            surveillance.watchlistMatchClaim,
            response.surveillance.watchlistMatchClaim,
        )
    }

    @Test
    fun `watchlist candidate claim remains only preserved stage90 evidence`() {
        val surveillance =
            surveillanceRecord()

        val claim =
            requireNotNull(
                surveillance.watchlistMatchClaim,
            )

        val response =
            SecurityResponseRecord.create(
                surveillance = surveillance,
                action =
                    SecurityResponseAction.from(
                        "Notify authorized security operator",
                    ),
                rationale =
                    "External candidate claim requires bounded human review.",
            )

        assertSame(
            claim,
            response.surveillance.watchlistMatchClaim,
        )

        assertEquals(
            "watchlist-reference:stage91-test",
            claim.referenceId,
        )

        assertEquals(
            910,
            claim.confidencePermille,
        )
    }

    @Test
    fun `blank response action remains deferred`() {
        val result =
            SecurityResponseCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage91-security-response-002",
                    ),
                surveillance =
                    surveillanceRecord(),
                action = "   ",
                rationale =
                    "Bounded supplied rationale.",
            )

        assertEquals(
            SecurityResponsePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank response rationale remains deferred`() {
        val result =
            SecurityResponseCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage91-security-response-003",
                    ),
                surveillance =
                    surveillanceRecord(),
                action =
                    "Notify owner",
                rationale = "   ",
            )

        assertEquals(
            SecurityResponsePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `ordinary non watchlist surveillance may also receive bounded response preparation`() {
        val source =
            SecuritySurveillanceSource.create(
                sourceId = "sensor:door-stage91",
                sourceType = "door-sensor",
            )

        val surveillance =
            SecuritySurveillanceRecord.create(
                source = source,
                signal =
                    SecuritySurveillanceSignal.create(
                        source = source,
                        occurredAtEpochMilliseconds =
                            2000L,
                        description =
                            "Door sensor supplied one bounded event.",
                    ),
            )

        val result =
            SecurityResponseCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage91-security-response-004",
                    ),
                surveillance = surveillance,
                action =
                    "Request additional observation",
                rationale =
                    "The supplied event alone does not establish a threat.",
            )

        assertEquals(
            SecurityResponsePreparationStatus.PREPARED,
            result.status,
        )

        assertNull(
            requireNotNull(result.record)
                .surveillance
                .watchlistMatchClaim,
        )
    }

    @Test
    fun `prepared result requires one security response record`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityResponsePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage91-result-001",
                    ),
                status =
                    SecurityResponsePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle security response record`() {
        val record =
            SecurityResponseRecord.create(
                surveillance =
                    surveillanceRecord(),
                action =
                    SecurityResponseAction.from(
                        "Notify owner",
                    ),
                rationale =
                    "Bounded supplied rationale.",
            )

        assertFailsWith<IllegalArgumentException> {
            SecurityResponsePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage91-result-002",
                    ),
                status =
                    SecurityResponsePreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no security response record`() {
        val result =
            SecurityResponsePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage91-result-003",
                    ),
                status =
                    SecurityResponsePreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun surveillanceRecord(): SecuritySurveillanceRecord {
        val source =
            SecuritySurveillanceSource.create(
                sourceId =
                    "camera:authorized-stage91-test",
                sourceType =
                    "network-camera",
            )

        val claim =
            SecurityWatchlistMatchClaim.create(
                referenceId =
                    "watchlist-reference:stage91-test",
                sourceSystem =
                    "authorized-external-watchlist-matcher",
                confidencePermille =
                    910,
            )

        return SecuritySurveillanceRecord.create(
            source = source,
            signal =
                SecuritySurveillanceSignal.create(
                    source = source,
                    occurredAtEpochMilliseconds =
                        1000L,
                    description =
                        "Authorized external surveillance source supplied one bounded candidate event.",
                ),
            watchlistMatchClaim = claim,
        )
    }
}
