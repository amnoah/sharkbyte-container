package sharkbyte.container.example;

import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;
import sharkbyte.container.core.ContainerPacketListener;

public class ContainerExample extends JavaPlugin {

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(new ContainerPacketListener());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListener());
    }
}
