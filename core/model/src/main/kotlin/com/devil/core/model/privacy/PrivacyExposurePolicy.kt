package com.devil.core.model.privacy

/**
 * Stage 46 pure privacy-exposure policy.
 *
 * This policy performs no I/O and consumes only explicitly supplied privacy
 * classifications and context.
 *
 * It does not:
 *
 * - inspect raw conversation text;
 * - inspect notification content;
 * - inspect memory content;
 * - authenticate a subject;
 * - infer Owner Mode;
 * - grant authorization;
 * - grant Android permission;
 * - invoke UnifiedDevilRuntime;
 * - execute an action;
 * - persist information;
 * - or transmit information.
 *
 * PrivacyExposureTarget.OWNER_PRESENTATION is descriptive destination context
 * only. It does not prove that a protected owner context exists.
 *
 * Therefore non-public owner presentation that depends on protected context
 * must fail closed when protectedContextEstablished is false.
 */
class PrivacyExposurePolicy {

    fun assess(
        request: PrivacyExposureRequest,
    ): PrivacyExposureAssessment {
        return when (request.classification) {
            PrivacyDataClassification.PUBLIC ->
                PrivacyExposureAssessment.create(
                    status = PrivacyExposureStatus.ALLOWED,
                    request = request,
                    rationale =
                        "PUBLIC information is not blocked by the bounded Stage 46 privacy policy.",
                )

            PrivacyDataClassification.PRIVATE ->
                assessPrivate(
                    request = request,
                )

            PrivacyDataClassification.SENSITIVE ->
                assessSensitive(
                    request = request,
                )

            PrivacyDataClassification.HIGHLY_SENSITIVE ->
                assessHighlySensitive(
                    request = request,
                )
        }
    }

    private fun assessPrivate(
        request: PrivacyExposureRequest,
    ): PrivacyExposureAssessment {
        return when (request.target) {
            PrivacyExposureTarget.INTERNAL_PROCESSING ->
                PrivacyExposureAssessment.create(
                    status = PrivacyExposureStatus.ALLOWED,
                    request = request,
                    rationale =
                        "PRIVATE information is allowed for bounded internal processing.",
                )

            PrivacyExposureTarget.OWNER_PRESENTATION ->
                if (request.protectedContextEstablished) {
                    PrivacyExposureAssessment.create(
                        status = PrivacyExposureStatus.ALLOWED,
                        request = request,
                        rationale =
                            "PRIVATE information is allowed for the supplied protected owner-presentation context.",
                    )
                } else {
                    PrivacyExposureAssessment.create(
                        status = PrivacyExposureStatus.RESTRICTED,
                        request = request,
                        rationale =
                            "PRIVATE owner presentation requires explicit protected-context evidence.",
                    )
                }

            PrivacyExposureTarget.SUBJECT_PRESENTATION ->
                PrivacyExposureAssessment.create(
                    status = PrivacyExposureStatus.RESTRICTED,
                    request = request,
                    rationale =
                        "PRIVATE information requires stronger protected-context evidence before subject presentation.",
                )

            PrivacyExposureTarget.EXTERNAL_SYSTEM ->
                PrivacyExposureAssessment.create(
                    status = PrivacyExposureStatus.BLOCKED,
                    request = request,
                    rationale =
                        "PRIVATE information is blocked from unrestricted external-system exposure.",
                )
        }
    }

    private fun assessSensitive(
        request: PrivacyExposureRequest,
    ): PrivacyExposureAssessment {
        if (!request.protectedContextEstablished) {
            return PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.RESTRICTED,
                request = request,
                rationale =
                    "SENSITIVE information requires explicit protected-context evidence.",
            )
        }

        return when (request.target) {
            PrivacyExposureTarget.INTERNAL_PROCESSING,
            PrivacyExposureTarget.OWNER_PRESENTATION,
            ->
                PrivacyExposureAssessment.create(
                    status = PrivacyExposureStatus.ALLOWED,
                    request = request,
                    rationale =
                        "SENSITIVE information is permitted only within the supplied protected internal or owner-presentation context.",
                )

            PrivacyExposureTarget.SUBJECT_PRESENTATION,
            PrivacyExposureTarget.EXTERNAL_SYSTEM,
            ->
                PrivacyExposureAssessment.create(
                    status = PrivacyExposureStatus.BLOCKED,
                    request = request,
                    rationale =
                        "SENSITIVE information remains blocked from subject or external-system exposure.",
                )
        }
    }

    private fun assessHighlySensitive(
        request: PrivacyExposureRequest,
    ): PrivacyExposureAssessment {
        return if (
            request.target == PrivacyExposureTarget.INTERNAL_PROCESSING &&
            request.protectedContextEstablished
        ) {
            PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.ALLOWED,
                request = request,
                rationale =
                    "HIGHLY_SENSITIVE information is allowed only for explicitly protected bounded internal processing.",
            )
        } else {
            PrivacyExposureAssessment.create(
                status = PrivacyExposureStatus.BLOCKED,
                request = request,
                rationale =
                    "HIGHLY_SENSITIVE information fails closed outside explicitly protected bounded internal processing.",
            )
        }
    }
}
