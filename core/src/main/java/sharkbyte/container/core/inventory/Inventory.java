package sharkbyte.container.core.inventory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import net.kyori.adventure.text.Component;
import sharkbyte.container.core.WindowIDTracker;

import java.util.ArrayList;
import java.util.List;

public abstract class Inventory {

    protected final User user;
    protected final List<Slot> slots;
    protected final int windowID;

    protected boolean changedTitle;
    protected String title;


    public Inventory(User user, String title, int slotCount) {
        this.user = user;
        this.title = title;
        windowID = WindowIDTracker.getID(user);

        this.slots = new ArrayList<>(slotCount);
        for (int i = 0; i < slotCount; i++) slots.add(new Slot(new ItemStack.Builder().type(ItemTypes.AIR).build()));
    }

    /**
     * Calling this method will open the window on the client.
     */
    public void create() {
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

    /**
     * Calling this method will close the window on the client.
     */
    public void destroy() {
        user.sendPacket(new WrapperPlayServerCloseWindow(
                windowID
        ));
    }

    public void update() {


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
    }

    // https://wiki.vg/Inventory

    protected abstract String getLegacyName();

    protected abstract int getModernID();
}
