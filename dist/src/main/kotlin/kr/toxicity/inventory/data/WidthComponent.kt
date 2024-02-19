package kr.toxicity.inventory.data

import net.kyori.adventure.text.Component

data class WidthComponent(val component: Component, val width: Int) {
    operator fun plus(other: WidthComponent) = WidthComponent(component.append(other.component), width + other.width)
}