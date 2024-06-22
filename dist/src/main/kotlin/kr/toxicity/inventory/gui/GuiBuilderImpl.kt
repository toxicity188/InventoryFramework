package kr.toxicity.inventory.gui

import kr.toxicity.inventory.api.enums.AnimationType
import kr.toxicity.inventory.api.gui.Gui
import kr.toxicity.inventory.api.gui.GuiAnimation
import kr.toxicity.inventory.api.gui.GuiAsset
import kr.toxicity.inventory.api.gui.GuiBuilder
import kr.toxicity.inventory.api.gui.GuiExecutor
import kr.toxicity.inventory.api.gui.GuiHolder
import kr.toxicity.inventory.api.gui.GuiText
import kr.toxicity.inventory.data.AnimationComponent
import kr.toxicity.inventory.data.WidthComponent
import kr.toxicity.inventory.manager.AnimationManager
import kr.toxicity.inventory.manager.BackgroundManager
import kr.toxicity.inventory.manager.TextManager
import kr.toxicity.inventory.util.*
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import kotlin.math.roundToInt

class GuiBuilderImpl: GuiBuilder {

    private val assets = ArrayList<GuiAsset>()
    private var executor: GuiExecutor = GuiExecutor.EMPTY
    private var row = 6

    override fun append(asset: GuiAsset): GuiBuilder {
        assets.add(asset)
        return this
    }

    override fun rows(row: Int): GuiBuilder {
        if (row < 1) throw RuntimeException("row < 1")
        if (row > 6) throw RuntimeException("row > 6")
        this.row = row
        return this
    }

    override fun executor(executor: GuiExecutor): GuiBuilder {
        this.executor = executor
        return this
    }

    override fun build(): Gui {
        return GuiImpl()
    }

    private inner class GuiImpl: Gui {
        override fun open(player: Player) {
            var comp = EMPTY_WIDTH_COMPONENT
            var maxWidth = 0
            val assetsComp = ArrayList<Pair<GuiAsset.Align, WidthComponent>>()
            assets.forEach {
                BackgroundManager.getBackground(it.javaClass)?.let { background ->
                    val addComp = background + NEGATIVE_ONE_SPACE_COMPONENT + NEW_LAYER
                    if (maxWidth < addComp.width) maxWidth = addComp.width
                    assetsComp.add(it.align to addComp)
                }
                if (it is GuiText) {
                    val text = it.text(player)
                    TextManager.getText(it.javaClass)?.let { converter ->
                        val addComp = converter.toComponent(text)
                        if (maxWidth < addComp.width) maxWidth = addComp.width
                        assetsComp.add(it.align to addComp)
                    }
                }
            }
            assetsComp.forEach {
                when (it.first) {
                    GuiAsset.Align.LEFT -> {
                        comp += it.second + (-it.second.width).toSpaceComponent()
                    }
                    GuiAsset.Align.CENTER -> {
                        val value = ((maxWidth - it.second.width).toDouble() / 2).roundToInt()
                        comp += value.toSpaceComponent() + it.second + (-value - it.second.width).toSpaceComponent()
                    }
                    GuiAsset.Align.RIGHT -> {
                        val value = maxWidth - it.second.width
                        comp += value.toSpaceComponent() + it.second + (-value - it.second.width).toSpaceComponent()
                    }
                }
            }

            val renderer = mutableMapOf<Class<*>, AnimationRenderer>().apply {
                assets.forEach {
                    AnimationManager.getAnimation(it.javaClass)?.let { animation ->
                        put(it.javaClass, AnimationRenderer(animation))
                    }
                }
            }
            val holder = GuiHolder(comp.component, this, executor, object : GuiAnimation {
                override fun init(clazz: Class<out GuiAsset>) {
                    renderer[clazz]?.i = 0
                }

                override fun play(asset: GuiAsset) {
                    AnimationManager.getAnimation(asset.javaClass)?.let { animation ->
                        renderer[asset.javaClass] = AnimationRenderer(animation)
                    }
                }

                override fun toggle(clazz: Class<out GuiAsset>) {
                    renderer[clazz]?.let {
                        it.enabled = !it.enabled
                    }
                }

                override fun stop(asset: Class<out GuiAsset>) {
                    renderer.remove(asset)
                }

            }, row * 9)
            val oldHolder = player.openInventory.topInventory.holder
            if (oldHolder is GuiHolder) {
                holder.parent = oldHolder
            }
            holder.executor.init(player, holder)
            player.openInventory(holder.inventory)
            GuiTask {
                if (player.isValid && !holder.isCancelled && player.openInventory.topInventory.holder === holder) {
                    if (renderer.isNotEmpty()) {
                        var finalComp = comp
                        renderer.values.forEach {
                            if (it.enabled) {
                                val next = it.next()
                                finalComp += next + NEGATIVE_ONE_SPACE_COMPONENT + NEW_LAYER + (-next.width).toSpaceComponent()
                            }
                        }
                        holder.setInventory(finalComp.component)
                        player.openInventory(holder.inventory)
                        holder.isCancelled = false
                    }
                    false
                } else {
                    player.closeInventory()
                    holder.executor.end(player, holder)
                    holder.parent?.let { parent ->
                        PLUGIN.taskLater(1) {
                            parent.gui.open(player)
                        }
                    }
                    true
                }
            }
        }
    }

    private class GuiTask(predicate: () -> Boolean) {
        private val task = PLUGIN.taskTimer(1, 1) {
            if (predicate()) cancel()
        }
        private fun cancel() {
            task.cancel()
        }
    }

    private class AnimationRenderer(val component: AnimationComponent) {
        var i = 0
        var enabled = true

        fun next(): WidthComponent {
            if (i++ == component.list.lastIndex) i = when (component.type) {
                AnimationType.PLAY_ONCE_WITHOUT_REMAIN -> {
                    enabled = false
                    0
                }
                AnimationType.PLAY_ONCE -> component.list.lastIndex
                AnimationType.LOOP -> 0
            }
            return component.list[i]
        }
    }
}