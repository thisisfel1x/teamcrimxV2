package de.fel1x.teamcrimx.floorislava.gamehandler;

public class GamestateHandler {
    private Gamestate gamestate = Gamestate.IDLE;

    public Gamestate getGamestate() {
        return this.gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }
}
