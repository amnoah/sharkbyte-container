package sharkbyte.container.core.inventory.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.inventory.Slot;
import sharkbyte.container.core.inventory.Inventory;

public class Hopper extends Inventory {

    public Hopper(User user) {
        this(user, "");
    }

    public Hopper(User user, String title) {
        super(user, title);

        // A hopper has 5 slots.
        for (int i = 0; i < 5; i++) slots.add(new Slot(new ItemStack.Builder().type(ItemTypes.AIR).build()));
    }

    @Override
    public void destroy() {

    }

    @Override
    protected String getLegacyName() {
        return "minecraft:hopper";
    }

    @Override
    protected int getWindowType() {
        // The addition of the Crafter displaced the ID by 1.
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) return 16;
        return 15;
    }
}
