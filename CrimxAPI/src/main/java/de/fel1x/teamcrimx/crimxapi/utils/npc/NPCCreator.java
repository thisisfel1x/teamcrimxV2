package de.fel1x.teamcrimx.crimxapi.utils.npc;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.modifier.MetadataModifier;
import com.github.juliarn.npc.profile.Profile;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class NPCCreator {

    private final Location npcLocation;

    /**
     * NPCPool, null if plugin isn't initialized
     */
    private final NPCPool npcPool = CrimxSpigotAPI.getInstance().getNpcPool();

    private boolean shouldImitatePlayer = true;
    private boolean shouldLookAtPlayer = true;

    private Profile npcProfile;

    public NPCCreator(Location npcLocation) {
        this.npcLocation = npcLocation;
    }

    public NPCCreator shouldImitatePlayer(boolean shouldImitatePlayer) {
        this.shouldImitatePlayer = shouldImitatePlayer;
        return this;
    }

    public NPCCreator shouldLookAtPlayer(boolean shouldLookAtPlayer) {
        this.shouldLookAtPlayer = shouldLookAtPlayer;
        return this;
    }

    public NPCCreator createProfile(String value, String signature) {
        this.npcProfile = new Profile(UUID.randomUUID(), "",
                Collections.singleton(new Profile.Property("textures", value, signature)));
        this.npcProfile.complete();
        return this;
    }

    public NPCCreator createProfileByUUID(UUID uuid, String name) {
        this.npcProfile = new Profile(uuid);
        this.npcProfile.complete();

        this.npcProfile.setName(name);

        return this;
    }

    public NPCCreator addHeaders(String[] lines) {
        Collections.reverse(Arrays.asList(lines));

        for (int i = 0; i < lines.length; i++) {
            int finalI = i;
            this.npcLocation.getWorld()
                    .spawn(this.npcLocation.clone().add(0, -0.25 + 0.25 * i, 0), ArmorStand.class,
                            CreatureSpawnEvent.SpawnReason.CUSTOM, armorStand -> {
                                armorStand.setVisible(false);

                                armorStand.setCustomNameVisible(true);
                                armorStand.setCustomName(lines[finalI]);
                                armorStand.setGravity(false);
                            });
        }

        return this;
    }

    public NPC spawn() {
        return NPC.builder()
                .location(this.npcLocation)
                .imitatePlayer(this.shouldImitatePlayer)
                .spawnCustomizer((npc, player) -> npc.metadata()
                        .queue(MetadataModifier.EntityMetadata.SKIN_LAYERS, true).send())
                .lookAtPlayer(this.shouldLookAtPlayer)
                .profile(this.npcProfile)
                .build(this.npcPool);
    }

}
