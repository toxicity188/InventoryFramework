package kr.toxicity.inventory.manager

import kr.toxicity.inventory.data.GlobalResource
import kr.toxicity.inventory.data.PluginInfo
import kr.toxicity.inventory.data.PluginResource

interface FrameworkManager {
    fun start(pluginInfo: List<PluginInfo>)
    fun reload(pluginResources: List<PluginResource>, globalResource: GlobalResource)
    fun end(pluginInfo: List<PluginInfo>)
}