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
    private val parent = build
        .subFolder("assets")

    private val assets = parent
        .subFolder("inventoryframework")
    private val minecraftAssets = parent
        .subFolder("minecraft")

    val font = assets
        .subFolder("font")

    val textures = assets
        .subFolder("textures")

    val models = assets
        .subFolder("models")
        .subFolder("item")

    val minecraftModels = minecraftAssets
        .subFolder("models")
        .subFolder("item")
}