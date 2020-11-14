package de.fel1x.bingo.scenarios;

import de.fel1x.bingo.Bingo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class EnderGamesTeleport implements IBingoScenario {

    Bingo bingo = Bingo.getInstance();
    Random random = new Random();

    @Override
    public void execute() {

        List<Player> players = this.bingo.getData().getPlayers();

        if (players.size() == 1) return;

        Player player1 = players.get(this.random.nextInt(players.size()));
        Player player2 = players.get(this.random.nextInt(players.size()));

        while (player2.equals(player1)) {

            player2 = players.get(this.random.nextInt(players.size()));

        }

        Location player1Location = player1.getLocation();
        Location player2Location = player2.getLocation();

        player1.teleport(player2Location);
        player2.teleport(player1Location);

        player1.playSound(player1.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 5);
        player2.playSound(player2.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 5);

        player1.sendMessage(this.bingo.getPrefix() + "§7Du wurdest mit " + player2.getDisplayName() + " §7getauscht!");
        player2.sendMessage(this.bingo.getPrefix() + "§7Du wurdest mit " + player1.getDisplayName() + " §7getauscht!");

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
