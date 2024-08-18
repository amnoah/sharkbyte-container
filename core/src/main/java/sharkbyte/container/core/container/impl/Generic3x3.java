package sharkbyte.container.core.container.impl;

import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.container.core.container.Container;

public class Generic3x3 extends Container {

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
