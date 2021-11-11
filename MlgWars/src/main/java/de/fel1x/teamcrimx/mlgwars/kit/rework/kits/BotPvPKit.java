package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import de.fel1x.teamcrimx.mlgwars.utils.entites.ZombieEquipment;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BotPvPKit extends Kit {

    private final ZombieEquipment zombieEquipment = new ZombieEquipment();

    public BotPvPKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.EGG, 5)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Decoy", TextColor.fromHexString("#ffffff"))))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public boolean shouldConsiderCooldown() {
        return true;
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        this.player.launchProjectile(Egg.class).setMetadata(this.getClass().getName(),
                new FixedMetadataValue(this.mlgWars, this.player.getName()));

        this.removeItemByAmount(1);
        this.setCooldown(2000);
        this.player.setCooldown(Material.EGG, 40);
    }

    @EventHandler
    public void on(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof Player player) {
            if(!this.compareUniqueIDs(player.getUniqueId())) {
                return;
            }
        }

        if(event.getEntity().hasMetadata(this.getClass().getName()) && event.getEntity() instanceof Egg egg) {
            if(!egg.getMetadata(this.getClass().getName()).get(0).asString().equalsIgnoreCase(this.player.getName())) {
                return;
            }

            egg.getWorld().spawn(egg.getLocation(), Zombie.class, zombie -> {
                Bukkit.getMobGoals().addGoal(zombie, 1, new FindNextPlayerMobGoal(this.mlgWars, zombie));

                Disguise disguise = new PlayerDisguise(this.player.getName(), this.player.getName());
                disguise.setReplaceSounds(true);
                DisguiseAPI.disguiseEntity(zombie, disguise);

                zombie.setShouldBurnInDay(false);

                zombie.customName(this.player.displayName().color(NamedTextColor.WHITE));
                zombie.setCustomNameVisible(true);

                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, true, false));

                try {
                    zombie.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(20);
                    zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20 + this.random.nextInt(5));
                } catch (Exception ignored) {}

                zombie.setMetadata(this.player.getName(), new FixedMetadataValue(this.mlgWars, null));
                zombie.setMetadata("botOwner", new FixedMetadataValue(this.mlgWars, this.player.getUniqueId()));

                zombie.getEquipment().setItemInMainHand(this.zombieEquipment.getSwords()[this.random.nextInt(this.zombieEquipment.getSwords().length)]);

                zombie.getEquipment().setHelmet(this.zombieEquipment.getHelmets()[this.random.nextInt(this.zombieEquipment.getHelmets().length)]);
                zombie.getEquipment().setChestplate(this.zombieEquipment.getChestplates()[this.random.nextInt(this.zombieEquipment.getChestplates().length)]);
                zombie.getEquipment().setLeggings(this.zombieEquipment.getLeggins()[this.random.nextInt(this.zombieEquipment.getLeggins().length)]);
                zombie.getEquipment().setBoots(this.zombieEquipment.getShoes()[this.random.nextInt(this.zombieEquipment.getShoes().length)]);
            });
        }

    }

    @Override
    public void run() {

    }

    public class FindNextPlayerMobGoal implements Goal<Zombie> {
        private final GoalKey<Zombie> key;
        private final Mob mob;
        private LivingEntity target;

        public FindNextPlayerMobGoal(MlgWars mlgWars, Mob mob) {
            this.key = GoalKey.of(Zombie.class, new NamespacedKey(mlgWars, "find_enemies"));
            this.mob = mob;
        }

        @Override
        public boolean shouldActivate() {
            return true;
        }

        @Override
        public boolean shouldStayActive() {
            return true;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            this.mob.setTarget(null);
            this.mob.getPathfinder().stopPathfinding();
        }

        @Override
        public void tick() {
            if(this.target == null || this.target.isDead() || !this.target.isValid()
                    || (this.target instanceof Player player && BotPvPKit.this.mlgWars.getData()
                    .getGamePlayers().get(player.getUniqueId()).isSpectator())) {
                this.findTarget();
                return;
            }

            try {
                this.mob.setTarget(this.target);
                if (this.mob.getLocation().distanceSquared(this.target.getLocation()) < 6.25) {
                    this.mob.getPathfinder().stopPathfinding();
                } else {
                    this.mob.getPathfinder().moveTo(this.target, 1.0D);
                }
            } catch (Exception ignored) {}
        }

        @Override
        public @NotNull GoalKey<Zombie> getKey() {
            return this.key;
        }

        @Override
        public @NotNull EnumSet<GoalType> getTypes() {
            return EnumSet.of(GoalType.TARGET, GoalType.MOVE);
        }

        private void findTarget() {
            List<Entity> possibleMobs = this.mob.getWorld().getNearbyEntities(this.mob.getLocation(), 50, 20, 50,
                    entity -> entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.PLAYER)
                    .stream().filter(entity -> !entity.hasMetadata(BotPvPKit.this.player.getName())
                            && !entity.getName().equalsIgnoreCase(BotPvPKit.this.player.getName())) // Todo: Team Implementation
                    .collect(Collectors.toList());

            double closestDistance = -1.0;
            Entity closestPlayer = null;
            for (Entity player : possibleMobs) {
                double distance = player.getLocation().distanceSquared(this.mob.getLocation());
                if (closestDistance != -1.0 && !(distance < closestDistance)) {
                    continue;
                }
                closestDistance = distance;
                closestPlayer = player;
            }
            this.target = (LivingEntity) closestPlayer;
            //this.target = (LivingEntity) possibleMobs.get(new Random().nextInt(possibleMobs.size()));
        }
    }
}
