package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.identity.IdentityId
import com.devil.core.runtime.education.EducationSessionCoordinator
import com.devil.core.runtime.education.EducationSessionPreparationStatus

/**
 * Stage 316 bounded Education Alpha coordinator.
 *
 * This Android-side Alpha composition delegates education-session preparation
 * to the existing Stage 85 EducationSessionCoordinator. It does not create a
 * second education architecture or bypass the established education domain.
 *
 * Inputs are explicit. This coordinator does not infer learner identity,
 * educational intent, age, child status, mastery, or progress.
 *
 * It does not authenticate, authorize, teach, generate curriculum, execute,
 * verify mastery, perform constitutional Learning, commit Memory, or persist
 * educational state.
 *
 * EDUCATION_ALPHA != ANOTHER_INTELLIGENCE.
 * EDUCATION_SESSION != AUTHENTICATION.
 * EDUCATION_SESSION != AUTHORIZATION.
 * PREPARED != TAUGHT.
 * PREPARED != VERIFIED_MASTERY.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 */
class Stage316EducationAlphaCoordinator(
    private val educationSessionCoordinator:
        EducationSessionCoordinator = EducationSessionCoordinator(),
) {
    fun prepare(
        traceId: TraceId,
        sessionId: EducationSessionId,
        subjectIdentityId: IdentityId,
        subject: String,
        objective: String,
    ): Stage316EducationAlphaResult {
        val preparation =
            educationSessionCoordinator.prepare(
                traceId = traceId,
                sessionId = sessionId,
                subjectIdentityId = subjectIdentityId,
                subject = subject,
                objective = objective,
            )

        if (
            preparation.status !=
            EducationSessionPreparationStatus.PREPARED
        ) {
            return Stage316EducationAlphaResult.create(
                status = Stage316EducationAlphaStatus.DEFERRED,
            )
        }

        val session =
            requireNotNull(
                preparation.session,
            )

        return Stage316EducationAlphaResult.create(
            status = Stage316EducationAlphaStatus.AVAILABLE,
            session = session,
        )
    }
}
