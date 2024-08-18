package sharkbyte.container.core.slot;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

public interface ClickEvent {

    void handleClick(WrapperPlayClientClickWindow.WindowClickType type);
}
