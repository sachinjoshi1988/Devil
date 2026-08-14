package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage85EducationFoundationGovernanceTest {

    @Test
    fun `bounded education session may be prepared without creating another intelligence`() {
        val traceId =
            TraceId.from(
                "trace-stage85-education-001",
            )

        val sessionId =
            EducationSessionId.from(
                "education-session:001",
            )

        val subjectIdentityId =
            IdentityId.from(
                "identity:learner-001",
            )

        val result =
            EducationSessionCoordinator().prepare(
                traceId = traceId,
                sessionId = sessionId,
                subjectIdentityId = subjectIdentityId,
                subject = "Mathematics",
                objective = "Understand introductory algebra.",
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            EducationSessionPreparationStatus.PREPARED,
            result.status,
        )

        val session =
            requireNotNull(result.session)

        assertSame(
            sessionId,
            session.sessionId,
        )

        assertSame(
            subjectIdentityId,
            session.subjectIdentityId,
        )

        assertEquals(
            "Mathematics",
            session.objective.subject,
        )

        assertEquals(
            "Understand introductory algebra.",
            session.objective.objective,
        )
    }

    @Test
    fun `education session identity is normalized and required`() {
        assertEquals(
            "education-session:001",
            EducationSessionId.from(
                "  education-session:001  ",
            ).value,
        )

        assertFailsWith<IllegalArgumentException> {
            EducationSessionId.from("   ")
        }
    }

    @Test
    fun `education objective normalizes supplied subject and objective`() {
        val objective =
            EducationObjective.create(
                subject = "  Physics  ",
                objective =
                    "  Understand the relationship between force and motion.  ",
            )

        assertEquals(
            "Physics",
            objective.subject,
        )

        assertEquals(
            "Understand the relationship between force and motion.",
            objective.objective,
        )
    }

    @Test
    fun `education objective rejects blank subject`() {
        assertFailsWith<IllegalArgumentException> {
            EducationObjective.create(
                subject = "   ",
                objective = "Understand introductory chemistry.",
            )
        }
    }

    @Test
    fun `education objective rejects blank objective`() {
        assertFailsWith<IllegalArgumentException> {
            EducationObjective.create(
                subject = "Chemistry",
                objective = "   ",
            )
        }
    }

    @Test
    fun `blank education subject remains deferred`() {
        val result =
            EducationSessionCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage85-education-002",
                    ),
                sessionId =
                    EducationSessionId.from(
                        "education-session:002",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:learner-002",
                    ),
                subject = "   ",
                objective = "Understand cells.",
            )

        assertEquals(
            EducationSessionPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.session)
    }

    @Test
    fun `blank educational objective remains deferred`() {
        val result =
            EducationSessionCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage85-education-003",
                    ),
                sessionId =
                    EducationSessionId.from(
                        "education-session:003",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:learner-003",
                    ),
                subject = "Biology",
                objective = "   ",
            )

        assertEquals(
            EducationSessionPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.session)
    }

    @Test
    fun `education record preserves explicitly supplied subject identity without authenticating it`() {
        val identity =
            IdentityId.from(
                "identity:learner-004",
            )

        val record =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:004",
                    ),
                subjectIdentityId = identity,
                objective =
                    EducationObjective.create(
                        subject = "History",
                        objective = "Study the Industrial Revolution.",
                    ),
            )

        assertSame(
            identity,
            record.subjectIdentityId,
        )
    }

    @Test
    fun `prepared result requires education session`() {
        assertFailsWith<IllegalArgumentException> {
            EducationSessionPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage85-result-001",
                    ),
                status =
                    EducationSessionPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle education session`() {
        val session =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:005",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:learner-005",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Geography",
                        objective = "Understand plate tectonics.",
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            EducationSessionPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage85-result-002",
                    ),
                status =
                    EducationSessionPreparationStatus.DEFERRED,
                session = session,
            )
        }
    }
}
