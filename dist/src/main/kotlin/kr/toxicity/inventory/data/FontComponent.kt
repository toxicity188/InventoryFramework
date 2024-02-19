package kr.toxicity.inventory.data

import kr.toxicity.inventory.util.EMPTY_WIDTH_COMPONENT
import kr.toxicity.inventory.util.NEGATIVE_ONE_SPACE_COMPONENT
import kr.toxicity.inventory.util.toSpaceComponent
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component

class FontComponent(
    private val xComponent: WidthComponent,
    private val spaceComponent: WidthComponent,
    private val key: Key,
    private val widthMap: Map<Char, Int>
) {
    companion object {
        private val space = 4.toSpaceComponent()
    }
    fun toComponent(target: String): WidthComponent {
        var comp = EMPTY_WIDTH_COMPONENT + xComponent
        target.forEachIndexed { index, char ->
            if (char == ' ') {
                comp += space
            } else {
                widthMap[char]?.let { width ->
                    comp += WidthComponent(Component.text(char).font(key), width) + NEGATIVE_ONE_SPACE_COMPONENT
                }
            }
            if (index < target.lastIndex) comp += spaceComponent
        }
        return comp
    }
}