package kr.toxicity.inventory.data

import kr.toxicity.inventory.util.subFolder
import org.bukkit.plugin.Plugin

class PluginInfo(
    val plugin: Plugin,
    private val directory: String,
    val classes: List<Class<*>>
) {
    val dir = plugin.name.lowercase()
    val dataFolder
        get() = plugin.dataFolder.apply {
            if (!exists()) mkdir()
        }.subFolder(directory)
}