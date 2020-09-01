package de.fel1x.capturetheflag.kits;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KitHandler {

    private Map<Player, Kit> selectedKit;

    public KitHandler() {

        selectedKit = new HashMap<>();

    }

    public Map<Player, Kit> getSelectedKit() {
        return selectedKit;
    }
}
