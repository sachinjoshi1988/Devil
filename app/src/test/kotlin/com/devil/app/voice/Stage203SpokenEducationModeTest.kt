package com.devil.app.voice

import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage203SpokenEducationModeTest {

    @Test
    fun `existing language education session becomes available spoken education mode`() {
        val session =
            languageEducationSession(
                targetLanguage = "English",
            )

        val result =
            AndroidSpokenEducationModeCoordinator()
                .integrate(session)

        assertEquals(
            AndroidSpokenEducationModeStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            session,
            result.languageEducationSession,
        )
    }

    @Test
    fun `multilingual target language preserves exact Stage 120 provenance`() {
        val session =
            languageEducationSession(
                targetLanguage = "French",
            )

        val result =
            AndroidSpokenEducationModeCoordinator()
                .integrate(session)

        assertEquals(
            AndroidSpokenEducationModeStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            session,
            result.languageEducationSession,
        )
        assertEquals(
            "French",
            result.languageEducationSession?.targetLanguage,
        )
    }

    @Test
    fun `missing education session remains deferred`() {
        val result =
            AndroidSpokenEducationModeCoordinator()
                .integrate(null)

        assertEquals(
            AndroidSpokenEducationModeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageEducationSession)
    }

    @Test
    fun `available result requires education session`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidSpokenEducationModeResult.create(
                status = AndroidSpokenEducationModeStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `deferred result rejects education session`() {
        val session =
            languageEducationSession(
                targetLanguage = "Spanish",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSpokenEducationModeResult.create(
                status = AndroidSpokenEducationModeStatus.DEFERRED,
                languageEducationSession = session,
            )
        }
    }

    private fun languageEducationSession(
        targetLanguage: String,
    ): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage203-$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage203-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = targetLanguage,
                        objective = "Support bounded spoken language education.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = targetLanguage,
        )
    }
}
