package kr.toxicity.inventory.api.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class GuiHolder implements InventoryHolder {
    private Inventory inventory;
    private GuiHolder parent;
    private final GuiExecutor executor;
    private final GuiAnimation animation;
    private boolean cancelled;
    private final Gui gui;
    public GuiHolder(@NotNull Component component, @NotNull Gui gui, @NotNull GuiExecutor executor, @NotNull GuiAnimation animation, int size) {
        this.executor = Objects.requireNonNull(executor);
        this.animation = Objects.requireNonNull(animation);
        this.gui = Objects.requireNonNull(gui);
        inventory = Bukkit.createInventory(this, size, component);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull Component component) {
        var newInventory = Bukkit.createInventory(this, this.inventory.getSize(), component);
        newInventory.setContents(this.inventory.getContents());
        this.inventory = newInventory;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public @NotNull Gui getGui() {
        return gui;
    }

    public @Nullable GuiHolder getParent() {
        return parent;
    }

    public @NotNull GuiAnimation getAnimation() {
        return animation;
    }

    public @NotNull GuiExecutor getExecutor() {
        return executor;
    }

    public void setParent(@Nullable GuiHolder parent) {
        this.parent = parent;
    }
}
