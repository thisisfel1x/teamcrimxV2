package de.fel1x.teamcrimx.crimxlobby.minigames.connectfour.objects;

import de.fel1x.teamcrimx.crimxlobby.CrimxLobby;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class Game {

    private final CrimxLobby crimxLobby;
    private final Random random = new Random();

    private final Player player1; // color: red
    private final Player player2; // color: blue

    private Player currentTurn;
    private boolean finished = false;

    private final Gui gui;

    private final int maxRows = 6;
    private final int maxColumns = 7;
    // value 0: not clicked yet, value 1: player1, value 2: player2
    private final int[][] gridTable;

    public Game(CrimxLobby crimxLobby, Player player1, Player player2) {
        this.crimxLobby = crimxLobby;
        this.player1 = player1;
        this.player2 = player2;

        this.currentTurn = this.random.nextBoolean() ? this.player1 : this.player2;

        this.gridTable = new int[this.maxRows][this.maxColumns];

        this.gui = Gui.gui().title(Component.text("VierGewinnt ● ", NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(this.player1.displayName())
                .append(Component.text(" vs ", NamedTextColor.DARK_GRAY))
                .append(this.player2.displayName()))
                .rows(6)
                .create();

        this.gui.setCloseGuiAction(event -> {
            if(!this.finished) {
                this.stopGame(this.getPartner((Player) event.getPlayer()), FinishReason.PLAYER_LEFT_MATCH);
            }
        });

        // Game Separator
        for(int i = 1; i < 7; i++) {
            this.gui.setItem(i, 8, ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                    .name(Component.empty())
                    .asGuiItem());
            this.gui.setItem(i, 9, ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                    .name(Component.empty())
                    .asGuiItem());
        }

        this.gui.setItem(3, 9, ItemBuilder.skull().owner(player1)
                .name(player1.displayName())
                .asGuiItem());
        this.gui.setItem(4, 9, ItemBuilder.skull().owner(player2)
                .name(player2.displayName())
                .asGuiItem());
        this.gui.setItem(6, 9, ItemBuilder.from(Material.BARRIER)
                .name(Component.text("§cAufgeben", NamedTextColor.RED))
                .asGuiItem(event -> {
                    if(event.getWhoClicked() instanceof Player player) {
                        Player winner = this.player1;
                        if(player.getUniqueId().equals(this.player1.getUniqueId())) {
                            winner = this.player2;
                        }
                        this.stopGame(winner, FinishReason.PLAYER_LEFT_MATCH);
                    }
                }));

        this.crimxLobby.getConnectFourGameManager().addGame(this);

        this.updateTurnIndicator();
        this.updatePlayerInventory();
        this.fillBoardAndStartGameLogic();
    }

    private void updatePlayerInventory() {
        this.player1.getInventory().setItem(22, ItemBuilder.from(Material.RED_BED)
                .name(Component.text("Du bist Team Rot", NamedTextColor.RED))
                .build());
        this.player2.getInventory().setItem(22, ItemBuilder.from(Material.BLUE_BED)
                .name(Component.text("Du bist Team Blau", NamedTextColor.BLUE))
                .build());
    }

    private void updateTurnIndicator() {
        Material color = Material.BLUE_STAINED_GLASS_PANE;
        if(this.currentTurn.getUniqueId().equals(this.player1.getUniqueId())) {
            color = Material.RED_STAINED_GLASS_PANE;
        }
        Component name = Component.text("Team ", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text(color == Material.BLUE_STAINED_GLASS_PANE ? "Blau" : "Rot",
                        color == Material.BLUE_STAINED_GLASS_PANE ? NamedTextColor.BLUE : NamedTextColor.RED)
                        .append(Component.text(" ist am Zug", NamedTextColor.GRAY)));
        this.gui.updateItem(1, 9, ItemBuilder.from(color).name(name)
                .lore(Component.empty(), Component.text("Aktueller Zug", NamedTextColor.GRAY), Component.empty())
                .asGuiItem());
    }

    private void fillBoardAndStartGameLogic() {
        for(int row = 0; row < this.maxRows; row++) {
            for(int column = 0; column < this.maxColumns; column++) {
                int finalRow = row;
                int finalColumn = column;
                this.gui.setItem(row + 1, column + 1, ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                        .name(Component.text("Freies Feld", NamedTextColor.WHITE))
                        .asGuiItem(event -> {
                    if(event.getWhoClicked() instanceof Player player) {
                        if(!this.hasTurn(player)) {
                            return;
                        }
                        int slotToCheck = event.getSlot();
                        int rowToCheck = finalRow;
                        for(int checker = this.maxRows; checker > finalRow; checker--) {
                            int slot = this.getSlotFromRowCol(checker, finalColumn);
                            boolean empty = this.gui.getGuiItem(slot + 1).getItemStack().getType() == Material.WHITE_STAINED_GLASS_PANE;
                            if(empty) {
                                slotToCheck = slot + 1;
                                rowToCheck = checker;
                                break;
                            }
                        }
                        this.gridTable[rowToCheck - 1][finalColumn] = this.getTurnId();
                        this.gui.updateItem(slotToCheck, this.getUpdatedGuiItem(player));
                        this.checkWinOrNextTurn(rowToCheck - 1, finalColumn);
                    }
                }));
                this.gridTable[row][column] = 0;
            }
        }
    }

    private int getSlotFromRowCol(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }


    private void checkWinOrNextTurn(int row, int column) {
        // WIN DETECTION
        int player = this.getTurnId(); //player ID

        int winner = this.checkWin(this.gridTable);
        if(player == winner) {
            this.stopGame(this.currentTurn, FinishReason.WIN);
            return;
        }

        // or next turn
        Player nextTurnPlayer = this.player1;
        if(this.currentTurn.getUniqueId().equals(this.player1.getUniqueId())) {
            nextTurnPlayer = this.player2;
        }
        this.currentTurn = nextTurnPlayer;
        // update turn color indicator
        this.updateTurnIndicator();
    }

    private int checkWin(int[][] board) {
        final int HEIGHT = board.length;
        final int WIDTH = board[0].length;
        final int EMPTY_SLOT = 0;
        for (int r = 0; r < HEIGHT; r++) { // iterate rows, bottom to top
            for (int c = 0; c < WIDTH; c++) { // iterate columns, left to right
                int player = board[r][c];
                if (player == EMPTY_SLOT)
                    continue; // don't check empty slots

                if (c + 3 < WIDTH &&
                        player == board[r][c+1] && // look right
                        player == board[r][c+2] &&
                        player == board[r][c+3])
                    return player;
                if (r + 3 < HEIGHT) {
                    if (player == board[r+1][c] && // look up
                            player == board[r+2][c] &&
                            player == board[r+3][c])
                        return player;
                    if (c + 3 < WIDTH &&
                            player == board[r+1][c+1] && // look up & right
                            player == board[r+2][c+2] &&
                            player == board[r+3][c+3])
                        return player;
                    if (c - 3 >= 0 &&
                            player == board[r+1][c-1] && // look up & left
                            player == board[r+2][c-2] &&
                            player == board[r+3][c-3])
                        return player;
                }
            }
        }
        return EMPTY_SLOT; // no winner found
    }

    private GuiItem getUpdatedGuiItem(Player player) {
        Material color = Material.BLUE_STAINED_GLASS_PANE;
        if(player.getUniqueId().equals(this.player1.getUniqueId())) {
            color = Material.RED_STAINED_GLASS_PANE;
        }
        Component name = Component.text("Team ", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text(color == Material.BLUE_STAINED_GLASS_PANE ? "Blau" : "Rot", 
                        color == Material.BLUE_STAINED_GLASS_PANE ? NamedTextColor.BLUE : NamedTextColor.RED));
        return ItemBuilder.from(color)
                .name(name)
                .asGuiItem(event -> {
                    if(event.getWhoClicked() instanceof Player clickPlayer) {
                        if (!hasTurn(clickPlayer)) {
                            return;
                        }
                        clickPlayer.playSound(clickPlayer.getLocation(),
                                Sound.ENTITY_VILLAGER_NO, 1f, 1.25f);
                    }
                });
    }

    private int getTurnId() {
        return this.currentTurn.getUniqueId().equals(this.player1.getUniqueId()) ? 1 : 2;
    }

    private boolean hasTurn(Player player) {
        return player.getUniqueId().equals(this.currentTurn.getUniqueId());
    }

    public void stopGame(Player winner, FinishReason finishReason) {
        if(finishReason == FinishReason.PLAYER_WENT_OFFLINE) {
            winner = this.getPartner(winner);
        }

        this.finished = true;

        this.player1.closeInventory();
        this.player2.closeInventory();
        this.crimxLobby.getConnectFourGameManager().getActiveConnectFourGames().remove(this);

        winner.sendMessage(this.crimxLobby.getConnectFourGameManager().getPrefix()
                .append(Component.text("Du hast das Spiel gegen ", NamedTextColor.GRAY))
                .append(this.getPartner(winner).displayName())
                .append(Component.text( " gewonnen! ", NamedTextColor.GREEN)));
        winner.playSound(winner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.75f, 0.5f);

        this.getPartner(winner).sendMessage(this.crimxLobby.getConnectFourGameManager().getPrefix()
                .append(Component.text("Du hast das Spiel gegen ", NamedTextColor.GRAY))
                .append(winner.displayName())
                .append(Component.text( " verloren! ", NamedTextColor.RED)));
        this.getPartner(winner)
                .playSound(this.getPartner(winner).getLocation(), Sound.ENTITY_VILLAGER_NO, 0.75f, 1f);

        this.player1.getInventory().setItem(21, null);
        this.player2.getInventory().setItem(21, null);

    }

    public Player getPartner(Player player) {
        if (player.getUniqueId().equals(this.player1.getUniqueId())) {
            return this.player2;
        }
        return this.player1;
    }

    public boolean isPlaying(Player player) {
        return (player.getUniqueId().equals(this.player1.getUniqueId())
                || player.getUniqueId().equals(this.player2.getUniqueId()));
    }

    public void startGame() {
        this.gui.open(this.player1);
        this.gui.open(this.player2);
    }

    public enum FinishReason {
        PLAYER_LEFT_MATCH, PLAYER_WENT_OFFLINE, DRAW, WIN
    }
}
