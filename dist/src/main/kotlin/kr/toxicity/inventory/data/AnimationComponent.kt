package kr.toxicity.inventory.data

import kr.toxicity.inventory.api.enums.AnimationType

data class AnimationComponent(
    val list: List<WidthComponent>,
    val type: AnimationType
)