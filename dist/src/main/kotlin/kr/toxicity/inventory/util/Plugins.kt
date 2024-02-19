package kr.toxicity.inventory.util

import kr.toxicity.inventory.api.InventoryFramework
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

val PLUGIN
    get() = InventoryFramework.getInstance()

val DATA_FOLDER
    get() = PLUGIN.dataFolder.apply {
        if (!exists()) mkdir()
    }

fun Plugin.info(message: String) = logger.info(message)
fun Plugin.warn(message: String) = logger.warning(message)

fun Plugin.task(block: () -> Unit) = Bukkit.getScheduler().runTask(this, block)
fun Plugin.asyncTask(block: () -> Unit) = Bukkit.getScheduler().runTaskAsynchronously(this, block)
fun Plugin.taskLater(delay: Long, block: () -> Unit) = Bukkit.getScheduler().runTaskLater(this, block, delay)
fun Plugin.asyncTaskLater(delay: Long, block: () -> Unit) = Bukkit.getScheduler().runTaskLaterAsynchronously(this, block, delay)
fun Plugin.taskTimer(delay: Long, period: Long, block: () -> Unit) = Bukkit.getScheduler().runTaskTimer(this, block, delay, period)
fun Plugin.asyncTaskTimer(delay: Long, period: Long, block: () -> Unit) = Bukkit.getScheduler().runTaskTimerAsynchronously(this, block, delay, period)