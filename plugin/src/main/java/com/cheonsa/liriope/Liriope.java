package com.cheonsa.liriope;

import com.cheonsa.liriope.api.LiriopeAPI;
import com.cheonsa.liriope.border.BorderManager;
import com.cheonsa.liriope.command.BorderCommands;
import com.cheonsa.liriope.command.parameter.ColorType;
import com.cheonsa.liriope.command.parameter.DurationType;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitLamp;

import java.time.Duration;

public final class Liriope extends JavaPlugin {

    private final BorderManager borderManager = new BorderManager();

    @Override
    public void onEnable() {
        LiriopeAPI.setApi(this.borderManager);
        this.getServer().getScheduler().runTaskTimer(this, this.borderManager::tick, 1, 1);
        BukkitLamp.builder(this)
            .parameterTypes(types -> types
                .addParameterType(Duration.class, new DurationType())
                .addParameterType(Color.class, new ColorType()))
            .build()
            .register(BorderCommands.all(this.borderManager));
    }

    @Override
    public void onDisable() {
        this.borderManager.clear();
    }
}
