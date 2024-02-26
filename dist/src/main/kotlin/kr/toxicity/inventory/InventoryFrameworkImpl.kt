package kr.toxicity.inventory

import kr.toxicity.inventory.api.InventoryFramework
import kr.toxicity.inventory.api.annotation.InventoryPlugin
import kr.toxicity.inventory.api.gui.GuiBuilder
import kr.toxicity.inventory.data.GlobalResource
import kr.toxicity.inventory.data.PluginInfo
import kr.toxicity.inventory.data.PluginResource
import kr.toxicity.inventory.gui.GuiBuilderImpl
import kr.toxicity.inventory.manager.*
import kr.toxicity.inventory.util.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.function.Consumer
import java.util.jar.JarEntry
import java.util.jar.JarFile

class InventoryFrameworkImpl: InventoryFramework() {

    private val info = mutableListOf<PluginInfo>()
    private val managers = listOf(
        ConfigManager,
        BackgroundManager,
        AnimationManager,
        TextManager,
        InventoryManager
    )

    override fun onEnable() {
        getCommand("inventoryframework")?.setExecutor { commandSender, _, _, _ ->
            if (commandSender.isOp) {
                asyncTask {
                    commandSender.sendMessage(Component.text("Reload complete! (${reload()} ms)").color(NamedTextColor.GREEN))
                }
            }
            true
        }
        val getFile = JavaPlugin::class.java.getDeclaredField("file").apply {
            isAccessible = true
        }
        task {
            Bukkit.getPluginManager().plugins.forEach { plugin ->
                plugin.javaClass.getAnnotation(InventoryPlugin::class.java)?.let { use ->
                    val classes = mutableListOf<Class<*>>()
                    JarFile(getFile[plugin] as File).use { jar ->
                        jar.entries().asIterator().forEach { entry ->
                            if (!entry.isDirectory && entry.name.endsWith("class")) {
                                fun addClass(clazz: Class<*>) {
                                    classes.add(clazz)
                                    clazz.classes.forEach { subClass ->
                                        addClass(subClass)
                                    }
                                }
                                runCatching {
                                    addClass(Class.forName(entry.name.substringBeforeLast(".").replace('/','.')))
                                }
                            }
                        }
                    }
                    info.add(PluginInfo(
                        plugin,
                        use.directory,
                        classes
                    ))
                }
            }
            managers.forEach {
                it.start(info)
            }
            reload()
            info("Plugin enabled.")
        }
    }

    override fun reload(): Long {
        val time = System.currentTimeMillis()
        val global = GlobalResource()
        val infoList = info.map { plugin ->
            PluginResource(global, plugin)
        }
        managers.forEach {
            it.reload(infoList, global)
        }
        return System.currentTimeMillis() - time
    }

    override fun onDisable() {
        managers.forEach {
            it.end(info)
        }
        info("Plugin disabled.")
    }

    override fun builder(): GuiBuilder = GuiBuilderImpl()
    override fun getEmptyItem(metaConsumer: Consumer<ItemMeta>): ItemStack {
        return ConfigManager.getEmptyItem {
            metaConsumer.accept(it)
        }
    }

    override fun loadAssets(prefix: String, dir: File) {
        JarFile(file).use {
            it.entries().asIterator().forEach { entry ->
                if (!entry.name.startsWith(prefix)) return@forEach
                if (entry.name.length <= prefix.length + 1) return@forEach
                val name = entry.name.substring(prefix.length + 1)
                if (entry.isDirectory) {
                    val file = File(dir, name)
                    if (!file.exists()) file.mkdir()
                } else {
                    getResource(entry.name)?.buffered()?.use { stream ->
                        val file = File(dir, name)
                        if (!file.exists()) {
                            file.createNewFile()
                            file.outputStream().buffered().use { fos ->
                                stream.copyTo(fos)
                            }
                        }
                    }
                }
            }
        }
    }
}