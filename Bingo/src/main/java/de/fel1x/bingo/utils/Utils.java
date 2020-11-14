package de.fel1x.bingo.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class Utils {

    public static ChatColor getChatColor(Color color) {
        if (color.equals(Color.AQUA)) return ChatColor.AQUA;
        else if (color.equals(Color.BLACK)) return ChatColor.BLACK;
        else if (color.equals(Color.BLUE)) return ChatColor.BLUE;
        else if (color.equals(Color.SILVER)) return ChatColor.GRAY;
        else if (color.equals(Color.GREEN)) return ChatColor.DARK_GREEN;
        else if (color.equals(Color.LIME)) return ChatColor.GREEN;
        else if (color.equals(Color.OLIVE)) return ChatColor.DARK_PURPLE;
        else if (color.equals(Color.ORANGE)) return ChatColor.GOLD;
        else if (color.equals(Color.PURPLE)) return ChatColor.DARK_PURPLE;
        else if (color.equals(Color.RED)) return ChatColor.RED;
        else if (color.equals(Color.WHITE)) return ChatColor.WHITE;
        else if (color.equals(Color.YELLOW)) return ChatColor.YELLOW;
        else if (color.equals(Color.GRAY)) return ChatColor.DARK_GRAY;
        else return null;
    }
}
