package de.fel1x.bingo.inventories.voting;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.objects.BingoDifficulty;
import de.fel1x.bingo.tasks.LobbyTask;
import de.fel1x.bingo.utils.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class VotingInventory implements InventoryProvider {

    public static final SmartInventory VOTING_INVENTORY = SmartInventory.builder()
            .id("votingInventory")
            .provider(new VotingInventory())
            .size(3, 9)
            .title(Bingo.getInstance().getPrefix() + "§eSchwierigkeitsvoting")
            .manager(Bingo.getInstance().getInventoryManager())
            .build();

    private final Bingo bingo = Bingo.getInstance();
    private final VotingManager votingManager = this.bingo.getVotingManager();

    @Override
    public void init(Player player, InventoryContents contents) {

        int column = 2;

        for (BingoDifficulty bingoDifficulty : BingoDifficulty.values()) {
            contents.set(1, column, ClickableItem.of(new ItemBuilder(bingoDifficulty.getMaterial())
                            .setName("§8● " + bingoDifficulty.getDisplayName())
                            .addGlow(this.votingManager.containsPlayer(player.getUniqueId(), bingoDifficulty))
                            .setLore("§7Votes §8● §a" + this.votingManager.getVotesByDifficulty(bingoDifficulty))
                            .toItemStack(),
                    inventoryClickEvent -> {
                        if(this.bingo.getBingoTask() instanceof LobbyTask) {
                            LobbyTask lobbyTask = (LobbyTask) this.bingo.getBingoTask();
                            if(lobbyTask.getCountdown() <= 15) {
                                player.closeInventory();
                                player.sendMessage(this.bingo.getPrefix() + "§cDu kannst nicht mehr abstimmen!");
                                return;
                            }
                        }
                        this.votingManager.addPlayerToDifficulty(player.getUniqueId(), bingoDifficulty);

                        player.closeInventory();
                        player.sendMessage(this.bingo.getPrefix() + " §7Du hast für die Schwierigkeit "
                                + bingoDifficulty.getDisplayName() + " §7abgestimmt!");
                    }));

            column += 2;
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
