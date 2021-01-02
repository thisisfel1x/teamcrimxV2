package de.fel1x.bingo.listener.player;

import de.fel1x.bingo.Bingo;
import de.fel1x.bingo.gamehandler.Gamestate;
import de.fel1x.bingo.objects.BingoPlayer;
import de.fel1x.bingo.objects.BingoTeam;
import de.fel1x.bingo.tasks.LobbyTask;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Bingo bingo;

    public JoinListener(Bingo bingo) {
        this.bingo = bingo;
        bingo.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        BingoPlayer bingoPlayer = new BingoPlayer(player);
        CrimxPlayer crimxPlayer = new CrimxPlayer(bingoPlayer.getICloudPlayer());
        Gamestate gamestate = this.bingo.getGamestateHandler().getGamestate();

        if (!crimxPlayer.checkIfPlayerExistsInCollection(player.getUniqueId(), MongoDBCollection.BINGO)) {
            bingoPlayer.createPlayerData();
        }

        bingoPlayer.cleanupOnJoin();
        bingoPlayer.fetchPlayerData();
        event.setJoinMessage(null);

        int neededPlayers = (BingoTeam.RED.getTeamSize() * 2) - this.bingo.getData().getPlayers().size();

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(18);
        this.bingo.getData().getPlayerGG().put(player.getUniqueId(), false);

        bingoPlayer.setScoreboardOnJoin();

        switch (gamestate) {

            case IDLE:
            case LOBBY:

                bingoPlayer.addToPlayers();
                event.setJoinMessage(this.bingo.getPrefix() + "§a" + player.getDisplayName() + " §7hat das Spiel betreten");

                player.getInventory().addItem(new ItemBuilder(Material.CHEST_MINECART)
                        .setName("§8● §aWähle dein Team").toItemStack(),
                        new ItemBuilder(Material.PAPER).setName("§8● §eSchwierigkeitsvoting").toItemStack());

                player.teleport(this.bingo.getSpawnLocation());

                this.bingo.getData().getPlayers().forEach(players -> this.bingo.getLobbyScoreboard().updateBoard(players,
                        String.format("§8● §a%s§8/§c%s",
                                this.bingo.getData().getPlayers().size(), BingoTeam.RED.getTeamSize() * 6),
                        "players", "§a"));

                break;

            case INGAME:
            case PREGAME:
                bingoPlayer.addToSpectators();
                bingoPlayer.activateSpectatorMode();

                break;

            case ENDING:
                player.teleportAsync(this.bingo.getSpawnLocation());
                break;

        }

        if (this.bingo.getData().getPlayers().size() >= neededPlayers
                && gamestate.equals(Gamestate.IDLE)) {

            this.bingo.startTimerByClass(LobbyTask.class);

        }

    }
}
