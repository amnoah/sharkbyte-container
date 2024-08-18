package sharkbyte.container.core.container;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.kyori.adventure.text.Component;
import sharkbyte.container.core.ContainerPacketListener;
import sharkbyte.container.core.WindowIDTracker;
import sharkbyte.container.core.slot.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a container.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public abstract class Container {

    protected final User user;
    protected final List<Slot> slots;
    protected int windowID;

    protected boolean changedTitle, open;
    protected String title;


    public Container(User user, String title, int slotCount) {
        this.user = user;
        this.title = title;
        changedTitle = false;
        open = false;

        this.slots = new ArrayList<>(slotCount);
        for (int i = 0; i < slotCount; i++) slots.add(new Slot(new ItemStack.Builder().type(ItemTypes.AIR).build()));
    }

    /*
     * Getters.
     */

    /**
     * Return the given slot.
     * If you are modifying the ItemStack already present in a slot (example: slot.getItem().setAmount(5)), you MUST
     * mark slot.setChanged as true.
     */
    public Slot getSlot(int slot) {
        return slots.get(slot);
    }

    public int getSlots() {
        return slots.size();
    }

    public int getWindowID() {
        return windowID;
    }

    /*
     * Setters.
     */

    /**
     * Set the ItemStack in a given slot.
     */
    public void setItem(int slot, ItemStack item) {
        slots.get(slot).setItem(item);
    }

    public void setTitle(String title) {
        if (!this.title.equals(title)) return;
        this.title = title;
        changedTitle = true;
    }

    /*
     * Data to be implemented.
     * Information from here: https://wiki.vg/Inventory
     */

    protected abstract String getLegacyName();

    protected abstract int getModernID();

    /*
     * Inventory Handlers.
     */

    /**
     *
     */
    public void create() {
        ContainerPacketListener.addContainer(this);
    }

    /**
     *
     */
    public void destroy() {
        close();
        ContainerPacketListener.removeContainer(this);
    }

    /*
     * Inventory Packet Handlers.
     */

    public void close() {
        if (!open) return;

        user.sendPacket(new WrapperPlayServerCloseWindow(
                windowID
        ));
        open = false;
    }

    public void open() {
        if (open) return;

        windowID = WindowIDTracker.getID(user);

        /*
         * Because windows were rewritten in 1.14, we have to use separate constructors depending on the version
         * that we're in.
         *
         * This will open the window for the client.
         */
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) {
            user.writePacket(new WrapperPlayServerOpenWindow(
                    windowID,
                    getModernID(),
                    Component.text(title),
                    slots.size(),
                    true,
                    0
            ));
        } else {
            user.writePacket(new WrapperPlayServerOpenWindow(
                    windowID,
                    getLegacyName(),
                    Component.text(title),
                    slots.size(),
                    0
            ));
        }

        changedTitle = false;

        // Next we set all items in the window.

        List<ItemStack> items = new ArrayList<>(slots.size());
        for (Slot slot : slots) {
            items.add(slot.getItem());
            slot.setChanged(false);
        }

        user.writePacket(new WrapperPlayServerWindowItems(
                windowID,
                0,
                items,
                null
        ));

        user.flushPackets();
    }

    public void update() {
        if (!open) return;

        if (changedTitle) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14)) {
                user.writePacket(new WrapperPlayServerOpenWindow(
                        windowID,
                        getModernID(),
                        Component.text(title),
                        slots.size(),
                        true,
                        0
                ));
            } else {
                user.writePacket(new WrapperPlayServerOpenWindow(
                        windowID,
                        getLegacyName(),
                        Component.text(title),
                        slots.size(),
                        0
                ));
            }

            changedTitle = false;
        }

        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            if (!slot.hasChanged()) continue;

            user.writePacket(new WrapperPlayServerSetSlot(
                    windowID,
                    0,
                    i,
                    slot.getItem()
            ));

            slot.setChanged(false);
        }

        user.flushPackets();
    }

    /*
     * Packet Listeners.
     */

    public void handlePacketReceive(PacketPlayReceiveEvent event) {
        if (!open) return;

        switch (event.getPacketType()) {
            case CLOSE_WINDOW:
                handleCloseWindow();
                break;
            case CLICK_WINDOW:
                WrapperPlayClientClickWindow clickWindow = new WrapperPlayClientClickWindow(event);
                if (clickWindow.getWindowId() != windowID) break;
                handleClickWindow(clickWindow);
                break;
        }
    }

    public void handlePacketSend(PacketPlaySendEvent event) {
        if (!open) return;

        switch (event.getPacketType()) {
            case CLOSE_WINDOW:
            case OPEN_HORSE_WINDOW:
                handleCloseWindow();
                break;
            case OPEN_WINDOW:
                WrapperPlayServerOpenWindow openWindow = new WrapperPlayServerOpenWindow(event);
                if (openWindow.getContainerId() != windowID) handleCloseWindow();
                break;
            case WINDOW_ITEMS:
                WrapperPlayServerWindowItems windowItems = new WrapperPlayServerWindowItems(event);
                if (windowItems.getWindowId() != windowID) break;
                for (int i = 0; i < Math.min(slots.size(), windowItems.getItems().size()); i++) {
                    handleModifySlot(i, windowItems.getItems().get(i));
                }
                break;
            case SET_SLOT:
                WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(event);
                if (setSlot.getWindowId() != windowID) break;
                handleModifySlot(setSlot.getSlot(), setSlot.getItem());
                break;
        }
    }

    protected void handleClickWindow(WrapperPlayClientClickWindow wrapper) {
        // If the clicked slot is in the player's own inventory then don't continue.
        if (wrapper.getSlot() >= slots.size()) return;
        slots.get(wrapper.getSlot()).handleClick(wrapper.getWindowClickType());
    }

    protected void handleCloseWindow() {
        open = false;
    }

    protected void handleModifySlot(int slot, ItemStack itemStack) {
        slots.get(slot).setItem(itemStack);
        slots.get(slot).setChanged(false);
    }
}
