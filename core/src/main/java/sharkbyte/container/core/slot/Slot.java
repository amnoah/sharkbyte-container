package sharkbyte.container.core.slot;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

public class Slot {

    private boolean changed;
    private ClickEvent clickEvent = null;
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

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    public void setItem(ItemStack item) {
        if (this.item.equals(item)) return;
        this.item = item;
        this.changed = true;
    }

    /*
     * Handlers.
     */

    public void handleClick(WrapperPlayClientClickWindow.WindowClickType clickType) {
        if (clickEvent != null) clickEvent.handleClick(clickType);
    }
}
