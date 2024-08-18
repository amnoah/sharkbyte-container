package sharkbyte.container.core.inventory.impl;

import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.inventory.Inventory;

public class Generic9x3 extends Inventory {

    public Generic9x3(User user) {
        this(user, "");
    }

    public Generic9x3(User user, String title) {
        super(user, title, 27);
    }

    @Override
    protected String getLegacyName() {
        return "minecraft:chest";
    }

    @Override
    protected int getModernID() {
        return 2;
    }
}
