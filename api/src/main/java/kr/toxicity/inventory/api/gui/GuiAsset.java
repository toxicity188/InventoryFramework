package kr.toxicity.inventory.api.gui;

import org.jetbrains.annotations.NotNull;

public interface GuiAsset {
    enum Align {
        LEFT,
        CENTER,
        RIGHT
    }
    default @NotNull Align getAlign() {
        return Align.LEFT;
    }
}
