package com.devil.app.device

/**
 * Immutable Stage 40 snapshot of bounded Android device knowledge.
 *
 * Every value in this record must originate from the Android platform source
 * used to construct the snapshot.
 *
 * This record intentionally excludes sensitive or persistent identifiers such
 * as IMEI, serial number, Android ID, phone number, account identifiers,
 * location, and network hardware addresses.
 *
 * A snapshot describes observed device/platform facts only.
 *
 * Device fact
 * != owner identity
 * != authentication evidence
 * != authorization
 * != command
 * != execution request
 * != memory commitment
 * != verified Outcome.
 */
@ConsistentCopyVisibility
data class AndroidDeviceKnowledgeSnapshot private constructor(
    val sdkInt: Int,
    val androidRelease: String,
    val manufacturer: String,
    val model: String,
    val device: String,
    val product: String,
) {
    companion object {

        fun create(
            sdkInt: Int,
            androidRelease: String,
            manufacturer: String,
            model: String,
            device: String,
            product: String,
        ): AndroidDeviceKnowledgeSnapshot {
            require(sdkInt > 0) {
                "Android SDK level must be positive."
            }

            return AndroidDeviceKnowledgeSnapshot(
                sdkInt = sdkInt,
                androidRelease =
                    requireText(
                        value = androidRelease,
                        fieldName = "Android release",
                    ),
                manufacturer =
                    requireText(
                        value = manufacturer,
                        fieldName = "Android manufacturer",
                    ),
                model =
                    requireText(
                        value = model,
                        fieldName = "Android model",
                    ),
                device =
                    requireText(
                        value = device,
                        fieldName = "Android device",
                    ),
                product =
                    requireText(
                        value = product,
                        fieldName = "Android product",
                    ),
            )
        }

        private fun requireText(
            value: String,
            fieldName: String,
        ): String {
            val normalizedValue = value.trim()

            require(normalizedValue.isNotEmpty()) {
                "$fieldName must not be blank."
            }

            return normalizedValue
        }
    }
}
