package com.devil.app.education

import com.devil.core.model.education.EducationSessionRecord

/**
 * Stage 316 bounded Education Alpha result.
 *
 * This result preserves an existing Stage 85 education session for Android
 * presentation only.
 *
 * EDUCATION_ALPHA != EDUCATION_AUTHORITY.
 * EDUCATION_SESSION != SECURITY_SESSION.
 * AVAILABLE != TAUGHT.
 * AVAILABLE != LEARNED.
 * AVAILABLE != VERIFIED_MASTERY.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 */
@ConsistentCopyVisibility
data class Stage316EducationAlphaResult private constructor(
    val status: Stage316EducationAlphaStatus,
    val session: EducationSessionRecord?,
) {
    companion object {
        fun create(
            status: Stage316EducationAlphaStatus,
            session: EducationSessionRecord? = null,
        ): Stage316EducationAlphaResult {
            when (status) {
                Stage316EducationAlphaStatus.AVAILABLE ->
                    require(session != null) {
                        "Available Stage 316 Education Alpha requires one existing education session."
                    }

                Stage316EducationAlphaStatus.DEFERRED ->
                    require(session == null) {
                        "Deferred Stage 316 Education Alpha must not contain an education session."
                    }
            }

            return Stage316EducationAlphaResult(
                status = status,
                session = session,
            )
        }
    }
}
