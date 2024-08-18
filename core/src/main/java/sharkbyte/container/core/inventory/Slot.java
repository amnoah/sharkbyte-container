package sharkbyte.container.core.inventory;

import com.github.retrooper.packetevents.protocol.item.ItemStack;

public class Slot {

    private boolean changed;
    private ItemStack item;

    public Slot(ItemStack item) {
        this.item = item;
        this.changed = false;
    }

    /*
     * Getters.
     */

    public ItemStack getItem() {
        return item;
    }

    public boolean hasChanged() {
        return changed;
    }

    /*
     * Setters.
     */

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void setItem(ItemStack item) {
        if (this.item.equals(item)) return;
        this.item = item;
        this.changed = true;
    }
}
