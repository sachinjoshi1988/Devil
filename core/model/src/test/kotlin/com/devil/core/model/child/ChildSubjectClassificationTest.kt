package com.devil.core.model.child

import kotlin.test.Test
import kotlin.test.assertEquals

class ChildSubjectClassificationTest {

    @Test
    fun `classification preserves explicit bounded states`() {
        assertEquals(
            listOf(
                ChildSubjectClassification.CHILD,
                ChildSubjectClassification.NOT_CHILD,
                ChildSubjectClassification.UNKNOWN,
            ),
            ChildSubjectClassification.entries,
        )
    }
}
