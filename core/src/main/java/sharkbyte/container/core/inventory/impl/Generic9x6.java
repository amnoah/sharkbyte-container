package sharkbyte.container.core.inventory.impl;

import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.inventory.Inventory;

public class Generic9x6 extends Inventory {

    public Generic9x6(User user) {
        this(user, "");
    }

    public Generic9x6(User user, String title) {
        super(user, title, 54);
    }

    @Override
    protected String getLegacyName() {
        return "minecraft:chest";
    }

    @Override
    protected int getModernID() {
        return 5;
    }
}
