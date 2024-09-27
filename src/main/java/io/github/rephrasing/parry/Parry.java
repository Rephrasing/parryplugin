package io.github.rephrasing.parry;

import org.bukkit.plugin.java.JavaPlugin;

public class Parry extends JavaPlugin {

    private static Parry instance;
    private static final int parryCooldownTicks = 5;
    private static final int parryEffectsDuranceTicks = 5;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new ParryEventListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static int getParryCooldownTicks() {
        return parryCooldownTicks;
    }

    public static int getParryEffectsDuranceTicks() {
        return parryEffectsDuranceTicks;
    }

    public static Parry getInstance() {
        return instance;
    }
}
