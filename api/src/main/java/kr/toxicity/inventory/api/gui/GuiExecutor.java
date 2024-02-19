package kr.toxicity.inventory.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface GuiExecutor {
    GuiExecutor EMPTY = new GuiExecutor() {
        @Override
        public void init(@NotNull Player player, @NotNull GuiAnimation animation) {

        }

        @Override
        public void click(@NotNull Player player, @NotNull ClickData data, @NotNull GuiAnimation animation) {

        }

        @Override
        public void end(@NotNull Player player, @NotNull GuiAnimation animation) {

        }
    };
    void init(@NotNull Player player, @NotNull GuiAnimation animation);
    void click(@NotNull Player player, @NotNull ClickData data, @NotNull GuiAnimation animation);
    void end(@NotNull Player player, @NotNull GuiAnimation animation);
}
