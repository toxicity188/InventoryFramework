package kr.toxicity.inventory.data

import kr.toxicity.inventory.util.DATA_FOLDER
import kr.toxicity.inventory.util.PLUGIN
import kr.toxicity.inventory.util.clearFolder
import kr.toxicity.inventory.util.subFolder

class GlobalResource {
    val build = DATA_FOLDER.subFolder("build")
        .clearFolder()

    init {
        PLUGIN.loadAssets("pack", build)
    }

    val assets = build
        .subFolder("assets")
        .subFolder("inventoryframework")

    val font = assets
        .subFolder("font")

    val textures = assets
        .subFolder("textures")
}