package kr.toxicity.inventory.example;

import kr.toxicity.inventory.api.annotation.InventoryText;
import kr.toxicity.inventory.api.gui.GuiAsset;
import kr.toxicity.inventory.api.gui.GuiText;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@InventoryText(
        multiplier = 0.5,
        y = -20,
        asset = "test.ttf"
)
public class TestAsset2 implements GuiAsset, GuiText {
    @Override
    public @NotNull String text(@NotNull Player player) {
        return "배고픔 : " + player.getFoodLevel();
    }
}
