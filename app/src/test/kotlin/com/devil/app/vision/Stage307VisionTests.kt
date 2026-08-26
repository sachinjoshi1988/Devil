package com.devil.app.vision

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 307 Vision Tests completion coverage for the already-established
 * bounded Devil vision and multimodal architecture.
 *
 * This is test-only completion evidence. It does not modify production
 * architecture or establish new vision capability.
 *
 * Protected boundaries:
 *
 * VISION_INTEGRATED != IMAGE_UNDERSTOOD.
 * FRAME_CAPTURED != SEMANTIC_UNDERSTANDING.
 * VISION_AVAILABLE != AUTHENTICATION.
 * VISUAL_INPUT != VERIFIED_OUTCOME.
 * IMAGE_UNDERSTOOD != VERIFIED_REALITY.
 * IMAGE_DESCRIPTION != PERSON_IDENTITY.
 * IMAGE_UNDERSTANDING != FACE_AUTHENTICATION.
 * IMAGE_UNDERSTANDING != OCR.
 * IMAGE_UNDERSTANDING != MEMORY.
 * IMAGE_UNDERSTANDING != CONSTITUTIONAL_VERIFICATION.
 * CAMERA_ID != PERSON_IDENTITY.
 * CAMERA_CONTEXT != AUTHENTICATION.
 * CAMERA_CONTEXT != VERIFIED_REALITY.
 * CAMERA_UNDERSTANDING != MEMORY.
 * DOCUMENT_VISION != OCR.
 * DOCUMENT_VISION != DOCUMENT_AUTHENTICITY.
 * DOCUMENT_VISION != CONSTITUTIONAL_VERIFICATION.
 * SCREEN_VISION != EXECUTION.
 * SCREEN_VISION != AUTHENTICATION.
 * SCREEN_VISION != CONSTITUTIONAL_VERIFICATION.
 * VOICE_PLUS_VISION != AUTHENTICATION.
 * VOICE_PLUS_VISION != AUTHORIZATION.
 * VOICE_PLUS_VISION != CONSTITUTIONAL_VERIFICATION.
 * EDUCATIONAL_VISION != VERIFIED_CORRECTNESS.
 * EDUCATIONAL_VISION != CONSTITUTIONAL_LEARNING.
 * EDUCATIONAL_VISION != CONSTITUTIONAL_VERIFICATION.
 * MULTIMODAL_EVIDENCE_GOVERNED != CONSTITUTIONAL_OBSERVATION.
 * MULTIMODAL_EVIDENCE_GOVERNED != CONSTITUTIONAL_VERIFICATION.
 * GOVERNED_PROVENANCE != SOURCE_TRUST.
 * GOVERNED_MULTIMODAL_CONTEXT != OUTCOME.
 *
 * Stage 307 does not create another runtime, Brain, Planner, Executive,
 * Vision Authority, authentication mechanism, Memory Authority,
 * execution mechanism, OCR engine, face-recognition system,
 * constitutional Verification authority, or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 308 Cross-Device Tests.
 */
class Stage307VisionTests {

    @Test
    fun `Stage 307 preserves Stage 205 vision integration boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidVisionIntegrationV2Coordinator.kt"),
            "VISION_INTEGRATED != IMAGE_UNDERSTOOD.",
            "FRAME_CAPTURED != SEMANTIC_UNDERSTANDING.",
            "VISION_AVAILABLE != AUTHENTICATION.",
            "VISUAL_INPUT != VERIFIED_OUTCOME.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 206 image understanding boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidImageUnderstandingCoordinator.kt"),
            "IMAGE_UNDERSTOOD != VERIFIED_REALITY.",
            "IMAGE_DESCRIPTION != PERSON_IDENTITY.",
            "IMAGE_UNDERSTANDING != FACE_AUTHENTICATION.",
            "IMAGE_UNDERSTANDING != OCR.",
            "IMAGE_UNDERSTANDING != MEMORY.",
            "IMAGE_UNDERSTANDING != CONSTITUTIONAL_VERIFICATION.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 207 camera understanding boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidCameraUnderstandingCoordinator.kt"),
            "CAMERA_ID != PERSON_IDENTITY.",
            "CAMERA_CONTEXT != AUTHENTICATION.",
            "CAMERA_CONTEXT != VERIFIED_REALITY.",
            "CAMERA_UNDERSTANDING != MEMORY.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 208 document vision boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidDocumentVisionCoordinator.kt"),
            "DOCUMENT_VISION != OCR.",
            "DOCUMENT_VISION != DOCUMENT_AUTHENTICITY.",
            "DOCUMENT_VISION != CONSTITUTIONAL_VERIFICATION.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 209 screen vision boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidScreenVisionCoordinator.kt"),
            "SCREEN_VISION != EXECUTION.",
            "SCREEN_VISION != AUTHENTICATION.",
            "SCREEN_VISION != CONSTITUTIONAL_VERIFICATION.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 210 voice vision boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidVoiceVisionInteractionCoordinator.kt"),
            "VOICE_PLUS_VISION != AUTHENTICATION.",
            "VOICE_PLUS_VISION != AUTHORIZATION.",
            "VOICE_PLUS_VISION != CONSTITUTIONAL_VERIFICATION.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 211 educational vision boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidEducationalVisionCoordinator.kt"),
            "EDUCATIONAL_VISION != VERIFIED_CORRECTNESS.",
            "EDUCATIONAL_VISION != CONSTITUTIONAL_LEARNING.",
            "EDUCATIONAL_VISION != CONSTITUTIONAL_VERIFICATION.",
        )
    }

    @Test
    fun `Stage 307 preserves Stage 212 multimodal evidence boundaries`() {
        assertContainsAll(
            source("app/src/main/kotlin/com/devil/app/vision/AndroidMultimodalEvidenceGovernanceCoordinator.kt"),
            "MULTIMODAL_EVIDENCE_GOVERNED != CONSTITUTIONAL_OBSERVATION.",
            "MULTIMODAL_EVIDENCE_GOVERNED != CONSTITUTIONAL_VERIFICATION.",
            "GOVERNED_PROVENANCE != SOURCE_TRUST.",
            "GOVERNED_MULTIMODAL_CONTEXT != OUTCOME.",
        )
    }

    @Test
    fun `Stage 307 representative Stage 205 through 212 tests retain bounded evidence`() {
        val tests =
            listOf(
                "Stage205VisionIntegrationV2Test.kt",
                "Stage206ImageUnderstandingTest.kt",
                "Stage207CameraUnderstandingTest.kt",
                "Stage208DocumentVisionTest.kt",
                "Stage209ScreenVisionTest.kt",
                "Stage210VoiceVisionInteractionTest.kt",
                "Stage211EducationalVisionTest.kt",
                "Stage212MultimodalEvidenceGovernanceTest.kt",
            ).map {
                source("app/src/test/kotlin/com/devil/app/vision/$it")
            }

        tests.forEachIndexed { index, test ->
            assertTrue(test.contains("@Test"), "Vision test $index lacks test evidence.")
            assertTrue(
                test.contains("assertEquals") || test.contains("assertTrue"),
                "Vision test $index lacks positive assertions.",
            )
            assertTrue(
                test.contains("DEFERRED"),
                "Vision test $index lacks deferred coverage.",
            )
            assertTrue(
                test.contains("assertFailsWith") || test.contains("assertSame"),
                "Vision test $index lacks invariant/provenance coverage.",
            )
        }
    }

    @Test
    fun `Stage 307 completion remains test only`() {
        val stage307 =
            source("app/src/test/kotlin/com/devil/app/vision/Stage307VisionTests.kt")

        assertContainsAll(
            stage307,
            "This is test-only completion evidence.",
            "does not modify production",
            "does not create another runtime",
            "Stage 308 Cross-Device Tests",
        )

        assertFalse(
            stage307.contains("class Stage307Vision" + "Coordinator"),
        )
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 307 vision boundary: $marker",
            )
        }
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("../$path"),
                File("../../$path"),
            )

        val file =
            candidates.firstOrNull { it.isFile }
                ?: error("Unable to locate repository source for Stage 307: $path")

        return file.readText()
    }
}
