package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecurityWatchlistMatchClaim
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Stage90SecurityWatchlistMatchGovernanceTest {

    @Test
    fun `complete externally supplied watchlist candidate claim may be preserved`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-001",
                    ),
                sourceId = "camera:authorized-entry",
                sourceType = "network-camera",
                occurredAtEpochMilliseconds = 1000L,
                description =
                    "Authorized external surveillance source supplied one bounded candidate signal.",
                externalWatchlistReferenceId =
                    "watchlist-reference:opaque-001",
                externalWatchlistSourceSystem =
                    "authorized-external-watchlist-matcher",
                externalWatchlistConfidencePermille = 940,
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.PREPARED,
            result.status,
        )

        val claim =
            assertNotNull(
                assertNotNull(result.record)
                    .watchlistMatchClaim,
            )

        assertEquals(
            "watchlist-reference:opaque-001",
            claim.referenceId,
        )

        assertEquals(
            "authorized-external-watchlist-matcher",
            claim.sourceSystem,
        )

        assertEquals(
            940,
            claim.confidencePermille,
        )
    }

    @Test
    fun `ordinary surveillance signal requires no watchlist claim`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-002",
                    ),
                sourceId = "sensor:door",
                sourceType = "door-sensor",
                occurredAtEpochMilliseconds = 2000L,
                description =
                    "Door sensor supplied one bounded event.",
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.PREPARED,
            result.status,
        )

        assertNull(
            assertNotNull(result.record)
                .watchlistMatchClaim,
        )
    }

    @Test
    fun `partial external watchlist claim fails closed`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-003",
                    ),
                sourceId = "camera:entry",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 3000L,
                description =
                    "Supplied bounded signal.",
                externalWatchlistReferenceId =
                    "watchlist-reference:opaque-003",
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank external watchlist reference fails closed`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-004",
                    ),
                sourceId = "camera:entry",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 4000L,
                description =
                    "Supplied bounded signal.",
                externalWatchlistReferenceId = "   ",
                externalWatchlistSourceSystem =
                    "authorized-matcher",
                externalWatchlistConfidencePermille = 900,
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank external watchlist source system fails closed`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-005",
                    ),
                sourceId = "camera:entry",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 5000L,
                description =
                    "Supplied bounded signal.",
                externalWatchlistReferenceId =
                    "watchlist-reference:opaque-005",
                externalWatchlistSourceSystem = "   ",
                externalWatchlistConfidencePermille = 900,
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `external watchlist confidence above bound fails closed`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-006",
                    ),
                sourceId = "camera:entry",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 6000L,
                description =
                    "Supplied bounded signal.",
                externalWatchlistReferenceId =
                    "watchlist-reference:opaque-006",
                externalWatchlistSourceSystem =
                    "authorized-matcher",
                externalWatchlistConfidencePermille = 1001,
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `external watchlist confidence below bound fails closed`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-watchlist-007",
                    ),
                sourceId = "camera:entry",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 7000L,
                description =
                    "Supplied bounded signal.",
                externalWatchlistReferenceId =
                    "watchlist-reference:opaque-007",
                externalWatchlistSourceSystem =
                    "authorized-matcher",
                externalWatchlistConfidencePermille = -1,
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `watchlist claim model normalizes supplied opaque metadata`() {
        val claim =
            SecurityWatchlistMatchClaim.create(
                referenceId =
                    "  watchlist-reference:opaque-008  ",
                sourceSystem =
                    "  authorized-external-matcher  ",
                confidencePermille = 875,
            )

        assertEquals(
            "watchlist-reference:opaque-008",
            claim.referenceId,
        )

        assertEquals(
            "authorized-external-matcher",
            claim.sourceSystem,
        )

        assertEquals(
            875,
            claim.confidencePermille,
        )
    }

    @Test
    fun `watchlist claim rejects blank reference`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityWatchlistMatchClaim.create(
                referenceId = "   ",
                sourceSystem =
                    "authorized-external-matcher",
                confidencePermille = 800,
            )
        }
    }

    @Test
    fun `watchlist claim rejects blank source system`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityWatchlistMatchClaim.create(
                referenceId =
                    "watchlist-reference:opaque-010",
                sourceSystem = "   ",
                confidencePermille = 800,
            )
        }
    }

    @Test
    fun `watchlist claim rejects invalid confidence`() {
        assertFailsWith<IllegalArgumentException> {
            SecurityWatchlistMatchClaim.create(
                referenceId =
                    "watchlist-reference:opaque-011",
                sourceSystem =
                    "authorized-external-matcher",
                confidencePermille = 1001,
            )
        }
    }
}
