package de.fel1x.bingo.objects.stats;

public class Stats {

    private int itemsCrafted;
    private int itemsPickedUp;
    private int gamesPlayed;
    private int gamesWon;
    private int placement;

    public Stats(int itemsCrafted, int itemsPickedUp, int gamesPlayed, int gamesWon, int placement) {
        this.itemsCrafted = itemsCrafted;
        this.itemsPickedUp = itemsPickedUp;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.placement = placement;
    }

    public int getItemsCrafted() {
        return this.itemsCrafted;
    }

    public void setItemsCrafted(int itemsCrafted) {
        this.itemsCrafted = itemsCrafted;
    }

    public int getItemsPickedUp() {
        return this.itemsPickedUp;
    }

    public void setItemsPickedUp(int itemsPickedUp) {
        this.itemsPickedUp = itemsPickedUp;
    }

    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getPlacement() {
        return this.placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public void increaseItemsCrafted() {
        this.setItemsCrafted(this.getItemsCrafted() + 1);
    }

    public void increaseItemsPickedUp() {
        this.setItemsPickedUp(this.getItemsPickedUp() + 1);
    }

    public void increaseGamesByOne() {
        this.setGamesPlayed(this.getGamesPlayed() + 1);
    }

    public void increaseWinsByOne() {
        this.setGamesWon(this.getGamesWon() + 1);
    }

}
