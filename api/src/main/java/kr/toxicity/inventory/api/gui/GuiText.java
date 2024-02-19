package kr.toxicity.inventory.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface GuiText {
    @NotNull String text(@NotNull Player player);
}
