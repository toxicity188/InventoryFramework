package kr.toxicity.inventory.api;

import kr.toxicity.inventory.api.gui.GuiBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class InventoryFramework extends JavaPlugin {
    private static InventoryFramework instance;
    @Override
    public final void onLoad() {
        if (instance != null) throw new RuntimeException("InventoryFramework is already activated.");
        instance = this;
    }
    public static @NotNull InventoryFramework getInstance() {
        return Objects.requireNonNull(instance, "InventoryFramework is not activated.");
    }

    public abstract long reload();
    public abstract @NotNull GuiBuilder builder();
    public abstract void loadAssets(@NotNull String prefix, @NotNull File dir);
    public abstract @NotNull ItemStack getEmptyItem(@NotNull Consumer<ItemMeta> metaConsumer);
}
