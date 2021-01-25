package de.fel1x.bingo.scenarios;

import de.fel1x.bingo.Bingo;
import de.fel1x.teamcrimx.crimxapi.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerXRay implements IBingoScenario {

    private final Bingo bingo = Bingo.getInstance();
    private final ArrayList<Material> toReplace = new ArrayList<>(Arrays.asList(Material.GRASS_BLOCK, Material.DIRT,
            Material.STONE, Material.GRANITE, Material.ANDESITE));

    @Override
    public void execute() {
        for (Player player : this.bingo.getData().getPlayers()) {

            Location playerLocation = player.getLocation();
            Cuboid cuboid = new Cuboid(playerLocation.clone().add(3, 3, 3),
                    playerLocation.clone().subtract(3, 3, 3));

            cuboid.getBlocks().stream().filter(block -> this.toReplace.contains(block.getType()))
                    .forEach(block -> block.setType(Material.AIR));
        }
    }

    @Override
    public String getName() {
        return "Spieler X-Ray";
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.DIAMOND;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "", "§7In einem §e5x5 Radius §7um den Spieler", "§7wird §eStein §7mit §eGlas vertauscht", ""
        };
    }
}
