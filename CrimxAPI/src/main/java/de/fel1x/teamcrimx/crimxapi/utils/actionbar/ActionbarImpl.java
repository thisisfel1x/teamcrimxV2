package de.fel1x.teamcrimx.crimxapi.utils.actionbar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ActionbarImpl {

    public Component getAdditionalComponent() {
        return Component.text("Ein Fehler ist aufgetreten", NamedTextColor.RED);
    }

    public void sendActionbar(Player player, @Nullable String content) {
        this.sendActionbar(player, LegacyComponentSerializer.legacySection().deserialize(content));
    }

    public void sendActionbar(Player player, @Nullable Component content) {
        Component finalComponent = this.getAdditionalComponent();
        if(content != null) {
            finalComponent = finalComponent.append(Component.text(" x ", NamedTextColor.DARK_GRAY))
                    .append(content);
        }
        this.send(player, finalComponent);
    }

    private void send(Player player, Component finalComponent) {
        player.sendActionBar(finalComponent);
    }
}
