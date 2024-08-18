package sharkbyte.container.core;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenHorseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import sharkbyte.container.core.container.Container;

import java.util.HashSet;
import java.util.Set;

/**
 * This class listens to packets via PacketEvents.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class ContainerPacketListener extends SimplePacketListenerAbstract {

    /*
     * Tracked Containers.
     */

    private final static Set<Container> CONTAINERS = new HashSet<>();

    /**
     * Add a Container to pass packets to.
     */
    public static void addContainer(Container container) {
        CONTAINERS.add(container);
    }

    /**
     * Remove a Container from receiving packets.
     */
    public static void removeContainer(Container container) {
        CONTAINERS.remove(container);
    }

    /*
     * Packet Listeners.
     */

    /**
     * Remove a player when they disconnect.
     */
    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        WindowIDTracker.removeUser(event.getUser());
    }

    /**
     * Pass the packet to containers whenever a packet is received.
     */
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        for (Container container : CONTAINERS) container.handlePacketReceive(event);
    }

    /**
     * Track windowIDs and pass the packet to containers whenever a packet is sent.
     */
    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        switch (event.getPacketType()) {
            case JOIN_GAME:
                WindowIDTracker.updateUser(event.getUser(), 1);
                break;
            case OPEN_WINDOW:
                WindowIDTracker.updateUser(event.getUser(), new WrapperPlayServerOpenWindow(event).getContainerId());
                break;
            case OPEN_HORSE_WINDOW:
                WindowIDTracker.updateUser(event.getUser(), new WrapperPlayServerOpenHorseWindow(event).getWindowId());
                break;
        }

        for (Container container : CONTAINERS) container.handlePacketSend(event);
    }
}
