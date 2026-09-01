package com.devil.app.diagnostic

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

/**
 * Stage 314 debug-build-only uncaught-crash recorder.
 *
 * This provider exists only in the debug source set and is initialized by
 * Android before Devil's normal application UI is used.
 *
 * It records uncaught exception evidence into the public Downloads collection,
 * then delegates to Android's previously installed uncaught-exception handler.
 *
 * It does not alter Devil runtime authority, capability selection,
 * authorization, execution, observation, verification, outcome, learning,
 * or memory.
 *
 * CRASH_RECORDED != VERIFIED_CAUSE.
 */
class Stage314DebugCrashRecorderProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        val appContext =
            context?.applicationContext
                ?: return false

        val previousHandler =
            Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            runCatching {
                recordCrash(
                    context = appContext,
                    threadName = thread.name,
                    throwable = throwable,
                )
            }

            if (previousHandler != null) {
                previousHandler.uncaughtException(
                    thread,
                    throwable,
                )
            } else {
                Process.killProcess(Process.myPid())
                exitProcess(10)
            }
        }

        return true
    }

    private fun recordCrash(
        context: Context,
        threadName: String,
        throwable: Throwable,
    ) {
        val recordedAtMillis =
            System.currentTimeMillis()

        val report =
            Stage314DebugCrashReportFormatter.format(
                threadName = threadName,
                throwable = throwable,
                recordedAtMillis = recordedAtMillis,
            )

        val timestamp =
            SimpleDateFormat(
                "yyyyMMdd-HHmmss-SSS",
                Locale.ROOT,
            ).format(
                Date(recordedAtMillis),
            )

        val values =
            ContentValues().apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "Devil-Stage314-crash-$timestamp.txt",
                )
                put(
                    MediaStore.MediaColumns.MIME_TYPE,
                    "text/plain",
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        "Download/DevilDiagnostics",
                    )
                }
            }

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Files.getContentUri(
                    "external",
                )
            }

        val uri =
            requireNotNull(
                context.contentResolver.insert(
                    collection,
                    values,
                ),
            ) {
                "Stage 314 debug crash report could not allocate a Downloads entry."
            }

        try {
            requireNotNull(
                context.contentResolver.openOutputStream(
                    uri,
                    "w",
                ),
            ).bufferedWriter().use { writer ->
                writer.write(report)
            }
        } catch (throwable: Throwable) {
            context.contentResolver.delete(
                uri,
                null,
                null,
            )
            throw throwable
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? = null

    override fun getType(
        uri: Uri,
    ): String? = null

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? = null

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0
}
