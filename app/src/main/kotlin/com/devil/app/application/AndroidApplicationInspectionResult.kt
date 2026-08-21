package com.devil.app.application

/**
 * Stage 177 bounded Android application inspection result.
 *
 * A FOUND result contains exactly one application descriptor.
 *
 * A NOT_FOUND result contains no descriptor.
 */
@ConsistentCopyVisibility
data class AndroidApplicationInspectionResult private constructor(
    val status: AndroidApplicationInspectionStatus,
    val application: AndroidApplicationDescriptor?,
) {
    companion object {
        fun create(
            status: AndroidApplicationInspectionStatus,
            application: AndroidApplicationDescriptor? = null,
        ): AndroidApplicationInspectionResult {
            when (status) {
                AndroidApplicationInspectionStatus.FOUND ->
                    require(application != null) {
                        "Found Android application inspection results require an application descriptor."
                    }

                AndroidApplicationInspectionStatus.NOT_FOUND ->
                    require(application == null) {
                        "Not-found Android application inspection results must not contain an application descriptor."
                    }
            }

            return AndroidApplicationInspectionResult(
                status = status,
                application = application,
            )
        }
    }
}
