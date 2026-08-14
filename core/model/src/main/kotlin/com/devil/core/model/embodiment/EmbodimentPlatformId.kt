package com.devil.core.model.embodiment

/**
 * Identifies the platform family represented by one Devil embodiment.
 *
 * This is deliberately a validated value object rather than a closed platform
 * enum. Future embodiments may therefore be represented without changing the
 * constitutional meaning of existing embodiments.
 *
 * A platform identifier is descriptive architectural metadata only.
 *
 * It does not prove that:
 *
 * - the platform currently exists;
 * - an application process is running;
 * - the embodiment is reachable;
 * - capabilities are registered or available;
 * - operating-system permission exists;
 * - a subject is authenticated;
 * - Devil authorization exists;
 * - or execution may occur.
 *
 * PLATFORM_IDENTITY != CAPABILITY.
 * PLATFORM_IDENTITY != AUTHORITY.
 */
@ConsistentCopyVisibility
data class EmbodimentPlatformId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): EmbodimentPlatformId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Embodiment platform identity must not be blank."
            }

            return EmbodimentPlatformId(
                value = normalizedValue,
            )
        }
    }
}
