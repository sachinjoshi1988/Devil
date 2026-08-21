package com.devil.core.model.creative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CreativeMediaIntegrationStage166Test {

    @Test
    fun `record preserves exact Stage 87 project and normalized integration metadata`() {
        val project = creativeProject()

        val record =
            CreativeMediaIntegrationRecord.create(
                creativeProject = project,
                integrationFocus =
                    "  Provider-neutral Creative Media integration  ",
                suppliedCreativeMediaContextDescription =
                    "  Existing Creative Media foundation is supplied for integration.  ",
                integrationObjective =
                    "  Preserve one Devil intelligence and bounded Creative Media integration.  ",
            )

        assertSame(
            project,
            record.creativeProject,
        )
        assertEquals(
            "Provider-neutral Creative Media integration",
            record.integrationFocus,
        )
        assertEquals(
            "Existing Creative Media foundation is supplied for integration.",
            record.suppliedCreativeMediaContextDescription,
        )
        assertEquals(
            "Preserve one Devil intelligence and bounded Creative Media integration.",
            record.integrationObjective,
        )
    }

    @Test
    fun `record rejects blank integration focus`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaIntegrationRecord.create(
                creativeProject = creativeProject(),
                integrationFocus = "   ",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation.",
                integrationObjective =
                    "Preserve bounded integration.",
            )
        }
    }

    @Test
    fun `record rejects blank creative media context description`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaIntegrationRecord.create(
                creativeProject = creativeProject(),
                integrationFocus =
                    "Creative Media integration",
                suppliedCreativeMediaContextDescription = "   ",
                integrationObjective =
                    "Preserve bounded integration.",
            )
        }
    }

    @Test
    fun `record rejects blank integration objective`() {
        assertFailsWith<IllegalArgumentException> {
            CreativeMediaIntegrationRecord.create(
                creativeProject = creativeProject(),
                integrationFocus =
                    "Creative Media integration",
                suppliedCreativeMediaContextDescription =
                    "Existing Creative Media foundation.",
                integrationObjective = "   ",
            )
        }
    }

    private fun creativeProject(): CreativeMediaProjectRecord {
        return CreativeMediaProjectRecord.create(
            projectId =
                CreativeMediaProjectId.from(
                    "creative-project:stage166",
                ),
            objective =
                CreativeMediaObjective.create(
                    medium =
                        CreativeMediaMedium.from(
                            "multimodal",
                        ),
                    objective =
                        "Preserve bounded Creative Media project context.",
                ),
        )
    }
}
