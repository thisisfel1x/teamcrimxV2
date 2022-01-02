package de.fel1x.teamcrimx.crimxlobby.listeners.player;

import co.aikar.util.Counter;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.inventories.NavigatorInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.CosmeticReworkInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.LobbySwitcherInventory;
import de.fel1x.teamcrimx.crimxlobby.inventories.rework.ProfileInventory;
import de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.objects.Invitation;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;
import java.util.Random;

public class InteractListener implements Listener {

    private final CrimxLobby crimxLobby;
    private final Random random = new Random();
    private final String[] npcMessages = {
            "Sein oder nicht sein, dass ist hier die Frage",
            "Was du mir sagst, das vergesse ich. Was du mir zeigst, daran erinnere ich mich. Was du mich tun lässt, das verstehe ich.",
            "Gläubiger haben ein besseres Gedächtnis als Schuldner",
            "Für die Welt bist du irgendjemand, aber für irgendjemand bist du die Welt.",
            "Freundschaft ist wie Geld, leichter gewonnen als erhalten",
            "Wer einen Fehler gemacht hat und ihn nicht korrigiert, begeht einen Zweiten.",
            "Lernen, ohne zu denken, ist eitel; denken, ohne zu lernen, gefährlich",
            "Es ist besser, ein einziges kleines Licht anzuzünden, als die Dunkelheit zu verfluchen",
            "Was nennen die Menschen am liebsten dumm? Das Gescheite, das sie nicht verstehen",
            "Das Wort „Vegetarier“ kommt aus dem Sanskrit und bedeutet “zu blöd zum Jagen“.",
            "Lieber locker vom Hocker, als hektisch über'n Ecktisch!",
            "Wenn eine Frau wütend ist, nimm sie in den Arm.",
            "Wenn das auch nicht hilft, halte Abstand und wirf ihr Schokolade zu.",
            "Das Problem will ich nicht, zeig mir das Nächste!",
            "Ich schlafe im Sommer mit offenem Fenster. 1832 Mücken gefällt das.",
            "Ich spreche drei Sprachen: ironisch, sarkastisch und zweideutig.",
            "Suche neuen Schutzengel. Meiner ist mit den Nerven am Ende.",
            "Niemand ist so uninteressant wie ein Mensch ohne Interesse.",
            "Zickezacke, Hühnerkacke, hoi, hoi, hoi!",
            "Im Wonnemonat Mai, da brat' ich mir ein Ei!",
            "Nachts ist es kälter als draußen.",
            "Wenn der Hahn kräht auf dem Mist, wird das Wetter besser oder es bleibt, wie es ist.",
            "Der frühe Vogel kann mich mal.",
            "Arbeit hat noch niemanden umgebracht, aber ich will kein Risiko eingehen.",
            "Auch ein Traumjob berechtigt nicht zum Schlaf während der Arbeitszeit.",
            "Verrückte Leute wissen nicht, dass sie verrückt sind, aber ich weiß, dass ich verrückt bin. Das bedeutet, dass ich nicht verrückt bin. Ist das nicht verrückt?",
            "Jeder Mensch hat ein Recht auf meine eigene Meinung!",
            "Noch 361 Tage bis Weihnachten und überall hängt schon der Weihnachtsschmuck. Manche Leute übertreiben es echt!",
            "Dass ich in Englisch träume, stört mich nicht, aber mich nerven die deutschen Untertitel!",
            "Nichts ist unmöglich? Dann schlag mal `ne Drehtür zu!",
            "Die Summe der Intelligenz auf unserem Planeten ist konstant, aber die Bevölkerung wächst."
    };
    private final Component prefix = Component.text("Dealer ", TextColor.fromHexString("#8803fc"))
            .decoration(TextDecoration.ITALIC, false).append(Component.text("● ", NamedTextColor.DARK_GRAY));

    public InteractListener(CrimxLobby crimxLobby) {
        this.crimxLobby = crimxLobby;
        this.crimxLobby.getPluginManager().registerEvents(this, this.crimxLobby);
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        Action action = event.getAction();
        ItemStack item = event.getItem();

        if (lobbyPlayer.isInBuild()) {
            return;
        }

        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock() != null
                    && event.getClickedBlock().getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }

        if (item == null) {
            return;
        }

        if (event.hasItem() && item.getType() == Material.FISHING_ROD) {
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            if (event.hasItem()) {
                switch (item.getType()) {
                    case MUSIC_DISC_CAT -> NavigatorInventory.getNavigatorInventory().open(player);
                    case RED_DYE, LIME_DYE, PURPLE_DYE -> lobbyPlayer.updatePlayerHiderState();
                    case CHEST_MINECART -> CosmeticReworkInventory.COSMETICS_REWORK_INVENTORY.open(player);
                    case PLAYER_HEAD -> ProfileInventory.PROFILE_REWORK_INVENTORY.open(player);
                    case MOJANG_BANNER_PATTERN -> LobbySwitcherInventory.getLobbySwitcherGui().open(player);
                    case NAME_TAG -> this.updateNickItem(player);
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerArmorStandManipulateEvent event) {

        Player player = event.getPlayer();
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);

        if (lobbyPlayer.isInBuild()) {
            return;
        }

        event.setCancelled(true);

    }

    @EventHandler
    public void on(PlayerInteractEntityEvent event) {
        if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.SHEARS
                && event.getHand() == EquipmentSlot.HAND && event.getRightClicked() instanceof Player target) {

            Player whoInteracted = event.getPlayer();

            if(this.crimxLobby.getConnectFourGameManager().isPlaying(target)) {
                whoInteracted.sendMessage(this.crimxLobby.getConnectFourGameManager().getPrefix()
                        .append(target.displayName())
                        .append(Component.text( " befindet sich bereits in einer Runde", NamedTextColor.GRAY)));
                return;
            }

            if(this.crimxLobby.getConnectFourGameManager().hasInvitedRecently(whoInteracted)) {
                whoInteracted.sendMessage(this.crimxLobby.getConnectFourGameManager().getPrefix()
                        .append(Component.text("Du hast einen ", NamedTextColor.GRAY))
                        .append(Component.text("anderen Spieler", NamedTextColor.RED))
                        .append(Component.text( " erst kürzlich eingeladen", NamedTextColor.GRAY)));
                return;
            }

            whoInteracted.sendMessage(this.crimxLobby.getConnectFourGameManager().getPrefix()
                    .append(Component.text("Du hast ", NamedTextColor.GRAY))
                    .append(target.displayName())
                    .append(Component.text(" eingeladen", NamedTextColor.GRAY)));

            Component invitationComponent = this.crimxLobby.getConnectFourGameManager().getPrefix()
                    .append(Component.text("Du wurdest von ", NamedTextColor.GRAY))
                    .append(whoInteracted.displayName())
                    .append(Component.text( " eingeladen. Klicke zum ", NamedTextColor.GRAY))
                    .append(Component.text("ANNEHMEN", NamedTextColor.GREEN, TextDecoration.BOLD)
                            .clickEvent(ClickEvent.runCommand("/connectfour accept=" + whoInteracted.getUniqueId())));

            target.sendMessage(invitationComponent);
            this.crimxLobby.getConnectFourGameManager().addInvitation(new Invitation(whoInteracted, target));
            return;
        }

        Player player = event.getPlayer();
        event.setCancelled(true);

        if(event.getRightClicked().getUniqueId().equals(this.crimxLobby.getWanderingTraderNPCEntity().getUniqueId())) {
            if(player.hasMetadata("npcWaitTime")) {
                long waitTime = player.getMetadata("npcWaitTime").get(0).asLong();
                if(waitTime > System.currentTimeMillis()) {
                    return;
                } else {
                    player.setMetadata("npcCount", new FixedMetadataValue(this.crimxLobby, 0));
                    player.removeMetadata("npcWaitTime", this.crimxLobby);
                }
            }

            if(player.hasMetadata("npcCount")) {
                int interactionCount = player.getMetadata("npcCount").get(0).asInt();
                if(interactionCount > 10) {
                    player.sendMessage(this.prefix.append(Component.text("Jetzt reichts du Knecht", NamedTextColor.RED)));
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5f, 0.75f);
                    player.setMetadata("npcWaitTime", new FixedMetadataValue(this.crimxLobby,
                            System.currentTimeMillis() + (1000 * 15)));
                    return;
                }
            }

            int interactionCount = 0;
            if(player.hasMetadata("npcCount")) {
                interactionCount = player.getMetadata("npcCount").get(0).asInt();
            }
            player.setMetadata("npcCount", new FixedMetadataValue(this.crimxLobby, interactionCount + 1));
            player.sendMessage(this.prefix.append(
                    Component.text(this.npcMessages[this.random.nextInt(this.npcMessages.length)], NamedTextColor.GRAY)));
            player.playSound(player.getLocation(), this.random.nextBoolean() ? Sound.ENTITY_VILLAGER_YES : Sound.ENTITY_VILLAGER_NO,
                    1f, 1f);
        }
    }
    private void updateNickItem(Player player) {
        if (!player.hasPermission("crimxlobby.vip")) {
            return;
        }

        Objects.requireNonNull(this.crimxLobby.getCrimxAPI().getMongoDB()
                .getObjectFromDocumentAsyncOrDefault(player.getUniqueId(), MongoDBCollection.USERS, "nick", false)).
                thenAccept(nickActivated -> {

                    boolean inverted = !(Boolean) nickActivated;

                    ItemStack itemStack = player.getInventory().getItem(4);

                    if (itemStack == null || itemStack.getItemMeta() == null) {
                        return;
                    }

                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.displayName(Component.text("● ", NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, false)
                            .append(Component.text("Nick ", NamedTextColor.DARK_PURPLE))
                            .append(Component.text("» ", NamedTextColor.GRAY))
                            .append(Component.text((inverted ? "aktiviert" : "deaktiviert"),
                                    (inverted ? NamedTextColor.GREEN : NamedTextColor.RED))));

                    itemStack.setItemMeta(itemMeta);

                    this.crimxLobby.getCrimxAPI().getMongoDB()
                            .insertObjectInDocumentAsync(player.getUniqueId(), MongoDBCollection.USERS, "nick", inverted);
                });
    }
}
