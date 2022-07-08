package de.fel1x.teamcrimx.mlgwars.kit.rework.kits;

import de.fel1x.teamcrimx.crimxapi.utils.Actionbar;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxapi.utils.ProgressBar;
import de.fel1x.teamcrimx.mlgwars.MlgWars;
import de.fel1x.teamcrimx.mlgwars.kit.rework.Kit;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FarmerKit extends Kit {

    private int timer = 60;
    private boolean hadGlow = false;

    private final DisguiseType[] disguiseType = {
            DisguiseType.CHICKEN,
            DisguiseType.COW,
            DisguiseType.SHEEP,
            DisguiseType.PIG,
    };

    public FarmerKit(Player player, MlgWars mlgWars) {
        super(player, mlgWars);
    }

    @Override
    public void initializeKit() {
        super.initializeKit();
        this.player.getInventory().setItem(0, this.getInteractionItemStack());
    }

    @Override
    public @Nullable ItemStack getInteractionItemStack() {
        return new ItemBuilder(Material.GOLD_NUGGET)
                .setName(Component.text("● ", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Verwandler", NamedTextColor.BLUE)))
                .setPDC(this.mlgWars, "KIT", this.getClass().getName())
                .toItemStack();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event) {
        this.removeItemByAmount(1);
        this.hadGlow = this.player.isGlowing();

        for (int i = 0; i < 3; i++) {
            this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.SHEEP);
            this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.PIG);
            this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.CHICKEN);
            this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.COW);

        }

        DisguiseType toDisguise = this.disguiseType[this.random.nextInt(this.disguiseType.length)];
        this.player.sendMessage(this.mlgWars.prefix()
                .append(this.mlgWars.miniMessage().deserialize("<gray>Du bist nun als <green>"))
                        .append(Component.translatable(toDisguise.getEntityType().translationKey()))
                .append(Component.text(" für 60 Sekunden versteckt", NamedTextColor.GRAY)));

        MobDisguise disguise = new MobDisguise(toDisguise);
        disguise.setVelocitySent(false);
        disguise.setViewSelfDisguise(false);
        disguise.setReplaceSounds(true);
        DisguiseAPI.disguiseEntity(this.player, disguise);

        this.runTaskTimer(this.mlgWars, 0L, 20L);
        this.gamePlayer.setActionbarOverridden(true);
    }

    @Override
    public void run() {
        this.gamePlayer.getMlgActionbar().sendActionbar(this.player, "§6Farmer (versteckt) §8● "
                + ProgressBar.getProgressBar(this.timer, 60, 15,
                '█', ChatColor.GREEN, ChatColor.DARK_GRAY));

        if(this.timer <= 0) {
            if (DisguiseAPI.isDisguised(this.player)) {
                DisguiseAPI.undisguiseToAll(this.player);
            }
            this.gamePlayer.getMlgActionbar().sendActionbar(this.player, "§6Farmer §8● §7Du bist nun wieder §asichtbar");
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.5f, 0.5f);

            Actionbar.sendOnlySubtitle(this.player, "§aWieder sichtbar!", 5, 20, 5);

            this.player.setGlowing(this.hadGlow);

            this.cancel();
            this.gamePlayer.setActionbarOverridden(false);
            return;
        }

        this.timer--;
    }
}
