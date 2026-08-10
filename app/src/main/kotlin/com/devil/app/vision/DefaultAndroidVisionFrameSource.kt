package com.devil.app.vision

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Size
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Stage 41 bounded Camera2 implementation of AndroidVisionFrameSource.
 *
 * This source performs one explicitly requested still-frame capture.
 *
 * Camera2 is asynchronous. This adapter isolates Camera2 callbacks on a dedicated
 * HandlerThread and exposes the existing bounded synchronous Stage 41 source
 * contract only to a non-main-thread caller.
 *
 * Main-thread capture is rejected rather than blocking Android UI execution.
 *
 * The source:
 *
 * - checks Android CAMERA permission;
 * - verifies the explicitly supplied cameraId exists;
 * - chooses one bounded JPEG output size;
 * - opens only that camera;
 * - creates one still-capture session;
 * - requests one JPEG frame;
 * - copies the encoded frame bytes into AndroidVisionFrame;
 * - closes the Image, capture session, CameraDevice, ImageReader, and callback
 *   thread after the bounded attempt.
 *
 * It does not:
 *
 * - infer a camera from conversation text;
 * - grant CAMERA permission;
 * - grant Devil authorization;
 * - perform face recognition;
 * - identify or authenticate a person;
 * - interpret visual content;
 * - invoke UnifiedDevilRuntime;
 * - persist image data;
 * - create logical memory;
 * - verify a task effect;
 * - or establish an Outcome.
 *
 * Camera opened
 * != visual understanding.
 *
 * Frame captured
 * != trusted reality.
 *
 * Frame captured
 * != authorization.
 *
 * Frame captured
 * != verified success.
 */
class DefaultAndroidVisionFrameSource(
    context: Context,
    private val captureTimeoutMilliseconds: Long =
        DEFAULT_CAPTURE_TIMEOUT_MILLISECONDS,
) : AndroidVisionFrameSource {

    private val applicationContext =
        context.applicationContext

    private val cameraManager =
        applicationContext.getSystemService(
            CameraManager::class.java,
        )

    init {
        require(captureTimeoutMilliseconds > 0L) {
            "Android vision capture timeout must be positive."
        }
    }

    override fun capture(
        request: AndroidVisionFrameRequest,
    ): AndroidVisionFrameCaptureResult {
        if (
            Looper.myLooper() ==
            Looper.getMainLooper()
        ) {
            return AndroidVisionFrameCaptureResult.failed()
        }

        if (
            applicationContext.checkSelfPermission(
                Manifest.permission.CAMERA,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return AndroidVisionFrameCaptureResult
                .permissionUnavailable()
        }

        val manager =
            cameraManager
                ?: return AndroidVisionFrameCaptureResult
                    .cameraUnavailable()

        val cameraExists =
            try {
                manager.cameraIdList.contains(
                    request.cameraId,
                )
            } catch (
                exception: CameraAccessException,
            ) {
                false
            } catch (
                exception: SecurityException,
            ) {
                return AndroidVisionFrameCaptureResult
                    .permissionUnavailable()
            }

        if (!cameraExists) {
            return AndroidVisionFrameCaptureResult
                .cameraUnavailable()
        }

        val outputSize =
            try {
                selectBoundedJpegSize(
                    characteristics =
                        manager.getCameraCharacteristics(
                            request.cameraId,
                        ),
                )
            } catch (
                exception: CameraAccessException,
            ) {
                null
            } catch (
                exception: SecurityException,
            ) {
                return AndroidVisionFrameCaptureResult
                    .permissionUnavailable()
            }
                ?: return AndroidVisionFrameCaptureResult
                    .cameraUnavailable()

        val callbackThread =
            HandlerThread(
                "DevilStage41CameraCapture",
            )

        callbackThread.start()

        val callbackHandler =
            Handler(
                callbackThread.looper,
            )

        val imageReader =
            ImageReader.newInstance(
                outputSize.width,
                outputSize.height,
                ImageFormat.JPEG,
                1,
            )

        val completion =
            CountDownLatch(1)

        val completed =
            AtomicBoolean(false)

        val result =
            AtomicReference<AndroidVisionFrameCaptureResult?>(
                null,
            )

        val cameraReference =
            AtomicReference<CameraDevice?>(
                null,
            )

        val sessionReference =
            AtomicReference<CameraCaptureSession?>(
                null,
            )

        fun complete(
            captureResult: AndroidVisionFrameCaptureResult,
        ) {
            if (
                completed.compareAndSet(
                    false,
                    true,
                )
            ) {
                result.set(
                    captureResult,
                )

                completion.countDown()
            }
        }

        imageReader.setOnImageAvailableListener(
            { reader ->
                var image: Image? = null

                try {
                    image =
                        reader.acquireNextImage()
                            ?: run {
                                complete(
                                    AndroidVisionFrameCaptureResult
                                        .failed(),
                                )

                                return@setOnImageAvailableListener
                            }

                    val planes =
                        image.planes

                    if (planes.isEmpty()) {
                        complete(
                            AndroidVisionFrameCaptureResult
                                .failed(),
                        )

                        return@setOnImageAvailableListener
                    }

                    val buffer =
                        planes[0].buffer

                    val bytes =
                        ByteArray(
                            buffer.remaining(),
                        )

                    buffer.get(bytes)

                    if (bytes.isEmpty()) {
                        complete(
                            AndroidVisionFrameCaptureResult
                                .failed(),
                        )

                        return@setOnImageAvailableListener
                    }

                    val frame =
                        AndroidVisionFrame.create(
                            cameraId =
                                request.cameraId,
                            format =
                                AndroidVisionFrameFormat.JPEG,
                            capturedAtEpochMilliseconds =
                                System.currentTimeMillis(),
                            width =
                                image.width,
                            height =
                                image.height,
                            encodedBytes =
                                bytes,
                        )

                    complete(
                        AndroidVisionFrameCaptureResult
                            .captured(
                                frame = frame,
                            ),
                    )
                } catch (
                    exception: RuntimeException,
                ) {
                    complete(
                        AndroidVisionFrameCaptureResult
                            .failed(),
                    )
                } finally {
                    image?.close()
                }
            },
            callbackHandler,
        )

        try {
            manager.openCamera(
                request.cameraId,
                object : CameraDevice.StateCallback() {

                    override fun onOpened(
                        camera: CameraDevice,
                    ) {
                        cameraReference.set(
                            camera,
                        )

                        createCaptureSession(
                            camera = camera,
                            imageReader = imageReader,
                            callbackHandler =
                                callbackHandler,
                            sessionReference =
                                sessionReference,
                            complete = ::complete,
                        )
                    }

                    override fun onDisconnected(
                        camera: CameraDevice,
                    ) {
                        cameraReference.compareAndSet(
                            camera,
                            null,
                        )

                        camera.close()

                        complete(
                            AndroidVisionFrameCaptureResult
                                .cameraUnavailable(),
                        )
                    }

                    override fun onError(
                        camera: CameraDevice,
                        error: Int,
                    ) {
                        cameraReference.compareAndSet(
                            camera,
                            null,
                        )

                        camera.close()

                        complete(
                            AndroidVisionFrameCaptureResult
                                .failed(),
                        )
                    }
                },
                callbackHandler,
            )
        } catch (
            exception: SecurityException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .permissionUnavailable(),
            )
        } catch (
            exception: CameraAccessException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .cameraUnavailable(),
            )
        } catch (
            exception: IllegalArgumentException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .cameraUnavailable(),
            )
        } catch (
            exception: RuntimeException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .failed(),
            )
        }

        try {
            val finished =
                completion.await(
                    captureTimeoutMilliseconds,
                    TimeUnit.MILLISECONDS,
                )

            if (!finished) {
                complete(
                    AndroidVisionFrameCaptureResult
                        .failed(),
                )
            }

            return result.get()
                ?: AndroidVisionFrameCaptureResult
                    .failed()
        } catch (
            exception: InterruptedException,
        ) {
            Thread.currentThread().interrupt()

            return AndroidVisionFrameCaptureResult
                .failed()
        } finally {
            sessionReference
                .getAndSet(null)
                ?.close()

            cameraReference
                .getAndSet(null)
                ?.close()

            imageReader.close()

            callbackThread.quitSafely()

            try {
                callbackThread.join(
                    THREAD_JOIN_TIMEOUT_MILLISECONDS,
                )
            } catch (
                exception: InterruptedException,
            ) {
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun createCaptureSession(
        camera: CameraDevice,
        imageReader: ImageReader,
        callbackHandler: Handler,
        sessionReference:
            AtomicReference<CameraCaptureSession?>,
        complete:
            (AndroidVisionFrameCaptureResult) -> Unit,
    ) {
        try {
            camera.createCaptureSession(
                listOf(
                    imageReader.surface,
                ),
                object :
                    CameraCaptureSession.StateCallback() {

                    override fun onConfigured(
                        session: CameraCaptureSession,
                    ) {
                        sessionReference.set(
                            session,
                        )

                        captureStillFrame(
                            camera = camera,
                            session = session,
                            imageReader =
                                imageReader,
                            callbackHandler =
                                callbackHandler,
                            complete =
                                complete,
                        )
                    }

                    override fun onConfigureFailed(
                        session: CameraCaptureSession,
                    ) {
                        session.close()

                        complete(
                            AndroidVisionFrameCaptureResult
                                .failed(),
                        )
                    }
                },
                callbackHandler,
            )
        } catch (
            exception: CameraAccessException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .cameraUnavailable(),
            )
        } catch (
            exception: IllegalStateException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .failed(),
            )
        } catch (
            exception: RuntimeException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .failed(),
            )
        }
    }

    private fun captureStillFrame(
        camera: CameraDevice,
        session: CameraCaptureSession,
        imageReader: ImageReader,
        callbackHandler: Handler,
        complete:
            (AndroidVisionFrameCaptureResult) -> Unit,
    ) {
        try {
            val request =
                camera.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE,
                ).apply {
                    addTarget(
                        imageReader.surface,
                    )

                    set(
                        CaptureRequest.CONTROL_MODE,
                        CaptureRequest.CONTROL_MODE_AUTO,
                    )
                }.build()

            session.capture(
                request,
                object :
                    CameraCaptureSession.CaptureCallback() {

                    override fun onCaptureFailed(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        failure: CaptureFailure,
                    ) {
                        complete(
                            AndroidVisionFrameCaptureResult
                                .failed(),
                        )
                    }
                },
                callbackHandler,
            )
        } catch (
            exception: CameraAccessException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .cameraUnavailable(),
            )
        } catch (
            exception: IllegalStateException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .failed(),
            )
        } catch (
            exception: RuntimeException,
        ) {
            complete(
                AndroidVisionFrameCaptureResult
                    .failed(),
            )
        }
    }

    private fun selectBoundedJpegSize(
        characteristics: CameraCharacteristics,
    ): Size? {
        val configurationMap =
            characteristics.get(
                CameraCharacteristics
                    .SCALER_STREAM_CONFIGURATION_MAP,
            )
                ?: return null

        val jpegSizes =
            configurationMap.getOutputSizes(
                ImageFormat.JPEG,
            )
                ?: return null

        if (jpegSizes.isEmpty()) {
            return null
        }

        val bounded =
            jpegSizes.filter { size ->
                size.width.toLong() *
                    size.height.toLong() <=
                    MAX_FRAME_PIXEL_COUNT
            }

        return (
            if (bounded.isNotEmpty()) {
                bounded
            } else {
                jpegSizes.toList()
            }
        ).minByOrNull { size ->
            kotlin.math.abs(
                size.width.toLong() *
                    size.height.toLong() -
                    TARGET_FRAME_PIXEL_COUNT,
            )
        }
    }

    companion object {

        private const val DEFAULT_CAPTURE_TIMEOUT_MILLISECONDS =
            8_000L

        private const val THREAD_JOIN_TIMEOUT_MILLISECONDS =
            1_000L

        private const val MAX_FRAME_PIXEL_COUNT =
            2_500_000L

        private const val TARGET_FRAME_PIXEL_COUNT =
            2_000_000L
    }
}
