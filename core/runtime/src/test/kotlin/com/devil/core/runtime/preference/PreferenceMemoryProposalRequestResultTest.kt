package com.devil.core.runtime.preference

import com.devil.core.model.memory.MemoryProposalRequest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PreferenceMemoryProposalRequestResultTest {

    @Test
    fun `unavailable result contains no request`() {
        val result =
            PreferenceMemoryProposalRequestResult.create(
                status =
                    PreferenceMemoryProposalRequestStatus.UNAVAILABLE,
            )

        assertNull(result.request)
    }

    @Test
    fun `available result rejects missing request`() {
        assertFailsWith<IllegalArgumentException> {
            PreferenceMemoryProposalRequestResult.create(
                status =
                    PreferenceMemoryProposalRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `available result rejects generic request without preference candidate`() {
        val genericRequest =
            MemoryProposalRequest.create(
                learning =
                    PreferenceTestFixtures.learningRequest(),
            )

        assertFailsWith<IllegalArgumentException> {
            PreferenceMemoryProposalRequestResult.create(
                status =
                    PreferenceMemoryProposalRequestStatus.AVAILABLE,
                request = genericRequest,
            )
        }
    }
}
