package sharkbyte.container.example;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import sharkbyte.container.core.inventory.Inventory;
import sharkbyte.container.core.inventory.impl.Generic9x2;
import sharkbyte.container.core.inventory.impl.Hopper;

public class PacketListener extends SimplePacketListenerAbstract {

    private Inventory inventory;

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (!event.getPacketType().equals(PacketType.Play.Client.CHAT_MESSAGE)) return;

        if (inventory == null) {
            inventory = new Generic9x2(event.getUser(), "This is a title!");
        }

        inventory.create();
    }
}
