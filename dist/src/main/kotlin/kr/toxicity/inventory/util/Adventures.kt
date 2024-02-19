package kr.toxicity.inventory.util

import kr.toxicity.inventory.data.WidthComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

val SPACE_KEY = Key.key("inventoryframework:space")

val EMPTY_WIDTH_COMPONENT = WidthComponent(Component.empty().color(NamedTextColor.WHITE), 0)
val NEW_LAYER = WidthComponent(Component.text(0xC0000.parseChar()).font(SPACE_KEY), 0)
val NEGATIVE_ONE_SPACE_COMPONENT = WidthComponent(Component.text((0xD0000 - 1).parseChar()).font(SPACE_KEY), 0)

fun Int.parseChar(): String {
    return if (this <= 0xFFFF) this.toChar().toString()
    else {
        val t = this - 0x10000
        return "${((t ushr 10) + 0xD800).toChar()}${((t and 1023) + 0xDC00).toChar()}"
    }
}
fun Int.toSpaceComponent() = WidthComponent(Component.text((this + 0xD0000).parseChar()).font(SPACE_KEY), this)