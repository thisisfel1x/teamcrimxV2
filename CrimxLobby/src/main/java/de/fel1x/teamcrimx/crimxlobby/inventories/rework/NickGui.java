package de.fel1x.teamcrimx.crimxlobby.inventories.rework;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class NickGui {

    private static final Gui nickGui = Gui.gui()
            .title(Component.text("● Nick - Einstellungen"))
            .rows(3)
            .create();

    public NickGui() {

        nickGui.setItem(2, 4, ItemBuilder.from(Material.GLOW_INK_SAC).name(Component.text("Test", TextColor.fromHexString("#123456"))).asGuiItem(event -> {
            event.getWhoClicked().sendMessage("lol");
        }));

    }

    public static Gui getNickGui() {
        return nickGui;
    }
}
