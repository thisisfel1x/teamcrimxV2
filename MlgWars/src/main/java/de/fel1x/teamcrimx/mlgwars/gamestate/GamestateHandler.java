package de.fel1x.teamcrimx.mlgwars.gamestate;

public class GamestateHandler {

    private Gamestate gamestate;

    public GamestateHandler() {
        this.gamestate = Gamestate.IDLE;
    }

    public Gamestate getGamestate() {
        return this.gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }
}
