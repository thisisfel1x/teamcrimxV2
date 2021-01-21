package de.fel1x.bingo.scenarios;

public enum Scenario {

    ENDERGAMES_TELEPORT(EnderGamesTeleport.class, true),
    INVENTORY_SHUFFLE(InventoryShuffle.class, true),
    RANDOM_POTION_EFFECT(RandomPotionEffect.class, true),
    PLAYER_XRAY(PlayerXRay.class, true);

    private final Class<? extends IBingoScenario> scenarioClazz;
    private boolean isEnabled;

    Scenario(Class<? extends IBingoScenario> scenarioClazz, boolean isEnabled) {
        this.scenarioClazz = scenarioClazz;
        this.isEnabled = isEnabled;
    }

    public Class<? extends IBingoScenario> getScenarioClazz() {
        return this.scenarioClazz;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}
