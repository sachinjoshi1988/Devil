package com.devil.app.storage

@ConsistentCopyVisibility
data class AndroidStorageIntelligenceResult private constructor(
    val status: AndroidStorageIntelligenceStatus,
    val metadata: AndroidFileMetadataRecord?,
) {
    companion object {
        fun create(
            status: AndroidStorageIntelligenceStatus,
            metadata: AndroidFileMetadataRecord? = null,
        ): AndroidStorageIntelligenceResult {
            when (status) {
                AndroidStorageIntelligenceStatus.AVAILABLE ->
                    require(metadata != null) {
                        "Available Android storage intelligence requires file metadata."
                    }

                AndroidStorageIntelligenceStatus.DEFERRED ->
                    require(metadata == null) {
                        "Deferred Android storage intelligence must not contain file metadata."
                    }
            }

            return AndroidStorageIntelligenceResult(
                status = status,
                metadata = metadata,
            )
        }
    }
}
