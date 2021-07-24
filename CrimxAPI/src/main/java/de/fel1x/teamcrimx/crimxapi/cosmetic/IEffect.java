package de.fel1x.teamcrimx.crimxapi.cosmetic;

import org.bukkit.Location;

public interface IEffect {

    Object calculateMath(Object[] args);

    void displayParticles();

}
