package kr.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface GuiBuilder {
    @NotNull GuiBuilder append(@NotNull GuiAsset asset);
    @NotNull GuiBuilder rows(int row);
    @NotNull GuiBuilder executor(@NotNull GuiExecutor executor);
    @NotNull Gui build();
}
