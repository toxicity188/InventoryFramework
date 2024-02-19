package kr.toxicity.inventory.manager

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.toxicity.inventory.api.annotation.InventoryAnimation
import kr.toxicity.inventory.data.*
import kr.toxicity.inventory.equation.TEquation
import kr.toxicity.inventory.util.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import java.io.File
import kotlin.math.round

object AnimationManager: FrameworkManager {

    private val comp = HashMap<Class<*>, AnimationComponent>()

    override fun start(pluginInfo: List<PluginInfo>) {

    }

    fun getAnimation(clazz: Class<*>) = comp[clazz]

    override fun reload(pluginResources: List<PluginResource>, globalResource: GlobalResource) {
        comp.clear()
        pluginResources.forEach { resources ->
            val targetFolder = resources.info.dataFolder.subFolder("animation")

            val textures = resources.textures.subFolder("animation")
            val font = resources.font.subFolder("animation")
            var t = 0xD0000
            var index = 0
            resources.info.classes.forEach search@ { clazz ->
                val animation = clazz.getAnnotation(InventoryAnimation::class.java) ?: return@search
                val targetFile = File(targetFolder, animation.asset)
                if (!targetFile.exists()) {
                    PLUGIN.warn("This file doesn't exist: ${targetFile.name} in ${resources.info.plugin.name}")
                    return@search
                }
                runCatching {
                    val xEquation = TEquation(animation.xEquation)
                    val yEquation = TEquation(animation.yEquation)
                    val json = JsonArray()
                    val list = mutableListOf<WidthComponent>()
                    index++
                    val key = Key.key("inventoryframework:${resources.info.dir}/animation/${targetFile.nameWithoutExtension}_$index")
                    targetFile.toImage().removeEmptySide()?.let { image ->
                        image.save(File(textures, "${targetFile.nameWithoutExtension}.png"))
                        for (i in 1..<animation.playTime) {
                            val d = i.toDouble()
                            val c = (++t).parseChar()
                            json.add(JsonObject().apply {
                                addProperty("type", "bitmap")
                                addProperty("file", "inventoryframework:${resources.info.dir}/animation/${targetFile.nameWithoutExtension}.png")
                                addProperty("ascent", round(yEquation.evaluate(d)).toInt())
                                addProperty("height", image.height)
                                add("chars", JsonArray().apply {
                                    add(c)
                                })
                            })
                            list.add(round(xEquation.evaluate(d)).toInt().toSpaceComponent() + WidthComponent(Component.text(c).font(key), image.width))
                        }
                        JsonObject().apply {
                            add("providers", json)
                        }.save(File(font, "${targetFile.nameWithoutExtension}_${index}.json"))
                        comp[clazz] = AnimationComponent(list, animation.type)
                    } ?: run {
                        PLUGIN.warn("This image is empty: ${targetFile.name} in ${resources.info.plugin.name}")
                        return@search
                    }
                }
            }
        }
    }

    override fun end(pluginInfo: List<PluginInfo>) {
    }
}