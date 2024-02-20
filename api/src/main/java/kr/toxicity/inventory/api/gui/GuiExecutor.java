package kr.toxicity.inventory.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface GuiExecutor {
    GuiExecutor EMPTY = new GuiExecutor() {
        @Override
        public void init(@NotNull Player player, @NotNull GuiHolder holder) {

        }

        @Override
        public void click(@NotNull Player player, @NotNull ClickData data, @NotNull GuiHolder holder) {

        }

        @Override
        public void end(@NotNull Player player, @NotNull GuiHolder holder) {

        }
    };
    void init(@NotNull Player player, @NotNull GuiHolder holder);
    void click(@NotNull Player player, @NotNull ClickData data, @NotNull GuiHolder holder);
    void end(@NotNull Player player, @NotNull GuiHolder holder);
}
