package de.fel1x.teamcrimx.mlgwars.kit.rework;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryKitManager {

    private final Map<KitRegistry, InventoryKit> availableKits;

    public InventoryKitManager() {
        this.availableKits = new HashMap<>();

        this.registerKits(KitRegistry.STARTER, new InventoryKit("Starter", 0, Material.IRON_PICKAXE,
                "", "§7Jeder fängt mal klein an...", "§7Aber hiermit will keiner anfangen!"));
        this.registerKits(KitRegistry.GRAPPLER, new InventoryKit("Grappler", 3000, Material.FISHING_ROD,
                "", "§7Ziehe dich von einer Insel zur anderen!", "§cAchtung: kann nur 8x verwendet werden!", ""));
        this.registerKits(KitRegistry.JETPACK, new InventoryKit("Jetpack", 3000, Material.BREWING_STAND,
                "", "§7Fliege durch die Lüfte", ""));
        this.registerKits(KitRegistry.EXPLODER, new InventoryKit("Sprengmeister", 5000, Material.LEVER,
                "", "§7Sprenge platziertes TNT", "§7mit deinem Fernzünder in die Luft!", ""));
        this.registerKits(KitRegistry.ASTRONAUT, new InventoryKit("Astronaut", 5000, Material.FIREWORK_ROCKET,
                "", "§7Einmal ins All - der Traum...", "§7...der mit diesem Kit erreicht werden kann!", ""));
        this.registerKits(KitRegistry.BOT_PVP, new InventoryKit("BotPvP", 6000, Material.IRON_SWORD,
                "", "§7Lasse deine §aNPCs", "§7für dich §ckämpfen"));
        this.registerKits(KitRegistry.TRAPPER, new InventoryKit("Fallenleger", 5000, Material.PAPER,
                "", "§7Lege fallen wie ein echter", "§7Fortnut Battle Royale Spieler!", ""));
        this.registerKits(KitRegistry.CHICKEN_BRIDGE, new InventoryKit("Brückenbauer", 5000, Material.SCAFFOLDING,
                "", "§7Bob der Baumeister", "§7Jo, wir schaffen das!", ""));
        this.registerKits(KitRegistry.PULLER, new InventoryKit("Grabber", 4000, Material.CARROT_ON_A_STICK,
                "", "§7Ziehe Gegner an dich heran", "§7Auf #nohomo-Basis natürlich :P", ""));
        this.registerKits(KitRegistry.FARMER, new InventoryKit("Farmer", 4500, Material.HAY_BLOCK,
                "",  "§7Willkommen auf deiner Farm!", "§7Züchte Tiere und werde selbst zum Tier!", "",
                "§cAchtung: Du hast weniger Leben bei einer Verwandlung", ""));
        this.registerKits(KitRegistry.THROWER, new InventoryKit("Werfer", 5000, Material.STICK,
                "", "§7Achtung, Beschuss!", "§7Werfe mit allem um dich was nur geht", ""));
        this.registerKits(KitRegistry.TORNADO, new InventoryKit("Tornado", 8000, Material.DEAD_BUSH,
                "", "§7Mit diesem Kit eskalierst du komplett!", "§7Lass deinen Frust raus!", "", "§6[für feyju]", ""));
        this.registerKits(KitRegistry.STINKER, new InventoryKit("Stinker", 6500, Material.POISONOUS_POTATO,
                "", "§7Scheisse, der Deo hat versagt!", "§7Betäube deine Mitspieler mit deinem Geruch",
                "§c(andere Stinker sind immun!)", "", "§6[für Boerek_Obama]", ""));
        this.registerKits(KitRegistry.TANK, new InventoryKit("Tank", 8000, Material.NETHERITE_CHESTPLATE,
                "", "§7Stärke, weniger Knockback & Langsamkeit", "§7Das sind deine Fähigkeiten", "", "§6[für Xx_LP_KottPvP_xX]", ""));
        this.registerKits(KitRegistry.BOAT_GLIDER, new InventoryKit("Boatglider", 4000, Material.OAK_BOAT,
                "", "§7Katapultiere dich mit", "§7 einem Stoß in die Luft", ""));
        this.registerKits(KitRegistry.DUMP, new InventoryKit("Pure Dummheit", 3000, Material.BREAD,
                "", "§7Wer mit diesem Kit spielt", "§7erhält automatisch Dummheit Stufe 1000", "", "§6[für Pilzkuuh]"));
        this.registerKits(KitRegistry.KANGAROO, new InventoryKit("Känguru", 5000, Material.SLIME_BALL,
                "", "§7Springe höher als die höchsten,", "§7und weiter als die weitesten!", ""));
    }

    private void registerKits(KitRegistry kitRegistry, InventoryKit inventoryKit) {
        this.availableKits.put(kitRegistry, inventoryKit);
    }

    public Map<KitRegistry, InventoryKit> getAvailableKits() {
        return this.availableKits;
    }

    public static class InventoryKit {

        private final Component kitName;
        private final Component[] kitDescription;
        private final int kitCost;
        private final Material kitMaterial;

        public InventoryKit(String kitName, int kitCost, Material kitMaterial, String... kitDescription) {
            LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder()
                    .hexColors().build();

            this.kitName = legacyComponentSerializer.deserialize(kitName);
            this.kitCost = kitCost;
            this.kitMaterial = kitMaterial;

            this.kitDescription = new Component[kitDescription.length];
            for (int i = 0; i < kitDescription.length; i++) {
                this.kitDescription[i] = legacyComponentSerializer.deserialize(kitDescription[i]);
            }
        }

        public Component getKitName() {
            return this.kitName;
        }

        public Component[] getKitDescription() {
            return this.kitDescription;
        }

        public int getKitCost() {
            return this.kitCost;
        }

        public Material getKitMaterial() {
            return this.kitMaterial;
        }

        public ItemStack toItemStack() {
            return ItemBuilder.from(this.kitMaterial)
                    .name(Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                            .append(this.kitName.color(NamedTextColor.GOLD)))
                    .lore(this.kitDescription).build();
        }

        public Component getScoreboardName() {
            return Component.text("● ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                    .append(this.kitName.color(TextColor.fromHexString("#C10B8A"))); // #f6c806 #3d9efc
        }

    }
}
