package kr.toxicity.inventory.manager

import kr.toxicity.inventory.api.gui.ClickData
import kr.toxicity.inventory.api.gui.GuiHolder
import kr.toxicity.inventory.api.enums.MouseButton
import kr.toxicity.inventory.data.GlobalResource
import kr.toxicity.inventory.data.PluginInfo
import kr.toxicity.inventory.data.PluginResource
import kr.toxicity.inventory.util.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

object InventoryManager: FrameworkManager {
    override fun start(pluginInfo: List<PluginInfo>) {
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun close(e: InventoryCloseEvent) {
                val holder = e.inventory.holder
                if (holder is GuiHolder) {
                    holder.isCancelled = true
                }
            }
            @EventHandler
            fun click(e: InventoryClickEvent) {
                val player = e.whoClicked as? Player ?: return
                val holder = player.openInventory.topInventory.holder as? GuiHolder ?: return
                val cursor = e.cursor
                val current = e.currentItem ?: ItemStack(Material.AIR)
                holder.executor.click(player, ClickData(
                    e.clickedInventory == player.inventory,
                    e.slot,
                    if (e.isLeftClick) {
                        if (e.isShiftClick) MouseButton.SHIFT_LEFT else MouseButton.LEFT
                    } else if (e.isRightClick) {
                        if (e.isShiftClick) MouseButton.SHIFT_RIGHT else MouseButton.RIGHT
                    } else MouseButton.OTHER,
                    cursor,
                    current,
                    e
                ), holder)
            }
        }, PLUGIN)
    }

    override fun reload(pluginResources: List<PluginResource>, globalResource: GlobalResource) {
    }

    override fun end(pluginInfo: List<PluginInfo>) {
    }
}