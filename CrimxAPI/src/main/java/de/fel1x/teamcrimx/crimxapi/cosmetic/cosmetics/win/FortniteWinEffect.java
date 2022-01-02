package de.fel1x.teamcrimx.crimxapi.cosmetic.cosmetics.win;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.modifier.VisibilityModifier;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.WinAnimationCosmetic;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import de.fel1x.teamcrimx.crimxapi.utils.MathUtils;
import de.fel1x.teamcrimx.crimxapi.utils.npc.NPCCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FortniteWinEffect extends WinAnimationCosmetic {

    private List<NPC> npcs;
    private int countdown = 10;

    private String value = "ewogICJ0aW1lc3RhbXAiIDogMTU5Mzg1OTcyMDU5NSwKICAicHJvZmlsZUlkIiA6ICI5MWYwNGZlOTBmMzY0M2I1OGYyMGUzMzc1Zjg2ZDM5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdG9ybVN0b3JteSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZjVjZTk2NmQ5ZjU2MDM0MjVkMjBiMTMxMzgwMWUwNDNlODMyZjE5YjU5ZDMyMzU2Mjg3MDEyOWUzMjU5NzcyIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
    private String signature = "dJvehJskt3QemL1vUx+ILEG+sbn+qwmyappjrMmm+syHdhv7YjmcbZmobFE8aiP7qMGMiFS8jX/Mj1OQD5x1RPhSfdIOn87bIbgCZhw8v1RR+KWy5xqFIVHUX15W4Ybkb1BiGasGLtZp5Q/ERGifckWbnWJG/fm/HEuefm147RRDxj6Dt/fdOKONDc84UoLmHFP9ckgVnRIpw8AsLhuqwXG6wKvpmququHrqts9lZSIosSTr4UMgo77qdQ8DdBmzDstzy1DKgAc8WqPptB8SftyF6WygnfsflPtev3+93vk4yxN+aUsQTHLymWaruRe+F1/TN3GeYbKoR+IGQ7exF9K9IZ03fa7msTpFCj+tl2qke4FXNaZlVmNwO9LEF4Kn06FCn3RFbukz0EV/gDbHsAMb5aM356258GvQEHxulh6iAtbG8hXNR5PEqm7llaLXs5qGaO63yZQci/WhgqGHh/J/GqKIEYbf4/P3iaURxPcX1w7gT3oMnKVC0zxNd7nKLAyPjVH41RneFfrnXIF0UohxdcyPRmvfNKi0y0y3R5TQ+OKk41q2FGi8HTTSikDmQt3n3TSXNF7g73txTwqn5GkzOhdH4D02Vsf1wNX067jHAPBFlzPc4JfeyH8jETTV71uPKbf+qMyJQJGxHUVo4wiJEqH0sm8vIlUhnXqUEvY=";

    public FortniteWinEffect(Player player, CrimxSpigotAPI crimxSpigotAPI) {
        super(player, crimxSpigotAPI);
        this.npcs = new ArrayList<>();
    }

    @Override
    public Component getDisplayName() {
        return Component.text("Fortnut",  NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public Component[] getDescription() {
        return new Component[]{
                Component.empty(), Component.text("Epic Win", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false), Component.empty()
        };
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.PAPER;
    }

    @Override
    public CosmeticCategory getCosmeticCategory() {
        return CosmeticCategory.WIN_ANIMATION;
    }

    @Override
    public int getCost() {
        return 1000;
    }

    @Override
    public double maxDiscount() {
        return 0.2;
    }

    @Override
    public void win() {
        super.win();
        this.spawnNPCS();
        this.runTaskTimer(this.crimxSpigotAPI, 20L, 20L);
    }

    @Override
    public void run() {
        if(this.countdown > 0) {
            for (NPC npc : this.npcs) {
                if(this.random.nextBoolean()) {
                    npc.metadata().queue(MetadataModifier.EntityMetadata.POSE, EnumWrappers.EntityPose.CROUCHING).send();
                } else {
                    npc.metadata().queue(MetadataModifier.EntityMetadata.POSE, EnumWrappers.EntityPose.STANDING).send();
                }
                npc.rotation().queueLookAt(this.player.getEyeLocation()).send();
            }
        }

        if(this.countdown <= 0) {
            this.destroyNPCS();
            if(this.npcs.isEmpty()) {
                this.cancel();
            }
        }

        this.countdown--;
    }

    private void spawnNPCS() {
        for (Location circlePoint : MathUtils.getCirclePoints(this.player.getLocation(), 2.5, 5)) {
            NPC npc = new NPCCreator(circlePoint).addHeaders(new String[]{ "§bEPIC WIN" })
                    .createProfile(this.value, this.signature)
                    .shouldImitatePlayer(false).shouldImitatePlayer(false).spawn();
            this.npcs.add(npc);
        }
    }

    private void destroyNPCS() {
        try {
            for (NPC npc : this.npcs) {
                npc.getLocation().getNearbyEntitiesByType(ArmorStand.class, 2).forEach(ArmorStand::remove);
                npc.visibility().queueDestroy().send();
                npc.visibility().queuePlayerListChange(VisibilityModifier.PlayerInfoAction.REMOVE_PLAYER).send();
                this.npcs.remove(npc);
            }
        } catch (Exception ignored) { }
    }
}
