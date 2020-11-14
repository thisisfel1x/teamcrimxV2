package de.fel1x.bingo.inventories;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static de.fel1x.bingo.utils.Utils.getChatColor;

public class TeamSelectorInventory implements InventoryProvider {

    public static final SmartInventory TEAM_SELECTOR = SmartInventory.builder()
            .id("customInventory")
            .provider(new TeamSelectorInventory())
            .size(3, 9)
            .title(Bingo.getInstance().getPrefix() + "§7Wähle dein Team")
            .manager(Bingo.getInstance().getInventoryManager())
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if (state % 5 != 0) return;

        int i = 1;

        for (BingoTeam team : BingoTeam.values()) {

            String color = (team.getTeamSize() <= team.getTeamPlayers().size()) ? "§c" : "§a";

            List<String> loreLines = new ArrayList<>();
            loreLines.add("");
            loreLines.add("§7Spieler §8» " + color + team.getTeamPlayers().size() + "§8/§c" + team.getTeamSize());

            team.getTeamPlayers().forEach(teamPlayer -> {
                loreLines.add("  §8» §a" + teamPlayer.getDisplayName());
            });

            if (i == 4) i++;

            contents.set(1, i, ClickableItem.of(new ItemBuilder(Material.LEATHER_BOOTS)
                            .setLeatherArmorColor(team.getColor())
                            .setName(getChatColor(team.getColor()) + "Team " + team.getName())
                            .setLore(loreLines)
                            .toItemStack(),
                    inventoryClickEvent -> {

                        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) return;

                        if (team.getTeamPlayers().contains(player)) {
                            player.sendMessage(Bingo.getInstance().getPrefix() + "§7Du bist bereits in diesem Team!");
                            return;
                        }

                        if (team.getTeamSize() == team.getTeamPlayers().size()) {
                            player.sendMessage(Bingo.getInstance().getPrefix() + "§7Dieses Team ist bereits voll!");
                            return;
                        }

                        BingoPlayer bingoPlayer = new BingoPlayer(player);
                        bingoPlayer.setTeam(team);

                        Bingo.getInstance().getLobbyScoreboard().setLobbyScoreboard(player);

                        player.closeInventory();
                        player.sendMessage(Bingo.getInstance().getPrefix() + "§7Du bist nun in "
                                + getChatColor(team.getColor()) + "Team " + team.getName());

                    }));

            i++;

        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}
