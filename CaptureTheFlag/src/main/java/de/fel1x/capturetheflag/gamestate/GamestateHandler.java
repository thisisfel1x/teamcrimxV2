package de.fel1x.capturetheflag.gamestate;

public class GamestateHandler {

    public Gamestate gamestate;

    public GamestateHandler() {

        this.gamestate = Gamestate.LOBBY;

    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }
}
