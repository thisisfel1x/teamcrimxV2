package de.fel1x.teamcrimx.mlgwars.objects;

import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.Data;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.enums.Spawns;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GamePlayer {

    private final MlgWars mlgWars = MlgWars.getInstance();
    private final Data data = mlgWars.getData();

    private Player player;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public boolean isPlayer() {
        return this.data.getPlayers().contains(player);
    }

    public void addToPlayers() {
        this.data.getPlayers().add(player);
    }

    public void removeFromPlayers() {
        this.data.getPlayers().remove(player);
    }

    public boolean isSpectator() {
        return this.data.getSpectators().contains(player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(player);
    }

    public void cleanUpOnJoin() {

        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);

        this.player.setGameMode(GameMode.ADVENTURE);

        this.player.setHealth(20);
        this.player.setFoodLevel(25);

        this.player.getActivePotionEffects().forEach(potionEffect -> this.player.removePotionEffect(potionEffect.getType()));

    }

    public void cleanUpOnQuit() {

        this.removeFromPlayers();
        this.removeFromPlayers();

    }

    public void setJoinItems() {

        this.player.getInventory().setItem(0, new ItemBuilder(Material.STORAGE_MINECART).setName("§8● §aKitauswahl").toItemStack());
        this.player.getInventory().setItem(8, new ItemBuilder(Material.BARRIER).setName("§8● §cKein Kit ausgewählt").toItemStack());

        if(this.player.hasPermission("mlgwars.forcemap") || this.player.isOp()) {
            this.player.getInventory().setItem(1, new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§8● §cForcemap").toItemStack());
        }

    }

    public void teleport(Spawns spawns) {
        try {
            this.player.teleport(spawns.getLocation());
        } catch (NullPointerException exception) {
            this.player.sendMessage(this.mlgWars.getPrefix() + "§cEin Fehler ist aufgetreten!");
            exception.printStackTrace();
        }
    }


}
