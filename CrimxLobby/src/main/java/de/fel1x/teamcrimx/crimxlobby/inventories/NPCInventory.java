package de.fel1x.teamcrimx.crimxlobby.inventories;

import de.fel1x.teamcrimx.crimxapi.coins.CoinsAPI;
import de.fel1x.teamcrimx.crimxapi.database.mongodb.MongoDBCollection;
import de.fel1x.teamcrimx.crimxapi.objects.CrimxPlayer;
import de.fel1x.teamcrimx.crimxapi.utils.ItemBuilder;
import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import de.fel1x.teamcrimx.crimxlobby.objects.LobbyPlayer;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class NPCInventory implements InventoryProvider {

    public static final SmartInventory NPC_INVENTORY = SmartInventory.builder()
            .id("customInventory")
            .provider(new NPCInventory())
            .size(3, 9)
            .title("§8● §5Dein Profil")
            .manager(CrimxLobby.getInstance().getInventoryManager())
            .build();

    final CrimxLobby crimxLobby = CrimxLobby.getInstance();

    @Override
    public void init(Player player, InventoryContents contents) {

        AtomicReference<LobbyPlayer> lobbyPlayer = new AtomicReference<>(new LobbyPlayer(player));
        CrimxPlayer crimxPlayer = new CrimxPlayer(lobbyPlayer.get().getCloudPlayer());

        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1).setName(" ").toItemStack()));

        contents.set(0, 4, ClickableItem.empty(new ItemBuilder(crimxPlayer.getPlayerSkin()[0])
                .setName("§8● §a" + player.getDisplayName()).addGlow().toItemStack()));

        Date date = new Date(lobbyPlayer.get().getCloudPlayer().getFirstLoginTimeMillis());
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy',' HH:mm:ss 'Uhr'");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        String dateFormatted = formatter.format(date);

        contents.set(1, 2, ClickableItem.empty(new ItemBuilder(Material.EMERALD)
                .setName("§7Erster Join §8● §a§l" + dateFormatted).toItemStack()));

        date = new Date(lobbyPlayer.get().getCloudPlayer().getLastLoginTimeMillis());
        formatter = new SimpleDateFormat("dd.MM.yyyy',' HH:mm:ss 'Uhr'");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        dateFormatted = formatter.format(date);

        contents.set(1, 3, ClickableItem.empty(new ItemBuilder(Material.GOLD_NUGGET)
                .setName("§7Letzter Join §8● §a§l" + dateFormatted).toItemStack()));

        long lastRewardString = this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).getLastReward();

        String nowstring = new SimpleDateFormat("dd.MM.yyyy").format(new Date(System.currentTimeMillis()));
        String lastReward = new SimpleDateFormat("dd.MM.yyyy").format(new Date(lastRewardString));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Date nowDate = simpleDateFormat.parse(nowstring);
            Date lastRewardDate = simpleDateFormat.parse(lastReward);

            boolean canGet = lastRewardDate.before(nowDate);

            ItemBuilder itemBuilder = new ItemBuilder(Material.GOLD_INGOT).setName("§aTägliche Belohnung");
            if (canGet) {
                itemBuilder.addEnchant(Enchantment.ARROW_DAMAGE, 1).addGlow();
                itemBuilder.setLore(" ", "§7Hole dir deine §etägliche Belohnung §7ab", " ");
            } else {
                itemBuilder.setLore(" ", "§cKomm doch morgen wieder vorbei!", " ");
            }

            contents.set(1, 5, ClickableItem.of(itemBuilder.toItemStack(), event -> {
                if (canGet) {
                    player.sendMessage(this.crimxLobby.getPrefix() + "§7Du hast §a100 Coins §7durch die tägliche Belohnung erhalten!");
                    this.crimxLobby.getData().getLobbyDatabasePlayer().get(player.getUniqueId()).setLastReward(System.currentTimeMillis());
                    lobbyPlayer.get().saveObjectInDocument("lastReward", System.currentTimeMillis(), MongoDBCollection.LOBBY);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 0.75f);
                    CoinsAPI coinsAPI = new CoinsAPI(player.getUniqueId());
                    coinsAPI.addCoins(100);
                    lobbyPlayer.set(new LobbyPlayer(player, true));
                    int coins = (int) lobbyPlayer.get().getObjectFromMongoDocument("coins", MongoDBCollection.USERS);
                    this.crimxLobby.getLobbyScoreboard().updateBoard(player, String.format("§8● §e%s Coins", coins), "coins", "§e");
                } else {
                    player.sendMessage(this.crimxLobby.getPrefix() + "§7Du hast bereits deine tägliche Belohnung abgeholt!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 2, 0.5f);
                }

                player.closeInventory();

            }));

        } catch (ParseException ignored) {
            player.sendMessage(this.crimxLobby.getPrefix() + "§cEin Fehler ist aufgetreten!");
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
