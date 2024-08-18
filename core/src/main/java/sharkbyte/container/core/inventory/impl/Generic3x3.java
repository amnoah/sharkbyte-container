package sharkbyte.container.core.inventory.impl;

import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.inventory.Inventory;

public class Generic3x3 extends Inventory {

    public Generic3x3(User user) {
        this(user, "");
    }

    public Generic3x3(User user, String title) {
        super(user, title, 9);
    }

    @Override
    protected String getLegacyName() {
        return "minecraft:dispenser";
    }

    @Override
    protected int getModernID() {
        return 6;
    }
}
