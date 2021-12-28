package de.fel1x.teamcrimx.mlgwars.listener.player;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.gamestate.Gamestate;
import de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types.Tournament;
import de.fel1x.teamcrimx.mlgwars.objects.GamePlayer;
import de.fel1x.teamcrimx.mlgwars.utils.WinDetection;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class DeathListener implements Listener {

    private final MlgWars mlgWars;

    public DeathListener(MlgWars mlgWars) {
        this.mlgWars = mlgWars;
        this.mlgWars.getPluginManager().registerEvents(this, this.mlgWars);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerDeathEvent event) {
        boolean tournament = this.mlgWars.getGameType() instanceof Tournament;

        Player player = event.getEntity();
        Player killer;

        GamePlayer gamePlayer = this.mlgWars.getData().getGamePlayers().get(player.getUniqueId());
        Gamestate gamestate = gamePlayer.getCurrentGamestate();

        gamePlayer.onDeath();

        if (gamestate == Gamestate.PREGAME || gamestate == Gamestate.INGAME) {

            if (player.getKiller() != null) {
                killer = player.getKiller();
            } else if (this.mlgWars.getData().getLastHit().get(player) != null) {
                killer = this.mlgWars.getData().getLastHit().get(player);
            } else if (player.hasMetadata("lastZombieHit")) {
                UUID ownerUUID = (UUID) player.getMetadata("lastZombieHit").get(0).value();
                killer = ownerUUID != null ? Bukkit.getPlayer(ownerUUID) : null;
            } else {
                killer = null;
            }

            if (killer != null && killer.getName().equalsIgnoreCase(player.getName())) {
                killer = null;
            }

            if (killer != null) {
                GamePlayer killerGamePlayer = this.mlgWars.getData().getGamePlayers().get(killer.getUniqueId());
                killerGamePlayer.getCrimxCoins().addCoinsAsync(100);

                /* if (killerGamePlayer.getSelectedKit() == Kit.KANGAROO) { TODO
                    int currentEssences = 0;
                    if (killer.hasMetadata("essence")) {
                        currentEssences = killer.getMetadata("essence").get(0).asInt();
                    }
                    killer.setMetadata("essence", new FixedMetadataValue(this.mlgWars, currentEssences + 2));
                } */


                int gameKills;

                if (killer.hasMetadata("kills")) {
                    gameKills = killer.getMetadata("kills").get(0).asInt() + 1;
                } else {
                    gameKills = 1;
                }

                killer.setMetadata("kills", new FixedMetadataValue(this.mlgWars, gameKills));
                killerGamePlayer.getScoreboardHandler().updateBoard(killer, "§b" + gameKills, "kills");

                killerGamePlayer.getStats().increaseKillsByOne();

                killer.sendMessage(this.mlgWars.getPrefix() + "§7Du hast " + player.getDisplayName() + " §7getötet §a(+100 Coins)");
                event.setDeathMessage(String.format("%s %s §7wurde von %s §7getötet", this.mlgWars.getPrefix(),
                        player.getDisplayName(), killer.getDisplayName()));

                if(tournament) {
                    killerGamePlayer.getStats().addPoints(1);
                    killer.playSound(killer.getLocation(),
                            Sound.ENTITY_PLAYER_LEVELUP, 1f, 2.5f);
                    killer.sendMessage(this.mlgWars.getPrefix() + "§aEliminierung, +1 §7Punkt");
                }

            } else {
                event.setDeathMessage(this.mlgWars.getPrefix() + player.getDisplayName() + " §7ist gestorben");
            }

            if (DisguiseAPI.isDisguised(player)) {
                DisguiseAPI.undisguiseToAll(player);
            }

            gamePlayer.updateOnlineTime();

            int playersLeft = this.mlgWars.getData().getPlayers().size();

            String playersLeftMessage = null;

            int team = gamePlayer.getPlayerMlgWarsTeamId();
            this.mlgWars.getData().getGameTeams().get(team).getAlivePlayers().remove(player);

            if (this.mlgWars.getData().getGameTeams().get(team).getAlivePlayers().isEmpty()) {
                this.mlgWars.getData().getGameTeams().remove(team);
            }

            if (this.mlgWars.getTeamSize() > 1) {
                //if (player.hasMetadata("team")) {
                    playersLeftMessage = "§a" + playersLeft + " Spieler verbleiben §8(§a"
                            + this.mlgWars.getData().getGameTeams().size() + " Teams§8)";
                //}
            } else if (this.mlgWars.getTeamSize() == 1) {
                playersLeftMessage = "§a" + playersLeft + " Spieler verbleiben";
            }

            if (playersLeftMessage != null && playersLeft > 1) {
                Bukkit.broadcastMessage(this.mlgWars.getPrefix() + playersLeftMessage);
            }

            if (player.isGlowing()) {
                player.setGlowing(false);
            }

            Bukkit.getOnlinePlayers().forEach(inGamePlayer -> {
                GamePlayer loopGamePlayer = this.mlgWars.getData().getGamePlayers().get(inGamePlayer.getUniqueId());
                loopGamePlayer.getScoreboardHandler()
                        .updateBoard(inGamePlayer, "§c" + MlgWars.getInstance().getData().getPlayers().size(), "players");

            });

            if(tournament) {
                int points = -1;
                if(this.mlgWars.getData().getPlayers().size() == 5) {
                    points = 5;
                } else if(this.mlgWars.getData().getPlayers().size() == 3) {
                    points = 3;
                }
                if(points != -1) {
                    for (GamePlayer value : this.mlgWars.getData().getGamePlayers().values()) {
                        if(!value.isPlayer()) {
                            continue;
                        }
                        value.getStats().addPoints(points);
                        value.getPlayer().playSound(value.getPlayer().getLocation(),
                                Sound.ENTITY_PLAYER_LEVELUP, 1f, 2.5f);
                        value.getPlayer().sendMessage(this.mlgWars.getPrefix() + "§a+" + points + "§7Placement Punkte");
                    }
                }
            }

            new WinDetection(this.mlgWars);
        }

    }

}
