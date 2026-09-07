package com.devil.core.runtime.capability

import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingSemantics

/**
 * Stage 337J semantic capability-domain router.
 *
 * The router consumes only structured meaning already established by
 * Understanding and preserved through the constitutional chain.
 *
 * It never consumes raw conversation text, model output, Android state, or
 * speech-recognition text directly.
 *
 * Routing is deterministic and provider-neutral. Unsupported semantic shapes
 * fail closed rather than being guessed into a nearby capability domain.
 *
 * Routing does not select a CapabilityContract and does not replace the
 * constitutional Capability Selection Authority.
 */
interface GeneralIntentCapabilityRouter {

    fun route(
        semantics: UnderstandingSemantics?,
    ): GeneralIntentCapabilityRoute
}

/**
 * Default Stage 337J deterministic routing policy.
 *
 * The vocabulary here is restricted to canonical semantic targets and
 * predicates already emitted by the existing English, Hindi, and Marathi
 * Understanding policies.
 *
 * Semantic arguments remain preserved on UnderstandingSemantics but are not
 * reinterpreted here. Stage 337J routes only the established semantic domain.
 */
class DefaultGeneralIntentCapabilityRouter :
    GeneralIntentCapabilityRouter {

    override fun route(
        semantics: UnderstandingSemantics?,
    ): GeneralIntentCapabilityRoute {
        semantics
            ?: return GeneralIntentCapabilityRoute.UNSUPPORTED

        return when (semantics.intent) {
            UnderstandingIntent.GREETING,
            UnderstandingIntent.INFORMATIONAL,
            -> routeNonActionable(semantics)

            UnderstandingIntent.OPEN_TARGET ->
                routeOpenTarget(semantics)

            UnderstandingIntent.ACTION_REQUEST ->
                routeActionRequest(semantics)

            UnderstandingIntent.INFORMATION_QUERY ->
                routeInformationQuery(semantics)
        }
    }

    private fun routeNonActionable(
        semantics: UnderstandingSemantics,
    ): GeneralIntentCapabilityRoute {
        return if (
            semantics.actionability ==
            UnderstandingActionability.NON_ACTIONABLE
        ) {
            GeneralIntentCapabilityRoute.NO_CAPABILITY_REQUIRED
        } else {
            GeneralIntentCapabilityRoute.UNSUPPORTED
        }
    }

    private fun routeOpenTarget(
        semantics: UnderstandingSemantics,
    ): GeneralIntentCapabilityRoute {
        if (
            semantics.actionability !=
            UnderstandingActionability.ACTIONABLE
        ) {
            return GeneralIntentCapabilityRoute.UNSUPPORTED
        }

        val target =
            normalize(semantics.target)
                ?: return GeneralIntentCapabilityRoute.UNSUPPORTED

        return when (target) {
            "camera",
            "the camera",
            -> GeneralIntentCapabilityRoute.CAMERA

            "settings",
            "the settings",
            -> GeneralIntentCapabilityRoute.SETTINGS

            else ->
                GeneralIntentCapabilityRoute.UNSUPPORTED
        }
    }

    private fun routeActionRequest(
        semantics: UnderstandingSemantics,
    ): GeneralIntentCapabilityRoute {
        if (
            semantics.actionability !=
            UnderstandingActionability.ACTIONABLE
        ) {
            return GeneralIntentCapabilityRoute.UNSUPPORTED
        }

        val target =
            normalize(semantics.target)
                ?: return GeneralIntentCapabilityRoute.UNSUPPORTED

        val predicate =
            normalize(semantics.predicate)
                ?: return GeneralIntentCapabilityRoute.UNSUPPORTED

        return when {
            target == "volume" &&
                predicate in
                setOf(
                    "decrease",
                    "increase",
                    "set",
                ) ->
                GeneralIntentCapabilityRoute.DEVICE_CONTROL

            target == "alarm" &&
                predicate == "set" ->
                GeneralIntentCapabilityRoute.ALARM

            target == "message" &&
                predicate in
                setOf(
                    "send",
                    "reply",
                ) ->
                GeneralIntentCapabilityRoute.MESSAGING

            target == "contact" &&
                predicate == "call" ->
                GeneralIntentCapabilityRoute.CALL

            target == "media" &&
                predicate in
                setOf(
                    "play",
                    "pause",
                ) ->
                GeneralIntentCapabilityRoute.MEDIA

            else ->
                GeneralIntentCapabilityRoute.UNSUPPORTED
        }
    }

    private fun routeInformationQuery(
        semantics: UnderstandingSemantics,
    ): GeneralIntentCapabilityRoute {
        if (
            semantics.actionability !=
            UnderstandingActionability.ACTIONABLE
        ) {
            return GeneralIntentCapabilityRoute.UNSUPPORTED
        }

        val target =
            normalize(semantics.target)
                ?: return GeneralIntentCapabilityRoute.UNSUPPORTED

        val predicate =
            normalize(semantics.predicate)
                ?: return GeneralIntentCapabilityRoute.UNSUPPORTED

        if (predicate != "query") {
            return GeneralIntentCapabilityRoute.UNSUPPORTED
        }

        return when (target) {
            "device model",
            "android version",
            "device summary",
            "battery level" ->
                GeneralIntentCapabilityRoute.DEVICE_KNOWLEDGE

            "latest notification",
            "notifications",
            -> GeneralIntentCapabilityRoute.NOTIFICATIONS

            else ->
                GeneralIntentCapabilityRoute.GENERAL_INFORMATION
        }
    }

    private fun normalize(
        value: String?,
    ): String? {
        return value
            ?.trim()
            ?.lowercase()
            ?.takeIf { it.isNotEmpty() }
    }
}
