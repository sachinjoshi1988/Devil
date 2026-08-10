package com.devil.app.privacy

import com.devil.app.DevilApplication
import kotlin.test.Test
import kotlin.test.assertSame

class Stage46PrivacyProductionCompositionTest {

    @Test
    fun `application exposes one stable process scoped privacy composition`() {
        val application =
            DevilApplication()

        val exposureCoordinator =
            application.privacyExposureCoordinator

        val disclosureCoordinator =
            application.privacyDisclosureCoordinator

        val representationReducer =
            application.privacyRepresentationReducer

        val protectedContextResolver =
            application.privacyProtectedContextResolver

        assertSame(
            exposureCoordinator,
            application.privacyExposureCoordinator,
        )

        assertSame(
            disclosureCoordinator,
            application.privacyDisclosureCoordinator,
        )

        assertSame(
            representationReducer,
            application.privacyRepresentationReducer,
        )

        assertSame(
            protectedContextResolver,
            application.privacyProtectedContextResolver,
        )
    }
}
