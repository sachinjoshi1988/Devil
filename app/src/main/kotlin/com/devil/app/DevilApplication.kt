package com.devil.app

import android.app.Application
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import com.devil.core.runtime.UnifiedDevilRuntime

/**
 * Android process bootstrap for Devil.
 *
 * The Android application owns one process-scoped reference to the single
 * UnifiedDevilRuntime.
 *
 * This class does not create conversation input, trace identity, timestamps,
 * constitutional context, decisions, plans, capabilities, execution requests,
 * memory, or persistence.
 *
 * It grants no authority and performs no runtime work merely because the
 * Android process was created.
 */
class DevilApplication : Application() {

    val runtime: UnifiedDevilRuntime by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DefaultUnifiedDevilRuntime()
    }
}
