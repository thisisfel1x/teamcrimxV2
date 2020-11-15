package de.fel1x.teamcrimx.crimxlobby.minigames.jumpandrun;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class JumpAndRunPlayer {

    private Player player;
    private int woolColor;
    private Block lastBlock;
    private Block currentBlock;
    private Block nextBlock;
    private ArrayList<Location> possibleJumps;

    public JumpAndRunPlayer(Player player, int woolColor, Block lastBlock, Block currentBlock, Block nextBlock, ArrayList<Location> possibleJumps) {
        this.player = player;
        this.woolColor = woolColor;
        this.lastBlock = lastBlock;
        this.currentBlock = currentBlock;
        this.nextBlock = nextBlock;
        this.possibleJumps = possibleJumps;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWoolColor() {
        return this.woolColor;
    }

    public void setWoolColor(int woolColor) {
        this.woolColor = woolColor;
    }

    public Block getLastBlock() {
        return this.lastBlock;
    }

    public void setLastBlock(Block lastBlock) {
        this.lastBlock = lastBlock;
    }

    public Block getCurrentBlock() {
        return this.currentBlock;
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    public Block getNextBlock() {
        return this.nextBlock;
    }

    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    public ArrayList<Location> getPossibleJumps() {
        return this.possibleJumps;
    }

    public void setPossibleJumps(ArrayList<Location> possibleJumps) {
        this.possibleJumps = possibleJumps;
    }
}
