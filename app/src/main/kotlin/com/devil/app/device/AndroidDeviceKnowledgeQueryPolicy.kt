package com.devil.app.device

/**
 * Stage 40 bounded Device Knowledge query policy.
 *
 * The policy transforms one explicit predefined query and one genuine device
 * snapshot into descriptive presentation text.
 *
 * It never reads raw user text and never guesses facts absent from the
 * supplied snapshot.
 */
class AndroidDeviceKnowledgeQueryPolicy {

    fun evaluate(
        query: AndroidDeviceKnowledgeQuery,
        snapshot: AndroidDeviceKnowledgeSnapshot,
    ): AndroidDeviceKnowledgeResult {
        val presentation =
            when (query.type) {
                AndroidDeviceKnowledgeQueryType.DEVICE_SUMMARY ->
                    "${snapshot.manufacturer} ${snapshot.model}, Android ${snapshot.androidRelease} (SDK ${snapshot.sdkInt})."

                AndroidDeviceKnowledgeQueryType.ANDROID_VERSION ->
                    "Android ${snapshot.androidRelease} (SDK ${snapshot.sdkInt})."

                AndroidDeviceKnowledgeQueryType.DEVICE_MODEL ->
                    "${snapshot.manufacturer} ${snapshot.model}."
            }

        return AndroidDeviceKnowledgeResult.create(
            queryType = query.type,
            snapshot = snapshot,
            presentation = presentation,
        )
    }
}
