package de.fel1x.capturetheflag.inventories;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.gameplayer.GamePlayer;
import de.fel1x.capturetheflag.team.Team;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
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
            .title("§e§lWähle dein Team")
            .manager(CaptureTheFlag.getInstance().getInventoryManager())
            .build();

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {

        List<String> loreRed = new ArrayList<>();
        List<String> loreBlue = new ArrayList<>();

        Team.RED.getTeamPlayers().forEach(current -> {

            loreRed.add("§7> §a" + current.getDisplayName());

        });
        Team.BLUE.getTeamPlayers().forEach(current -> {

            loreBlue.add("§7> §a" + current.getDisplayName());

        });

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));
        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.RED_BANNER, 1).setName("§cTeam Rot §8» §a" + Team.RED.getTeamPlayers().size() + "§8/§75")
                .setLore(loreRed).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            if (Team.RED.getTeamPlayers().size() > 4) {
                player.sendMessage(this.captureTheFlag.getPrefix() + "§cDieses Team ist voll!");
                player.closeInventory();
                return;
            }

            gamePlayer.removeTeam(Team.BLUE);
            gamePlayer.addTeam(Team.RED);

            player.closeInventory();


        }));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.BLUE_BANNER, 1).setName("§9Team Blau §8» §a" + Team.BLUE.getTeamPlayers().size() + "§8/§75")
                .setLore(loreBlue).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            if (Team.BLUE.getTeamPlayers().size() > 4) {
                player.sendMessage(this.captureTheFlag.getPrefix() + "§cDieses Team ist voll!");
                player.closeInventory();
                return;
            }

            gamePlayer.removeTeam(Team.RED);
            gamePlayer.addTeam(Team.BLUE);

            player.closeInventory();


        }));


    }

    @Override
    public void update(Player player, InventoryContents contents) {

        List<String> loreRed = new ArrayList<>();
        List<String> loreBlue = new ArrayList<>();

        Team.RED.getTeamPlayers().forEach(current -> {

            loreRed.add("§7> §a" + current.getDisplayName());

        });
        Team.BLUE.getTeamPlayers().forEach(current -> {

            loreBlue.add("§7> §a" + current.getDisplayName());

        });

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if (state % 5 != 0) return;

        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.RED_BANNER, 1).setName("§cTeam Rot §8» §a" + Team.RED.getTeamPlayers().size() + "§8/§75")
                .setLore(loreRed).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            gamePlayer.addTeam(Team.RED);
            gamePlayer.removeTeam(Team.BLUE);

            player.closeInventory();


        }));

        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.BLUE_BANNER, 1).setName("§9Team Blau §8» §a" + Team.BLUE.getTeamPlayers().size() + "§8/§75")
                .setLore(loreBlue).toItemStack(), event -> {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player clickedPlayer = (Player) event.getWhoClicked();
            GamePlayer gamePlayer = new GamePlayer(clickedPlayer);

            gamePlayer.addTeam(Team.BLUE);
            gamePlayer.removeTeam(Team.RED);

            player.closeInventory();


        }));


    }
}
