package de.fel1x.capturetheflag.gameplayer;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.kits.Kit;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class GamePlayer {

    private final CaptureTheFlag captureTheFlag = CaptureTheFlag.getInstance();
    private final Data data = captureTheFlag.getData();

    private final Player player;

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void addToInGamePlayers() {
        this.data.getPlayers().add(player);
    }

    public void removeFromInGamePlayers() {
        this.data.getPlayers().remove(player);
    }

    public void addToSpectators() {
        this.data.getSpectators().add(player);
    }

    public void removeFromSpectators() {
        this.data.getSpectators().remove(player);
    }

    public void setSpectator() {
        this.player.teleport(SpawnHandler.loadLocation("spectator"));
        this.player.setGameMode(GameMode.SPECTATOR);

        this.addToSpectators();

        this.data.getPlayers().forEach(gamePlayers -> gamePlayers.hidePlayer(this.captureTheFlag, this.player));
    }

    public void cleanupTeams() {
        Teams.RED.getTeamPlayers().remove(this.player);
        Teams.BLUE.getTeamPlayers().remove(this.player);
    }

    public void cleanupInventory() {
        this.player.getInventory().clear();
        this.player.getInventory().setArmorContents(null);
        this.player.setFoodLevel(28);
        this.player.setHealth(20);
        this.player.setGameMode(GameMode.SURVIVAL);
        this.player.setLevel(0);
        this.player.setExp(0);
        this.player.setWalkSpeed(0.2f);
        this.player.getActivePotionEffects().forEach(effect -> this.player.removePotionEffect(effect.getType()));
    }

    public void showToAll() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.captureTheFlag,
                () -> Bukkit.getOnlinePlayers().forEach(current -> {
                    current.showPlayer(this.captureTheFlag, this.player);
                    this.player.showPlayer(this.captureTheFlag, current);
                }), 20L);
    }

    public void teleportToLobby() {
        try {
            Location location = SpawnHandler.loadLocation("lobby");
            player.teleport(location);
        } catch (Exception ignored) {
            player.sendMessage("§cEs trat ein Fehler auf (TELEPORT_SPAWN)");
        }
    }

    public void addTeam(Teams teams) {
        if (teams.getTeamPlayers().size() <= 4) {
            if (!teams.getTeamPlayers().contains(this.player)) {
                teams.getTeamPlayers().add(this.player);
            }
            this.player.sendMessage("§7Du bist Team " + teams.getTeamName() + " §7beigetreten!");

            this.captureTheFlag.getScoreboardHandler().setGameScoreboard(player, teams);

        } else {
            this.player.sendMessage("Dieses Team ist voll!");
            this.player.closeInventory();
        }
    }

    public void removeTeam(Teams teams) {
        teams.getTeamPlayers().remove(this.player);
    }

    public Teams getTeam() {
        if (Teams.BLUE.getTeamPlayers().contains(this.player)) {
            return Teams.BLUE;
        } else if (Teams.RED.getTeamPlayers().contains(this.player)) {
            return Teams.RED;
        } else {
            return Teams.NONE;
        }
    }

    public boolean hasTeam() {
        return (Teams.BLUE.getTeamPlayers().contains(this.player) || Teams.RED.getTeamPlayers().contains(this.player));
    }

    public void teleportToTeamSpawn() {
        if (Teams.BLUE.getTeamPlayers().contains(player)) {
            Location blueSpawn = SpawnHandler.loadLocation("blueSpawn");
            this.player.teleport(blueSpawn);
        } else if (Teams.RED.getTeamPlayers().contains(player)) {
            Location redSpawn = SpawnHandler.loadLocation("redSpawn");
            this.player.teleport(redSpawn);
        } else {
            this.player.sendMessage("§cEs trat ein Fehler auf (NO_TEAM_SELECTED_TELEPORT_ERROR)");
        }
    }

    public Location getRespawnLocation() {
        if (Teams.BLUE.getTeamPlayers().contains(player)) {
            return SpawnHandler.loadLocation("blueSpawn");
        } else if (Teams.RED.getTeamPlayers().contains(player)) {
            return SpawnHandler.loadLocation("redSpawn");
        }
        return SpawnHandler.loadLocation("spectator");
    }

    public void checkForTeam() {
        if (this.hasTeam()) return;

        boolean random = ThreadLocalRandom.current().nextBoolean();

        if (random) {
            this.addTeam(Teams.RED);
        } else {
            this.addTeam(Teams.BLUE);
        }
    }

    public boolean isSpectator() {

        return this.data.getSpectators().contains(player);

    }

    public boolean isPlayer() {

        return this.data.getPlayers().contains(player);

    }

    public void selectKit(Kit kit) {
        this.captureTheFlag.getKitHandler().getSelectedKit().put(player, kit);

        this.player.closeInventory();
        this.player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURREOW, 5, 8);
        this.player.sendMessage("§7Du hast das §l" + kit.getName() + "§7-Kit ausgewählt!");

    }

    public Kit getSelectedKit() {
        return this.captureTheFlag.getKitHandler().getSelectedKit().get(player);
    }

    public void setKitItems() {
        Color dyeColor = null;

        if (getTeam().equals(Teams.RED)) {
            dyeColor = Color.RED;
        } else if (getTeam().equals(Teams.BLUE)) {
            dyeColor = Color.BLUE;
        }

        if (getSelectedKit() == null) {

            ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
            ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
            ItemStack leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
            ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

            ItemStack sword = new ItemStack(Material.STONE_SWORD);
            ItemStack bow = new ItemStack(Material.BOW);
            ItemStack arrow = new ItemStack(Material.ARROW, 16);

            player.getInventory().addItem(sword, bow, arrow);
            player.getInventory().setHelmet(helmet);
            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggins);
            player.getInventory().setBoots(boots);

            return;

        }

        switch (getSelectedKit()) {

            case TANK:

                ItemStack helmet = new ItemBuilder(Material.IRON_HELMET).toItemStack();
                ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE).toItemStack();
                ItemStack leggins = new ItemBuilder(Material.IRON_LEGGINGS).toItemStack();
                ItemStack boots = new ItemBuilder(Material.IRON_BOOTS).toItemStack();

                ItemStack sword = new ItemStack(Material.STONE_SWORD);

                this.player.getInventory().addItem(sword);
                this.player.getInventory().setHelmet(helmet);
                this.player.getInventory().setChestplate(chestplate);
                this.player.getInventory().setLeggings(leggins);
                this.player.getInventory().setBoots(boots);

                this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false));

                break;

            case ARCHER:

                helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
                chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
                leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
                boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

                sword = new ItemBuilder(Material.WOODEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();
                ItemStack bow = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.ARROW_DAMAGE, 1).toItemStack();
                ItemStack arrow = new ItemStack(Material.ARROW, 1);

                this.player.getInventory().addItem(sword, bow);
                this.player.getInventory().setItem(8, arrow);

                this.player.getInventory().setHelmet(helmet);
                this.player.getInventory().setChestplate(chestplate);
                this.player.getInventory().setLeggings(leggins);
                this.player.getInventory().setBoots(boots);

                break;

            case PYRO:

                helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
                chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
                leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
                boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

                sword = new ItemBuilder(Material.GOLDEN_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.FIRE_ASPECT, 1).toItemStack();

                this.player.getInventory().addItem(sword);

                this.player.getInventory().setHelmet(helmet);
                this.player.getInventory().setChestplate(chestplate);
                this.player.getInventory().setLeggings(leggins);
                this.player.getInventory().setBoots(boots);

                break;

            case MEDIC:

                helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
                chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
                leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
                boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

                sword = new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();

                ItemStack regPotion = new ItemBuilder(Material.POTION, 2).setColor(16417).toItemStack();
                ItemStack healPotion = new ItemBuilder(Material.POTION, 4).setColor(16453).toItemStack();
                ItemStack goldenApple = new ItemBuilder(Material.GOLDEN_APPLE).toItemStack();

                this.player.getInventory().addItem(sword, healPotion, regPotion, goldenApple);

                this.player.getInventory().setHelmet(helmet);
                this.player.getInventory().setChestplate(chestplate);
                this.player.getInventory().setLeggings(leggins);
                this.player.getInventory().setBoots(boots);

                break;
        }

    }
}
