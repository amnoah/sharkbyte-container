package sharkbyte.container.example;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import sharkbyte.container.core.container.Container;
import sharkbyte.container.core.container.impl.Generic9x2;

public class PacketListener extends SimplePacketListenerAbstract {

    private Container container;

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (!event.getPacketType().equals(PacketType.Play.Client.CHAT_MESSAGE)) return;

        if (container == null) {
            container = new Generic9x2(event.getUser(), "This is a title!");
        }

        container.create();
        container.open();
    }
}
