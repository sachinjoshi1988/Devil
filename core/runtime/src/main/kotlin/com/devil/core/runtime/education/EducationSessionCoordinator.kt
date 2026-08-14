package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId

/**
 * Stage 85 bounded Education Foundation coordinator.
 *
 * This coordinator prepares one education-domain session from explicitly
 * supplied structured inputs.
 *
 * It does not infer educational intent from raw user prose.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Unified Devil Runtime;
 * - create education-specific Memory or Security authorities;
 * - infer subject identity;
 * - authenticate a subject;
 * - establish trust;
 * - grant authorization;
 * - establish or validate a security session;
 * - infer age or child status;
 * - evaluate child policy;
 * - obtain guardian approval;
 * - generate curriculum;
 * - generate lessons;
 * - assess learner mastery;
 * - create Tasks or Plans;
 * - register or select capabilities;
 * - invoke UnifiedDevilRuntime;
 * - execute actions;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - propose or commit Memory;
 * - persist educational state;
 * - or communicate with a platform API.
 *
 * EDUCATION = DOMAIN OF THE ONE DEVIL INTELLIGENCE.
 * EDUCATION != ANOTHER INTELLIGENCE.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 * EDUCATION_SESSION != AUTHORIZATION.
 * EDUCATION_SESSION != EXECUTION.
 */
class EducationSessionCoordinator {

    fun prepare(
        traceId: TraceId,
        sessionId: EducationSessionId,
        subjectIdentityId: IdentityId,
        subject: String,
        objective: String,
    ): EducationSessionPreparationResult {
        if (subject.isBlank() || objective.isBlank()) {
            return deferred(
                traceId = traceId,
            )
        }

        val educationObjective =
            EducationObjective.create(
                subject = subject,
                objective = objective,
            )

        val session =
            EducationSessionRecord.create(
                sessionId = sessionId,
                subjectIdentityId = subjectIdentityId,
                objective = educationObjective,
            )

        return EducationSessionPreparationResult.create(
            traceId = traceId,
            status = EducationSessionPreparationStatus.PREPARED,
            session = session,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): EducationSessionPreparationResult {
        return EducationSessionPreparationResult.create(
            traceId = traceId,
            status = EducationSessionPreparationStatus.DEFERRED,
        )
    }
}
