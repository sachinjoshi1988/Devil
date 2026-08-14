package com.devil.core.model.creative

/**
 * Describes the explicitly supplied target medium for one bounded Creative
 * Media project.
 *
 * Stage 87 intentionally keeps the medium extensible rather than introducing
 * a closed platform- or model-specific enum.
 *
 * Examples may include supplied values such as:
 *
 * - text;
 * - image;
 * - audio;
 * - video;
 * - or a future creative medium.
 *
 * A medium value is descriptive project metadata only.
 *
 * It does not establish:
 *
 * - that a corresponding capability exists;
 * - that a generator or model exists;
 * - capability registration;
 * - capability availability;
 * - capability health;
 * - capability readiness;
 * - platform support;
 * - permission;
 * - authorization;
 * - execution;
 * - generated output;
 * - file creation;
 * - transport;
 * - persistence;
 * - or verified Outcome.
 *
 * CREATIVE_MEDIA_MEDIUM != CAPABILITY.
 * CREATIVE_MEDIA_MEDIUM != GENERATOR.
 * CREATIVE_MEDIA_MEDIUM != OUTPUT.
 */
@ConsistentCopyVisibility
data class CreativeMediaMedium private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): CreativeMediaMedium {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Creative Media medium must not be blank."
            }

            return CreativeMediaMedium(
                value = normalizedValue,
            )
        }
    }
}
