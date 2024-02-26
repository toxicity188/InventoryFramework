package kr.toxicity.inventory.manager

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.toxicity.inventory.data.GlobalResource
import kr.toxicity.inventory.data.PluginInfo
import kr.toxicity.inventory.data.PluginResource
import kr.toxicity.inventory.util.PLUGIN
import kr.toxicity.inventory.util.save
import kr.toxicity.inventory.util.warn
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.io.File

object ConfigManager: FrameworkManager {

    private var emptyMaterial = Material.BARRIER
    override fun start(pluginInfo: List<PluginInfo>) {
    }

    override fun reload(pluginResources: List<PluginResource>, globalResource: GlobalResource) {
        val configFile = File(PLUGIN.dataFolder, "config.yml")
        if (!configFile.exists()) PLUGIN.saveResource("config.yml", false)
        runCatching {
            val yaml = YamlConfiguration.loadConfiguration(configFile)
            yaml.getString("empty-material")?.let {
                emptyMaterial = Material.valueOf(it.uppercase())
            }
            JsonObject().apply {
                addProperty("parent", "minecraft:item/generated")
                add("textures", JsonObject().apply {
                    addProperty("layer0", "minecraft:item/${emptyMaterial.name.lowercase()}")
                })
                add("overrides", JsonArray().apply {
                    add(JsonObject().apply {
                        add("predicate", JsonObject().apply {
                            addProperty("custom_model_data", 1)
                        })
                        addProperty("model", "inventoryframework:item/empty")
                    })
                })
            }.save(File(globalResource.minecraftModels, "${emptyMaterial.name.lowercase()}.json"))
            JsonObject().apply {
                add("textures", JsonObject().apply {
                    addProperty("0", "inventoryframework:item/empty")
                })
            }.save(File(globalResource.models, "empty.json"))
        }.onFailure { e ->
            PLUGIN.warn("Unable to load config.yml")
            PLUGIN.warn("Reason: ${e.message}")
        }
    }

    fun getEmptyItem(block: (ItemMeta) -> Unit) = ItemStack(emptyMaterial).apply {
        itemMeta = itemMeta.apply {
            setCustomModelData(1)
            block(this)
        }
    }

    override fun end(pluginInfo: List<PluginInfo>) {
    }
}