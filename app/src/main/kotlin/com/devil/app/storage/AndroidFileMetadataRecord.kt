package com.devil.app.storage

@ConsistentCopyVisibility
data class AndroidFileMetadataRecord private constructor(
    val path: String,
    val sizeBytes: Long?,
    val regularFile: Boolean?,
) {
    companion object {
        fun create(
            path: String,
            sizeBytes: Long? = null,
            regularFile: Boolean? = null,
        ): AndroidFileMetadataRecord {
            val normalizedPath = path.trim()

            require(normalizedPath.isNotEmpty()) {
                "Android file metadata path must not be blank."
            }

            require(sizeBytes == null || sizeBytes >= 0L) {
                "Android file metadata size must not be negative."
            }

            return AndroidFileMetadataRecord(
                path = normalizedPath,
                sizeBytes = sizeBytes,
                regularFile = regularFile,
            )
        }
    }
}
