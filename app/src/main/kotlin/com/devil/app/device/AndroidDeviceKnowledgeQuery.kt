package com.devil.app.device

/**
 * One explicit Stage 40 request for bounded Android device knowledge.
 *
 * The request contains only a predefined query type.
 *
 * Query creation
 * != conversational understanding
 * != authorization
 * != execution.
 */
data class AndroidDeviceKnowledgeQuery(
    val type: AndroidDeviceKnowledgeQueryType,
)
