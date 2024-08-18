package sharkbyte.container.core.container.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.container.Container;

public class CraftingTable extends Container {

    public CraftingTable(User user) {
        this(user, "");
    }

    public CraftingTable(User user, String title) {
        super(user, title, 10);
    }

    @Override
    protected String getLegacyName() {
        return "minecraft:crafting_table";
    }

    @Override
    protected int getModernID() {
        // The addition of the Crafter displaced the ID by 1.
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) return 12;
        return 11;
    }
}
