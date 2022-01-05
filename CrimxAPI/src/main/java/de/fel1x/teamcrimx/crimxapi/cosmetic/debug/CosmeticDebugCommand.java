package de.fel1x.teamcrimx.crimxapi.cosmetic.debug;

import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticCategory;
import de.fel1x.teamcrimx.crimxapi.cosmetic.CosmeticRegistry;
import de.fel1x.teamcrimx.crimxapi.cosmetic.WinAnimationCosmetic;
import de.fel1x.teamcrimx.crimxapi.support.CrimxSpigotAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CosmeticDebugCommand implements CommandExecutor, TabCompleter {

    private final CrimxSpigotAPI crimxSpigotAPI;

    public CosmeticDebugCommand(CrimxSpigotAPI crimxSpigotAPI) {
        this.crimxSpigotAPI = crimxSpigotAPI;
        this.crimxSpigotAPI.getCommand("cosmeticdebug").setExecutor(this);
        this.crimxSpigotAPI.getCommand("cosmeticdebug").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            return false;
        }

        if(!player.hasPermission("crimxapi.cosmetic.debug")) {
            return false;
        }

        if(args.length != 1) {
            player.sendMessage("§7Cosmetics: §e"
                    + Arrays.stream(CosmeticRegistry.values())
                    .filter(cosmeticRegistry -> cosmeticRegistry.getCosmeticCategory() == CosmeticCategory.WIN_ANIMATION)
                    .map(Enum::name).collect(Collectors.joining(", ")));
            return false;
        }

        try {
            CosmeticRegistry cosmeticRegistry = CosmeticRegistry.valueOf(args[0]);
            WinAnimationCosmetic winAnimationCosmetic = (WinAnimationCosmetic) cosmeticRegistry.getCosmeticClass()
                    .getDeclaredConstructor(Player.class, CrimxSpigotAPI.class).newInstance(player, this.crimxSpigotAPI);
            winAnimationCosmetic.win();
        } catch (Exception ignored) { }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            return Arrays.stream(CosmeticRegistry.values())
                    .filter(cosmeticRegistry -> cosmeticRegistry.getCosmeticCategory() == CosmeticCategory.WIN_ANIMATION)
                    .map(Enum::name).collect(Collectors.toList());
        }

        return null;
    }
}
