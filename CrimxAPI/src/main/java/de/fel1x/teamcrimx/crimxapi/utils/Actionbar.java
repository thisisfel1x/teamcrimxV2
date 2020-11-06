package de.fel1x.teamcrimx.crimxapi.utils;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class Actionbar {

    public static void sendFullTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Title toSend = new Title(title, subtitle, fadeIn, stay, fadeOut);
        player.sendTitle(toSend);
    }

    public static void sendOnlySubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut) {
        Title toSend = new Title(" ", subtitle, fadeIn, stay, fadeOut);
        player.sendTitle(toSend);
    }

    public static void sendOnlyTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        Title toSend = new Title(title, " ", fadeIn, stay, fadeOut);
        player.sendTitle(toSend);
    }

    public static void sendActionbar(final Player p, final String message) {
        p.sendActionBar(new TextComponent(message));
    }

}
