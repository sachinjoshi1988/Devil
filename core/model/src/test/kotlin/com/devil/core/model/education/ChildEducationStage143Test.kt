package com.devil.core.model.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ChildEducationStage143Test {

    @Test
    fun `child education preserves education and child context and normalizes inputs`() {
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

        val record =
            ChildEducationRecord.create(
                educationSession = educationSession,
                childGuardianContext = childContext,
                childEducationFocus =
                    "  Foundational mathematics learning  ",
                childEducationObjective =
                    "  Prepare bounded child education support  ",
            )

        assertSame(
            educationSession,
            record.educationSession,
        )

        assertSame(
            childContext,
            record.childGuardianContext,
        )

        assertEquals(
            "Foundational mathematics learning",
            record.childEducationFocus,
        )

        assertEquals(
            "Prepare bounded child education support",
            record.childEducationObjective,
        )
    }

    @Test
    fun `child education rejects mismatched subject identity`() {
        assertFailsWith<IllegalArgumentException> {
            ChildEducationRecord.create(
                educationSession =
                    educationSession(
                        subjectIdentityId =
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
                    "Mathematics",
                childEducationObjective =
                    "Prepare bounded learning support",
            )
        }
    }

    @Test
    fun `child education rejects NOT_CHILD classification`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-not-child",
            )

        assertFailsWith<IllegalArgumentException> {
            ChildEducationRecord.create(
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.NOT_CHILD,
                    ),
                childEducationFocus =
                    "Mathematics",
                childEducationObjective =
                    "Prepare bounded learning support",
            )
        }
    }

    @Test
    fun `child education rejects UNKNOWN classification`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-unknown",
            )

        assertFailsWith<IllegalArgumentException> {
            ChildEducationRecord.create(
                educationSession =
                    educationSession(subjectIdentity),
                childGuardianContext =
                    childContext(
                        subjectIdentityId = subjectIdentity,
                        classification =
                            ChildSubjectClassification.UNKNOWN,
                    ),
                childEducationFocus =
                    "Mathematics",
                childEducationObjective =
                    "Prepare bounded learning support",
            )
        }
    }

    @Test
    fun `child education rejects blank focus`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-blank-focus",
            )

        assertFailsWith<IllegalArgumentException> {
            ChildEducationRecord.create(
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
        }
    }

    @Test
    fun `child education rejects blank objective`() {
        val subjectIdentity =
            IdentityId.from(
                "identity:stage143-blank-objective",
            )

        assertFailsWith<IllegalArgumentException> {
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
                    "Mathematics",
                childEducationObjective = "   ",
            )
        }
    }

    private fun educationSession(
        subjectIdentityId: IdentityId,
    ): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage143-model:${subjectIdentityId.value}",
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
