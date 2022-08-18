package com.saetova.gitlabciintegration.utils

import java.awt.Color
import javax.swing.ImageIcon

fun <T : Any> T.getImageIcon(path: String): ImageIcon {
    return ImageIcon(javaClass.getResource("/images/$path"))
}

fun getLinkTextColor(): Color = Color(40, 123, 222)