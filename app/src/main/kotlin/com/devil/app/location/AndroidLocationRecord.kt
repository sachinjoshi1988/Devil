package com.devil.app.location

/**
 * Stage 191 bounded explicitly supplied location record.
 *
 * It does not represent device-derived location or location authorization.
 *
 * SUPPLIED_LOCATION != DEVICE_LOCATION.
 */
@ConsistentCopyVisibility
data class AndroidLocationRecord private constructor(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        fun create(
            latitude: Double,
            longitude: Double,
        ): AndroidLocationRecord {
            require(latitude.isFinite()) {
                "Android location latitude must be finite."
            }

            require(longitude.isFinite()) {
                "Android location longitude must be finite."
            }

            require(latitude in -90.0..90.0) {
                "Android location latitude must be between -90 and 90."
            }

            require(longitude in -180.0..180.0) {
                "Android location longitude must be between -180 and 180."
            }

            return AndroidLocationRecord(
                latitude = latitude,
                longitude = longitude,
            )
        }
    }
}
