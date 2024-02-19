package kr.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface GuiBuilder {
    @NotNull GuiBuilder append(@NotNull GuiAsset asset);
    @NotNull GuiBuilder setExecutor(@NotNull GuiExecutor executor);
    @NotNull Gui build();
}
