package kr.toxicity.inventory.manager

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.toxicity.inventory.api.annotation.InventoryBackground
import kr.toxicity.inventory.data.GlobalResource
import kr.toxicity.inventory.data.PluginInfo
import kr.toxicity.inventory.data.PluginResource
import kr.toxicity.inventory.data.WidthComponent
import kr.toxicity.inventory.util.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import java.io.File

object BackgroundManager: FrameworkManager {

    private val comp = HashMap<Class<*>, WidthComponent>()

    override fun start(pluginInfo: List<PluginInfo>) {

    }

    fun getBackground(clazz: Class<*>) = comp[clazz]

    override fun reload(pluginResources: List<PluginResource>, globalResource: GlobalResource) {
        comp.clear()
        pluginResources.forEach { resources ->
            val folder = resources.info.dataFolder.subFolder("background")
            val textures = resources.textures.subFolder("background")
            var i = 0xD0000
            val json = JsonArray()
            val key = Key.key("inventoryframework:${resources.info.dir}/background")
            resources.info.classes.forEach search@ { clazz ->
                val background = clazz.getAnnotation(InventoryBackground::class.java) ?: return@search
                val targetFile = File(folder, background.asset)
                if (!targetFile.exists()) {
                    PLUGIN.warn("This file doesn't exist: ${targetFile.name} in ${resources.info.plugin.name}")
                    return@search
                }
                runCatching {
                    targetFile.toImage().removeEmptySide()?.let {
                        it.save(File(textures, "${targetFile.nameWithoutExtension}.png"))
                        val c = (++i).parseChar()
                        json.add(JsonObject().apply {
                            addProperty("type", "bitmap")
                            addProperty("file", "inventoryframework:${resources.info.dir}/background/${targetFile.nameWithoutExtension}.png")
                            addProperty("ascent", 13 + background.y)
                            addProperty("height", it.height)
                            add("chars", JsonArray().apply {
                                add(c)
                            })
                        })
                        comp[clazz] = WidthComponent(Component.text((-8 + background.x + 0xD0000).parseChar()).font(SPACE_KEY), 0) + WidthComponent(Component.text(c).font(key), it.width)
                    } ?: run {
                        PLUGIN.warn("This image is empty: ${targetFile.name} in ${resources.info.plugin.name}")
                        return@search
                    }

                }.onFailure { e ->
                    PLUGIN.warn("Error has occurred at ${targetFile.name} in ${resources.info.plugin.name}")
                    PLUGIN.warn("Reason: ${e.message}")
                }
            }
            JsonObject().apply {
                add("providers", json)
            }.save(File(resources.font, "background.json"))
        }
    }

    override fun end(pluginInfo: List<PluginInfo>) {
    }
}