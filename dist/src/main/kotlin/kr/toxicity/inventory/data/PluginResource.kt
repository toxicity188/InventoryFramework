package kr.toxicity.inventory.data

import kr.toxicity.inventory.util.subFolder

class PluginResource(resource: GlobalResource, val info: PluginInfo) {
    val font = resource.font
        .subFolder(info.dir)

    val textures = resource.textures
        .subFolder(info.dir)
}