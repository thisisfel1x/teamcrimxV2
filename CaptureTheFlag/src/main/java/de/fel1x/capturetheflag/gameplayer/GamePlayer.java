package de.fel1x.capturetheflag.gameplayer;

import de.fel1x.capturetheflag.CaptureTheFlag;
import de.fel1x.capturetheflag.Data;
import de.fel1x.capturetheflag.filehandler.SpawnHandler;
import de.fel1x.capturetheflag.kits.Kit;
import de.fel1x.capturetheflag.team.Teams;
import de.fel1x.capturetheflag.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.Random;

public class GamePlayer {

    Random random = new Random();

    private Data data = CaptureTheFlag.getInstance().getData();
    private Player player;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void addToInGamePlayers() {

        data.getPlayers().add(player);

    }

    public void removeFromInGamePlayers() {

        data.getPlayers().remove(player);

    }

    public void addToSpectators() {

        data.getSpectators().add(player);

    }

    public void removeFromSpectators() {

        data.getSpectators().remove(player);

    }

    public void setSpectator() {

        player.teleport(SpawnHandler.loadLocation("spectator"));
        player.setGameMode(GameMode.SPECTATOR);

        addToSpectators();

        data.getPlayers().forEach(gamePlayers -> {

            gamePlayers.hidePlayer(player);

        });

    }

    public void cleanupTeams() {

        Teams.RED.getTeamPlayers().remove(player);
        Teams.BLUE.getTeamPlayers().remove(player);

        //CaptureTheFlag.getInstance().getScoreboardHandler().handleQuit(player);

    }

    public void cleanupInventory() {

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFoodLevel(28);
        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.setLevel(0);
        player.setExp(0);
        player.setWalkSpeed(0.2f);
        player.getActivePotionEffects().forEach(effect -> {
            player.removePotionEffect(effect.getType());
        });

    }

    public void showToAll() {

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureTheFlag.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(current -> {
                current.showPlayer(player);
                player.showPlayer(current);
            });
        }, 20L);

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

        if(teams.getTeamPlayers().size() <= 4) {

            if(!teams.getTeamPlayers().contains(player)) {
                teams.getTeamPlayers().add(player);
            }
            player.sendMessage("§7Du bist Team " + teams.getTeamName() + " §7beigetreten!");

            CaptureTheFlag.getInstance().getScoreboardHandler().setGameScoreboard(player, teams);

        } else {

            player.sendMessage("Dieses Team ist voll!");
            player.closeInventory();

        }


    }

    public void removeTeam(Teams teams) {

        teams.getTeamPlayers().remove(player);

    }

    public Teams getTeam() {

        if(Teams.BLUE.getTeamPlayers().contains(player))
                return Teams.BLUE;

        if(Teams.RED.getTeamPlayers().contains(player))
            return Teams.RED;

        return null;

    }

    public boolean hasTeam() {

        return (Teams.BLUE.getTeamPlayers().contains(player) || Teams.RED.getTeamPlayers().contains(player));

    }

    public void teleportToTeamSpawn() {

        if(Teams.BLUE.getTeamPlayers().contains(player)) {

            Location blueSpawn = SpawnHandler.loadLocation("blueSpawn");
            player.teleport(blueSpawn);

        } else if(Teams.RED.getTeamPlayers().contains(player)) {

            Location redSpawn = SpawnHandler.loadLocation("redSpawn");
            player.teleport(redSpawn);

        } else {

            player.sendMessage("§cEs trat ein Fehler auf (NO_TEAM_SELECTED_TELEPORT_ERROR)");

        }


    }

    public Location getRespawnLocation() {

        if(Teams.BLUE.getTeamPlayers().contains(player)) {

            return SpawnHandler.loadLocation("blueSpawn");

        } else if(Teams.RED.getTeamPlayers().contains(player)) {

            return SpawnHandler.loadLocation("redSpawn");

        }

        return SpawnHandler.loadLocation("spectator");



    }

    public void checkForTeam() {

        if(hasTeam()) return;

        int number = random.nextInt(1);

        if(number == 0) {
            addTeam(Teams.RED);
        } else {
            addTeam(Teams.BLUE);
        }

    }

    public boolean isSpectator() {

        return this.data.getSpectators().contains(player);

    }

    public boolean isPlayer() {

        return this.data.getPlayers().contains(player);

    }

    public void selectKit(Kit kit) {

        CaptureTheFlag.getInstance().getKitHandler().getSelectedKit().put(player, kit);

        player.closeInventory();
        player.playSound(player.getLocation(), Sound.CAT_MEOW, 5, 8);
        player.sendMessage("§7Du hast das §l" + kit.getName() + "§7-Kit ausgewählt!");

    }

    public Kit getSelectedKit() {

        return CaptureTheFlag.getInstance().getKitHandler().getSelectedKit().get(player);

    }

    public void setKitItems() {

        Color dyeColor = null;

        if(getTeam().equals(Teams.RED)) {
            dyeColor = Color.RED;
        } else if(getTeam().equals(Teams.BLUE)) {
            dyeColor = Color.BLUE;
        }

        if(getSelectedKit() == null) {

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

                player.getInventory().addItem(sword);
                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggins);
                player.getInventory().setBoots(boots);

                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, true, false));

                break;

            case ARCHER:

                helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
                chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
                leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
                boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

                sword = new ItemBuilder(Material.WOOD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).toItemStack();
                ItemStack bow = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.ARROW_DAMAGE, 1).toItemStack();
                ItemStack arrow = new ItemStack(Material.ARROW, 1);

                player.getInventory().addItem(sword, bow);
                player.getInventory().setItem(8, arrow);

                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggins);
                player.getInventory().setBoots(boots);

                break;

            case PYRO:

                helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
                chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
                leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
                boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

                sword = new ItemBuilder(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.FIRE_ASPECT, 1).toItemStack();

                player.getInventory().addItem(sword);

                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggins);
                player.getInventory().setBoots(boots);

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

                player.getInventory().addItem(sword, healPotion, regPotion, goldenApple);

                player.getInventory().setHelmet(helmet);
                player.getInventory().setChestplate(chestplate);
                player.getInventory().setLeggings(leggins);
                player.getInventory().setBoots(boots);


                break;

        }

    }

    @Deprecated
    public void setInventory() {

        Color dyeColor = null;

        if(getTeam().equals(Teams.RED)) {
            dyeColor = Color.RED;
        } else if(getTeam().equals(Teams.BLUE)) {
            dyeColor = Color.BLUE;
        }

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack leggins = new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(dyeColor).toItemStack();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(dyeColor).toItemStack();

        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemStack wood = new ItemStack(Material.LOG, 32);
        ItemStack planks = new ItemStack(Material.WOOD, 64);
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 12);
        ItemStack arrow = new ItemStack(Material.ARROW, 16);

        player.getInventory().addItem(sword, bow, pickaxe, axe, goldenApple, arrow, wood, wood, planks, planks);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggins);
        player.getInventory().setBoots(boots);


    }

}
