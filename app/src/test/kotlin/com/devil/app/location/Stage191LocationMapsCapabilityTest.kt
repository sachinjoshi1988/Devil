package com.devil.app.location

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage191LocationMapsCapabilityTest {

    @Test
    fun `valid explicitly supplied coordinates are preserved`() {
        val location =
            AndroidLocationRecord.create(
                latitude = 19.0760,
                longitude = 72.8777,
            )

        assertEquals(19.0760, location.latitude)
        assertEquals(72.8777, location.longitude)
    }

    @Test
    fun `invalid coordinates are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidLocationRecord.create(
                latitude = 91.0,
                longitude = 0.0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidLocationRecord.create(
                latitude = 0.0,
                longitude = 181.0,
            )
        }
    }

    @Test
    fun `explicit location becomes available unchanged`() {
        val location =
            AndroidLocationRecord.create(
                latitude = 28.6139,
                longitude = 77.2090,
            )

        val result =
            AndroidLocationMapsCoordinator()
                .integrate(location)

        assertEquals(AndroidLocationMapsStatus.AVAILABLE, result.status)
        assertEquals(location, result.location)
    }

    @Test
    fun `absent location remains deferred`() {
        val result =
            AndroidLocationMapsCoordinator()
                .integrate(null)

        assertEquals(AndroidLocationMapsStatus.DEFERRED, result.status)
        assertNull(result.location)
    }

    @Test
    fun `result state invariants are enforced`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidLocationMapsResult.create(
                status = AndroidLocationMapsStatus.AVAILABLE,
            )
        }

        val location =
            AndroidLocationRecord.create(
                latitude = 0.0,
                longitude = 0.0,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidLocationMapsResult.create(
                status = AndroidLocationMapsStatus.DEFERRED,
                location = location,
            )
        }
    }
}
