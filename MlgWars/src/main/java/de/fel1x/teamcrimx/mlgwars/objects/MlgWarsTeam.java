package de.fel1x.teamcrimx.mlgwars.objects;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class MlgWarsTeam {

    private int id;
    private int teamId;
    private int maxPlayers;
    private ArrayList<Player> teamPlayers;
    private ArrayList<Player> alivePlayers;
    private final java.awt.Color color;

    public MlgWarsTeam(int id, int teamId, int maxPlayers, ArrayList<Player> teamPlayers, ArrayList<Player> alivePlayers, java.awt.Color color) {
        this.id = id;
        this.teamId = teamId;
        this.maxPlayers = maxPlayers;
        this.teamPlayers = teamPlayers;
        this.alivePlayers = alivePlayers;
        this.color = color;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public ArrayList<Player> getTeamPlayers() {
        return this.teamPlayers;
    }

    public void setTeamPlayers(ArrayList<Player> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public ArrayList<Player> getAlivePlayers() {
        return this.alivePlayers;
    }

    public void setAlivePlayers(ArrayList<Player> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }

    public java.awt.Color getColor() {
        return this.color;
    }

    public ItemStack getGUIItemStack() {
        this.teamPlayers.removeIf(teamPlayer -> !teamPlayer.isOnline());

        MiniMessage miniMessage = MlgWars.getInstance().miniMessage();

        ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
        itemStack.editMeta(LeatherArmorMeta.class, leatherArmorMeta -> {
            leatherArmorMeta.setColor(Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue()));
        });

        String teamName = "Team #" + this.getTeamId();
        int color = this.color.darker().darker().getRGB();

        String displayName = "<#" + color + ">" + teamName + "<gray> (" + this.teamPlayers.size() + "/" + this.maxPlayers + " Spieler)";
        String joinInformation = "<gray>Klicke, um <#" + color + ">" + teamName + "<gray> beizutreten!";

        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        for(int i = 0; i < this.maxPlayers; i++) {
            Player teamPlayer = null;
            try {
                teamPlayer = this.getTeamPlayers().get(i);
            } catch (Exception ignored) {}
            Component loreDisplayName = teamPlayer != null ? teamPlayer.displayName() : Component.text("-", NamedTextColor.DARK_GRAY);
            Component completeLore = Component.text("» ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(loreDisplayName);
            lore.add(completeLore);
        }
        lore.add(Component.empty());
        lore.add(miniMessage.parse(joinInformation));

        return ItemBuilder.from(itemStack)
                .name(miniMessage.parse(displayName))
                .lore(lore)
                .build();
    }

}
