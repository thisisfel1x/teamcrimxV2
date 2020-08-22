package de.fel1x.capturetheflag.inventories;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.capturetheflag.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamSelector implements InventoryProvider {

    public static final SmartInventory TEAM_SELECTOR = SmartInventory.builder()
            .id("customInventory")
            .provider(new TeamSelector())
            .size(3, 9)
            .title("§a§lWähle dein Team")
            .manager(CaptureTheFlag.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        List<String> loreRed = new ArrayList<>();
        List<String> loreBlue = new ArrayList<>();

        Teams.RED.getTeamPlayers().forEach(current -> {

            loreRed.add("§7> §a" + current.getDisplayName());

        });
        Teams.BLUE.getTeamPlayers().forEach(current -> {

            loreBlue.add("§7> §a" + current.getDisplayName());

        });

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName(" ").toItemStack()));
        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.BANNER, 1, (byte) 1).setName("§cTeam Rot §8» §a" + Teams.RED.getTeamPlayers().size() + "§8/§75")
                .setLore(loreRed).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            if (Teams.RED.getTeamPlayers().size() > 4) {
                player.sendMessage("§cDieses Team ist voll!");
                player.closeInventory();
                return;
            }

            gamePlayer.removeTeam(Teams.BLUE);
            gamePlayer.addTeam(Teams.RED);

            player.closeInventory();


        }));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.BANNER, 1, (byte) 4).setName("§9Team Blau §8» §a" + Teams.BLUE.getTeamPlayers().size() + "§8/§75")
                .setLore(loreBlue).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            if (Teams.BLUE.getTeamPlayers().size() > 4) {
                player.sendMessage("§cDieses Team ist voll!");
                player.closeInventory();
                return;
            }

            gamePlayer.removeTeam(Teams.RED);
            gamePlayer.addTeam(Teams.BLUE);

            player.closeInventory();


        }));


    }

    @Override
    public void update(Player player, InventoryContents contents) {

        List<String> loreRed = new ArrayList<>();
        List<String> loreBlue = new ArrayList<>();

        Teams.RED.getTeamPlayers().forEach(current -> {

            loreRed.add("§7> §a" + current.getDisplayName());

        });
        Teams.BLUE.getTeamPlayers().forEach(current -> {

            loreBlue.add("§7> §a" + current.getDisplayName());

        });

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if (state % 5 != 0) return;

        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.BANNER, 1, (byte) 1).setName("§cTeam Rot §8» §a" + Teams.RED.getTeamPlayers().size() + "§8/§75")
                .setLore(loreRed).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            gamePlayer.addTeam(Teams.RED);
            gamePlayer.removeTeam(Teams.BLUE);

            player.closeInventory();


        }));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.BANNER, 1, (byte) 4).setName("§9Team Blau §8» §a" + Teams.BLUE.getTeamPlayers().size() + "§8/§75")
                .setLore(loreBlue).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            gamePlayer.addTeam(Teams.BLUE);
            gamePlayer.removeTeam(Teams.RED);

            player.closeInventory();


        }));


    }
}
