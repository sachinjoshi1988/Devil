package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class RussianEducationStage137Test {

    @Test
    fun `Russian Education preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("Russian")

        val record =
            RussianEducationRecord.create(
                multilingualTeaching = multilingual,
                russianLearningFocus =
                    "  Everyday Russian introductions  ",
                russianLearningObjective =
                    "  Prepare beginner Russian communication practice  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "Russian",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Russian introductions",
            record.russianLearningFocus,
        )

        assertEquals(
            "Prepare beginner Russian communication practice",
            record.russianLearningObjective,
        )
    }

    @Test
    fun `Russian Education accepts case insensitive Russian target`() {
        val record =
            RussianEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("rUsSiAn"),
                russianLearningFocus = "Greetings",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )

        assertEquals(
            "rUsSiAn",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `Russian Education rejects non Russian target language`() {
        assertFailsWith<IllegalArgumentException> {
            RussianEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Spanish"),
                russianLearningFocus = "Greetings",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )
        }
    }

    @Test
    fun `Russian Education rejects blank learning focus`() {
        assertFailsWith<IllegalArgumentException> {
            RussianEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Russian"),
                russianLearningFocus = "   ",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )
        }
    }

    @Test
    fun `Russian Education rejects blank learning objective`() {
        assertFailsWith<IllegalArgumentException> {
            RussianEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Russian"),
                russianLearningFocus = "Greetings",
                russianLearningObjective = "   ",
            )
        }
    }

    private fun multilingualTeaching(
        targetLanguage: String,
    ): MultilingualTeachingRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage137-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage137-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Russian Education context.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        return MultilingualTeachingRecord.create(
            languageEducationSession = languageSession,
            teachingFocus =
                "Beginner language communication",
            teachingObjective =
                "Prepare reusable multilingual teaching context",
        )
    }
}
