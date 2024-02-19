package kr.toxicity.inventory.example;

import kr.toxicity.inventory.api.annotation.InventoryAnimation;
import kr.toxicity.inventory.api.annotation.InventoryBackground;
import kr.toxicity.inventory.api.annotation.InventoryText;
import kr.toxicity.inventory.api.enums.AnimationType;
import kr.toxicity.inventory.api.gui.GuiAsset;
import kr.toxicity.inventory.api.gui.GuiText;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@InventoryBackground(
        asset = "test.png"
)
@InventoryAnimation(
        asset = "test.png",
        playTime = 20,
        type = AnimationType.LOOP,
        xEquation = "10cos(t/10 * pi) + 20",
        yEquation = "10sin(t/10 * pi) - 10"
)
@InventoryText(
        scale = 24,
        multiplier = 0.5,
        y = -10,
        asset = "test.ttf"
)
public class TestAsset implements GuiAsset, GuiText {
    @Override
    public @NotNull String text(@NotNull Player player) {
        return "체력 : " + player.getHealth();
    }
}
