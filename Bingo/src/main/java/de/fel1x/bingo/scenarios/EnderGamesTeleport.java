package de.fel1x.bingo.scenarios;

import de.fel1x.bingo.Bingo;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EnderGamesTeleport implements IBingoScenario {

    private final Bingo bingo = Bingo.getInstance();

    @Override
    public void execute() {

        List<Player> players = this.bingo.getData().getPlayers();

        if (players.size() == 1) return;

        Collections.shuffle(players);
        HashMap<Location, String> locations = new HashMap<>();

        players.forEach(player -> locations.put(player.getLocation(), player.getDisplayName()));

        int counter = 0;

        for (Location location : locations.keySet()) {
            Player playerToTeleport = players.get(counter);
            playerToTeleport.teleportAsync(location);

            playerToTeleport.sendMessage(this.bingo.getPrefix() + "§7Du wurdest mit " + locations.get(location) + " §7getauscht!");
            playerToTeleport.playSound(playerToTeleport.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 5);
            playerToTeleport.playEffect(EntityEffect.TELEPORT_ENDER);
        }

    }

    @Override
    public String getName() {
        return "EnderGames Teleport";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.ENDER_PEARL;
    }
}
