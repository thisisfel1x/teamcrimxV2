package de.fel1x.teamcrimx.crimxapi.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Actionbar {

    public static void sendTitle(Player p, String msg, int fadein, int stay, int fadeout) {

        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}"), fadein, stay, fadeout);

        PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;

        con.sendPacket(packet);

    }

    public static void sendSubTitle(Player p, String msg, int fadein, int stay, int fadeout) {

        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}"), fadein, stay, fadeout);

        PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;

        con.sendPacket(packet);

    }

    public static void sendActiobar(final Player p, final String message) {

        CraftPlayer player = (CraftPlayer) p;

        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}");

        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);

        player.getHandle().playerConnection.sendPacket(ppoc);

    }

}
