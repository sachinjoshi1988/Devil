package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.runtime.education.FrenchEducationCoordinator
import com.devil.core.runtime.education.FrenchEducationPreparationStatus
import com.devil.core.runtime.education.LanguageEducationFoundationCoordinator
import com.devil.core.runtime.education.LanguageEducationFoundationStatus
import com.devil.core.runtime.education.MultilingualTeachingCoordinator
import com.devil.core.runtime.education.MultilingualTeachingPreparationStatus

/**
 * Stage 318 bounded Foreign Language Alpha coordinator.
 *
 * This Android-side Alpha composition consumes an already prepared Stage 85
 * education session and delegates strictly through the existing Stage 120,
 * Stage 133, and Stage 134 Education architecture.
 *
 * French is one representative dedicated foreign-language specialization for
 * Stage 318 Alpha validation. Existing German, Spanish, Russian, Mandarin
 * Chinese, and additional-language architecture remains unchanged.
 *
 * This coordinator does not create another language architecture, teach French,
 * complete lessons, translate, conduct conversation, capture or recognize
 * speech, score pronunciation, infer proficiency, authenticate, authorize,
 * execute, perform constitutional Learning, commit Memory, or persist learner
 * state.
 *
 * FOREIGN_LANGUAGE_ALPHA != ANOTHER_INTELLIGENCE.
 * FOREIGN_LANGUAGE_ALPHA != NEW_LANGUAGE_ARCHITECTURE.
 * STAGE318_FRENCH_ALPHA != ONLY_SUPPORTED_FOREIGN_LANGUAGE.
 * MULTILINGUAL_CONTEXT != TRANSLATION_ENGINE.
 * FRENCH_EDUCATION_REQUIRES_TARGET_LANGUAGE_FRENCH.
 * PREPARED != FRENCH_TAUGHT.
 * PREPARED != TRANSLATION_PERFORMED.
 * PREPARED != CONVERSATION_OCCURRED.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class Stage318ForeignLanguageAlphaCoordinator(
    private val languageEducationFoundationCoordinator:
        LanguageEducationFoundationCoordinator =
        LanguageEducationFoundationCoordinator(),
    private val multilingualTeachingCoordinator:
        MultilingualTeachingCoordinator =
        MultilingualTeachingCoordinator(),
    private val frenchEducationCoordinator:
        FrenchEducationCoordinator =
        FrenchEducationCoordinator(),
) {
    fun prepare(
        traceId: TraceId,
        educationSession: EducationSessionRecord,
        targetLanguage: String,
        teachingFocus: String,
        teachingObjective: String,
        frenchLearningFocus: String,
        frenchLearningObjective: String,
    ): Stage318ForeignLanguageAlphaResult {
        val languagePreparation =
            languageEducationFoundationCoordinator.prepare(
                traceId = traceId,
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        if (
            languagePreparation.status !=
            LanguageEducationFoundationStatus.PREPARED
        ) {
            return deferred()
        }

        val languageSession =
            requireNotNull(languagePreparation.languageSession)

        val multilingualPreparation =
            multilingualTeachingCoordinator.prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                teachingFocus = teachingFocus,
                teachingObjective = teachingObjective,
            )

        if (
            multilingualPreparation.status !=
            MultilingualTeachingPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val multilingualTeaching =
            requireNotNull(multilingualPreparation.teaching)

        val frenchPreparation =
            frenchEducationCoordinator.prepare(
                traceId = traceId,
                multilingualTeaching = multilingualTeaching,
                frenchLearningFocus = frenchLearningFocus,
                frenchLearningObjective = frenchLearningObjective,
            )

        if (
            frenchPreparation.status !=
            FrenchEducationPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val frenchEducation =
            requireNotNull(frenchPreparation.frenchEducation)

        return Stage318ForeignLanguageAlphaResult.create(
            status = Stage318ForeignLanguageAlphaStatus.AVAILABLE,
            languageSession = languageSession,
            multilingualTeaching = multilingualTeaching,
            frenchEducation = frenchEducation,
        )
    }

    private fun deferred(): Stage318ForeignLanguageAlphaResult =
        Stage318ForeignLanguageAlphaResult.create(
            status = Stage318ForeignLanguageAlphaStatus.DEFERRED,
        )
}
