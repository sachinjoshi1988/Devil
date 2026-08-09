package com.devil.app.accessibility

/**
 * Stage 38 bounded Android accessibility action types.
 *
 * CLICK_VISIBLE_TEXT requests one accessibility click against an explicitly
 * supplied visible textual target.
 *
 * Stage 38 deliberately does not infer user intent, choose targets from raw
 * conversation text, grant authorization, or establish successful outcome.
 *
 * Accessibility action type != execution approval.
 */
enum class AndroidAccessibilityActionType {
    CLICK_VISIBLE_TEXT,
}
