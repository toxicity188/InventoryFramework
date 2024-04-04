package kr.toxicity.inventory.manager

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.toxicity.inventory.api.annotation.InventoryText
import kr.toxicity.inventory.data.FontComponent
import kr.toxicity.inventory.data.GlobalResource
import kr.toxicity.inventory.data.PluginInfo
import kr.toxicity.inventory.data.PluginResource
import kr.toxicity.inventory.util.*
import net.kyori.adventure.key.Key
import java.awt.AlphaComposite
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File

object TextManager: FrameworkManager {

    private const val FONT_SPLIT_SIZE = 16
    private val srcOver = AlphaComposite.getInstance(AlphaComposite.SRC_OVER)
    private val fontMap = HashMap<Class<*>, FontComponent>()
    private val charWidthMap = HashMap<String, FontRenderData>()

    override fun start(pluginInfo: List<PluginInfo>) {

    }

    fun getText(clazz: Class<*>) = fontMap[clazz]

    private class FontRenderData(
        val chars: List<Pair<String,JsonArray>>,
        val widthMap: Map<Char, Int>
    )

    override fun reload(pluginResources: List<PluginResource>, globalResource: GlobalResource) {
        fontMap.clear()
        charWidthMap.clear()
        pluginResources.forEach { resources ->
            val textFolder = resources.info.dataFolder.subFolder("text")
            val textFont = resources.font.subFolder("text")
            val textTextures = resources.textures.subFolder("text")

            val splitPow = FONT_SPLIT_SIZE * FONT_SPLIT_SIZE

            var fontAmount = 0

            resources.info.classes.forEach search@ { clazz ->
                val text = clazz.getAnnotation(InventoryText::class.java) ?: return@search
                val targetFile = File(textFolder, text.asset)
                if (!targetFile.exists()) {
                    PLUGIN.warn("This file doesn't exist: ${targetFile.name} in ${resources.info.plugin.name}")
                    return@search
                }
                val fontHeight = Math.round(text.scale.toDouble() * 1.4).toInt()
                val saveLocation = "${targetFile.nameWithoutExtension.lowercase()}_${text.scale}"
                val array = JsonArray().apply {
                    add(JsonObject().apply {
                        addProperty("type", "space")
                        add("advances", JsonObject().apply {
                            addProperty(" ", 4)
                        })
                    })
                }

                when (targetFile.extension) {
                    "ttf", "oft" -> {
                        runCatching {
                            charWidthMap[saveLocation]?.let { render ->
                                render.chars.forEach { pair ->
                                    array.add(JsonObject().apply {
                                        addProperty("type", "bitmap")
                                        addProperty("file", pair.first)
                                        addProperty("ascent", text.y)
                                        addProperty("height", Math.round(fontHeight.toDouble() * text.multiplier).toInt())
                                        add("chars", pair.second)
                                    })
                                }
                                fontMap[clazz] = FontComponent(
                                    text.x.toSpaceComponent(),
                                    text.space.toSpaceComponent(),
                                    Key.key("inventoryframework:${resources.info.dir}/text/font_${++fontAmount}"),
                                    render.widthMap
                                )
                            } ?: run {
                                val fontTexture = textTextures.subFolder(saveLocation)
                                val fontInstance = targetFile.inputStream().buffered().use {
                                    Font.createFont(Font.TRUETYPE_FONT, it).deriveFont(text.scale.toFloat())
                                }
                                val map = HashMap<Int, MutableList<Pair<Char, BufferedImage>>>()
                                (Char.MIN_VALUE..Char.MAX_VALUE).forEach { char ->
                                    if (fontInstance.canDisplay(char)) {
                                        BufferedImage(text.scale, fontHeight, BufferedImage.TYPE_INT_ARGB).apply {
                                            createGraphics().run {
                                                composite = srcOver
                                                font = fontInstance
                                                renderingHints[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                                                renderingHints[RenderingHints.KEY_FRACTIONALMETRICS] = RenderingHints.VALUE_FRACTIONALMETRICS_ON
                                                drawString(char.toString(), 0, text.scale)
                                                dispose()
                                            }
                                        }.removeEmptyWidth()?.let { image ->
                                            map.getOrPut(image.width) {
                                                mutableListOf()
                                            }.add(char to image)
                                        }
                                    }
                                }
                                var i = 0
                                val charJsonList = ArrayList<Pair<String,JsonArray>>()
                                fun save(width: Int, targetList: List<Pair<Char, BufferedImage>>) {
                                    val split = targetList.split(FONT_SPLIT_SIZE)
                                    val name = "${saveLocation}_${++i}.png"
                                    BufferedImage(targetList.size.coerceAtMost(FONT_SPLIT_SIZE) * width, ((targetList.size - 1) / FONT_SPLIT_SIZE + 1).coerceAtLeast(1).coerceAtMost(
                                        FONT_SPLIT_SIZE) * fontHeight, BufferedImage.TYPE_INT_ARGB).apply {
                                        createGraphics().run {
                                            composite = srcOver
                                            renderingHints[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                                            renderingHints[RenderingHints.KEY_FRACTIONALMETRICS] = RenderingHints.VALUE_FRACTIONALMETRICS_ON
                                            split.forEachIndexed { index1, pairs ->
                                                pairs.forEachIndexed { index2, pair ->
                                                    drawImage(pair.second, index2 * width, index1 * fontHeight, null)
                                                }
                                            }
                                            dispose()
                                        }
                                    }.save(File(fontTexture, name))
                                    val fileName = "inventoryframework:${resources.info.dir}/text/$saveLocation/$name"
                                    val charJson = JsonArray().apply {
                                        split.forEach { pairs ->
                                            add(pairs.map {
                                                it.first
                                            }.joinToString(""))
                                        }
                                    }
                                    array.add(JsonObject().apply {
                                        addProperty("type", "bitmap")
                                        addProperty("file", fileName)
                                        addProperty("ascent", text.y)
                                        addProperty("height", Math.round(fontHeight.toDouble() * text.multiplier).toInt())
                                        add("chars", charJson)
                                    })
                                    charJsonList.add(fileName to charJson)
                                }
                                map.forEach { entry ->
                                    entry.value.split(splitPow).forEach { targetList ->
                                        if (targetList.size == splitPow || targetList.size <= FONT_SPLIT_SIZE) {
                                            save(entry.key, targetList)
                                        } else {
                                            val split2 = targetList.split(FONT_SPLIT_SIZE)
                                            save(entry.key, split2.subList(0, split2.lastIndex).sum())
                                            save(entry.key, split2.lastElement)
                                        }
                                    }
                                }
                                fontAmount++
                                val charWidth = HashMap<Char, Int>().apply {
                                    map.entries.forEach { entry ->
                                        entry.value.forEach {
                                            put(it.first, Math.round(entry.key * text.multiplier).toInt())
                                        }
                                    }
                                }
                                fontMap[clazz] = FontComponent(
                                    text.x.toSpaceComponent(),
                                    text.space.toSpaceComponent(),
                                    Key.key("inventoryframework:${resources.info.dir}/text/font_$fontAmount"),
                                    charWidth
                                )
                                charWidthMap[saveLocation] = FontRenderData(charJsonList, charWidth)
                            }
                            JsonObject().apply {
                                add("providers", array)
                            }.save(File(textFont, "font_${fontAmount}.json"))
                        }.onFailure { e ->
                            PLUGIN.warn("Unable to read this font: ${targetFile.name} in ${resources.info.plugin.name}")
                            PLUGIN.warn("Reason: ${e.message}")
                        }
                    }
                    else -> {
                        PLUGIN.warn("Unsupported font format found: ${targetFile.name} in ${resources.info.plugin.name}")
                    }
                }
            }
        }
    }

    override fun end(pluginInfo: List<PluginInfo>) {
    }
}