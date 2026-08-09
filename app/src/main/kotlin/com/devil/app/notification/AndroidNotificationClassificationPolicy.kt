package com.devil.app.notification

import android.app.Notification
import java.util.Locale

/**
 * Stage 39 bounded notification classification policy.
 *
 * The policy uses only explicit Android notification category metadata.
 *
 * It does not:
 *
 * - inspect notification prose to infer intent;
 * - inspect package names to infer identity;
 * - authenticate a sender;
 * - decide that content is truthful;
 * - decide that content is important;
 * - grant authorization;
 * - invoke the Unified Devil Runtime;
 * - speak notification content;
 * - persist notification content;
 * - or execute an action.
 *
 * Known Android platform categories use Notification.CATEGORY_* constants.
 *
 * Explicit extended/raw categories that have no corresponding Android
 * Notification constant are matched only by their normalized literal value.
 *
 * Unknown or absent metadata is never guessed.
 */
class AndroidNotificationClassificationPolicy {

    fun classify(
        rawCategory: String?,
    ): AndroidNotificationClassificationResult {
        val normalized =
            rawCategory
                ?.trim()
                ?.lowercase(Locale.ROOT)
                ?.takeIf {
                    it.isNotEmpty()
                }

        val classification =
            when (normalized) {
                null ->
                    AndroidNotificationClassification.UNKNOWN

                Notification.CATEGORY_MESSAGE ->
                    AndroidNotificationClassification.MESSAGE

                Notification.CATEGORY_CALL,
                "missed_call",
                ->
                    AndroidNotificationClassification.CALL

                Notification.CATEGORY_EMAIL ->
                    AndroidNotificationClassification.EMAIL

                Notification.CATEGORY_EVENT ->
                    AndroidNotificationClassification.EVENT

                Notification.CATEGORY_REMINDER ->
                    AndroidNotificationClassification.REMINDER

                Notification.CATEGORY_ALARM ->
                    AndroidNotificationClassification.ALARM

                Notification.CATEGORY_NAVIGATION ->
                    AndroidNotificationClassification.NAVIGATION

                Notification.CATEGORY_TRANSPORT ->
                    AndroidNotificationClassification.TRANSPORT

                Notification.CATEGORY_PROGRESS ->
                    AndroidNotificationClassification.PROGRESS

                Notification.CATEGORY_SERVICE ->
                    AndroidNotificationClassification.SERVICE

                Notification.CATEGORY_STATUS ->
                    AndroidNotificationClassification.STATUS

                Notification.CATEGORY_SYSTEM ->
                    AndroidNotificationClassification.SYSTEM

                Notification.CATEGORY_SOCIAL ->
                    AndroidNotificationClassification.SOCIAL

                Notification.CATEGORY_RECOMMENDATION ->
                    AndroidNotificationClassification.RECOMMENDATION

                Notification.CATEGORY_PROMO ->
                    AndroidNotificationClassification.PROMOTION

                Notification.CATEGORY_ERROR ->
                    AndroidNotificationClassification.ERROR

                Notification.CATEGORY_STOPWATCH ->
                    AndroidNotificationClassification.TIMER

                else ->
                    classifyExplicitExtendedCategory(
                        normalized = normalized,
                    )
            }

        return AndroidNotificationClassificationResult(
            classification = classification,
            rawCategory = normalized,
        )
    }

    private fun classifyExplicitExtendedCategory(
        normalized: String,
    ): AndroidNotificationClassification {
        return when (normalized) {
            "location" ->
                AndroidNotificationClassification.LOCATION

            "finance",
            "financial",
            "payment",
            ->
                AndroidNotificationClassification.FINANCIAL

            "security" ->
                AndroidNotificationClassification.SECURITY

            else ->
                AndroidNotificationClassification.OTHER
        }
    }
}
