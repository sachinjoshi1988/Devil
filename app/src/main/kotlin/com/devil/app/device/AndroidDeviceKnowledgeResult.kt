package com.devil.app.device

/**
 * Stage 40 result for one bounded Device Knowledge query.
 *
 * The result preserves the snapshot used to answer the request together with
 * presentation text derived only from approved snapshot fields.
 *
 * Presentation text is descriptive device knowledge only.
 *
 * It is not:
 *
 * - authenticated owner information;
 * - memory;
 * - a constitutional decision;
 * - execution evidence;
 * - verification;
 * - or final Outcome.
 */
@ConsistentCopyVisibility
data class AndroidDeviceKnowledgeResult private constructor(
    val queryType: AndroidDeviceKnowledgeQueryType,
    val snapshot: AndroidDeviceKnowledgeSnapshot,
    val presentation: String,
) {
    companion object {

        fun create(
            queryType: AndroidDeviceKnowledgeQueryType,
            snapshot: AndroidDeviceKnowledgeSnapshot,
            presentation: String,
        ): AndroidDeviceKnowledgeResult {
            val normalizedPresentation =
                presentation.trim()

            require(normalizedPresentation.isNotEmpty()) {
                "Android device knowledge presentation must not be blank."
            }

            return AndroidDeviceKnowledgeResult(
                queryType = queryType,
                snapshot = snapshot,
                presentation = normalizedPresentation,
            )
        }
    }
}
