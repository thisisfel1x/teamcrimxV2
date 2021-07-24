package de.fel1x.teamcrimx.crimxapi.friends;

import org.bukkit.Material;

public enum FriendSettings {

    /**
     * Can a player jump to his friends server (e.g. for spectating)?
     */
    CAN_JUMP_TO_FRIEND(Material.NETHERITE_BOOTS, "Nachspringen", "Kann ein Spieler dir nachspringen?"),

    /**
     * Can a player can invite his friend to his party?
     */
    CAN_INVITE_TO_PARTY(Material.SWEET_BERRIES, "Partyeinladungen", "Kann man dir Partyeinladungen schicken?"),

    /**
     * Can a player can invite his friend to his clan?
     */
    CAN_INVITE_TO_CLAN(Material.SHIELD, "Claneinladungen", "Kann man dir Claneinladungen schicken?"),

    /**
     * Can a player send his friend a private message?
     */
    CAN_WRITE_PRIVATE(Material.ENDER_EYE, "Private Nachrichten", "Kann man dir Privat schreiben?");

    private final Material displayMaterial;
    private final String name;
    private final String description;

    FriendSettings(Material displayMaterial, String name, String description) {
        this.displayMaterial = displayMaterial;
        this.name = name;
        this.description = description;
    }

    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
