package sharkbyte.container.core;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.HashMap;
import java.util.Map;

/**
 * This class tracks the latest windowID a player has been sent.
 * We use the value before that to hopefully prevent conflicts.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class WindowIDTracker {

    private final static Map<User, Integer> WINDOW_ID_MAP = new HashMap<>();

    public static int getID(User user) {
        return WINDOW_ID_MAP.get(user);
    }

    public static void removeUser(User user) {
        WINDOW_ID_MAP.remove(user);
    }

    public static void updateUser(User user, int currentID) {
        WINDOW_ID_MAP.put(user, currentID == 1 ? 100 : currentID - 1);
    }
}
