package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage195VoiceArchitectureV2Test {

    @Test
    fun `explicit existing voice sources establish available architecture with exact provenance`() {
        val inputSource = testInputSource()
        val outputSource = testOutputSource()

        val result =
            AndroidVoiceArchitectureV2Coordinator()
                .integrate(
                    inputSource = inputSource,
                    outputSource = outputSource,
                )

        assertEquals(
            AndroidVoiceArchitectureV2Status.AVAILABLE,
            result.status,
        )
        assertSame(inputSource, result.inputSource)
        assertSame(outputSource, result.outputSource)
    }

    @Test
    fun `missing input source remains deferred`() {
        val result =
            AndroidVoiceArchitectureV2Coordinator()
                .integrate(
                    inputSource = null,
                    outputSource = testOutputSource(),
                )

        assertEquals(
            AndroidVoiceArchitectureV2Status.DEFERRED,
            result.status,
        )
        assertNull(result.inputSource)
        assertNull(result.outputSource)
    }

    @Test
    fun `missing output source remains deferred`() {
        val result =
            AndroidVoiceArchitectureV2Coordinator()
                .integrate(
                    inputSource = testInputSource(),
                    outputSource = null,
                )

        assertEquals(
            AndroidVoiceArchitectureV2Status.DEFERRED,
            result.status,
        )
        assertNull(result.inputSource)
        assertNull(result.outputSource)
    }

    @Test
    fun `available result requires both voice sources`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceArchitectureV2Result.create(
                status = AndroidVoiceArchitectureV2Status.AVAILABLE,
                inputSource = testInputSource(),
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceArchitectureV2Result.create(
                status = AndroidVoiceArchitectureV2Status.AVAILABLE,
                outputSource = testOutputSource(),
            )
        }
    }

    @Test
    fun `deferred result rejects voice sources`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceArchitectureV2Result.create(
                status = AndroidVoiceArchitectureV2Status.DEFERRED,
                inputSource = testInputSource(),
            )
        }
    }

    private fun testInputSource(): AndroidVoiceInputSource {
        return object : AndroidVoiceInputSource {
            override fun startListening(
                listener: AndroidVoiceInputListener,
            ) = Unit

            override fun cancel() = Unit

            override fun release() = Unit
        }
    }

    private fun testOutputSource(): AndroidVoiceOutputSource {
        return object : AndroidVoiceOutputSource {
            override fun speak(
                text: String,
                listener: AndroidVoiceOutputListener,
            ) = Unit

            override fun stop() = Unit

            override fun release() = Unit
        }
    }
}
