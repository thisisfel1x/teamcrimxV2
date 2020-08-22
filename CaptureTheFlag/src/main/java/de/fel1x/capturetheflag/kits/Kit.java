package de.fel1x.capturetheflag.kits;

import org.bukkit.Material;

public enum Kit {

    TANK("§eTank", new String[]{"§aVorteile:", " §8» §eSteinschwert (Sharp 1)", " §8» §eFull Iron", "§cNachteile", " §8» §eLangsamkeit 1"}, Material.DIAMOND_CHESTPLATE, "TANK"),
    ARCHER("§bArcher", new String[]{"§aVorteile:", " §8» §eHolzschwert (Sharp 1)", " §8» §eBogen (Stärke 1 / Infinity)", " §8» §eFull Leather", "§cNachteile", " §8» §e"}, Material.BOW, "ARCHER"),
    PYRO("§6Pyro", new String[]{"§aVorteile:", " §8» §eGoldschwert (Sharp 2 / Fire 1)", " §8» §eFull Leather", "§cNachteile", " §8» §e"}, Material.FLINT_AND_STEEL, "PYRO"),
    MEDIC("§cMedic", new String[]{"§aVorteile:", " §8» §eEisenschwert (Sharp 1)", " §8» §eFull Leather", " §8» §e4x Healpotion", " §8» §e2x Trank der Regeneration", " §8» §e1x Goldapfel", "§cNachteile", " §8» §e"}, Material.GOLDEN_APPLE, "MEDIC");

    String name;
    String[] description;
    Material material;
    String enumName;

    Kit(String name, String[] description, Material material, String enumName) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.enumName = enumName;
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public String getEnumName() {
        return enumName;
    }
}
