package com.devil.app.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Stage 251 Final Design System geometry.
 *
 * Devil uses restrained angular geometry rather than generic highly-rounded
 * cards. Small controls remain practical while larger surfaces receive
 * asymmetric cut-corner treatment for the final futuristic visual language.
 *
 * Shape does not encode authority, security, execution, success, or truth.
 *
 * DESIGN_SHAPE != CONSTITUTIONAL_STATE.
 */
internal val DevilShapes =
    Shapes(
        extraSmall =
            RoundedCornerShape(
                4.dp,
            ),
        small =
            CutCornerShape(
                topStart = 0.dp,
                topEnd = 6.dp,
                bottomEnd = 0.dp,
                bottomStart = 6.dp,
            ),
        medium =
            CutCornerShape(
                topStart = 0.dp,
                topEnd = 10.dp,
                bottomEnd = 0.dp,
                bottomStart = 10.dp,
            ),
        large =
            CutCornerShape(
                topStart = 0.dp,
                topEnd = 16.dp,
                bottomEnd = 0.dp,
                bottomStart = 16.dp,
            ),
        extraLarge =
            CutCornerShape(
                topStart = 0.dp,
                topEnd = 24.dp,
                bottomEnd = 0.dp,
                bottomStart = 24.dp,
            ),
    )
