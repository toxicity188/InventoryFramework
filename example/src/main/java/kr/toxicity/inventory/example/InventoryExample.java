package kr.toxicity.inventory.example;

import kr.toxicity.inventory.api.InventoryFramework;
import kr.toxicity.inventory.api.annotation.InventoryPlugin;
import kr.toxicity.inventory.api.gui.ClickData;
import kr.toxicity.inventory.api.gui.GuiExecutor;
import kr.toxicity.inventory.api.gui.GuiHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@InventoryPlugin
public class InventoryExample extends JavaPlugin {
    @Override
    public void onEnable() {
        var command = getCommand("testinventory");
        if (command != null) command.setExecutor((commandSender, command1, s, strings) -> {
            if (commandSender.isOp() && commandSender instanceof Player player) {
                InventoryFramework.getInstance().builder()
                        .append(new TestAsset())
                        .append(new TestAsset2())
                        .setExecutor(new GuiExecutor() {
                            @Override
                            public void init(@NotNull Player player, @NotNull GuiHolder holder) {
                                player.sendMessage("Hello world!");
                            }

                            @Override
                            public void click(@NotNull Player player, @NotNull ClickData data, @NotNull GuiHolder holder) {
                                player.sendMessage("The clicked slot is " + data.clickedSlot() + "!");
                                data.setCancelled(true);
                                if (data.clickedSlot() == 0) {
                                    holder.getAnimation().toggle(TestAsset.class);
                                }
                            }

                            @Override
                            public void end(@NotNull Player player, @NotNull GuiHolder holder) {
                                player.sendMessage("Ended!");
                            }
                        })
                        .build()
                        .open(player);
            }
            return true;
        });
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }
}
