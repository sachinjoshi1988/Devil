package com.devil.core.runtime.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage143ChildEducationGovernanceTest {

    @Test
    fun `explicit child context may prepare bounded child education integration`() {
        val traceId =
            TraceId.from(
                "trace-stage143-child-education-001",
            )

        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-child",
            )

        val educationSession =
            educationSession(
                subjectIdentityId = subjectIdentity,
            )

        val childContext =
            childContext(
                subjectIdentityId = subjectIdentity,
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val result =
            ChildEducationCoordinator().prepare(
                traceId = traceId,
                educationSession = educationSession,
                childGuardianContext = childContext,
                childEducationFocus =
                    "Foundational mathematics",
                childEducationObjective =
                    "Prepare bounded child learning support",
            )

        assertEquals(
            ChildEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val childEducation =
            requireNotNull(result.childEducation)

        assertSame(
            educationSession,
            childEducation.educationSession,
        )

        assertSame(
            childContext,
            childEducation.childGuardianContext,
        )

        assertEquals(
            subjectIdentity,
            childEducation.educationSession.subjectIdentityId,
        )

        assertEquals(
            ChildSubjectClassification.CHILD,
            childEducation.childGuardianContext.classification,
        )
    }

    @Test
    fun `mismatched education and child identities remain deferred`() {
        val result =
            ChildEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage143-child-education-002",
                    ),
                educationSession =
                    educationSession(
                        IdentityId.from(
                            "identity:stage143-child-a",
                        ),
                    ),
                childGuardianContext =
                    childContext(
                        subjectIdentityId =
                            IdentityId.from(
                                "identity:stage143-child-b",
                            ),
                        classification =
                            ChildSubjectClassification.CHILD,
                    ),
                childEducationFocus =
                    "Reading",
                childEducationObjective =
                    "Prepare bounded learning support",
            )

        assertEquals(
            ChildEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.childEducation,
        )
    }

    @Test
    fun `NOT_CHILD classification remains deferred`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-not-child",
            )

        val result =
            ChildEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage143-child-education-003",
                    ),
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.NOT_CHILD,
                    ),
                childEducationFocus =
                    "Reading",
                childEducationObjective =
                    "Prepare bounded learning support",
            )

        assertEquals(
            ChildEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.childEducation,
        )
    }

    @Test
    fun `UNKNOWN classification cannot be promoted to child`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-unknown",
            )

        val result =
            ChildEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage143-child-education-004",
                    ),
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.UNKNOWN,
                    ),
                childEducationFocus =
                    "Reading",
                childEducationObjective =
                    "Prepare bounded learning support",
            )

        assertEquals(
            ChildEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.childEducation,
        )
    }

    @Test
    fun `blank child education focus remains deferred`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-blank-focus",
            )

        val result =
            ChildEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage143-child-education-005",
                    ),
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.CHILD,
                    ),
                childEducationFocus = "   ",
                childEducationObjective =
                    "Prepare bounded learning support",
            )

        assertEquals(
            ChildEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.childEducation,
        )
    }

    @Test
    fun `blank child education objective remains deferred`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-blank-objective",
            )

        val result =
            ChildEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage143-child-education-006",
                    ),
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.CHILD,
                    ),
                childEducationFocus =
                    "Reading",
                childEducationObjective = "   ",
            )

        assertEquals(
            ChildEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.childEducation,
        )
    }

    @Test
    fun `prepared child education result requires child education context`() {
        assertFailsWith<IllegalArgumentException> {
            ChildEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage143-result-001",
                    ),
                status =
                    ChildEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred child education result cannot smuggle child education context`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-result-child",
            )

        val childEducation =
            ChildEducationRecord.create(
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.CHILD,
                    ),
                childEducationFocus =
                    "Reading",
                childEducationObjective =
                    "Prepare bounded learning support",
            )

        assertFailsWith<IllegalArgumentException> {
            ChildEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage143-result-002",
                    ),
                status =
                    ChildEducationPreparationStatus.DEFERRED,
                childEducation = childEducation,
            )
        }
    }

    private fun educationSession(
        subjectIdentityId: IdentityId,
    ): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage143-runtime:${subjectIdentityId.value}",
                ),
            subjectIdentityId = subjectIdentityId,
            objective =
                EducationObjective.create(
                    subject = "Child Education",
                    objective =
                        "Prepare bounded Child Education integration context.",
                ),
        )
    }

    private fun childContext(
        subjectIdentityId: IdentityId,
        classification: ChildSubjectClassification,
    ): ChildGuardianContext {
        return ChildGuardianContext.create(
            subjectIdentityId = subjectIdentityId,
            classification = classification,
        )
    }
}
