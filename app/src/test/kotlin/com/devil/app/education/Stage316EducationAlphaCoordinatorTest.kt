package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Stage316EducationAlphaCoordinatorTest {

    private val coordinator =
        Stage316EducationAlphaCoordinator()

    @Test
    fun `explicit education inputs preserve Stage 85 session provenance`() {
        val result =
            coordinator.prepare(
                traceId = TraceId.from("stage316-alpha-trace"),
                sessionId =
                    EducationSessionId.from(
                        "stage316-alpha-session",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                subject = "General Education",
                objective =
                    "Support bounded owner education alpha testing.",
            )

        assertEquals(
            Stage316EducationAlphaStatus.AVAILABLE,
            result.status,
        )

        val session = assertNotNull(result.session)

        assertEquals(
            "stage316-alpha-session",
            session.sessionId.value,
        )
        assertEquals(
            "android-primary-local-subject",
            session.subjectIdentityId.value,
        )
        assertEquals(
            "General Education",
            session.objective.subject,
        )
        assertEquals(
            "Support bounded owner education alpha testing.",
            session.objective.objective,
        )
    }

    @Test
    fun `blank education input fails closed without session`() {
        val result =
            coordinator.prepare(
                traceId = TraceId.from("stage316-deferred-trace"),
                sessionId =
                    EducationSessionId.from(
                        "stage316-deferred-session",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                subject = " ",
                objective =
                    "Support bounded owner education alpha testing.",
            )

        assertEquals(
            Stage316EducationAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.session)
    }
}
