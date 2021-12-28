package de.fel1x.teamcrimx.mlgwars.maphandler.gametype.types;

import de.fel1x.teamcrimx.mlgwars.MlgWars;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tournament extends SoloGameType {

    public Tournament(MlgWars mlgWars) {
        super(mlgWars);
    }

    @Override
    public String getGameTypeName() {
        return "Turnier";
    }

    @Override
    public void gameInit() {
        super.gameInit();
    }

    @Override
    public void finish() {
        super.finish();

        Bukkit.getScheduler().runTaskAsynchronously(this.mlgWars, () -> {
            List<Document> documents = this.mlgWars.getTop(12); // todo: better
            List<Component> messages = new ArrayList<>();
            messages.add(Component.empty());
            messages.add(Component.text("Turnier - Standings:", NamedTextColor.YELLOW, TextDecoration.BOLD));

            for (int i = 0; i < 12; i++) {

                try {
                    Document currentDocument = documents.get(i);
                    int points = currentDocument.getInteger("points");
                    String name = currentDocument.getString("name");

                    String component = "<white><bold>#" + (i + 1) + " " + name + "<reset><dark_gray> - <gray>Punkte: <green>" + points;

                    messages.add(this.mlgWars.miniMessage().parse(component));

                } catch (Exception ignored) {}
            }
            messages.forEach(Bukkit::broadcast);
        });
    }
}



















