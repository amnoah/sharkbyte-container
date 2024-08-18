package sharkbyte.container.core;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;

public class ContainerPacketListener extends SimplePacketListenerAbstract {

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        WindowIDTracker.removeUser(event.getUser());
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        switch (event.getPacketType()) {
            case JOIN_GAME:
                WindowIDTracker.updateUser(event.getUser(), 1);
                break;
        }
    }
}
