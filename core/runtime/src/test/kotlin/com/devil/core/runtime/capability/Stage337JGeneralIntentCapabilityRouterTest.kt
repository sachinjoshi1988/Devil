package com.devil.core.runtime.capability

import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingSemanticArgument
import com.devil.core.model.understanding.UnderstandingSemantics
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Stage 337J — General Intent & Capability Router.
 *
 * GENERAL_INTENT_ROUTER != CAPABILITY_SELECTION_AUTHORITY.
 * INTENT_ROUTE != CAPABILITY_SELECTED.
 * ROUTE_CANDIDATE != CAPABILITY_AVAILABLE.
 * ROUTE_CANDIDATE != AUTHORIZED.
 * ROUTE_CANDIDATE != EXECUTABLE.
 * NO_CAPABILITY_REQUIRED != FAILURE.
 * UNSUPPORTED_ROUTE != GUESSED_ROUTE.
 */
class Stage337JGeneralIntentCapabilityRouterTest {

    private val router =
        DefaultGeneralIntentCapabilityRouter()

    @Test
    fun `non actionable conversation requires no capability`() {
        assertEquals(
            GeneralIntentCapabilityRoute.NO_CAPABILITY_REQUIRED,
            router.route(
                UnderstandingSemantics.create(
                    intent = UnderstandingIntent.GREETING,
                    actionability =
                        UnderstandingActionability.NON_ACTIONABLE,
                    meaning = "greeting",
                ),
            ),
        )

        assertEquals(
            GeneralIntentCapabilityRoute.NO_CAPABILITY_REQUIRED,
            router.route(
                UnderstandingSemantics.create(
                    intent = UnderstandingIntent.INFORMATIONAL,
                    actionability =
                        UnderstandingActionability.NON_ACTIONABLE,
                    meaning = "informational statement",
                ),
            ),
        )
    }

    @Test
    fun `open target routes only canonical camera and settings domains`() {
        assertRoute(
            route = GeneralIntentCapabilityRoute.CAMERA,
            intent = UnderstandingIntent.OPEN_TARGET,
            target = "camera",
        )

        assertRoute(
            route = GeneralIntentCapabilityRoute.CAMERA,
            intent = UnderstandingIntent.OPEN_TARGET,
            target = "the camera",
        )

        assertRoute(
            route = GeneralIntentCapabilityRoute.SETTINGS,
            intent = UnderstandingIntent.OPEN_TARGET,
            target = "settings",
        )

        assertRoute(
            route = GeneralIntentCapabilityRoute.SETTINGS,
            intent = UnderstandingIntent.OPEN_TARGET,
            target = "the settings",
        )

        assertRoute(
            route = GeneralIntentCapabilityRoute.UNSUPPORTED,
            intent = UnderstandingIntent.OPEN_TARGET,
            target = "calculator",
        )
    }

    @Test
    fun `action requests route to exact established domains`() {
        listOf(
            "decrease",
            "increase",
            "set",
        ).forEach { predicate ->
            assertRoute(
                route =
                    GeneralIntentCapabilityRoute.DEVICE_CONTROL,
                intent = UnderstandingIntent.ACTION_REQUEST,
                target = "volume",
                predicate = predicate,
            )
        }

        assertRoute(
            route = GeneralIntentCapabilityRoute.ALARM,
            intent = UnderstandingIntent.ACTION_REQUEST,
            target = "alarm",
            predicate = "set",
        )

        listOf(
            "send",
            "reply",
        ).forEach { predicate ->
            assertRoute(
                route =
                    GeneralIntentCapabilityRoute.MESSAGING,
                intent = UnderstandingIntent.ACTION_REQUEST,
                target = "message",
                predicate = predicate,
            )
        }

        assertRoute(
            route = GeneralIntentCapabilityRoute.CALL,
            intent = UnderstandingIntent.ACTION_REQUEST,
            target = "contact",
            predicate = "call",
        )

        listOf(
            "play",
            "pause",
        ).forEach { predicate ->
            assertRoute(
                route = GeneralIntentCapabilityRoute.MEDIA,
                intent = UnderstandingIntent.ACTION_REQUEST,
                target = "media",
                predicate = predicate,
            )
        }
    }

    @Test
    fun `semantic arguments do not become new routing authority`() {
        val semantics =
            UnderstandingSemantics.create(
                intent = UnderstandingIntent.ACTION_REQUEST,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning = "set volume",
                target = "volume",
                predicate = "set",
                arguments =
                    listOf(
                        UnderstandingSemanticArgument.create(
                            name = "value",
                            value = "50",
                        ),
                    ),
            )

        assertEquals(
            GeneralIntentCapabilityRoute.DEVICE_CONTROL,
            router.route(semantics),
        )
    }

    @Test
    fun `information queries route device notification and general information domains`() {
        assertRoute(
            route =
                GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE,
            intent = UnderstandingIntent.INFORMATION_QUERY,
            target = "battery level",
            predicate = "query",
        )
        listOf(
            "device model",
            "android version",
            "device summary",
        ).forEach { target ->
            assertRoute(
                route =
                    GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE,
                intent =
                    UnderstandingIntent.INFORMATION_QUERY,
                target = target,
                predicate = "query",
            )
        }


        assertRoute(
            route = GeneralIntentCapabilityRoute.NOTIFICATIONS,
            intent = UnderstandingIntent.INFORMATION_QUERY,
            target = "notifications",
            predicate = "query",
        )

        assertRoute(
            route = GeneralIntentCapabilityRoute.NOTIFICATIONS,
            intent = UnderstandingIntent.INFORMATION_QUERY,
            target = "latest notification",
            predicate = "query",
        )

        assertRoute(
            route =
                GeneralIntentCapabilityRoute.GENERAL_INFORMATION,
            intent = UnderstandingIntent.INFORMATION_QUERY,
            target = "Kopargaon",
            predicate = "query",
        )
    }

    @Test
    fun `unsupported predicates fail closed instead of guessing a route`() {
        assertRoute(
            route = GeneralIntentCapabilityRoute.UNSUPPORTED,
            intent = UnderstandingIntent.ACTION_REQUEST,
            target = "volume",
            predicate = "explode",
        )

        assertRoute(
            route = GeneralIntentCapabilityRoute.UNSUPPORTED,
            intent = UnderstandingIntent.INFORMATION_QUERY,
            target = "battery level",
            predicate = "change",
        )

        assertEquals(
            GeneralIntentCapabilityRoute.UNSUPPORTED,
            router.route(null),
        )
    }

    private fun assertRoute(
        route: GeneralIntentCapabilityRoute,
        intent: UnderstandingIntent,
        target: String,
        predicate: String? = null,
    ) {
        val semantics =
            UnderstandingSemantics.create(
                intent = intent,
                actionability =
                    UnderstandingActionability.ACTIONABLE,
                meaning =
                    when (intent) {
                        UnderstandingIntent.OPEN_TARGET ->
                            "open target"

                        UnderstandingIntent.ACTION_REQUEST ->
                            "action request"

                        UnderstandingIntent.INFORMATION_QUERY ->
                            "query information"

                        UnderstandingIntent.GREETING,
                        UnderstandingIntent.INFORMATIONAL,
                        -> error(
                            "This helper is only for actionable semantics.",
                        )
                    },
                target = target,
                predicate = predicate,
            )

        assertEquals(
            route,
            router.route(semantics),
        )
    }
}
