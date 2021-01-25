package de.fel1x.bingo.settings;

import de.fel1x.bingo.inventories.settings.ScenarioInventory;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public enum Settings {

    EVENTS_ENABLED("Events", Material.ENDER_EYE,
            new String[] { "§7Sollen §eSpielerevents §7(z.B. Random Potioneffect) aktiviert werden?" },
            true, true, ScenarioInventory.SCENARIO_INVENTORY),
    DAYLIGHT_CYCLE("Tageszyklus", Material.LANTERN,
            new String[]{ "§7Soll der §eTageszyklus §7aktiv sein?" },
            true, false, null),
    DO_MOB_SPAWN("Mobspawning", Material.ZOMBIE_HEAD,
            new String[] { "§7Soll §eMobspawning §7aktiv sein?" },
            true, false, null),
    RANDOMIZER("Randomizer", Material.DIAMOND_PICKAXE, new String[]{ "§7Randomizer = §ezufällige §7Blockdrops" },
            false, false, null),
    CRAFT_RANDOMIZER("Crafting Randomizer", Material.CRAFTING_TABLE,
            new String[]{ "§7Crafting Randomizer = §ezufällige §7Craftingergebnisse" },
            false, false, null),
    HUNGER("Hunger", Material.BREAD, new String[]{ "§7Soll §eHunger §7aktiv sein?" },
            true, false, null),
    FALL_DAMAGE("Fallschaden", Material.DIAMOND_BOOTS, null, false, false, null),
    GENERIC_DAMAGE("Spielerschaden", Material.IRON_CHESTPLATE, null, false, false, null);

    private final String name;
    private final Material displayMaterial;
    private final String[] description;
    private boolean enabled;
    private final boolean hasConfiguration;
    private final Object inventoryClazz;

    Settings(String name, Material displayMaterial, String[] description, boolean enabled, boolean hasConfiguration, Object inventoryClazz) {
        this.name = name;
        this.displayMaterial = displayMaterial;
        this.description = description;
        this.enabled = enabled;
        this.hasConfiguration = hasConfiguration;
        this.inventoryClazz = inventoryClazz;
    }

    public String getName() {
        return this.name;
    }

    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    public String[] getDescription() {
        return this.description;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean hasConfiguration() {
        return this.hasConfiguration;
    }

    @Nullable
    public Object getInventoryClazz() {
        return this.inventoryClazz;
    }
}
