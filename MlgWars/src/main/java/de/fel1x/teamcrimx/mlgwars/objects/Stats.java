package de.fel1x.teamcrimx.mlgwars.objects;

public class Stats {

    private int kills;
    private int deaths;
    private int gamesPlayed;
    private int gamesWon;
    private int placement;
    private int gamePoints;
    private boolean win = false;

    public Stats(int kills, int deaths, int gamesPlayed, int gamesWon, int placement) {
        this.kills = kills;
        this.deaths = deaths;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.placement = placement;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
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

    public void increaseKillsByOne() {
        this.setKills(this.getKills() + 1);
    }

    public void increaseDeathsByOne() {
        this.setDeaths(this.getDeaths() + 1);
    }

    public void increaseGamesByOne() {
        this.setGamesPlayed(this.getGamesPlayed() + 1);
    }

    public void increaseWinsByOne() {
        this.setGamesWon(this.getGamesWon() + 1);
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isWin() {
        return win;
    }

    public int getGamePoints() {
        return gamePoints;
    }

    public void addPoints(int amount) {
        this.gamePoints += amount;
    }
}
