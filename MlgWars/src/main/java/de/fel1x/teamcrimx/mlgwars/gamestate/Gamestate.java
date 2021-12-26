package de.fel1x.teamcrimx.mlgwars.gamestate;

public enum Gamestate {

    IDLE, LOBBY, DELAY, PREGAME, INGAME, ENDING;

    public boolean isInGame(Gamestate gamestate) {
        return gamestate == DELAY || gamestate == PREGAME || gamestate == INGAME;
    }
}
