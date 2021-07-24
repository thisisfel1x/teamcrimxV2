package de.fel1x.teamcrimx.crimxapi.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

public class Actionbar {

    public static void sendFullTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Title toSend = Title.title(Component.text(title), Component.text(subtitle),
                Title.Times.of(Duration.ofMillis(fadeIn * 50L), Duration.ofMillis(stay * 50L),
                        Duration.ofMillis(fadeOut * 50L)));
        player.showTitle(toSend);
    }

    public static void sendOnlySubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendFullTitle(player, " ", subtitle, fadeIn, stay, fadeOut);
    }

    public static void sendOnlyTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        sendFullTitle(player, title, " ", fadeIn, stay, fadeOut);
    }

    public static void sendActionbar(final Player player, final String message) {
        player.sendActionBar(Component.text(message));
    }

}
