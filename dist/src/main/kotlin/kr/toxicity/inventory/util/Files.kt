package kr.toxicity.inventory.util

import java.io.File

fun File.subFolder(dir: String) = File(this, dir).apply {
    if (!exists()) mkdir()
}

fun File.clearFolder() = apply {
    deleteRecursively()
    mkdir()
}

fun File.forEach(block: (File) -> Unit) {
    listFiles()?.forEach(block)
}