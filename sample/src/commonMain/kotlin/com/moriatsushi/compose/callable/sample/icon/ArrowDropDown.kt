package com.moriatsushi.compose.callable.sample.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
val Icons.ArrowDropDown: ImageVector
    get() {
        if (arrowDropDownCache != null) return arrowDropDownCache!!

        arrowDropDownCache =
            ImageVector
                .Builder(
                    name = "Arrow_drop_down",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 960f,
                    viewportHeight = 960f,
                ).apply {
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                    ) {
                        moveTo(480f, 600f)
                        lineTo(280f, 400f)
                        horizontalLineToRelative(400f)
                        close()
                    }
                }.build()

        return arrowDropDownCache!!
    }

private var arrowDropDownCache: ImageVector? = null
