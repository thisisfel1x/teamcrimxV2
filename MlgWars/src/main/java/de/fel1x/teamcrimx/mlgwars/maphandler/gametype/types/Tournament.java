package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Tournament extends SoloGameType {

    public Tournament(MlgWars mlgWars) {
        super(mlgWars);
    }

    @Override
    public String getGameTypeName() {
        return "Turnier";
    }

    @Override
    public void finish() {
        super.finish();

        Bukkit.getScheduler().runTaskAsynchronously(this.mlgWars, () -> {
            List<Document> documents = this.mlgWars.getTop(12, true); // todo: better
            List<Component> messages = new ArrayList<>();
            messages.add(Component.empty());
            messages.add(Component.text("Turnier - Standings:", NamedTextColor.YELLOW, TextDecoration.BOLD));

            for (int i = 0; i < 12; i++) {
                String color;
                switch (i) {
                    case 0 -> color = "<#e2b007><bold>";
                    case 1 -> color = "<#c0c0c0><bold>";
                    case 2 -> color = "<#bf8970><bold>";
                    default -> color = "<white>";
                }

                try {
                    Document currentDocument = documents.get(i);
                    int points = currentDocument.getInteger("points");
                    String name = currentDocument.getString("name");

                    String component = color + "#" + (i + 1) + " " + name + "<reset><dark_gray> - <green>" + points + " Punkte";

                    messages.add(this.mlgWars.miniMessage().deserialize(component));

                } catch (Exception ignored) {}
            }
            messages.add(Component.empty());
            messages.forEach(Bukkit::broadcast);
        });
    }
}



















