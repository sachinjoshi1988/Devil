package com.devil.app.location

/**
 * Stage 191 bounded Location & Maps result.
 *
 * AVAILABLE contains exactly one explicitly supplied location.
 * DEFERRED contains no location.
 *
 * MAPS_INTELLIGENCE != NAVIGATION_EXECUTION.
 */
@ConsistentCopyVisibility
data class AndroidLocationMapsResult private constructor(
    val status: AndroidLocationMapsStatus,
    val location: AndroidLocationRecord?,
) {
    companion object {
        fun create(
            status: AndroidLocationMapsStatus,
            location: AndroidLocationRecord? = null,
        ): AndroidLocationMapsResult {
            when (status) {
                AndroidLocationMapsStatus.AVAILABLE ->
                    require(location != null) {
                        "Available Android location intelligence requires one location."
                    }

                AndroidLocationMapsStatus.DEFERRED ->
                    require(location == null) {
                        "Deferred Android location intelligence must not contain a location."
                    }
            }

            return AndroidLocationMapsResult(
                status = status,
                location = location,
            )
        }
    }
}
