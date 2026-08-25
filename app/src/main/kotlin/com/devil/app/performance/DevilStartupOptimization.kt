package com.devil.app.performance

/**
 * Stage 267 Startup Optimization.
 *
 * Stage 267 gives first UI composition priority over nonessential Android
 * service/source initialization during DevilActivity startup.
 *
 * The established Devil awakening presentation and its Stage 51 / Stage 252
 * timing contract remain unchanged.
 *
 * STARTUP_OPTIMIZATION != AWAKENING_SHORTENING.
 * STARTUP_OPTIMIZATION != STARTUP_PERFORMANCE_TARGET.
 * DEFERRED_INITIALIZATION != DISABLED_CAPABILITY.
 * DEFERRED_INITIALIZATION != AUTHORIZATION.
 * DEFERRED_INITIALIZATION != EXECUTION_APPROVAL.
 * SET_CONTENT_ESTABLISHED != APPLICATION_READY.
 * SET_CONTENT_ESTABLISHED != VERIFIED_OUTCOME.
 *
 * Stage 267 does not optimize memory, CPU, battery, network, offline behavior,
 * crash recovery, long-running stability, or device compatibility.
 *
 * Those remain Stage 268 and later Phase-S responsibilities.
 */
object DevilStartupOptimizationPolicy {

    /**
     * Nonessential Android presentation-support sources may initialize only
     * after the Compose content boundary has been established.
     *
     * This ordering policy does not change the authority or semantics of
     * any initialized source.
     */
    const val COMPOSE_CONTENT_FIRST: Boolean = true

    /**
     * Stage 267 preserves the established Devil awakening duration instead
     * of treating intentional presentation time as startup overhead.
     */
    const val PRESERVE_AWAKENING_TIMING: Boolean = true
}
