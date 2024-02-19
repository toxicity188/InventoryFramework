package kr.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface GuiAnimation {
    void init(@NotNull Class<? extends GuiAsset> clazz);
    void play(@NotNull GuiAsset asset);
    void toggle(@NotNull Class<? extends GuiAsset> clazz);
    void stop(@NotNull Class<? extends GuiAsset> asset);
}
