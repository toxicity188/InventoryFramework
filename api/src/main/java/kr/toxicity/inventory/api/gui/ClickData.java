package kr.toxicity.inventory.api.gui;

import kr.toxicity.inventory.api.enums.MouseButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ClickData(
        boolean isPlayerInventory,
        int clickedSlot,
        @NotNull MouseButton button,
        @NotNull ItemStack cursorItem,
        @NotNull ItemStack clickedItem,
        @NotNull InventoryClickEvent originalEvent
) {
    public boolean isCancelled() {
        return originalEvent.isCancelled();
    }
    public void setCancelled(boolean toCancel) {
        originalEvent.setCancelled(toCancel);
    }
}
