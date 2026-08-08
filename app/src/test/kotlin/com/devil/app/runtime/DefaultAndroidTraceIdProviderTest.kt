package com.devil.app.runtime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class DefaultAndroidTraceIdProviderTest {

    @Test
    fun `provide delegates trace validation and normalization to TraceId`() {
        val provider: AndroidTraceIdProvider =
            DefaultAndroidTraceIdProvider(
                rawTraceIdGenerator = {
                    "   trace-android-provider-001   "
                },
            )

        val traceId = provider.provide()

        assertEquals(
            "trace-android-provider-001",
            traceId.value,
        )
    }

    @Test
    fun `provide requests one raw trace identity per invocation`() {
        var nextValue = 0

        val provider = DefaultAndroidTraceIdProvider(
            rawTraceIdGenerator = {
                nextValue += 1
                "trace-android-provider-$nextValue"
            },
        )

        val first = provider.provide()
        val second = provider.provide()

        assertEquals(
            "trace-android-provider-1",
            first.value,
        )
        assertEquals(
            "trace-android-provider-2",
            second.value,
        )
        assertNotEquals(first, second)
    }

    @Test
    fun `provide preserves existing TraceId rejection of blank identity`() {
        val provider = DefaultAndroidTraceIdProvider(
            rawTraceIdGenerator = {
                "   "
            },
        )

        assertFailsWith<IllegalArgumentException> {
            provider.provide()
        }
    }

    @Test
    fun `default provider produces nonblank trace identity`() {
        val traceId =
            DefaultAndroidTraceIdProvider().provide()

        check(traceId.value.isNotBlank())
    }
}
