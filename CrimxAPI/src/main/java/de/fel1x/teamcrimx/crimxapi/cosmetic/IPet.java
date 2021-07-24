package de.fel1x.teamcrimx.crimxapi.cosmetic;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface IPet {

    UUID owner();

    Component customName();

}
