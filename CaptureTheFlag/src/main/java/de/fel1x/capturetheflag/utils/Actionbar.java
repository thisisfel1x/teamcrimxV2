package de.fel1x.capturetheflag.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void updateTimeBar(Player p) {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        final String message = "§fAktuelle Uhrzeit §8» §a§l" + simpleDateFormat.format(new Date());
        //final String message_2 =  " §8§l| §fPing §8» §b§l" + getPing(p) + "§f ms";
        // String message_3 = ((CrimxLobby.getInstance().getBuilders().contains(p)) ? "§8§l| §fBuild §8» §aJa" : "§8§l| §fBuild §8» §cNein");

        for (Player all : Bukkit.getOnlinePlayers()) {

            sendActiobar(all, message);

        }

    }

    public static int getPing(Player p) {

        int ping = ((CraftPlayer) p).getHandle().ping;
        return ping;

    }

}

