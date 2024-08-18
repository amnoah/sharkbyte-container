package sharkbyte.container.core.container.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.container.Container;

public class Anvil extends Container {

    public Anvil(User user) {
        this(user, "");
    }

    public Anvil(User user, String title) {
        super(user, title, 3);
    }

    @Override
    protected String getLegacyName() {
        return "minecraft:anvil";
    }

    @Override
    protected int getModernID() {
        // The addition of the Crafter displaced the ID by 1.
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) return 8;
        return 7;
    }
}
