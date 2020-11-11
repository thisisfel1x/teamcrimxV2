package de.fel1x.teamcrimx.mlgwars.inventories;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.objects.ScoreboardTeam;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamInventory implements InventoryProvider {

    public static final SmartInventory TEAM_INVENTORY = SmartInventory.builder()
            .id("teamInv")
            .provider(new TeamInventory())
            .size(1, 9)
            .title("§8● §e§lTeams")
            .manager(MlgWars.getInstance().getInventoryManager())
            .build();
    private final MlgWars mlgWars = MlgWars.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {
        for (int i = 0; i < this.mlgWars.getData().getGameTeams().size(); i++) {
            ScoreboardTeam scoreboardTeam = this.mlgWars.getData().getGameTeams().get(i);
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Spieler");

            if (scoreboardTeam.getTeamPlayers() != null) {
                for (Player teamPlayer : scoreboardTeam.getTeamPlayers()) {
                    lore.add(" §8- " + teamPlayer.getDisplayName());
                }
            }

            contents.set(0, i, ClickableItem.of(new ItemBuilder(Material.RED_BED)
                            .setName("§8● §7Team §a" + scoreboardTeam.getTeamId() + " §8(§a" + scoreboardTeam.getTeamPlayers().size()
                                    + "§8/§c" + scoreboardTeam.getMaxPlayers() + "§8)")
                            .setLore(lore)
                            .toItemStack(),
                    event -> {

                        if (scoreboardTeam.getTeamPlayers().size() >= scoreboardTeam.getMaxPlayers()) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cDieses Team ist bereits voll!");
                        } else {

                            GamePlayer gamePlayer = new GamePlayer(player);
                            gamePlayer.setTeam(scoreboardTeam);
                        }
                    }));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if (state % 5 != 0) {
            return;
        }

        for (int i = 0; i < this.mlgWars.getData().getGameTeams().size(); i++) {
            ScoreboardTeam scoreboardTeam = this.mlgWars.getData().getGameTeams().get(i);
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Spieler");

            for (Player teamPlayer : scoreboardTeam.getTeamPlayers()) {
                lore.add(" §8- " + teamPlayer.getDisplayName());
            }

            contents.set(0, i, ClickableItem.of(new ItemBuilder(Material.RED_BED)
                            .setName("§8● §7Team §a" + scoreboardTeam.getTeamId() + " §8(§a" + scoreboardTeam.getTeamPlayers().size()
                                    + "§8/§c" + scoreboardTeam.getMaxPlayers() + "§8)")
                            .setLore(lore)
                            .toItemStack(),
                    event -> {

                        if (scoreboardTeam.getTeamPlayers().size() >= scoreboardTeam.getMaxPlayers()) {
                            player.sendMessage(this.mlgWars.getPrefix() + "§cDieses Team ist bereits voll!");
                        } else {

                            GamePlayer gamePlayer = new GamePlayer(player);
                            gamePlayer.setTeam(scoreboardTeam);
                        }
                    }));
        }
    }
}
