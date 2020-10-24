package de.fel1x.teamcrimx.crimxapi.utils;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class Actionbar {

    public static void sendTitle(Player p, String msg, int fadein, int stay, int fadeout) {

        Title title = new Title(msg, "", fadein, stay, fadeout);
        p.sendTitle(title);

    }

    public static void sendSubTitle(Player p, String msg, int fadein, int stay, int fadeout) {

        Title title = new Title("", msg, fadein, stay, fadeout);
        p.sendTitle(title);

    }

    public static void sendActiobar(final Player p, final String message) {

        p.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));

    }

}
